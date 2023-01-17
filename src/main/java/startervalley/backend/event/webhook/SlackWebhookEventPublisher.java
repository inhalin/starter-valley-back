package startervalley.backend.event.webhook;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SlackWebhookEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(SlackWebhookDto dto) {
        applicationEventPublisher.publishEvent(SlackWebhookEvent.of(dto));
    }
}
