package startervalley.backend.dto.lunchbus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LunchbusUserDto {
    private String imageUrl;
    private String name;
}
