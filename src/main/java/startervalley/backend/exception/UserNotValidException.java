package startervalley.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
@Getter
public class UserNotValidException extends RuntimeException {

    public UserNotValidException() {
        super();
    }

    public UserNotValidException(String message) {
        super(message);
    }
}
