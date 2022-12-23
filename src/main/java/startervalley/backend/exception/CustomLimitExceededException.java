package startervalley.backend.exception;

import lombok.Getter;

@Getter
public class CustomLimitExceededException extends RuntimeException {
    private final int limit;

    public CustomLimitExceededException(String message, int limit) {
        super(message);
        this.limit = limit;
    }
}
