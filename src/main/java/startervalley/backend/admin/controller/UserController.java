package startervalley.backend.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import startervalley.backend.admin.dto.user.UserAttendanceDto;
import startervalley.backend.admin.dto.user.UserAttendanceResponse;
import startervalley.backend.admin.dto.user.UserDropoutRequest;
import startervalley.backend.admin.dto.user.UserResponse;
import startervalley.backend.admin.service.UserService;
import startervalley.backend.dto.common.BasicResponse;

import java.time.LocalDate;
import java.util.List;

@RestController(value = "UserControllerBO")
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> listUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/{userId}/attendance")
    public ResponseEntity<UserAttendanceResponse> showAttendance(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return ResponseEntity.ok(userService.findAttendanceByUser(userId, date));
    }

    @PutMapping("/{userId}/attendance")
    public ResponseEntity<BasicResponse> updateAttendance(@PathVariable Long userId, @RequestBody UserAttendanceDto attendanceDto) {
        userService.updateAttendanceInfo(userId, attendanceDto);

        return ResponseEntity.ok(BasicResponse.of(userId, "출석 상태가 정상적으로 변경되었습니다."));
    }

    @PutMapping("/{userId}/dropout")
    public ResponseEntity<BasicResponse> quit(@PathVariable Long userId,
                                              @RequestBody UserDropoutRequest request) {
        return ResponseEntity.ok(userService.approveDropout(userId, request));
    }
}
