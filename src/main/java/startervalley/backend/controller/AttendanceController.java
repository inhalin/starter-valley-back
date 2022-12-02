package startervalley.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("/attendances")
    public BaseResponseDto<List<AttendanceDto>> getUserAttendances(@Valid @ModelAttribute AttendanceYearMonthDto attendanceYearMonthDto) {
        log.info("attendanceDto: {}", attendanceYearMonthDto);
        return attendanceService.findUserAttendances(attendanceYearMonthDto);
    }

    @GetMapping("/attendances/today")
    public BaseResponseDto<TodayAttendanceDto> checkIfCheckedToday() {
        return attendanceService.checkIfCheckedToday();
    }

    @PostMapping("/attendances/check")
    public BaseResponseDto<Void> checkAttendance(@RequestBody AttendanceCheckDto attendanceCheckDto) {
        log.info("attendanceCheckDto: {}", attendanceCheckDto);
        return attendanceService.checkAttendance(attendanceCheckDto);
    }

    @PostMapping("/attendances/excuse")
    public BaseResponseDto<Void> excuseAttendance(@RequestBody AttendanceExcuseDto attendanceExcuseDto) {
        return attendanceService.excuseAttendance(attendanceExcuseDto);
    }

    @PostMapping("/attendances/post-google-form")
    public BaseResponseDto<Void> postGoogleForm() {
        return attendanceService.sendToGoogleForm();
    }
}
