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
    private String location;
    private Double latitude;
    private Double longitude;
    private LocalDate courseStartDate;
    private LocalDate courseEndDate;

    private String recruitUrl;
    private String submitUrl;

    @OneToMany(mappedBy = "generation")
    private List<User> users = new ArrayList<>();

    @Builder
    public Generation(Long id, String code, String description, String location, Double latitude, Double longitude, LocalDate courseStartDate, LocalDate courseEndDate, String recruitUrl, String submitUrl, List<User> users) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.courseStartDate = courseStartDate;
        this.courseEndDate = courseEndDate;
        this.recruitUrl = recruitUrl;
        this.submitUrl = submitUrl;
        this.users = users;
    }

    public void update(LocalDate courseStartDate, LocalDate courseEndDate, String description, String location, double latitude, double longitude, String recruitUrl, String submitUrl) {
        this.courseStartDate = courseStartDate;
        this.courseEndDate = courseEndDate;
        this.description = description;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.recruitUrl = recruitUrl;
        this.submitUrl = submitUrl;
    }
}
