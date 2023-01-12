package startervalley.backend.dto.store;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class StoreImageDto {

    private static final String S3_URL = "https://starter-valley-bucket.s3.ap-northeast-2.amazonaws.com/images/";

    private Long id;
    private String imageUrl;

    private String getImageURL(String uuid, String imgName, String path) {
        return S3_URL + uuid + "_" + imgName;
    }

    private String getThumbnailURL(String uuid, String imgName, String path) {
        return S3_URL + uuid + "_" + imgName;
    }

    public StoreImageDto(Long id, String uuid, String imgName, String path) {
        this.id = id;
        this.imageUrl = getImageURL(uuid, imgName, path);
    }

    public static String getImageURL(String uuid, String imgName) {
        return S3_URL + uuid + "_" + imgName;
    }
}
