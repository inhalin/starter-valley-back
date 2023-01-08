package startervalley.backend.dto.inquiry;

import lombok.Getter;
import startervalley.backend.entity.InquiryTarget;

import javax.validation.constraints.NotEmpty;

@Getter
public class InquiryRequest {

    @NotEmpty(message = "제목 입력은 필수입니다.")
    private String title;

    @NotEmpty(message = "내용 입력은 필수입니다.")
    private String content;

    private boolean anonymous;

    private InquiryTarget target;
}
