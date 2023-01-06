package startervalley.backend.dto.attendance;

import javax.validation.constraints.Size;

public class AttendanceCodeDto {

    @Size(min = 8, max = 8, message = "8자 입력해주세요.")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
