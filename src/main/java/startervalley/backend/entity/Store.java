package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import startervalley.backend.dto.request.StoreRequestDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Store extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @OneToMany(mappedBy = "store")
    private List<StoreImage> storeImageList = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<StoreTag> storeTagList = new ArrayList<>();

    @Builder
    public Store(Long id, String name, String address, String description, Category category) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.category = category;
    }

    public void update(StoreRequestDto storeRequestDto) {
        this.name = storeRequestDto.getName();
        this.address = storeRequestDto.getAddress();
        this.description = storeRequestDto.getDescription();
    }
}
