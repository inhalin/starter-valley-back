package startervalley.backend.exception;

public class AttendanceOutOfLimitedRangeException extends RuntimeException {

    public AttendanceOutOfLimitedRangeException() {
        super();
    }

    public AttendanceOutOfLimitedRangeException(String message) {
        super(message);
    }
}
