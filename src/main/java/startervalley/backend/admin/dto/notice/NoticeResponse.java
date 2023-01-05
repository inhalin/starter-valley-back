package startervalley.backend.admin.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class NoticeResponse {
    private List<NoticeListDto> notice;
    private int page;
    private int size;
    private long totalElement;
    private int totalPages;
    private boolean last;
}
