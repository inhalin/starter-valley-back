package startervalley.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class CodeValidationRequest {

    @NotNull
    private String code;

    @NotNull
    private Long generationId;
}
