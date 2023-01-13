package startervalley.backend.admin.dto.generation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import startervalley.backend.entity.Devpart;
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
    private String submitUrl;
    private String submitResultUrl;
    private String address1;
    private String address2;
    private Double latitude;
    private Double longitude;
    private int total;
    private long dropouts;

    private List<DevpartDto> devparts;

    public static GenerationResponse mapToResponse(Generation generation, List<User> users, List<Devpart> devparts) {
        return GenerationResponse.builder()
                .generationId(generation.getId())
                .startDate(generation.getCourseStartDate())
                .endDate(generation.getCourseEndDate())
                .description(generation.getDescription())
                .address1(generation.getAddress1())
                .address2(generation.getAddress2())
                .latitude(generation.getLatitude())
                .longitude(generation.getLongitude())
                .recruitUrl(generation.getRecruitUrl())
                .submitUrl(generation.getSubmitUrl())
                .submitResultUrl(generation.getSubmitResultUrl())
                .total(users.size())
                .dropouts(users.stream().filter(u -> !u.isActive()).count())
                .devparts(devparts.stream().map(DevpartDto::mapToDto).toList())
                .build();
    }
}
