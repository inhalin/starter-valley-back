package startervalley.backend.security.auth.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import startervalley.backend.entity.AuthProvider;

import java.util.Map;

@Getter
@Setter
@ToString
public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId();

    public abstract String getEmail();

    public abstract String getImageUrl();

    public abstract String getGithubUrl();

    public abstract AuthProvider getProvider();

    public String getUsername() {
        return getProvider().getName() + "_" + getId();
    }

    ;
}