package startervalley.backend.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.service.StoreService;

@RequiredArgsConstructor
@Component
public class StoreScheduler {

    private final StoreService storeService;

    @Transactional
    @Scheduled(cron = "1 0 0 ? * MON-FRI")
    public void changeTodayRecommendStore() {
        storeService.updateRecommendStoreList();
    }
}
