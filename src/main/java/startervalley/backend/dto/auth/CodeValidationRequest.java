package startervalley.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CodeValidationRequest {

    @NotNull
    private String code;

    @NotNull
    private Long generationId;
}
