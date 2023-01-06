package startervalley.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytebuddy.utility.RandomString;

import javax.persistence.*;

import java.time.LocalTime;

import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.*;

@NoArgsConstructor
@Getter
@Entity
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

    private LocalTime attendanceTime;

    @Column(columnDefinition = "TEXT")
    private String adminMemo;

    @Transient
    private static String attendanceCode;

    @Builder
    public Attendance(AttendanceId id, User user, String reason, AttendanceStatus status, LocalTime attendanceTime, String adminMemo) {
        this.id = id;
        this.user = user;
        this.reason = reason;
        this.status = status;
        this.attendanceTime = attendanceTime;
        this.adminMemo = adminMemo;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setAttendanceTime(LocalTime attendanceTime) {
        this.attendanceTime = attendanceTime;
    }

    public static String getAttendanceCode() {
        return attendanceCode;
    }

    public static void generateAttendanceRandomCode() {
        attendanceCode = RandomString.make();
    }
}
