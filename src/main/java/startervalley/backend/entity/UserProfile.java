package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@Builder
public class UserProfile {
    private String mbti;

    @Column(columnDefinition = "TEXT")
    private String oneliner;

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

    public UserProfile(String mbti, String oneliner, String intro, String likes, String dislikes, String interests, String contact) {
        this.mbti = mbti;
        this.oneliner = oneliner;
        this.intro = intro;
        this.likes = likes;
        this.dislikes = dislikes;
        this.interests = interests;
        this.contact = contact;
    }
}
