package startervalley.backend.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import startervalley.backend.entity.Attendance;
import startervalley.backend.repository.attendance.AttendanceRepository;

import java.util.List;

@SpringBootTest
class AttendanceRepositoryTest {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Test
    @DisplayName("출석 코드 보기")
    void getAttendanceCode() {
        String attendanceCode = Attendance.getAttendanceCode();
        System.out.println(attendanceCode);
    }

    @Test
    @DisplayName("출석 상태가 NULL인 데이터들 출력")
    void getAttendancesIsNull() {
        List<Attendance> result = attendanceRepository.findAllByStatusIsNull();
        System.out.println(result);
    }
}