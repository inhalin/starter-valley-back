package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@DynamicInsert
@Entity
@Getter
@NoArgsConstructor
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
    @Column(columnDefinition = "varchar(255) default 'USER'", nullable = false)
    private Role role;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "generation_id")
    private Generation generation;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(columnDefinition = "tinyint(1) default 0")
    private Boolean isLeader;

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

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<Attendance> attendances = new ArrayList<>();

    @Column(columnDefinition = "tinyint(1)")
    @ColumnDefault(value = "1")
    private boolean active;

    @Column(columnDefinition = "text")
    private String dropoutReason;

    private LocalDate dropoutDate;
    private LocalDateTime dropoutApprovedDate;


    public void setGeneration(Generation generation) {
        this.generation = generation;
    }

    public UserProfile setProfile(UserProfile profile) {
        this.profile = profile;
        return profile;
    }

    public void setConsecutiveDays(Integer consecutiveDays) {
        this.consecutiveDays = consecutiveDays;
    }

    @Builder
    public User(Long id, String username, String email, String name, Role role, Generation generation, Team team, Boolean isLeader, Devpart devpart, AuthProvider provider, String providerId, String refreshToken, String githubUrl, UserProfile profile, String imageUrl, Integer consecutiveDays, List<Attendance> attendances, boolean active, String dropoutReason, LocalDate dropoutDate, LocalDateTime dropoutApprovedDate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.role = role;
        this.generation = generation;
        this.team = team;
        this.isLeader = isLeader;
        this.devpart = devpart;
        this.provider = provider;
        this.providerId = providerId;
        this.refreshToken = refreshToken;
        this.githubUrl = githubUrl;
        this.profile = profile;
        this.imageUrl = imageUrl;
        this.consecutiveDays = consecutiveDays;
        this.attendances = attendances;
        this.active = active;
        this.dropoutReason = dropoutReason;
        this.dropoutDate = dropoutDate;
        this.dropoutApprovedDate = dropoutApprovedDate;
    }

    public void dropout(LocalDate dropoutDate, String reason) {
        this.active = false;
        this.dropoutReason = reason;
        this.dropoutDate = dropoutDate;
        this.dropoutApprovedDate = LocalDateTime.now();
    }

    public void updateDropout(LocalDate dropoutDate, String reason) {
        this.dropoutDate = dropoutDate;
        this.dropoutReason = reason;
    }

    public void setTeamInfo(Team team, boolean isLeader) {
        this.team = team;
        this.isLeader = isLeader;

        if (!this.team.getUsers().contains(this)) {
            this.team.getUsers().add(this);
        }
    }

    public void removeTeamInfo() {
        this.team.getUsers().remove(this);
        this.team = null;
        this.isLeader = false;
    }
}
