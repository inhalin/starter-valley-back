package startervalley.backend.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttendanceCheckDto {

    private Double latitude;

    private Double longtitude;

    private LocalDateTime time;
}
