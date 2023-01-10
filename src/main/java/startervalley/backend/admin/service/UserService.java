package startervalley.backend.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.admin.dto.user.*;
import startervalley.backend.dto.request.AttendanceYearMonthDto;
import startervalley.backend.entity.Attendance;
import startervalley.backend.entity.Generation;
import startervalley.backend.entity.User;
import startervalley.backend.exception.ResourceNotFoundException;
import startervalley.backend.repository.UserRepository;
import startervalley.backend.repository.attendance.AttendanceRepository;
import startervalley.backend.repository.generation.GenerationRepository;
import startervalley.backend.repository.adminuser.AdminUserRepository;
import startervalley.backend.service.AttendanceService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service(value = "UserServiceBO")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final GenerationRepository generationRepository;
    private final AttendanceService attendanceService;
    private final AttendanceRepository attendanceRepository;
    private final AdminUserRepository adminUserRepository;
    private final UserRepository userRepository;

    public List<UserResponse> findAllUsers() {

        List<Generation> generations = generationRepository.findAll();
        List<UserResponse> responseList = new ArrayList<>();

        for (Generation generation : generations) {
            List<UserDto> users = findUsersByGeneration(generation);

            responseList.add(UserResponse.builder()
                    .generationId(generation.getId())
                    .users(users)
                    .build());
        }

        return responseList;
    }

    private List<UserDto> findUsersByGeneration(Generation generation) {
        return generation.getUsers()
                .stream()
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .devpart(user.getDevpart().getName())
                        .attendance(attendanceService.findUserAttendances(user.getId(), new AttendanceYearMonthDto()))
                        .build())
                .toList();
    }


    public UserAttendanceResponse findAttendanceByUser(Long userId, LocalDate attendedDate) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));
        Attendance attendance = attendanceRepository.findByUserIdAndAttendedDate(userId, attendedDate);

        if (attendance == null) {
            throw new ResourceNotFoundException("Attendance", "attendedDate", attendedDate.toString());
        }

        return UserAttendanceResponse.builder()
                .id(user.getId())
                .date(attendance.getId().getAttendedDate())
                .name(user.getName())
                .status(attendance.getStatus())
                .reason(attendance.getReason())
                .adminMemo(attendance.getAdminMemo())
                .build();
    }

    @Transactional
    public void updateAttendanceInfo(Long userId, UserAttendanceDto attendanceDto) {
        attendanceRepository.updateStatusWithMemo(userId,
                attendanceDto.getDate(),
                attendanceDto.getStatus().name(),
                attendanceDto.getAdminMemo());
    }

    public List<AdminUserDto> findAdminUsers() {
        return adminUserRepository.findAll()
                .stream()
                .map(admin -> AdminUserDto.builder()
                        .id(admin.getId())
                        .username(admin.getUsername())
                        .name(admin.getName())
                        .phone(admin.getPhone())
                        .email(admin.getEmail())
                        .build())
                .toList();
    }
}
