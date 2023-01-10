package startervalley.backend.dto.store;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class StoreUpdateDto {

    private String description;

    @NotBlank(message = "카테고리를 지정해주세요.")
    private Long categoryId;

    private List<String> tagList;

    private List<Long> deleteImgIdList;
}
