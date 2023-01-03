package startervalley.backend.admin.dto.auth;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class AuthPasswordRequest {

    @NotEmpty(message = "기존 비밀번호 입력은 필수입니다.")
    private String password;

    @NotEmpty(message = "새로운 비밀번호 입력은 필수입니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d!@#$%^&*/]{8,}$", message = "비밀번호는 영문자, 숫자, 특수문자를 1개 이상 포함하여야 합니다.")
    private String newPassword;

    @NotEmpty(message = "새로운 비밀번호를 한번 더 입력하세요.")
    private String _newPassword;
}
