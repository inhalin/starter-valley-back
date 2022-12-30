package startervalley.backend.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.entity.User;
import startervalley.backend.repository.attendance.AttendanceRepository;
import startervalley.backend.repository.UserRepository;

import java.util.List;

@Transactional
@ActiveProfiles("local")
@SpringBootTest
public class SaveAttendanceGoogleSpread {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Test
    @DisplayName("구글 스프레드 시트에서 사용자 출석 저장해주기")
    @Rollback(value = false)
    @Commit
    @Disabled
    void saveUsersAttendanceFromSpreadSheet() {
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            attendanceService.saveAttendancesFromGoogleSpreadSheet(user.getId());
            attendanceService.updateConsecutiveDays(user.getId());
        }
    }
}