package startervalley.backend.dto.lunchbus;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class LunchbusDto {
    private Long busId;
    private String title;
    private int limit;
    private int count;
    private Boolean isDriver;
    private Boolean isPassenger;
    private LunchbusUserDto driver;
    private List<LunchbusUserDto> passengers;
    private String description;
    private String storeName;
    private String storeUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 E요일 HH시 mm분")
    private LocalDateTime closeAt;
}
