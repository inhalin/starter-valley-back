package startervalley.backend.dto.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class StoreImageDto {

    private static final String S3_URL = "https://starter-valley-bucket.s3.ap-northeast-2.amazonaws.com/";
    private String imageUrl;

    private String getImageURL(String uuid, String imgName, String path) {
        return S3_URL + uuid + "_" + imgName;
    }

    private String getThumbnailURL(String uuid, String imgName, String path) {
        return S3_URL + uuid + "_" + imgName;
    }

    public StoreImageDto(String uuid, String imgName, String path) {
        this.imageUrl = getImageURL(uuid, imgName, path);
    }
}
