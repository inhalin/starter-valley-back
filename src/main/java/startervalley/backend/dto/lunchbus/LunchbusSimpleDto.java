package startervalley.backend.dto.lunchbus;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM.dd. E HH:mm")
    private LocalDateTime closeAt;
}
