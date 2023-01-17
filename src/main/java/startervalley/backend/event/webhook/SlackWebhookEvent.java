package startervalley.backend.event.webhook;

import lombok.Getter;

@Getter
public class SlackWebhookEvent {

    private SlackWebhookDto webhookDto;

    public SlackWebhookEvent(SlackWebhookDto webhookDto) {
        this.webhookDto = webhookDto;
    }

    public static SlackWebhookEvent of(SlackWebhookDto webhookDto) {
        return new SlackWebhookEvent(webhookDto);
    }
}
