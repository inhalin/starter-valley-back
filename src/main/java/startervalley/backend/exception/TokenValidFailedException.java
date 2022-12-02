package startervalley.backend.exception;

public class TokenValidFailedException extends RuntimeException {

    public TokenValidFailedException() {
        super();
    }

    public TokenValidFailedException(String message) {
        super(message);
    }
}
