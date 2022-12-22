package startervalley.backend.exception;

public class StoreImageUploadFailedException extends RuntimeException {

    public StoreImageUploadFailedException() {
    }

    public StoreImageUploadFailedException(String message) {
        super(message);
    }
}
