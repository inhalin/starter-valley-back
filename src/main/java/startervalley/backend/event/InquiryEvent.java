package startervalley.backend.event;

import lombok.Getter;

@Getter
public class InquiryEvent {

    private String content;

    public InquiryEvent(String content) {
        this.content = content;
    }

    public static InquiryEvent of(String content) {
        return new InquiryEvent(content);
    }
}
