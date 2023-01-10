package startervalley.backend.admin.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import startervalley.backend.dto.response.AttendanceDto;
import startervalley.backend.entity.User;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String devpart;
    private List<AttendanceDto> attendance;

    public static UserDto mapToDto(User user, List<AttendanceDto> attendanceDtos) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .devpart(user.getDevpart().getName())
                .attendance(attendanceDtos)
                .build();
    }
}
