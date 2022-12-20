package startervalley.backend.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSelfProfileDto {
    private String name;
    private Long generationId;
    private Integer consecutiveDays;
    private String imageUrl;
    private String devpart;
    private Integer presentDays;
    private Integer lateDays;
    private Integer absentDays;
}
