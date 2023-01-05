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
            List<AttendanceStatusUserDto> userStatus = attendanceService.getStatusForTodayByGenerationId(generation.getId());

            List<UserSimpleDto> present = userStatus.stream()
                    .filter(status -> status.getStatus() != null && status.getStatus().equals(AttendanceStatus.PRESENT.name()))
                    .map(us -> UserSimpleDto.of(us.getUser_id(), us.getName()))
                    .toList();

            List<UserSimpleDto> late = userStatus.stream()
                    .filter(status -> status.getStatus() != null && status.getStatus().equals(AttendanceStatus.LATE.name()))
                    .map(us -> UserSimpleDto.of(us.getUser_id(), us.getName()))
                    .toList();

            List<UserSimpleDto> absent = userStatus.stream()
                    .filter(status -> status.getStatus() != null && status.getStatus().equals(AttendanceStatus.ABSENT.name()))
                    .map(us -> UserSimpleDto.of(us.getUser_id(), us.getName()))
                    .toList();

            List<UserSimpleDto> unchecked = userStatus.stream()
                    .filter(status -> status.getStatus() == null)
                    .map(user -> UserSimpleDto.of(user.getUser_id(), user.getName()))
                    .toList();

            AttendanceStatisticsResponse statistics = AttendanceStatisticsResponse.builder()
                    .generationId(generation.getId())
                    .total(userStatus.size())
                    .present(AttendanceStatisticsResponse.UserStatus.of(present, present.size()))
                    .late(AttendanceStatisticsResponse.UserStatus.of(late, late.size()))
                    .absent(AttendanceStatisticsResponse.UserStatus.of(absent, absent.size()))
                    .unchecked(AttendanceStatisticsResponse.UserStatus.of(unchecked, unchecked.size()))
                    .build();

            response.add(statistics);
        }

        return response;
    }
}
