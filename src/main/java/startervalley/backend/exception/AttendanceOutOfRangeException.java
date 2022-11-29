package startervalley.backend.exception;

public class AttendanceOutOfRangeException extends RuntimeException {

    public AttendanceOutOfRangeException() {
        super();
    }

    public AttendanceOutOfRangeException(String message) {
        super(message);
    }
}
