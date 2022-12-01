package startervalley.backend.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import startervalley.backend.dto.auth.AuthRequest;
import startervalley.backend.dto.auth.AuthResponse;
import startervalley.backend.dto.auth.GithubUserResponse;
import startervalley.backend.entity.Role;
import startervalley.backend.entity.User;
import startervalley.backend.repository.UserRepository;
import startervalley.backend.security.auth.CustomUserDetails;
import startervalley.backend.security.auth.client.ClientGithub;
import startervalley.backend.security.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class GithubAuthService {

    private final JwtTokenProvider tokenProvider;

    private final ClientGithub clientGithub;
    private final UserRepository userRepository;

    public AuthResponse login(AuthRequest authRequest) {
        String accessToken = clientGithub.getAccessToken(authRequest.getCode());
        GithubUserResponse userData = clientGithub.getUserData(accessToken);

        AuthResponse authResponse = AuthResponse.builder()
                .isNewMember(false)
                .build();

        User user = userRepository.findByEmailAndProvider(userData.getEmail(), userData.getProvider());

/*
        // 가져온 깃허브 정보로 DB에서 유저 찾기
        if (user != null) {
            // 유저 있으면 jwt 발급하고 성공 여부 응답에 포함해서 프론트에 리턴
            UserDetails userDetails = new CustomUserDetails(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwtAccessToken = tokenProvider.createAccessToken(authentication);

            authResponse.setIsNewMember(false);
            authResponse.setAccessToken(jwtAccessToken);
        }
        // 유저 없으면 jwt 발급하고 헤더에 넣기, 신규 유저 여부 응답에 포함해서 프론트에 리턴
*/

        if (user == null) {
            user = createUser(userData);
        }

        if (user.getName() == null) {
            authResponse.setIsNewMember(true);
        }

        authResponse.setAccessToken(createJwt(user));

        return authResponse;
    }

    private User createUser(GithubUserResponse userData) {
        User user = User.builder()
                .provider(userData.getProvider())
                .providerId(userData.getProvider() + "_" + userData.getId())
                .role(Role.USER)
                .githubUrl(userData.getHtmlUrl())
                .imageUrl(userData.getAvatarUrl())
                .email(userData.getEmail())
                .username(userData.getLogin())
                .build();
        userRepository.save(user);

        return user;
    }

    private String createJwt(User user) {
        CustomUserDetails userDetails = new CustomUserDetails(user);
        System.out.println("userDetails.getUser().getEmail() = " + userDetails.getUser().getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        System.out.println("principal.getEmail() = " + principal.getEmail());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.createAccessToken(authentication);
    }
}
