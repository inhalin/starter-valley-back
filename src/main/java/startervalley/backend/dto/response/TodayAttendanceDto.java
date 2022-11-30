package startervalley.backend.dto.response;

import lombok.Data;

@Data
public class TodayAttendanceDto {

    private boolean isChecked = true;
    private boolean needReason = false;
}
