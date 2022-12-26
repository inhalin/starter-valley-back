package startervalley.backend.dto.lunchbus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LunchbusResponse {
    private Long busId;
    private String code;
    private String message;
}
