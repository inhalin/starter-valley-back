package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class StoreTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;

    @Builder
    public StoreTag(Long id, Store store, Tag tag) {
        this.id = id;
        this.store = store;
        this.tag = tag;
    }
}
