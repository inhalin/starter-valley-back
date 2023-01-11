package startervalley.backend.admin.dto.generation;

import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
public class GenerationRequest {

    @NotNull(message = "기수를 입력해주세요.")
    @Min(value = 1, message = "1기부터 입력 가능합니다.")
    private Long generation;

    @NotNull(message = "교육 시작일을 입력해주세요.")
    private LocalDate courseStartDate;

    @NotNull(message = "교육 종료일을 입력해주세요.")
    private LocalDate courseEndDate;

    private String description;
    private Double latitude;
    private Double longitude;
    private String location;
    private String recruitUrl;
    private String submitUrl;
    private String submitResultUrl;
}
