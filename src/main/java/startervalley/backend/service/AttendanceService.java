package startervalley.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.constant.ResponseMessage;
import startervalley.backend.dto.request.AttendanceCheckDto;
import startervalley.backend.dto.request.AttendanceExcuseDto;
import startervalley.backend.dto.request.AttendanceYearMonthDto;
import startervalley.backend.dto.response.AttendanceDto;
import startervalley.backend.dto.response.BaseResponseDto;
import startervalley.backend.entity.*;
import startervalley.backend.exception.AttendanceAlreadyPresentException;
import startervalley.backend.exception.AttendanceOutOfLimitedRangeException;
import startervalley.backend.repository.AttendanceRepository;
import startervalley.backend.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static startervalley.backend.constant.ResponseMessage.ATTENDANCE_LIST;
import static startervalley.backend.entity.AttendanceStatus.LATE;
import static startervalley.backend.entity.AttendanceStatus.PRESENT;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AttendanceService {

    private final static int LIMITED_RANGE = 100;
    private final static LocalTime ABSENT_TIME = LocalTime.of(9, 0);
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;

    public BaseResponseDto<List<AttendanceDto>> findUserAttendances(AttendanceYearMonthDto attendanceYearMonthDto) {
        User user = userRepository.findById(1L).orElseThrow();
        LocalDate currentDate = getCurrentDate(attendanceYearMonthDto.getYear(), attendanceYearMonthDto.getMonth());
        List<Attendance> attendances = attendanceRepository.findAllByUserForMonth(user.getId(), currentDate.getYear(), currentDate.getMonthValue());
        List<AttendanceDto> result = attendances.stream().map(attendance ->
                        new AttendanceDto(attendance.getId().getAttendedDate(), attendance.getStatus()))
                .collect(Collectors.toList());

        return new BaseResponseDto<>(ATTENDANCE_LIST.toString(), result);
    }

    @Transactional
    public BaseResponseDto<Void> checkAttendance(AttendanceCheckDto attendanceCheckDto) {
        User user = userRepository.findById(1L).orElseThrow();
        Generation generation = user.getGeneration();

        checkRange(generation.getLatitude(), generation.getLongitude(), attendanceCheckDto.getLatitude(), attendanceCheckDto.getLongtitude());

        LocalDate today = LocalDate.now();
        AttendanceId attendanceId = new AttendanceId(user.getId(), today);
        Attendance attendance = attendanceRepository.findById(attendanceId).orElseThrow();

        AttendanceStatus status = checkIfOverAbsentTime(attendance);

        attendance.setStatus(status);

        return new BaseResponseDto<>(status.toString(), null);
    }

    @Transactional
    public BaseResponseDto<Void> excuseAttendance(AttendanceExcuseDto attendanceExcuseDto) {
        User user = userRepository.findById(1L).orElseThrow();
        LocalDate today = LocalDate.now();
        AttendanceId attendanceId = new AttendanceId(user.getId(), today);
        Attendance attendance = attendanceRepository.findById(attendanceId).orElseThrow();

        if (attendance.getStatus() == PRESENT) {
            throw new AttendanceAlreadyPresentException("이미 출석되었습니다.");
        }

        String description = attendanceExcuseDto.getDescription();
        attendance.setReason(description);
        return new BaseResponseDto<>(ResponseMessage.ATTENDANCE_EXCUSE.toString(), null);
    }

    private LocalDate getCurrentDate(Integer month, Integer year) {
        if (month == null || year == null) {
            return LocalDate.now();
        }
        return LocalDate.of(month, year, 1);
    }

    private AttendanceStatus checkIfOverAbsentTime(Attendance attendance) {
        LocalTime now = LocalTime.now();
        return now.isAfter(ABSENT_TIME) ? LATE : PRESENT;
    }

    private void checkRange(double x1, double y1, double x2, double y2) {
        int distance = distanceInMeterByHaversine(x1, y1, x2, y2);
        if (distance > LIMITED_RANGE)
            throw new AttendanceOutOfLimitedRangeException("범위는 " + LIMITED_RANGE + "m 안으로 가능합니다.");
    }

    private int distanceInMeterByHaversine(double x1, double y1, double x2, double y2) {
        int distance;
        double radius = 6371; // 지구 반지름(km)
        double toRadian = Math.PI / 180;

        double deltaLatitude = Math.abs(x1 - x2) * toRadian;
        double deltaLongitude = Math.abs(y1 - y2) * toRadian;

        double sinDeltaLat = Math.sin(deltaLatitude / 2);
        double sinDeltaLng = Math.sin(deltaLongitude / 2);
        double squareRoot = Math.sqrt(
                sinDeltaLat * sinDeltaLat +
                        Math.cos(x1 * toRadian) * Math.cos(x2 * toRadian) * sinDeltaLng * sinDeltaLng);

        distance = (int) (2 * radius * Math.asin(squareRoot) * 1000);
        return distance;
    }
}
