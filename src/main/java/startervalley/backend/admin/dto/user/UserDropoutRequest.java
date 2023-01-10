package startervalley.backend.admin.dto.user;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserDropoutRequest {
    private LocalDate dropoutDate;
    private String reason;
}
