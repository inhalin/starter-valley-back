package startervalley.backend.admin.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import startervalley.backend.entity.Generation;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long generationId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private List<UserDto> users;

    public static UserResponse mapToResponse(Generation generation, List<UserDto> users) {
        return UserResponse.builder()
                .generationId(generation.getId())
                .startDate(generation.getCourseStartDate())
                .endDate(generation.getCourseEndDate())
                .users(users)
                .build();
    }
}
