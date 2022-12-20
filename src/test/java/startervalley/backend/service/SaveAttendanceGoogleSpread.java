package startervalley.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.entity.Attendance;
import startervalley.backend.entity.AttendanceStatus;
import startervalley.backend.entity.User;
import startervalley.backend.repository.AttendanceRepository;
import startervalley.backend.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Stack;

@Transactional
@ActiveProfiles("dev")
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
    void saveUsersAttendanceFromSpreadSheet() {
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            attendanceService.saveAttendancesFromGoogleSpreadSheet(user.getId());
            attendanceService.updateConsecutiveDays(user.getId());
        }
    }
}