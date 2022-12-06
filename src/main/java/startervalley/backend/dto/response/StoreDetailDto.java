package startervalley.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class StoreDetailDto {

    private StoreResponseDto store;
    private List<StoreImageDto> images;
    private List<TagDto> tags;
}
