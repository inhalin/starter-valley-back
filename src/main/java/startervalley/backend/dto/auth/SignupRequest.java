package startervalley.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @NotNull(message = "이름을 작성해주세요")
    private String name;

    @NotNull(message = "파트를 정확히 입력해주세요.")
    private String devpart;

    @NotNull
    @Min(value = 1, message = "기수를 정확히 입력해주세요.")
    private Long generationId;

    @NotNull(message = "한줄소개를 작성해주세요.")
    private String introduction;
}
