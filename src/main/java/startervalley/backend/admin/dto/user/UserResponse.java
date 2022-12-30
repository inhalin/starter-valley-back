package startervalley.backend.admin.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long generationId;
    private List<UserDto> users;
}
