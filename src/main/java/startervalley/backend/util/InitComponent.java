package startervalley.backend.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import startervalley.backend.entity.Attendance;
import startervalley.backend.service.StoreService;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class InitComponent {

    private final StoreService storeService;

    public InitComponent(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostConstruct
    public void generateAttendanceCode() {
        log.info("출석 코드 생성");
        Attendance.generateAttendanceRandomCode();
    }

    @PostConstruct
    public void updateStoreRecommendList() {
        log.info("추천 가게 생성");
        storeService.updateRecommendStoreList();
    }
}
