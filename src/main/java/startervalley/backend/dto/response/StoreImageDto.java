package startervalley.backend.dto.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class StoreImageDto {

    private String imageUrl;

    private String getImageURL(String uuid, String imgName, String path) {
        return "/images/" + path + "/" + uuid + "_" + imgName;
    }

    private String getThumbnailURL(String uuid, String imgName, String path) {
        return "/images" + path + "/s_" + uuid + "_" + imgName;
    }

    public StoreImageDto(String uuid, String imgName, String path) {
        this.imageUrl = getImageURL(uuid, imgName, path);
    }
}
