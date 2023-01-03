package startervalley.backend.dto.auth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import startervalley.backend.entity.AuthProvider;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GithubUserResponse {
    private String login;
    private String id;
    private String email;
    private String avatarUrl;
    private String htmlUrl;
    private AuthProvider provider;

    public Map<String, String> getAttributes() {
        Map<String, String> attributes = new HashMap<>();

        attributes.put("username", login);
        attributes.put("email", email);
        attributes.put("imageUrl", avatarUrl);
        attributes.put("githubUrl", htmlUrl);
        attributes.put("id", id);
        attributes.put("provider", AuthProvider.GITHUB.name());
        attributes.put("role", AuthProvider.GITHUB.getRole());

        return attributes;
    }
}
