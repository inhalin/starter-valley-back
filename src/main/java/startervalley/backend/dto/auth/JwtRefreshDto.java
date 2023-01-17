package startervalley.backend.dto.auth;

import lombok.Getter;

@Getter
public class JwtRefreshDto {
    private String refreshToken;
}
