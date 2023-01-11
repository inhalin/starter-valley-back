package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
@Entity
public class Generation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "generation_id")
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
}
