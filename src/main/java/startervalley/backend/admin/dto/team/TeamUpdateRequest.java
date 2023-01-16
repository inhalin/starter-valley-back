package startervalley.backend.admin.dto.team;

import lombok.Getter;

@Getter
public class TeamUpdateRequest {
    private String name;
    private String description;
    private String notionUrl;
    private String releaseUrl;
}
