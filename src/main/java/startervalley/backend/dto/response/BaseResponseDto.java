package startervalley.backend.dto.response;

import lombok.Data;
import lombok.Getter;

@Getter
public class BaseResponseDto {

    private Object message;

    public BaseResponseDto(Object message) {
        this.message = message;
    }
}
