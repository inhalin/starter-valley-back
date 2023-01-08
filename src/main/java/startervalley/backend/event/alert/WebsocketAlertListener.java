package startervalley.backend.event.alert;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import startervalley.backend.event.alert.dto.AlertDto;
import startervalley.backend.handler.WebSocketHandler;

import java.io.IOException;

@Slf4j
@Component
public class WebsocketAlertListener {

    private final WebSocketHandler webSocketHandler;

    public WebsocketAlertListener(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Async
    @EventListener
    public void onAlert(AlertDto dto) throws IOException {
        Gson gson = new Gson();
        String s = gson.toJson(dto);
        webSocketHandler.sendHello(s);
    }
}
