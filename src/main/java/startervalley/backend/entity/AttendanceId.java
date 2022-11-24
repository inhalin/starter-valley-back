package startervalley.backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Embeddable
public class AttendanceId implements Serializable {
    private Long userId;
    private LocalDate attendedDate;

    public AttendanceId(Long userId, LocalDate attendedDate) {
        this.userId = userId;
        this.attendedDate = attendedDate;
    }
}
