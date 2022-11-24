package startervalley.backend.exception.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import startervalley.backend.constant.ExceptionMessage;
import startervalley.backend.exception.AttendanceAlreadyPresentException;
import startervalley.backend.exception.AttendanceOutOfLimitedRangeException;
import startervalley.backend.exception.AttendanceOverAbsentTimeException;

import static startervalley.backend.constant.ExceptionMessage.ATTENDANCE_ALREADY_PRESENT;
import static startervalley.backend.constant.ExceptionMessage.ATTENDANCE_OUT_OF_RANGE;


@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {
}
