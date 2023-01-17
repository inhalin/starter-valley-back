package startervalley.backend.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import startervalley.backend.dto.auth.AuthRequest;
import startervalley.backend.dto.auth.AuthResponse;
import startervalley.backend.dto.auth.GithubUserResponse;
import startervalley.backend.entity.User;
import startervalley.backend.repository.user.UserRepository;
import startervalley.backend.security.auth.client.ClientGithub;
import startervalley.backend.security.jwt.JwtTokenProvider;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GithubAuthService {

    private final JwtTokenProvider tokenProvider;

    private final ClientGithub clientGithub;
    private final UserRepository userRepository;

    public AuthResponse login(AuthRequest authRequest) {
        String githubAccessToken = clientGithub.getAccessToken(authRequest.getCode());
        GithubUserResponse userData = clientGithub.getUserData(githubAccessToken);

        User user = userRepository.findByUsername(userData.getLogin()).orElse(new User());
        boolean isNewMember = false;
        Map<String, String> attributes = new HashMap<>();

        String accessToken;
        String refreshToken = null;

        if (user.getName() == null) {
            isNewMember = true;
            attributes = userData.getAttributes();
        } else {
            userRepository.updateImageUrl(userData.getLogin(), userData.getAvatarUrl());
        }

        accessToken = tokenProvider.createAccessToken(user, attributes);
        Date expiration = tokenProvider.getExpiration(accessToken);

        if (!isNewMember) {
            refreshToken = tokenProvider.createRefreshToken(tokenProvider.getAuthentication(accessToken));
        }

        return AuthResponse.builder()
                .isNewMember(isNewMember)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiration(expiration)
                .build();
    }
}
