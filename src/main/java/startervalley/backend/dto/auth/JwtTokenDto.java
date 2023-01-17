package startervalley.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor(staticName = "of")
public class JwtTokenDto {
    private String accessToken;
    private String refreshToken;
    private Date expiration;
}
