package startervalley.backend.dto.store;

import lombok.*;

@Getter
@Setter
public class StoreResponseDto {

    private Long id;

    private String name;

    private String address;

    private String description;

    private String url;

    private String category;

    private String imageUrl;

    private long likeCount;

    private boolean myLikeStatus;

    private boolean own;

    public StoreResponseDto(Long id, String name, String address, String description, String category) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.category = category;
    }

    @Builder
    public StoreResponseDto(Long id, String name, String address, String description, String url, String category, String imageUrl, long likeCount, boolean myLikeStatus, boolean own) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.url = url;
        this.category = category;
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
        this.myLikeStatus = myLikeStatus;
        this.own = own;
    }
}
