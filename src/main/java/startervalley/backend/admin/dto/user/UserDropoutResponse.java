package startervalley.backend.admin.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import startervalley.backend.entity.Generation;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class UserDropoutResponse {
    private Long generationId;
    private List<UserDropoutDto> users;

    public static UserDropoutResponse mapToResponse(Generation generation, List<UserDropoutDto> users) {
        return UserDropoutResponse.builder()
                .generationId(generation.getId())
                .users(users)
                .build();
    }
}
