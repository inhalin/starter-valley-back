package startervalley.backend.dto.auth;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class CodeValidationRequest {

    @NotNull
    private String code;

    @NotNull
    private Long generationId;
}
