package startervalley.backend.dto.lunchbus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LunchbusSimpleDto {
    private Long busId;
    private String title;
    private Integer limit;
    private Integer count;
    private String driverName;
    private String driverImageUrl;
}
