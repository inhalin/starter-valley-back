package startervalley.backend.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import startervalley.backend.admin.dto.notice.NoticeDto;
import startervalley.backend.admin.dto.notice.NoticeRequest;
import startervalley.backend.admin.dto.notice.NoticeResponse;
import startervalley.backend.admin.service.NoticeService;
import startervalley.backend.dto.common.BasicResponse;
import startervalley.backend.security.auth.AdminUserDetails;
import startervalley.backend.util.PaginationConstants;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public ResponseEntity<NoticeResponse> list(
            @RequestParam(value = "page", defaultValue = PaginationConstants.DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(value = "size", defaultValue = PaginationConstants.DEFAULT_PAGE_SIZE, required = false) int size,
            @RequestParam(value = "sort", defaultValue = PaginationConstants.DEFAULT_SORT_BY, required = false) String sort,
            @RequestParam(value = "dir", defaultValue = PaginationConstants.DEFAULT_SORT_DIRECTION, required = false) String dir
    ) {
        return ResponseEntity.ok(noticeService.getAll(page, size, sort, dir));
    }

    @PostMapping
    public ResponseEntity<BasicResponse> create(@AuthenticationPrincipal AdminUserDetails userDetails,
                                                @Valid @RequestBody NoticeRequest request) {
        return ResponseEntity.ok(noticeService.createOne(userDetails.getId(), request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeDto> show(@PathVariable Long id) {
        return ResponseEntity.ok(noticeService.getOne(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BasicResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody NoticeRequest request) {
        return ResponseEntity.ok(noticeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BasicResponse> delete(@PathVariable Long id) {
        return ResponseEntity.ok(noticeService.deleteOne(id));
    }
}
