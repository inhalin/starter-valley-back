package startervalley.backend.exception.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import startervalley.backend.exception.*;

import java.util.Map;

import static startervalley.backend.constant.ExceptionMessage.ATTENDANCE_ALREADY_PRESENT;
import static startervalley.backend.constant.ExceptionMessage.ATTENDANCE_OUT_OF_RANGE;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AttendanceOutOfRangeException.class)
    public ErrorResult handleAttendanceOutOfLimitedRangeException(AttendanceOutOfRangeException e) {
        return new ErrorResult(ATTENDANCE_OUT_OF_RANGE);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AttendanceAlreadyPresentException.class)
    public ErrorResult handleAttendanceAlreadyPresentException(AttendanceAlreadyPresentException e) {
        return new ErrorResult(ATTENDANCE_ALREADY_PRESENT);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ErrorResult handleValidatorException(BindException e, WebRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        String defaultMessage = bindingResult.getFieldError().getDefaultMessage();
        return new ErrorResult(defaultMessage, request.getDescription(false));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(StoreImageUploadFailedException.class)
    public ErrorResult handleStoreImageUploadFailedException(StoreImageUploadFailedException e) {
        return new ErrorResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({TokenNotValidException.class, UserNotValidException.class})
    public ResponseEntity<ErrorResult> handleTokenValidationException(CustomValidationException e, WebRequest request) {
        ErrorResult errorResult = new ErrorResult(e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorResult, HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomLimitExceededException.class)
    public ResponseEntity<ErrorResult> handleLimitExceededException(CustomLimitExceededException e, WebRequest request) {
        Map<String, Object> message = Map.ofEntries(
                Map.entry("errorMessage", e.getMessage()),
                Map.entry("limit", e.getLimit())
        );
        return new ResponseEntity<>(new ErrorResult(message, request.getDescription(false)), HttpStatus.BAD_REQUEST);
    }
}
