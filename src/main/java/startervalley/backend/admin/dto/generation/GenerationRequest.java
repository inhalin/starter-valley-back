package startervalley.backend.admin.dto.generation;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Getter
public class GenerationRequest {

    @NotEmpty
    private Long id;

    private LocalDate courseStartDate;
    private LocalDate courseEndDate;
    private String description;
    private double latitude;
    private double longitude;
    private String location;
    private String recruitUrl;
    private String submitUrl;
}
