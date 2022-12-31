package startervalley.backend.admin.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
public class AuthLoginRequest {

    @NotEmpty(message = "아이디를 입력은 필수입니다.")
    private String username;

    @NotEmpty(message = "비밀번호 입력은 필수입니다.")
    private String password;
}
