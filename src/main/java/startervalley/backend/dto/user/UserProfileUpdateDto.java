package startervalley.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateDto {
    private String contact;
    private String mbti;
    private String intro;
    private String likes;
    private String dislikes;
    private String interests;
}
