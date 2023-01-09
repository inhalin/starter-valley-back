package startervalley.backend.service;

import com.slack.api.Slack;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class SlackService {

    @Value("${slack.token}")
    private String token;

    @Value("${slack.channel.monitor}")
    private String channel;

    public void postSlackMessage(String message) {
        log.info("try sending slack message");

        try {
            CompletableFuture<ChatPostMessageResponse> response = Slack.getInstance().methodsAsync(token)
                    .chatPostMessage(req -> req
                            .channel(channel)
                            .text(message));

            log.info("response = {}", response.get());
        } catch (ExecutionException | InterruptedException e) {
            log.info(e.getMessage());
        }
    }
}
