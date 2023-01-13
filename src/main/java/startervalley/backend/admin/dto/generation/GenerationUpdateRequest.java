package startervalley.backend.admin.dto.generation;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
public class GenerationUpdateRequest {

    @NotNull(message = "교육 시작일을 입력해주세요.")
    private LocalDate courseStartDate;

    @NotNull(message = "교육 종료일을 입력해주세요.")
    private LocalDate courseEndDate;

    private String description;
    private Double latitude;
    private Double longitude;
    private String address1;
    private String address2;
    private String recruitUrl;
    private String submitUrl;
    private String submitResultUrl;
}
