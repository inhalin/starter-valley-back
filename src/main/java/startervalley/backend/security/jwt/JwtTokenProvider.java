package startervalley.backend.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import startervalley.backend.entity.User;
import startervalley.backend.exception.TokenNotValidException;
import startervalley.backend.repository.UserRepository;
import startervalley.backend.security.auth.CustomUserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final String SECRET_KEY;
    private final UserRepository userRepository;

    @Value("${app.auth.token.expiry.access-token}")
    private Long ACCESS_TOKEN_EXPIRE_LENGTH;

    @Value("${app.auth.token.expiry.refresh-token}")
    private Long REFRESH_TOKEN_EXPIRE_LENGTH;

    private final String ISSUER = "startervalley";
    private final String AUTHORITIES_KEY = "role";

    public JwtTokenProvider(@Value("${app.auth.token.secret-key}") String secretKey, UserRepository userRepository) {
        this.SECRET_KEY = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.userRepository = userRepository;
    }

    public String createAccessToken(User user, Map<String, String> userData) {

        CustomUserDetails userDetails = new CustomUserDetails(user, userData);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Date now = new Date();
        Date validity = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_LENGTH);
        Map<String, Object> claims = new HashMap<>(userData);

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setClaims(claims)
                .setSubject(Optional.ofNullable(user.getUsername()).orElse(userData.get("username")))
                .setIssuer(ISSUER)
                .setIssuedAt(now)
                .setExpiration(validity)
                .compact();
    }

    public String createRefreshToken(Authentication authentication) {

        Date now = new Date();
        Date validity = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_LENGTH);

        String refreshToken = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setIssuer(ISSUER)
                .setIssuedAt(now)
                .setExpiration(validity)
                .compact();

        saveRefreshToken(authentication, refreshToken);

        return refreshToken;
    }

    private void saveRefreshToken(Authentication authentication, String refreshToken) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        userRepository.updateRefreshToken(user.getId(), refreshToken);
    }

    public Authentication getAuthentication(String accessToken) {

        Claims claims = parseClaims(accessToken);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        Optional<User> userOptional = userRepository.findByUsername(claims.getSubject());
        User user;
        Map<String, String> attributes = new HashMap<>();

        if (userOptional.isEmpty()) {
            user = new User();
            attributes = getAttributes(claims);
        } else {
            user = userOptional.get();
        }

        CustomUserDetails principal = new CustomUserDetails(user, attributes);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return userRepository.existsRefreshTokenByUsername(getUsername(token)) != null || parseClaims(token).get("email") != null;
        } catch (ExpiredJwtException e) {
            throw new TokenNotValidException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new TokenNotValidException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalStateException e) {
            throw new TokenNotValidException("JWT 토큰이 잘못되었습니다");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String getUsername(String accessToken) {
        return resolveClaims(accessToken, Claims::getSubject);
    }

    private <T> T resolveClaims(String accessToken, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(accessToken).getBody();
        return claimsResolver.apply(claims);
    }

    private Map<String, String> getAttributes(Claims claims) {
        Map<String, String> attributes = new HashMap<>();

        attributes.put("username", (String) claims.get("username"));
        attributes.put("email", (String) claims.get("email"));
        attributes.put("imageUrl", (String) claims.get("imageUrl"));
        attributes.put("githubUrl", (String) claims.get("githubUrl"));
        attributes.put("id", (String) claims.get("id"));
        attributes.put("provider", (String) claims.get("provider"));
        attributes.put("role", (String) claims.get("role"));

        return attributes;
    }
}
