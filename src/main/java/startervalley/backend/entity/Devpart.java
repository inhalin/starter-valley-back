package startervalley.backend.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Devpart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "devpart_id")
    private Long id;

    private String name;

    @Builder
    public Devpart(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
