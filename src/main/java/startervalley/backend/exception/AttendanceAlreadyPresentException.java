package startervalley.backend.exception;

public class AttendanceAlreadyPresentException extends RuntimeException {

    public AttendanceAlreadyPresentException() {
        super();
    }

    public AttendanceAlreadyPresentException(String message) {
        super(message);
    }
}
