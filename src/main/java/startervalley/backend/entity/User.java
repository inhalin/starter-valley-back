package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.*;
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

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    @Enumerated(STRING)
    @Column(columnDefinition = "varchar(255) default 'ANONYMOUS'", nullable = false)
    private Role role = Role.ANONYMOUS;

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
    private Devpart devpart;

    @Enumerated(STRING)
    private AuthProvider provider;

    private String refreshToken;
    private String githubUrl;

    @Embedded
    private UserProfile profile;

    private String imageUrl;

    @Builder
    public User(String email, String name, Role role, Generation generation, Team team, Boolean isLeader, Devpart devpart, AuthProvider provider, String refreshToken, String githubUrl, UserProfile profile, String imageUrl) {
        this.email = email;
        this.name = name;
        this.role = role;
        this.generation = generation;
        this.team = team;
        this.isLeader = isLeader;
        this.devpart = devpart;
        this.provider = provider;
        this.refreshToken = refreshToken;
        this.githubUrl = githubUrl;
        this.profile = profile;
        this.imageUrl = imageUrl;
    }

    public void setGeneration(Generation generation) {
        this.generation = generation;
    }
}
