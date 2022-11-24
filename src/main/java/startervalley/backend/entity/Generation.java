package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.*;

@Getter
@NoArgsConstructor
@Entity
public class Generation extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "generation_id")
    private Long id;

    private String description;
    private String location;
    private Double latitude;
    private Double longitude;
    private LocalDate courseStartDate;
    private LocalDate courseEndDate;

    @OneToMany(mappedBy = "generation")
    private List<User> users = new ArrayList<>();

    @Builder
    public Generation(Long id, String description, String location, Double latitude, Double longitude, List<User> users) {
        this.id = id;
        this.description = description;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.users = users;
    }
}
