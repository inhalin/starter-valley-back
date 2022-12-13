package startervalley.backend.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@ToString
public class StoreRequestDto {

    @NotBlank(message = "가게 이름을 지정해주세요.")
    private String name;

    private String address;

    private String description;

    private String url;

    @NotBlank(message = "카테고리를 지정해주세요.")
    private Long categoryId;

    private List<String> tagList;
}
