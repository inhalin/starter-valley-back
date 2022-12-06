package startervalley.backend.dto.user;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserProfileReadDto {
    private String imageUrl;
    private String name;
    private Boolean isLeader;
    private String devpart;
    private Long generationId;
    private List<UserProfileInfoMap> mainInfo;
    private List<UserProfileInfoMap> subInfo;
}
