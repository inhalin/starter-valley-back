package startervalley.backend.exception;

public class ResourceNotValidException extends RuntimeException {

    public ResourceNotValidException(String message) {
        super(message);
    }
}
