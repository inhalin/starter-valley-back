package startervalley.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import startervalley.backend.entity.Store;
import startervalley.backend.entity.User;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentResponseDto {

    private Long id;

    private String description;

    private boolean isOwn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd h:m:s", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd h:m:s", timezone = "Asia/Seoul")
    private LocalDateTime modifiedDate;

    @Builder
    public CommentResponseDto(Long id, String description, boolean isOwn, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.description = description;
        this.isOwn = isOwn;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
