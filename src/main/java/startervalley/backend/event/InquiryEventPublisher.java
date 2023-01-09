package startervalley.backend.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InquiryEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(String content) {
        applicationEventPublisher.publishEvent(InquiryEvent.of(content));
    }
}
