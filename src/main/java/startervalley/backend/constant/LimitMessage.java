package startervalley.backend.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LimitMessage {
    ACTIVE_LUNCHBUS(3, "동시에 운행 가능한 런치버스 수를 초과하였습니다."),
    JOIN_LUNCHBUS(null, "탑승 가능한 인원 수를 초과하였습니다.");

    private final Integer limit;
    private final String message;
}
