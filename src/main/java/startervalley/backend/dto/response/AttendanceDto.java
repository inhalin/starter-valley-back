package startervalley.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import startervalley.backend.entity.AttendanceStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AttendanceDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate localDate;

    private AttendanceStatus attendanceStatus;

    public AttendanceDto(LocalDate localDate, AttendanceStatus attendanceStatus) {
        this.localDate = localDate;
        this.attendanceStatus = attendanceStatus;
    }
}
