package startervalley.backend.admin.dto.user;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
public class UserDropoutRequest {

    @NotNull(message = "중도하차 일자를 입력해주세요.")
    private LocalDate dropoutDate;

    @NotEmpty(message = "사유를 입력해주세요.")
    private String reason;
}
