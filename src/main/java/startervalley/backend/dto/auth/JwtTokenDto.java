package startervalley.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class JwtTokenDto {
    private String accessToken;
    private String refreshToken;
}
