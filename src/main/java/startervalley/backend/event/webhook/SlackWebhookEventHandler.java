package startervalley.backend.event.webhook;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class SlackWebhookEventHandler {

    private final WebClient webClient;
    private final String webhookUri;

    public SlackWebhookEventHandler(WebClient webClient, @Value("${slack.webhook}") String webhookUri) {
        this.webClient = webClient;
        this.webhookUri = webhookUri;
    }

    @Async
    @TransactionalEventListener
    public void onEvent(SlackWebhookEvent event) {
        String ok = webClient.post()
                .uri(webhookUri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(event.getWebhookDto()), SlackWebhookDto.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("slack webhook response = {}", ok);
    }
}
