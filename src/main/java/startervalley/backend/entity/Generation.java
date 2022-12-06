package startervalley.backend.entity;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Builder
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

    @OneToMany(mappedBy = "generation")
    private List<User> users = new ArrayList<>();
}
