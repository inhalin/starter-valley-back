package startervalley.backend.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.admin.dto.dashboard.AttendanceStatisticsResponse;
import startervalley.backend.admin.dto.dashboard.AttendanceStatusUserDto;
import startervalley.backend.admin.dto.user.UserSimpleDto;
import startervalley.backend.entity.AttendanceStatus;
import startervalley.backend.entity.Generation;
import startervalley.backend.repository.generation.GenerationRepository;
import startervalley.backend.service.AttendanceService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {

    private final AttendanceService attendanceService;
    private final GenerationRepository generationRepository;

    public List<AttendanceStatisticsResponse> getAttendancesForToday() {
        List<AttendanceStatisticsResponse> response = new ArrayList<>();

        List<Generation> generations = generationRepository.findAll();
        for (Generation generation : generations) {
            List<AttendanceStatusUserDto> userStatusForToday = attendanceService.getStatusForTodayByGenerationId(generation.getId());
            List<UserSimpleDto> presentUsers = new ArrayList<>();
            List<UserSimpleDto> lateUsers = new ArrayList<>();
            List<UserSimpleDto> absentUsers = new ArrayList<>();

            long presentCount = userStatusForToday.stream().filter(userStatusDto -> {
                if (userStatusDto.getStatus().equals(AttendanceStatus.PRESENT.name())) {
                    presentUsers.add(UserSimpleDto.of(userStatusDto.getUser_id(), userStatusDto.getName()));
                }
                return userStatusDto.getStatus().equals(AttendanceStatus.PRESENT.name());
            }).count();

            long lateCount = userStatusForToday.stream().filter(userStatusDto -> {
                if (userStatusDto.getStatus().equals(AttendanceStatus.LATE.name())) {
                    lateUsers.add(UserSimpleDto.of(userStatusDto.getUser_id(), userStatusDto.getName()));
                }

                return userStatusDto.getStatus().equals(AttendanceStatus.LATE.name());
            }).count();

            long absentCount = userStatusForToday.stream().filter(userStatusDto -> {
                if (userStatusDto.getStatus().equals(AttendanceStatus.ABSENT.name())) {
                    absentUsers.add(UserSimpleDto.of(userStatusDto.getUser_id(), userStatusDto.getName()));
                }

                return userStatusDto.getStatus().equals(AttendanceStatus.ABSENT.name());
            }).count();

            AttendanceStatisticsResponse statistics = AttendanceStatisticsResponse.builder()
                    .generationId(generation.getId())
                    .total(userStatusForToday.size())
                    .present(AttendanceStatisticsResponse.UserStatus.of(presentCount, presentUsers))
                    .late(AttendanceStatisticsResponse.UserStatus.of(lateCount, lateUsers))
                    .absent(AttendanceStatisticsResponse.UserStatus.of(absentCount, absentUsers))
                    .build();

            response.add(statistics);
        }

        return response;
    }
}
