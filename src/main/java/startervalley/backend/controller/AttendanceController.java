package startervalley.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import startervalley.backend.dto.request.AttendanceCheckDto;
import startervalley.backend.dto.request.AttendanceExcuseDto;
import startervalley.backend.dto.request.AttendanceYearMonthDto;
import startervalley.backend.dto.response.AttendanceDto;
import startervalley.backend.dto.response.BaseResponseDto;
import startervalley.backend.dto.response.TodayAttendanceDto;
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
    public ResponseEntity<List<AttendanceDto>> getUserAttendances(@Valid @ModelAttribute AttendanceYearMonthDto attendanceYearMonthDto) {
        List<AttendanceDto> dto = attendanceService.findUserAttendances(attendanceYearMonthDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/today")
    public ResponseEntity<TodayAttendanceDto> checkIfCheckedToday() {
        TodayAttendanceDto dto = attendanceService.checkIfCheckedToday();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/check")
    public ResponseEntity<BaseResponseDto> checkAttendance(@RequestBody AttendanceCheckDto attendanceCheckDto) {
        BaseResponseDto dto = attendanceService.checkAttendance(attendanceCheckDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/excuse")
    public void excuseAttendance(@RequestBody AttendanceExcuseDto attendanceExcuseDto) {
        attendanceService.excuseAttendance(attendanceExcuseDto);
    }

    @PostMapping("/post-google-form")
    public void postGoogleForm() {
        attendanceService.sendToGoogleForm();
    }
}
