package startervalley.backend.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
public class Team {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "team_id")
    private Long id;

    private String name;
    private String description;
}
