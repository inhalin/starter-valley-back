package startervalley.backend.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.*;

@Entity
@Getter
public class Generation extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "generation_id")
    private Long id;

    private String description;
    private String location;
    private String latitude;
    private String longitude;
    private LocalDate courseStartDate;
    private LocalDate courseEndDate;

    @OneToMany(mappedBy = "generation")
    private List<User> users = new ArrayList<>();
}
