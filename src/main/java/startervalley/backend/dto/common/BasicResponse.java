package startervalley.backend.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class BasicResponse {
    private Long targetId;
    private Object message;
}
