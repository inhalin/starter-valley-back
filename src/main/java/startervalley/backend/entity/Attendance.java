package startervalley.backend.entity;

import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter
public class Attendance extends BaseTimeEntity {

    @EmbeddedId
    private AttendanceId id;

    @MapsId("userId")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String reason;

    @Enumerated(STRING)
    private AttendanceStatus status;
}
