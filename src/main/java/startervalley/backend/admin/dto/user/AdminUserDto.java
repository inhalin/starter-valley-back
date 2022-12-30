package startervalley.backend.admin.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AdminUserDto {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String phone;
}
