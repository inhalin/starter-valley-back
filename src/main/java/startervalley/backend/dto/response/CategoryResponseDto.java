package startervalley.backend.dto.response;

import lombok.Getter;

@Getter
public class CategoryResponseDto {

    private Long id;

    private String name;

    public CategoryResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
