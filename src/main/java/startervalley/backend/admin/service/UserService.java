package startervalley.backend.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.admin.dto.user.*;
import startervalley.backend.dto.common.BasicResponse;
import startervalley.backend.dto.request.AttendanceYearMonthDto;
import startervalley.backend.entity.AdminUser;
import startervalley.backend.entity.Attendance;
import startervalley.backend.entity.Generation;
import startervalley.backend.entity.User;
import startervalley.backend.exception.CustomValidationException;
import startervalley.backend.exception.ResourceNotFoundException;
import startervalley.backend.exception.UserNotValidException;
import startervalley.backend.repository.user.UserRepository;
import startervalley.backend.repository.adminuser.AdminUserRepository;
import startervalley.backend.repository.attendance.AttendanceRepository;
import startervalley.backend.repository.generation.GenerationRepository;
import startervalley.backend.service.AttendanceService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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
            List<UserDto> users = generation.getUsers()
                    .stream()
                    .filter(User::isActive)
                    .map(user -> UserDto.mapToDto(user, attendanceService.findUserAttendances(user.getId(), new AttendanceYearMonthDto())))
                    .toList();

            responseList.add(UserResponse.mapToResponse(generation, users));
        }

        return responseList;
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

    @Transactional
    public BasicResponse approveDropout(Long userId, UserDropoutRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        if (!user.isActive()) {
            throw new CustomValidationException("이미 중도 하차한 수강생입니다.");
        }

        user.dropout(request.getDropoutDate(), request.getReason());

        return BasicResponse.of(userId, "중도 하차생이 정상적으로 등록되었습니다.");
    }

    public List<UserDropoutResponse> findAllDropouts() {

        List<Generation> generations = generationRepository.findAll();
        List<UserDropoutResponse> responseList = new ArrayList<>();

        for (Generation generation : generations) {
            List<UserDropoutDto> users = generation.getUsers()
                    .stream()
                    .filter(user -> !user.isActive())
                    .map(UserDropoutDto::mapToDto)
                    .toList();

            responseList.add(UserDropoutResponse.mapToResponse(generation, users));
        }

        return responseList;
    }

    public UserDropoutDto findOneDropout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        if (user.isActive()) {
            throw new UserNotValidException("중도 하차한 수강생이 아닙니다.");
        }

        return UserDropoutDto.mapToDto(user);
    }

    @Transactional
    public BasicResponse updateDropoutInfo(Long userId, UserDropoutRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        if (request.getDropoutDate().equals(user.getDropoutDate()) && request.getReason().equals(user.getDropoutReason())) {
            return BasicResponse.of(user.getId(), "변경된 내용이 없습니다.");
        }

        user.updateDropout(request.getDropoutDate(), request.getReason());

        return BasicResponse.of(user.getId(), "중도 하차 상세 내용이 정상적으로 변경되었습니다.");
    }

    public List<UserSimpleDto> listAvailableForTeamByGenerationId(Long generationId) {
        return userRepository.listAvailableForTeamByGenerationId(generationId)
                .stream()
                .map(user -> UserSimpleDto.of(user.getId(), user.getName()))
                .toList();
    }

    @Transactional
    public void deleteAdminUser(Long loginId, Long id) {

        if (loginId != 1) {
            throw new UserNotValidException("최고 관리자만 운영진 삭제가 가능합니다");
        }

        if (id == 1) {
            throw new UserNotValidException("최고 관리자는 삭제할 수 없습니다.");
        }

        AdminUser adminUser = adminUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AdminUser", "id", id.toString()));

        log.info("관리자 삭제: id = {}, username = {}",adminUser.getId(), adminUser.getUsername());

        adminUserRepository.delete(adminUser);
    }
}
