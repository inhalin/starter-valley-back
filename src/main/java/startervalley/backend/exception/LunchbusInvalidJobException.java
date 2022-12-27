package startervalley.backend.exception;

import lombok.Getter;

@Getter
public class LunchbusInvalidJobException extends RuntimeException {
    private final String message;
    private final String code;

    public LunchbusInvalidJobException(String message, String code) {
        super(message);
        this.message = message;
        this.code = code;
    }
}
