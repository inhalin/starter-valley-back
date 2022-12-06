package startervalley.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

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
    private String providerId;

    private String refreshToken;
    private String githubUrl;

    @Embedded
    private UserProfile profile;

    private String imageUrl;

    @ColumnDefault(value = "0")
    private Integer consecutiveDays;

    public void setGeneration(Generation generation) {
        this.generation = generation;
    }

    public UserProfile setProfile(UserProfile profile) {
        this.profile = profile;
        return profile;
    }
}
