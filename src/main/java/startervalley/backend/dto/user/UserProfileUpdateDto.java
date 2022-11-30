package startervalley.backend.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileUpdateDto {
    private String contact;
    private String mbti;
    private String intro;
    private String likes;
    private String dislikes;
    private String interests;

    public UserProfileUpdateDto() {
    }

    public UserProfileUpdateDto(String contact, String mbti, String intro, String likes, String dislikes, String interests) {
        this.contact = contact;
        this.mbti = mbti;
        this.intro = intro;
        this.likes = likes;
        this.dislikes = dislikes;
        this.interests = interests;
    }
}
