package startervalley.backend.dto.user;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserProfileReadDto {
    private String imageUrl;
    private String name;
    private List<String> mainInfo;
    private String intro;
    private List<String> subInfo;
}
