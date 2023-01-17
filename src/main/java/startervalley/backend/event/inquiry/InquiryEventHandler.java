package startervalley.backend.event.inquiry;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import startervalley.backend.service.SlackService;

@Component
@RequiredArgsConstructor
public class InquiryEventHandler {

    private final SlackService slackService;

    @Async
    @TransactionalEventListener
    public void onInquiryEvent(InquiryEvent event) {
        slackService.postSlackMessage(event.getContent());
    }
}
