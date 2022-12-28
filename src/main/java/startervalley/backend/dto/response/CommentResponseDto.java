package startervalley.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class CommentResponseDto {

    private Long id;

    private String description;

    private String author;

    private boolean isOwn;

    private List<TagDto> tagList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedDate;

    @Builder
    public CommentResponseDto(Long id, String description, String author, boolean isOwn, List<TagDto> tagList, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.description = description;
        this.author = author;
        this.isOwn = isOwn;
        this.tagList = tagList;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
