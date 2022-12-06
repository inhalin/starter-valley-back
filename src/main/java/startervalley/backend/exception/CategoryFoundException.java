package startervalley.backend.exception;

public class CategoryFoundException extends NotFoundException {

    public CategoryFoundException() {
    }

    public CategoryFoundException(String message) {
        super(message);
    }
}
