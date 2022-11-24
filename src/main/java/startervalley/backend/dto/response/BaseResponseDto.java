package startervalley.backend.dto.response;

import lombok.Data;

@Data
public class BaseResponseDto<T> {

    private String message;

    private T data;

    public BaseResponseDto(String message, T data) {
        this.message = message;
        this.data = data;
    }
}
