package startervalley.backend.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import startervalley.backend.event.alert.WebsocketAlertEventPublisher;
import startervalley.backend.event.alert.dto.AlertDto;
import startervalley.backend.handler.WebSocketHandler;
import startervalley.backend.repository.HolidayRepository;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;

import static startervalley.backend.event.alert.dto.AlertType.ATTENDANCE;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketScheduler {

    private final HolidayRepository holidayRepository;
    private final WebsocketAlertEventPublisher noticeEventPublisher;

    @Scheduled(cron = "0 55 8 ? * MON-FRI")
    public void sendAfter5Minute() throws IOException {

        if (checkIfWeekendOrHoliday(LocalDate.now())) {
            return;
        }

        AlertDto alertDto = new AlertDto(ATTENDANCE, "출석해주세요!");
        noticeEventPublisher.publishEvent(alertDto);
    }

    private boolean checkIfWeekendOrHoliday(LocalDate localDate) {
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return true;
        }

        return holidayRepository.existsByDate(localDate);
    }
}
