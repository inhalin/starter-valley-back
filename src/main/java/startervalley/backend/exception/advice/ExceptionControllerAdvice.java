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
    @ExceptionHandler(AttendanceWeekendException.class)
    public ErrorResult handleAttendanceWeekendException(AttendanceWeekendException e) {
        return new ErrorResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ErrorResult handleValidatorException(BindException e, WebRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        String defaultMessage = bindingResult.getFieldError().getDefaultMessage();
        return new ErrorResult(defaultMessage, request.getDescription(false));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotOwnerException.class)
    public ErrorResult handleNotOwnerException(NotOwnerException e) {
        return new ErrorResult("해당 게시글/댓글 작성자가 아닙니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(StoreImageUploadFailedException.class)
    public ErrorResult handleStoreImageUploadFailedException(StoreImageUploadFailedException e) {
        return new ErrorResult(e.getMessage());
    }

    @ExceptionHandler(TokenNotValidException.class)
    public ResponseEntity<ErrorResult> handleTokenValidationException(TokenNotValidException e, WebRequest request) {
        ErrorResult errorResult = new ErrorResult(e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorResult, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CustomLimitExceededException.class)
    public ResponseEntity<ErrorResult> handleLimitExceededException(CustomLimitExceededException e, WebRequest request) {
        Map<String, Object> message = Map.ofEntries(
                Map.entry("errorMessage", e.getMessage()),
                Map.entry("limit", e.getLimit())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResult.of(message, request.getDescription(false)));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResult> handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResult.of(e.getMessage(), request.getDescription(false)));
    }

    @ExceptionHandler(ResourceDuplicateException.class)
    public ResponseEntity<ErrorResult> handleResourceDuplicateException(ResourceDuplicateException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResult.of(e.getMessage(), request.getDescription(false)));
    }

    @ExceptionHandler(UserNotValidException.class)
    public ResponseEntity<ErrorResult> handleUserNotValidException(UserNotValidException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResult.of(e.getMessage(), request.getDescription(false)));
    }

    @ExceptionHandler(LunchbusInvalidJobException.class)
    public ResponseEntity<CodedErrorResult> handleLunchbusNotAllowedException(LunchbusInvalidJobException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CodedErrorResult.of(e.getMessage(), e.getCode(), request.getDescription(false)));
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<ErrorResult> handleCustomValidationException(CustomValidationException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResult.of(e.getMessage(), request.getDescription(false)));
    }

    @ExceptionHandler(PasswordNotValidException.class)
    public ResponseEntity<ErrorResult> handlePasswordNotValidException(PasswordNotValidException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResult.of(e.getMessage(), request.getDescription(false)));
    }
}
