package startervalley.backend.dto.board;

import lombok.Getter;

import javax.validation.constraints.Size;

@Getter
public class BoardRequestDto {

    @Size(min = 1, max = 100, message = "제목은 1-100자 사이로 입력해주세요.")
    private String title;

    @Size(min = 1, max = 500, message = "내용은 1-500자 사이로 입력해주세요.")
    private String content;
}
