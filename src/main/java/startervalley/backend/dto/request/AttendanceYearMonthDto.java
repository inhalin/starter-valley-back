package startervalley.backend.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;

@Data
public class AttendanceYearMonthDto {

    private Integer year;

    @Range(min = 1L, max = 12L, message = "월은 1~12 사이로 지정해주세요.")
    private Integer month;
}
