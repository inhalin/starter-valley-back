package startervalley.backend.dto.user;

import lombok.Builder;
import lombok.Getter;
import startervalley.backend.entity.Team;

@Getter
@Builder
public class UserCardDto {
    private String username;
    private String name;
    private String devpart;
    private Long generationId;
    private String email;
    private Boolean isLeader;
    private String intro;
    private String githubUrl;
    private String imageUrl;
    private Team team;
    private String mbti;
}
