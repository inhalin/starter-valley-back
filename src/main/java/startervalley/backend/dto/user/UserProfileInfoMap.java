package startervalley.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileInfoMap {
    private String title;
    private String description;

    public static UserProfileInfoMap of(String title, String description) {
        return new UserProfileInfoMap(title, description);
    }
}
