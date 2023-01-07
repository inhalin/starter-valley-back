package startervalley.backend.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import startervalley.backend.admin.dto.dashboard.AttendanceStatisticsResponse;
import startervalley.backend.admin.service.StatisticsService;
import startervalley.backend.entity.Attendance;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/dashboards")
public class DashboardController {

    private final StatisticsService statisticsService;

    @GetMapping("/statistics/attendance")
    public ResponseEntity<List<AttendanceStatisticsResponse>> todayAttendance() {
        return ResponseEntity.ok(statisticsService.getAttendancesForToday());
    }

    @GetMapping("/attendance-code")
    public ResponseEntity<String> getAttendanceCode() {
        return ResponseEntity.ok(Attendance.getAttendanceCode());
    }

}
