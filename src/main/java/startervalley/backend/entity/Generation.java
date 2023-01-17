package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@DynamicUpdate
public class Generation extends BaseTimeEntity {

    @Id
    @Column(name = "generation_id", unique = true, nullable = false)
    private Long id;

    @Column(updatable = false)
    private String code;

    private String description;
    private String address1;
    private String address2;
    private Double latitude;
    private Double longitude;
    private LocalDate courseStartDate;
    private LocalDate courseEndDate;

    private String recruitUrl;
    private String submitUrl;
    private String submitResultUrl;

    @OneToMany(mappedBy = "generation")
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "generation")
    private List<Team> teams = new ArrayList<>();

    @Builder
    public Generation(Long id, String code, String description, String address1, String address2, Double latitude, Double longitude, LocalDate courseStartDate, LocalDate courseEndDate, String recruitUrl, String submitUrl, String submitResultUrl, List<User> users, List<Team> teams) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.address1 = address1;
        this.address2 = address2;
        this.latitude = latitude;
        this.longitude = longitude;
        this.courseStartDate = courseStartDate;
        this.courseEndDate = courseEndDate;
        this.recruitUrl = recruitUrl;
        this.submitUrl = submitUrl;
        this.submitResultUrl = submitResultUrl;
        this.users = users;
        this.teams = teams;
    }

    public void update(LocalDate courseStartDate, LocalDate courseEndDate, String description, String address1, String address2, Double latitude, Double longitude, String recruitUrl, String submitUrl, String submitResultUrl) {
        this.courseStartDate = courseStartDate;
        this.courseEndDate = courseEndDate;
        this.description = description;
        this.address1 = address1;
        this.address2 = address2;
        this.latitude = latitude;
        this.longitude = longitude;
        this.recruitUrl = recruitUrl;
        this.submitUrl = submitUrl;
        this.submitResultUrl = submitResultUrl;
    }

    public void setTeam(Team team) {
        this.teams.add(team);
    }
}
