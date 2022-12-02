package startervalley.backend.dto.user;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserProfileReadDto {
    private String imageUrl;
    private String name;
    private List<String> mainInfo;
    private String intro;
    private List<String> subInfo;
}
