package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class StoreImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;

    private String imgName;

    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @Builder
    public StoreImage(Long id, String uuid, String imgName, String path, Store store) {
        this.id = id;
        this.uuid = uuid;
        this.imgName = imgName;
        this.path = path;
        this.store = store;
    }
}
