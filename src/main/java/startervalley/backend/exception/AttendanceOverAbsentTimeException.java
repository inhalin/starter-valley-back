package startervalley.backend.exception;

public class AttendanceOverAbsentTimeException extends RuntimeException {

    public AttendanceOverAbsentTimeException() {
        super();
    }

    public AttendanceOverAbsentTimeException(String message) {
        super(message);
    }
}
