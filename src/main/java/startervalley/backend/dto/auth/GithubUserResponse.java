package startervalley.backend.dto.auth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import startervalley.backend.entity.AuthProvider;

@Data
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
}
