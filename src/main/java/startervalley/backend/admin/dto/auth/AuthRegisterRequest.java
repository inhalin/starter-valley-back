package startervalley.backend.admin.dto.auth;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class AuthRegisterRequest {

    @NotEmpty(message = "아이디 입력은 필수입니다.")
    @Size(min = 4, message = "아이디는 4자 이상어야 합니다.")
    private String username;

    @NotEmpty(message = "비밀번호 입력은 필수입니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d!@#$%^&*/]{8,}$", message = "비밀번호는 영문자, 숫자, 특수문자를 1개 이상 포함하여야 합니다.")
    private String password;

    @NotEmpty(message = "이름 입력은 필수입니다.")
    private String name;

    @NotEmpty(message = "이메일 입력은 필수입니다.")
    @Email(message = "이메일 형식이 잘못되었습니다.")
    private String email;

    @NotEmpty(message = "휴대폰번호 입력은 필수입니다.")
    @Pattern(regexp = "^01\\d-?\\d{3,4}-?\\d{4}$", message = "휴대폰번호 형식이 잘못되었습니다.")
    private String phone;
}
