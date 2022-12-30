package startervalley.backend.admin.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class UserSimpleDto {
    private Long id;
    private String name;
}
