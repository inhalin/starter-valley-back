package startervalley.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateDto {
    private String contact;

    @Pattern(regexp = "[a-zA-Z]{4}", message = "MBTI는 영문자 4글자로 적어주세요.")
    private String mbti;

    private String intro;
    private String likes;
    private String dislikes;
    private String interests;
}
