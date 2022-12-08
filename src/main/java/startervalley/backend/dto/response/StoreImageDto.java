package startervalley.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Getter
@ToString
public class StoreImageDto {

    private String imageUrl;

    private String getImageURL(String uuid, String imgName, String path){
        return URLEncoder.encode(path+"/"+uuid+"_"+imgName, StandardCharsets.UTF_8);
    }

    private String getThumbnailURL(String uuid, String imgName, String path){
        return URLEncoder.encode(path+"/s_"+uuid+"_"+imgName,StandardCharsets.UTF_8);
    }

    public StoreImageDto(String uuid, String imgName, String path) {
       this.imageUrl = getImageURL(uuid, imgName, path);
    }
}
