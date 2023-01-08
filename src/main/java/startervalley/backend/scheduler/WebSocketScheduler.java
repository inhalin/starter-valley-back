package startervalley.backend.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import startervalley.backend.event.alert.WebsocketAlertEventPublisher;
import startervalley.backend.event.alert.dto.AlertDto;
import startervalley.backend.handler.WebSocketHandler;

import java.io.IOException;

import static startervalley.backend.event.alert.dto.AlertType.ATTENDANCE;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketScheduler {

    private final WebSocketHandler webSocketHandler;
    private final WebsocketAlertEventPublisher noticeEventPublisher;

    @Scheduled(cron = "0/5 * * * * *")
    public void sendAfter5Minute() throws IOException {
        AlertDto alertDto = new AlertDto(ATTENDANCE, "출석해주세요!");
        noticeEventPublisher.publishEvent(alertDto);
    }
}
