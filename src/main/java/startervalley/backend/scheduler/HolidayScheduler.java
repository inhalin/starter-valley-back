package startervalley.backend.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.service.HolidayService;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class HolidayScheduler {

    private final HolidayService holidayService;

    @Transactional
    @Scheduled(cron = "1 0 0 1 1 *")
    public void addTodayAttendance() {
        LocalDate now = LocalDate.now();
        holidayService.saveHolidays(now);
    }
}
