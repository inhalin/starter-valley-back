package startervalley.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import startervalley.backend.admin.dto.notice.NoticeDto;
import startervalley.backend.admin.dto.notice.NoticeResponse;
import startervalley.backend.admin.service.NoticeService;
import startervalley.backend.util.PaginationConstants;

@RestController(value = "NoticeControllerFO")
@RequiredArgsConstructor
@RequestMapping("/api/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public ResponseEntity<NoticeResponse> list(
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_PAGE_SIZE, required = false) int size,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_SORT_BY, required = false) String sort,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_SORT_DIRECTION, required = false) String dir
    ) {
        return ResponseEntity.ok(noticeService.getAll(page, size, sort, dir));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeDto> show(@PathVariable Long id) {
        return ResponseEntity.ok(noticeService.getOne(id));
    }
}
