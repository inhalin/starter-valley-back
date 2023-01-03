package startervalley.backend.admin.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import startervalley.backend.dto.response.AttendanceDto;

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
}
