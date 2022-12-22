package startervalley.backend.exception;

public class TokenNotValidException extends RuntimeException {

    public TokenNotValidException() {
        super();
    }

    public TokenNotValidException(String message) {
        super(message);
    }
}
