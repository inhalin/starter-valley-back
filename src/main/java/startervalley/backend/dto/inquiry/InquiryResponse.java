package startervalley.backend.dto.inquiry;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import startervalley.backend.entity.Inquiry;
import startervalley.backend.entity.InquiryTarget;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class InquiryResponse {
    private Long id;
    private String title;
    private String content;
    private String name;
    private InquiryTarget target;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDateTime createdDate;

    public static InquiryResponse mapToResponse(Inquiry inquiry, String name) {
        return InquiryResponse.builder()
                .id(inquiry.getId())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .name(name)
                .target(inquiry.getTarget())
                .createdDate(inquiry.getCreatedDate())
                .build();
    }
}
