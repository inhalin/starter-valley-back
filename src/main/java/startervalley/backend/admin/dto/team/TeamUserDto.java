package startervalley.backend.admin.dto.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import startervalley.backend.entity.User;

@Getter
@AllArgsConstructor
@Builder
public class TeamUserDto {
    private Long userId;
    private String name;
    private boolean leader;
    private boolean active;

    public static TeamUserDto mapToDto(User user) {
        return TeamUserDto.builder()
                .userId(user.getId())
                .name(user.getName())
                .leader(user.getIsLeader())
                .active(user.isActive())
                .build();
    }
}
