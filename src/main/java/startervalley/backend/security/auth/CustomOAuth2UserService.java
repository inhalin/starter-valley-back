package startervalley.backend.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import startervalley.backend.entity.AuthProvider;
import startervalley.backend.entity.User;
import startervalley.backend.repository.UserRepository;
import startervalley.backend.security.auth.user.OAuth2UserInfo;
import startervalley.backend.security.auth.user.OAuth2UserInfoFactory;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        return process(userRequest, oAuth2User);
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        AuthProvider authProvider = AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authProvider, oAuth2User.getAttributes());

        User user = userRepository.findByEmailAndProvider(oAuth2UserInfo.getEmail(), authProvider);

        if (user == null) {   // 신규 유저
            user = createUser(oAuth2UserInfo, authProvider);
        }

        return new CustomUserDetails(user, oAuth2User.getAttributes());
    }

    private User createUser(OAuth2UserInfo userAttributes, AuthProvider authProvider) {
        User user = User.builder()
                .username(authProvider.getName() + "_" + userAttributes.getId())
                .email(userAttributes.getEmail())
                .provider(authProvider)
                .role(authProvider.getRole())
                .githubUrl(userAttributes.getGithubUrl())
                .imageUrl(userAttributes.getImageUrl())
                .build();

        return userRepository.save(user);
    }
}
