package startervalley.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    private String code;
    private String name;
    private String devpart;
    private Long generationId;
    private String introduction;
}
