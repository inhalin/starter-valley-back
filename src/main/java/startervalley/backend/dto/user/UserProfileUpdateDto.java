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

    @Pattern(regexp = "([EI][SN][TF][JP])*$", flags = {Pattern.Flag.CASE_INSENSITIVE}, message = "정확한 MBTI를 적어주세요.")
    private String mbti;

    private String intro;
    private String likes;
    private String dislikes;
    private String interests;
}
