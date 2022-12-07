package startervalley.backend.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import startervalley.backend.dto.auth.AuthRequest;
import startervalley.backend.dto.auth.AuthResponse;
import startervalley.backend.dto.auth.GithubUserResponse;
import startervalley.backend.entity.User;
import startervalley.backend.repository.UserRepository;
import startervalley.backend.security.auth.client.ClientGithub;
import startervalley.backend.security.auth.client.GithubUser;
import startervalley.backend.security.jwt.JwtTokenProvider;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GithubAuthService {

    private final JwtTokenProvider tokenProvider;

    private final ClientGithub clientGithub;
    private final UserRepository userRepository;

    public AuthResponse login(AuthRequest authRequest) {
        String accessToken = clientGithub.getAccessToken(authRequest.getCode());
        GithubUserResponse userData = clientGithub.getUserData(accessToken);

        User user = userRepository.findByEmailAndProvider(userData.getEmail(), userData.getProvider());
        boolean isNewMember = false;
        Map<String, String> attributes = userData.getAttributes();

        if (user == null) {
            user = new GithubUser();
            isNewMember = true;
        }

        return AuthResponse.builder()
                .isNewMember(isNewMember)
                .accessToken(tokenProvider.createAccessToken(user, attributes))
                .build();
    }
}
