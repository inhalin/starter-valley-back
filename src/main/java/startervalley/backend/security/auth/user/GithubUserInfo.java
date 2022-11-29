package startervalley.backend.security.auth.user;

import startervalley.backend.entity.AuthProvider;

import java.util.Map;

public class GithubUserInfo extends OAuth2UserInfo{

    public GithubUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return ((Integer) attributes.get("id")).toString();
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("avatar_url");
    }

    @Override
    public String getGithubUrl() {
        return (String) attributes.get("html_url");
    }

    @Override
    public AuthProvider getProvider() {
        return AuthProvider.GITHUB;
    }
}
