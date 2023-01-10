package startervalley.backend.admin.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import startervalley.backend.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class UserDropoutDto {
    private Long id;
    private String name;
    private String reason;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDate dropoutDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime approvedDate;

    public static UserDropoutDto mapToDto(User user) {
        return UserDropoutDto.builder()
                .id(user.getId())
                .name(user.getName())
                .reason(user.getDropoutReason())
                .dropoutDate(user.getDropoutDate())
                .approvedDate(user.getDropoutApprovedDate())
                .build();
    }
}
