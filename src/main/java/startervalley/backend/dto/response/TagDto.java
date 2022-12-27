package startervalley.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TagDto {

    private Long id;

    private String content;

    @Builder
    public TagDto(Long id, String content) {
        this.id = id;
        this.content = content;
    }
}
