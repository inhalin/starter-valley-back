package startervalley.backend.dto.user;

import lombok.Builder;
import lombok.Getter;

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
    private String team;
    private String mbti;
}
