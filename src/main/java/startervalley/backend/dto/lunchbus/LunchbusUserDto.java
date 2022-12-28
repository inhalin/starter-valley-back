package startervalley.backend.dto.lunchbus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LunchbusUserDto {
    private Long userId;
    private String imageUrl;
    private String name;
}
