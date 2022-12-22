package startervalley.backend.dto.lunchbus;

import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
public class LunchbusInsertRequest {

    @NotEmpty(message = "제목은 필수입니다.")
    private String title;

    @NotEmpty(message = "상세 설명을 필수입니다.")
    @Size(max = 300, message = "상세 설명은 300자를 초과할 수 없습니다.")
    private String description;

    @Min(value = 2, message = "최소 인원은 2명입니다.")
    @Max(value = 9, message = "최대 인원은 9명입니다.")
    private Integer limit;

    private String storeName;
    private String storeUrl;
}
