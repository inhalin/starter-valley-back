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
import startervalley.backend.repository.AttendanceRepository;
import startervalley.backend.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

import static startervalley.backend.entity.AttendanceStatus.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class AttendanceScheduler {

    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;

    @Transactional
    @Scheduled(cron = "1 0 0 ? * MON-FRI")
    public void addTodayAttendance() {
        List<User> userList = userRepository.findAll();
        LocalDate today = LocalDate.now();
        for (User user : userList) {
            AttendanceId attendanceId = new AttendanceId(user.getId(), today);
            Attendance attendance = Attendance.builder().id(attendanceId).user(user).build();
            attendanceRepository.save(attendance);
        }
    }

    // 15시에 자동으로 시작하는 스케쥴러
    @Transactional
    @Scheduled(cron = "0 0 15 ? * MON-FRI")
    public void setAttendanceStatusToAbsent() throws Exception {

        List<Attendance> allByStatusIsNull = attendanceRepository.findAllByStatusIsNull();
        for (Attendance attendance : allByStatusIsNull) {
            attendance.setStatus(ABSENT);
        }
    }
}
