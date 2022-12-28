package startervalley.backend.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.entity.Attendance;
import startervalley.backend.entity.AttendanceId;
import startervalley.backend.entity.AttendanceStatus;
import startervalley.backend.entity.User;
import startervalley.backend.exception.AttendanceWeekendException;
import startervalley.backend.repository.AttendanceRepository;
import startervalley.backend.repository.HolidayRepository;
import startervalley.backend.repository.UserRepository;
import startervalley.backend.service.AttendanceService;
import startervalley.backend.service.HolidayService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static startervalley.backend.entity.AttendanceStatus.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class AttendanceScheduler {

    private final HolidayRepository holidayRepository;
    private final UserRepository userRepository;
    private final AttendanceService attendanceService;
    private final AttendanceRepository attendanceRepository;

    @Transactional
    @Scheduled(cron = "1 0 0 ? * MON-FRI")
    public void addTodayAttendance() {
        LocalDate today = LocalDate.now();

        if (checkIfWeekendOrHoliday(today)) {
            return;
        }

        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            AttendanceId attendanceId = new AttendanceId(user.getId(), today);
            Attendance attendance = Attendance.builder().id(attendanceId).user(user).build();
            attendanceRepository.save(attendance);
        }
    }

    // 15시에 자동으로 시작하는 스케쥴러
    @Transactional
    @Scheduled(cron = "0 0 15 ? * MON-FRI")
    public void setAttendanceStatusToAbsent() {

        if (checkIfWeekendOrHoliday(LocalDate.now())) {
            return;
        }

        List<Attendance> allByStatusIsNull = attendanceRepository.findAllByStatusIsNull();
        for (Attendance attendance : allByStatusIsNull) {
            attendance.setStatus(ABSENT);
            attendance.getUser().setConsecutiveDays(0);
        }
    }

    @Transactional
    @Scheduled(cron = "0 5 15 ? * MON-FRI")
    public void updateConsecutiveDays() {
        List<User> userList = userRepository.findAll();
        userList.forEach(u -> attendanceService.updateConsecutiveDays(u.getId()));
    }

    private boolean checkIfWeekendOrHoliday(LocalDate localDate) {
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return true;
        }

        if (holidayRepository.existsByDate(localDate)) {
            return true;
        }

        return false;
    }
}
