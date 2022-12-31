package startervalley.backend.exception.advice;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CodedErrorResult {

    private String code;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private String details;

    public CodedErrorResult(String message, String code, String details) {
        this.timestamp = LocalDateTime.now();
        this.code = code;
        this.message = message;
        this.details = details;
    }

    public static CodedErrorResult of(String message, String code, String details) {
        return new CodedErrorResult(message, code, details);
    }
}
