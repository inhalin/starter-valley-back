package startervalley.backend.admin.dto.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import startervalley.backend.entity.Team;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class TeamResponse {
    private Long generation;
    private Long teamId;
    private String name;
    private String notionUrl;
    private String releaseUrl;
    private String description;
    private List<TeamUserDto> users;

    public static TeamResponse mapToResponse(Team team, List<TeamUserDto> teamUserDtos) {
        return TeamResponse.builder()
                .generation(team.getGeneration().getId())
                .teamId(team.getId())
                .name(team.getName())
                .notionUrl(team.getNotionUrl())
                .releaseUrl(team.getReleaseUrl())
                .description(team.getDescription())
                .users(teamUserDtos)
                .build();
    }
}
