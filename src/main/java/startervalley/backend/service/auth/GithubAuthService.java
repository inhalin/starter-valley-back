package startervalley.backend.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import startervalley.backend.dto.auth.AuthRequest;
import startervalley.backend.dto.auth.AuthResponse;
import startervalley.backend.dto.auth.GithubUserResponse;
import startervalley.backend.entity.Role;
import startervalley.backend.entity.User;
import startervalley.backend.repository.user.UserRepository;
import startervalley.backend.security.auth.client.ClientGithub;
import startervalley.backend.security.jwt.JwtTokenProvider;

import java.util.HashMap;
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
        Map<String, String> attributes = new HashMap<>();

        if (user == null) {
            user = new User();
            isNewMember = true;
            attributes = userData.getAttributes();
        } else {
            attributes.put("role", Role.USER.name());
            userRepository.updateImageUrl(userData.getLogin(), userData.getAvatarUrl());
        }

        return AuthResponse.builder()
                .isNewMember(isNewMember)
                .accessToken(tokenProvider.createAccessToken(user, attributes))
                .build();
    }
}
