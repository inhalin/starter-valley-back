package startervalley.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GithubTokenRequest {
    private String clientId;
    private String clientSecret;
    private String code;
}
