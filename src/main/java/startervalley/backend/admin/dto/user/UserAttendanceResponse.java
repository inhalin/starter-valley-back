package startervalley.backend.admin.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import startervalley.backend.entity.AttendanceStatus;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class UserAttendanceResponse {

    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate date;

    private AttendanceStatus status;

    private String name;

    private String reason;

    private String adminMemo;
}
