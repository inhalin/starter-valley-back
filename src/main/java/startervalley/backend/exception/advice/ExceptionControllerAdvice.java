package startervalley.backend.exception.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import startervalley.backend.exception.AttendanceAlreadyPresentException;
import startervalley.backend.exception.AttendanceOutOfRangeException;
import startervalley.backend.exception.NotFoundException;
import startervalley.backend.exception.StoreImageUploadException;

import static startervalley.backend.constant.ExceptionMessage.ATTENDANCE_ALREADY_PRESENT;
import static startervalley.backend.constant.ExceptionMessage.ATTENDANCE_OUT_OF_RANGE;


@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AttendanceOutOfRangeException.class)
    public ErrorResult attendanceOutOfLimitedRangeExHandler(AttendanceOutOfRangeException e) {
        return new ErrorResult(ATTENDANCE_OUT_OF_RANGE);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AttendanceAlreadyPresentException.class)
    public ErrorResult attendanceAlreadyPresentExHandler(AttendanceAlreadyPresentException e) {
        return new ErrorResult(ATTENDANCE_ALREADY_PRESENT);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(StoreImageUploadException.class)
    public ErrorResult StoreImageUploadExceptionExHandler(StoreImageUploadException e) {
        return new ErrorResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ErrorResult validatorExHandler(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        String defaultMessage = bindingResult.getFieldError().getDefaultMessage();
        return new ErrorResult(defaultMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResult notFoundExHandler(NotFoundException e) {
        return new ErrorResult(e.getMessage());
    }
}
