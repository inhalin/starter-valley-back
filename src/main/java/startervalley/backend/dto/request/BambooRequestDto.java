package startervalley.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class BambooRequestDto {

    @Size(min = 1, max = 350, message = "1~350자 내로 적어주세요.")
    private String content;
}
