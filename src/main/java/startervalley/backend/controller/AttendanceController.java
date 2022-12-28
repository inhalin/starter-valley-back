package startervalley.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import startervalley.backend.dto.request.AttendanceCheckDto;
import startervalley.backend.dto.request.AttendanceExcuseDto;
import startervalley.backend.dto.request.AttendanceYearMonthDto;
import startervalley.backend.dto.response.AttendanceDto;
import startervalley.backend.dto.response.BaseResponseDto;
import startervalley.backend.dto.response.TodayAttendanceDto;
import startervalley.backend.security.auth.CustomUserDetails;
import startervalley.backend.service.AttendanceService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<List<AttendanceDto>> getUserAttendances(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                  @Valid @ModelAttribute AttendanceYearMonthDto attendanceYearMonthDto) {
        Long userId = userDetails.getId();
        List<AttendanceDto> dto = attendanceService.findUserAttendances(userId, attendanceYearMonthDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/today")
    public ResponseEntity<TodayAttendanceDto> checkIfCheckedToday(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        TodayAttendanceDto dto = attendanceService.checkIfAttendToday(userId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/check")
    public ResponseEntity<BaseResponseDto> checkAttendance(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                           @RequestBody AttendanceCheckDto attendanceCheckDto) {
        Long userId = userDetails.getId();
        BaseResponseDto dto = attendanceService.checkAttendance(userId, attendanceCheckDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/excuse")
    public void excuseAttendance(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @RequestBody AttendanceExcuseDto attendanceExcuseDto) {
        Long userId = userDetails.getId();
        attendanceService.excuseAttendance(userId, attendanceExcuseDto);
    }

    @PostMapping("/post-google-form")
    public void postGoogleForm(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        attendanceService.sendToGoogleForm(userId);
    }
}
