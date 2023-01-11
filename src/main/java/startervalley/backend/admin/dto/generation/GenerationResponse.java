package startervalley.backend.admin.dto.generation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import startervalley.backend.entity.Generation;
import startervalley.backend.entity.User;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class GenerationResponse {
    private Long generationId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String description;
    private String recruitUrl;
    private String submissionUrl;
    private String location;
    private double latitude;
    private double longitude;
    private int total;
    private int dropouts;

    public static GenerationResponse mapToResponse(Generation generation, List<User> users) {
        return GenerationResponse.builder()
                .generationId(generation.getId())
                .startDate(generation.getCourseStartDate())
                .endDate(generation.getCourseEndDate())
                .description(generation.getDescription())
                .location(generation.getLocation())
                .latitude(generation.getLatitude())
                .longitude(generation.getLongitude())
                .recruitUrl(generation.getRecruitUrl())
                .submissionUrl(generation.getSubmitUrl())
                .total(users.size())
                .dropouts((int) users.stream().filter(u -> !u.isActive()).count())
                .build();
    }
}
