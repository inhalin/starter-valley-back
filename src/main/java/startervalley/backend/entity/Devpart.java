package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String koname;

    private Long generationId;

    @Builder
    public Devpart(Long id, String name, String koname, Long generationId) {
        this.id = id;
        this.name = name;
        this.koname = koname;
        this.generationId = generationId;
    }

    public Devpart(String name, String koname, Long generationId) {
        this.name = name;
        this.koname = koname;
        this.generationId = generationId;
    }

    public static Devpart mapToEntity(String name, String koname, Long generationId) {
        return new Devpart(name, koname, generationId);
    }

    public void updateKoname(String koname) {
        this.koname = koname;
    }
}
