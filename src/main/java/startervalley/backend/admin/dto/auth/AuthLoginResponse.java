package startervalley.backend.admin.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class AuthLoginResponse {
    private String accessToken;
    private String refreshToken;
}
