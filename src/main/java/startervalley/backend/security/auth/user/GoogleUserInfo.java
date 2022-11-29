package startervalley.backend.security.auth.user;

import startervalley.backend.entity.AuthProvider;

import java.util.Map;

public class GoogleUserInfo extends OAuth2UserInfo {
    public GoogleUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("picture");
    }

    @Override
    public String getGithubUrl() {
        return null;
    }

    @Override
    public AuthProvider getProvider() {
        return AuthProvider.GOOGLE;
    }
}
