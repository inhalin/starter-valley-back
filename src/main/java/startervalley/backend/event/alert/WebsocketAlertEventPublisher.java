package startervalley.backend.event.alert;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import startervalley.backend.event.alert.dto.AlertDto;

@Component
public class WebsocketAlertEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public WebsocketAlertEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishEvent(AlertDto alertDto) {
        applicationEventPublisher.publishEvent(alertDto);
    }
}
