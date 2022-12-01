package startervalley.backend.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import startervalley.backend.entity.User;
import startervalley.backend.repository.UserRepository;
import startervalley.backend.security.auth.CustomUserDetails;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final String SECRET_KEY;
    private final String COOKIE_REFRESH_TOKEN_KEY;

    @Value("${app.auth.token.expiry.access-token}")
    private Long ACCESS_TOKEN_EXPIRE_LENGTH;

    @Value("${app.auth.token.expiry.refresh-token}")
    private Long REFRESH_TOKEN_EXPIRE_LENGTH;

    private final String AUTHORITIES_KEY = "role";

    @Autowired
    private UserRepository userRepository;

    public JwtTokenProvider(
            @Value("${app.auth.token.secret-key}") String secretKey,
            @Value("${app.auth.token.refresh-key}") String refreshKey) {
        this.SECRET_KEY = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.COOKIE_REFRESH_TOKEN_KEY = refreshKey;
    }

    public String createAccessToken(Authentication authentication) {

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date validity = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_LENGTH);
        String id = user.getUsername();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")); // 신규 anonymous

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(id)
                .claim(AUTHORITIES_KEY, role)
                .setIssuer("startervalley")
                .setIssuedAt(now)
                .setExpiration(validity)
                .compact();
    }

    public void createRefreshToken(Authentication authentication, HttpServletResponse response) {

        Date now = new Date();
        Date validity = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_LENGTH);

        String refreshToken = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setIssuer("startervalley")
                .setIssuedAt(now)
                .setExpiration(validity)
                .compact();

//        saveRefreshToken(authentication, refreshToken);

        ResponseCookie cookie = ResponseCookie.from(COOKIE_REFRESH_TOKEN_KEY, refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .maxAge(REFRESH_TOKEN_EXPIRE_LENGTH)
                .path("/")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    private void saveRefreshToken(Authentication authentication, String refreshToken) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        userRepository.updateRefreshToken(user.getId(), refreshToken);
    }

    // Access Token을 검사하고 얻은 정보로 Authentication 객체 생성
    public Authentication getAuthentication(String accessToken) {

        Claims claims = parseClaims(accessToken);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        User user = userRepository.findById(Long.valueOf(claims.getSubject())).orElseThrow();

        CustomUserDetails principal = new CustomUserDetails(user);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalStateException e) {
            log.info("JWT 토큰이 잘못되었습니다");
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return false;
    }

    // Access Token 만료시 갱신때 사용할 정보를 얻기 위해 Claim 리턴
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
