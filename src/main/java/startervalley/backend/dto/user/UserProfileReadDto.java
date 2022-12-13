package startervalley.backend.dto.user;

import lombok.AllArgsConstructor;
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
    private List<InfoMap> mainInfo;
    private List<InfoMap> subInfo;

    @Getter
    @AllArgsConstructor(staticName = "of")
    @Builder
    public static class InfoMap {
        private String title;
        private String description;
    }
}