package startervalley.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class CommentRequestDto {

    @Size(min = 1, max = 200, message = "1~200 글자수 채우기")
    @NotBlank(message = "내용을 채워주세요.")
    private String content;

    private List<String> tagList;

}
