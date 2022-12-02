package startervalley.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryResponseDto {

    private String name;

    public CategoryResponseDto(String name) {
        this.name = name;
    }
}
