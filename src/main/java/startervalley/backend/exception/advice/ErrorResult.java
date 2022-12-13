package startervalley.backend.exception.advice;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResult {
    private Object message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;
    private String details;

    public ErrorResult(Object message) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
    }

    public ErrorResult(Object message, String details) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.details = details;
    }
}
