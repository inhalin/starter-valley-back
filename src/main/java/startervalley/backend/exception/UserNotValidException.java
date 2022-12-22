package startervalley.backend.exception;

public class UserNotValidException extends CustomValidationException {

    public UserNotValidException() {
        super();
    }

    public UserNotValidException(String message) {
        super(message);
    }
}
