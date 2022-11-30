package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;
import startervalley.backend.dto.user.UserProfileUpdateDto;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@Builder
public class UserProfile {
    private String mbti;

    @Column(columnDefinition = "TEXT")
    private String intro;

    @Column(columnDefinition = "TEXT")
    private String likes;

    @Column(columnDefinition = "TEXT")
    private String dislikes;

    @Column(columnDefinition = "TEXT")
    private String interests;

    private String contact;

    public UserProfile() {
    }

    public UserProfile(String mbti, String intro, String likes, String dislikes, String interests, String contact) {
        this.mbti = mbti;
        this.intro = intro;
        this.likes = likes;
        this.dislikes = dislikes;
        this.interests = interests;
        this.contact = contact;
    }

    public void updateProfile(UserProfileUpdateDto dto) {
        this.contact = dto.getContact();
        this.intro = dto.getIntro();
        this.mbti = dto.getMbti();
        this.likes = dto.getLikes();
        this.dislikes = dto.getDislikes();
        this.interests = dto.getInterests();
    }
}
