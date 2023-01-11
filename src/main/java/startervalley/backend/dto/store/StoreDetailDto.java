package startervalley.backend.dto.store;

import lombok.AllArgsConstructor;
import lombok.Getter;
import startervalley.backend.dto.response.TagDto;

import java.util.List;

@AllArgsConstructor
@Getter
public class StoreDetailDto {

    private StoreResponseDto store;
    private List<StoreImageDto> images;
    private List<TagDto> tags;
}
