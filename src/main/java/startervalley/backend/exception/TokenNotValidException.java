package startervalley.backend.exception;

public class TokenNotValidException extends CustomValidationException {

    public TokenNotValidException() {
        super();
    }

    public TokenNotValidException(String message) {
        super(message);
    }
}
