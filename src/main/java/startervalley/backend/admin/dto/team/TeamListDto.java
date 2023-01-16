package startervalley.backend.admin.dto.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import startervalley.backend.entity.Generation;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class TeamListDto {
    private Long generation;
    private List<TeamSimpleDto> teams;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class TeamSimpleDto {
        private Long teamId;
        private String name;
        private String notionUrl;
        private String releaseUrl;
        private String description;
    }

    public static TeamListDto mapToDto(Generation generation) {
        return TeamListDto.builder()
                .generation(generation.getId())
                .teams(generation.getTeams().stream()
                        .map(team -> TeamSimpleDto.builder()
                                .teamId(team.getId())
                                .name(team.getName())
                                .notionUrl(team.getNotionUrl())
                                .releaseUrl(team.getReleaseUrl())
                                .description(team.getDescription())
                                .build())
                        .toList())
                .build();
    }
}
