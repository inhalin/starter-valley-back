package startervalley.backend.exception.advice;

import lombok.Data;
import startervalley.backend.constant.ExceptionMessage;

@Data
public class ErrorResult {
    private Object message;

    public ErrorResult(Object message) {
        this.message = message;
    }
}
