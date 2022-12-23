package startervalley.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BambooResponseDto {

    private Long id;

    private String content;

    private String randomName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    @Builder
    public BambooResponseDto(Long id, String content, String randomName, LocalDateTime createdDate) {
        this.id = id;
        this.content = content;
        this.randomName = randomName;
        this.createdDate = createdDate;
    }
}
