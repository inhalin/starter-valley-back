package startervalley.backend.dto.lunchbus;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LunchbusDto {
    private Long busId;
    private String title;
    private Integer limit;
    private Integer count;
    private Boolean isDriver;
    private Boolean isPassenger;
    private LunchbusUserDto driver;
    private List<LunchbusUserDto> passengers;
    private String description;
    private String storeName;
    private String storeUrl;
}
