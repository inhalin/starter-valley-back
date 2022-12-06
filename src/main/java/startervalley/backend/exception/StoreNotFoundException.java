package startervalley.backend.exception;

public class StoreNotFoundException extends NotFoundException {

    public StoreNotFoundException() {
    }

    public StoreNotFoundException(String message) {
        super(message);
    }
}
