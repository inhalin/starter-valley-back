package startervalley.backend.entity;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Getter
public class AttendanceId implements Serializable {
    private Long userId;
    private LocalDateTime attendedDate;
}
