package startervalley.backend.dto.response;

import lombok.*;
import startervalley.backend.dto.request.StoreRequestDto;

@Getter
@Setter
@ToString
public class StoreResponseDto {

    private Long id;

    private String name;

    private String address;

    private String description;

    private String category;

    private long likeCount;

    private boolean myLikeStatus;

    public StoreResponseDto(Long id, String name, String address, String description, String category) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.category = category;
    }

    @Builder
    public StoreResponseDto(Long id, String name, String address, String description, String category, long likeCount, boolean myLikeStatus) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.category = category;
        this.likeCount = likeCount;
        this.myLikeStatus = myLikeStatus;
    }
}