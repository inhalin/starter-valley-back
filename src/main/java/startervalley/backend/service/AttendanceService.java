package startervalley.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import startervalley.backend.admin.dto.dashboard.AttendanceStatusUserDto;
import startervalley.backend.dto.attendance.AttendanceCodeDto;
import startervalley.backend.dto.request.AttendanceCheckDto;
import startervalley.backend.dto.request.AttendanceExcuseDto;
import startervalley.backend.dto.request.AttendanceYearMonthDto;
import startervalley.backend.dto.response.AttendanceDto;
import startervalley.backend.dto.response.BaseResponseDto;
import startervalley.backend.dto.response.TodayAttendanceDto;
import startervalley.backend.entity.*;
import startervalley.backend.exception.AttendanceAlreadyPresentException;
import startervalley.backend.exception.AttendanceOutOfRangeException;
import startervalley.backend.exception.AttendanceWeekendException;
import startervalley.backend.exception.ResourceNotFoundException;
import startervalley.backend.repository.attendance.AttendanceRepository;
import startervalley.backend.repository.HolidayRepository;
import startervalley.backend.repository.user.UserRepository;
import startervalley.backend.util.GoogleSpreadSheet;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static startervalley.backend.entity.AttendanceStatus.LATE;
import static startervalley.backend.entity.AttendanceStatus.PRESENT;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AttendanceService {

    private final GoogleSpreadSheet googleSpreadSheet;
    private static final int LIMITED_RANGE = 100;
    private static final LocalTime LATE_TIME = LocalTime.of(9, 1);


    @Value("${google-form.key}")
    private String GOOGLE_FORM_KEY;
    @Value("${google-form.entry.name}")
    private String entryName;
    @Value("${google-form.entry.reason}")
    private String entryReason;
    @Value("${google-form.entry.status}")
    private String entryStatus;

    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final HolidayRepository holidayRepository;
    private final WebClient webClient;

    public List<AttendanceDto> findUserAttendances(Long userId, AttendanceYearMonthDto attendanceYearMonthDto) {
        User user = getUserOrElseThrow(userId);
        LocalDate currentDate = getDate(attendanceYearMonthDto.getYear(), attendanceYearMonthDto.getMonth());
        List<Attendance> attendances;
        attendances = (currentDate == null) ? attendanceRepository.findAllByUser(user) :
                attendanceRepository.findAllByUserForMonth(user.getId(), currentDate.getYear(), currentDate.getMonthValue());

        return attendances.stream().map(attendance ->
                        new AttendanceDto(attendance.getId().getAttendedDate(), attendance.getStatus()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveAttendancesFromGoogleSpreadSheet(Long userId) {
        User user = getUserOrElseThrow(userId);
        String name = user.getName();
        try {
            List<AttendanceDto> attendanceDtos = googleSpreadSheet.makeAttendanceList(name);

            for (AttendanceDto attendanceDto : attendanceDtos) {
                AttendanceId attendanceId = new AttendanceId(userId, attendanceDto.getLocalDate());
                Attendance attendance = Attendance.builder()
                        .id(attendanceId)
                        .user(user)
                        .status(attendanceDto.getAttendanceStatus())
                        .build();
                attendanceRepository.save(attendance);
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    public void updateConsecutiveDays(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        List<Attendance> attendanceList = attendanceRepository.findAllByUserOrderByDate(user.getId());
        int consecutiveDays = 0;
        for (Attendance attendance : attendanceList) {
            if (attendance.getStatus() == AttendanceStatus.ABSENT) {
                break;
            }
            consecutiveDays += 1;
        }
        user.setConsecutiveDays(consecutiveDays);
    }

    @Transactional
    public BaseResponseDto doAttendance(Long userId, AttendanceCheckDto attendanceCheckDto) {
        User user = getUserOrElseThrow(userId);
        Generation generation = user.getGeneration();
        LocalDate today = LocalDate.now();

        checkIfWeekendOrHolidayOrElseThrow(today);
        checkRangeOrElseThrow(generation.getLatitude(), generation.getLongitude(), attendanceCheckDto.getLatitude(), attendanceCheckDto.getLongtitude());

        AttendanceId attendanceId = new AttendanceId(user.getId(), today);
        Attendance attendance = attendanceRepository.findById(attendanceId).orElseThrow();

        checkIfAlreadyAttendOrElseThrow(attendance.getStatus());

        AttendanceStatus status = checkIfOverPresentTime();
        attendance.setStatus(status);
        attendance.setAttendanceTime(LocalTime.now());
        user.setConsecutiveDays(user.getConsecutiveDays() + 1);
        return new BaseResponseDto(status);
    }

    @Transactional
    public BaseResponseDto doAttendanceByCode(Long userId, AttendanceCodeDto attendanceCodeDto) {
        User user = getUserOrElseThrow(userId);
        String inputCode = attendanceCodeDto.getCode();
        if (!inputCode.equals(Attendance.getAttendanceCode())) {
            return new BaseResponseDto("ATTENDANCE CODE INCORRECT");
        }
        LocalDate today = LocalDate.now();

        checkIfWeekendOrHolidayOrElseThrow(today); // 휴일인지 체크

        AttendanceId attendanceId = new AttendanceId(user.getId(), today);
        Attendance attendance = attendanceRepository.findById(attendanceId).orElseThrow();

        checkIfAlreadyAttendOrElseThrow(attendance.getStatus());

        AttendanceStatus status = checkIfOverPresentTime();
        attendance.setStatus(status);
        attendance.setAttendanceTime(LocalTime.now());
        user.setConsecutiveDays(user.getConsecutiveDays() + 1);
        return new BaseResponseDto(status);
    }

    private void checkIfWeekendOrHolidayOrElseThrow(LocalDate today) {
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            throw new AttendanceWeekendException("WEEKEND CAN NOT ATTEND");
        }

        if (holidayRepository.existsByDate(today)) {
            throw new AttendanceWeekendException("HOLIDAY CAN NOT ATTEND");
        }
    }

    @Transactional
    public void excuseAttendance(Long userId, AttendanceExcuseDto attendanceExcuseDto) {
        User user = getUserOrElseThrow(userId);
        LocalDate today = LocalDate.now();
        AttendanceId attendanceId = new AttendanceId(user.getId(), today);
        Attendance attendance = attendanceRepository.findById(attendanceId).orElseThrow();

        if (attendance.getReason() != null) {
            throw new AttendanceAlreadyPresentException("이미 사유를 적었습니다.");
        }

        String description = attendanceExcuseDto.getDescription();
        attendance.setReason(description);
    }

    @Transactional
    public TodayAttendanceDto checkIfAttendToday(Long userId) {
        User user = getUserOrElseThrow(userId);

        LocalDate today = LocalDate.now();
        checkIfWeekendOrHolidayOrElseThrow(today);
        AttendanceId attendanceId = new AttendanceId(user.getId(), today);
        Optional<Attendance> optional = attendanceRepository.findById(attendanceId);
        Attendance attendance;
        if (optional.isEmpty()) {
            attendance = Attendance.builder().id(attendanceId).user(user).build();
            attendanceRepository.save(attendance);
        } else {
            attendance = optional.get();
        }

        TodayAttendanceDto todayAttendanceDto = new TodayAttendanceDto();
        try {
            todayAttendanceDto.setChecked(checkIfAlreadyAttendOrElseThrow(attendance.getStatus()));
        } catch (AttendanceAlreadyPresentException ignored) {
        }
        todayAttendanceDto.setNeedReason(checkNeedReason(attendance));
        return todayAttendanceDto;
    }

    public void sendToGoogleForm(Long userId) {
        User user = getUserOrElseThrow(userId);
        LocalDate today = LocalDate.now();
        AttendanceId attendanceId = new AttendanceId(user.getId(), today);
        Attendance attendance = attendanceRepository.findById(attendanceId).orElseThrow();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        String name = user.getName();
        params.add(entryName, name);
        String attendanceStatus;
        attendanceStatus = switch (attendance.getStatus()) {
            case PRESENT -> "정상 출석 (~09:00)";
            case LATE -> {
                params.add(entryReason, attendance.getReason());
                yield "지각 (09:01~09:30)";
            }
            case ABSENT -> {
                params.add(entryReason, attendance.getReason());
                yield "결석 (09:30~)";
            }
        };
        params.add(entryStatus, attendanceStatus);

        webClient.post()
                .uri("https://docs.google.com/forms/d/e/{key}/formResponse", GOOGLE_FORM_KEY)
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    private User getUserOrElseThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", String.valueOf(userId)));
    }

    private LocalDate getDate(Integer month, Integer year) {
        if (month == null || year == null) {
            return null;
        }
        return LocalDate.of(month, year, 1);
    }

    private boolean checkIfAlreadyAttendOrElseThrow(AttendanceStatus status) {
        if (status != null) {
            throw new AttendanceAlreadyPresentException("이미 출석되었습니다.");
        }
        return false;
    }

    private boolean checkNeedReason(Attendance attendance) {
        if (attendance.getStatus() == PRESENT || attendance.getStatus() == null) {
            return false;
        }
        return attendance.getReason() == null;
    }

    private AttendanceStatus checkIfOverPresentTime() {
        LocalTime now = LocalTime.now();
        return now.isAfter(LATE_TIME) ? LATE : PRESENT;
    }

    private void checkRangeOrElseThrow(double x1, double y1, double x2, double y2) {
        int distance = distanceInMeterByHaversine(x1, y1, x2, y2);
        if (distance > LIMITED_RANGE)
            throw new AttendanceOutOfRangeException("범위는 " + LIMITED_RANGE + "m 안으로 가능합니다.");
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

    public List<AttendanceStatusUserDto> getStatusForTodayByGenerationId(Long generationId) {
        return attendanceRepository.findStatusByDateAndGenerationId(LocalDate.now(), generationId);
    }
}
