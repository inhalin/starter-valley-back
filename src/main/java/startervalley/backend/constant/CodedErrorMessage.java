package startervalley.backend.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CodedErrorMessage {
    LUNCHBUS_EXCEEDED("exceeded", "탑승 인원이 초과되었습니다."),
    LUNCHBUS_ALREADY_JOINED("already joined", "이미 탑승중인 버스입니다."),
    LUNCHBUS_NOT_PASSENGER("not passenger", "탑승한 버스가 아닙니다."),
    LUNCHBUS_INACTIVE("inactive", "운행이 종료된 버스입니다."),
    LUNCHBUS_OWNER("owner", "본인이 운행하는 버스입니다.");

    private final String code;
    private final String message;
}
