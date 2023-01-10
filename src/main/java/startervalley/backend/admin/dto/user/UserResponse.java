package startervalley.backend.admin.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import startervalley.backend.entity.Generation;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long generationId;
    private List<UserDto> users;

    public static UserResponse mapToResponse(Generation generation, List<UserDto> users) {
        return UserResponse.builder()
                .generationId(generation.getId())
                .users(users)
                .build();
    }
}
