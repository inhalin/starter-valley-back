package startervalley.backend.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Devpart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "devpart_id")
    private Long id;

    private String name;
}
