package startervalley.backend.admin.dto.notice;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class NoticeRequest {

    @NotEmpty(message = "제목 입력은 필수입니다.")
    private String title;

    @NotEmpty(message = "내용 입력은 필수입니다.")
    private String content;
}
