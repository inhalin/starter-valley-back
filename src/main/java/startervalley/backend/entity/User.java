package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) default 'NONE'")
    private Role role = Role.NONE;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "generation_id")
    private Generation generation;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(columnDefinition="tinyint(1) default 0")
    private Boolean isLeader = false;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "devpart_id")
    private Devpart devPart;

    private String githubKey;
    private String githubUrl;

    @Embedded
    private UserProfile profile;

    private String profileImageUrl;


    @Builder
    public User(String email, String githubKey, String githubUrl, String profileImageUrl) {
        this.email = email;
        this.githubKey = githubKey;
        this.githubUrl = githubUrl;
        this.profileImageUrl = profileImageUrl;
    }
}
