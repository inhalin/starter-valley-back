package startervalley.backend.admin.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import startervalley.backend.admin.dto.user.UserSimpleDto;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class AttendanceStatisticsResponse {
    private Long generationId;
    private int total;
    private UserStatus present;
    private UserStatus late;
    private UserStatus absent;
    private UserStatus unchecked;

    @Getter
    @AllArgsConstructor(staticName = "of")
    public static class UserStatus {
        private List<UserSimpleDto> users;
        private long count;
    }
}
