package startervalley.backend.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import startervalley.backend.admin.dto.notice.NoticeDto;
import startervalley.backend.admin.dto.notice.NoticeListDto;
import startervalley.backend.admin.dto.notice.NoticeRequest;
import startervalley.backend.admin.service.NoticeService;
import startervalley.backend.dto.common.BasicResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public ResponseEntity<List<NoticeListDto>> list() {
        return ResponseEntity.ok(noticeService.getAll());
    }

    @PostMapping
    public ResponseEntity<BasicResponse> create(@RequestBody NoticeRequest request,
                                                @RequestParam(value = "userId", required = false, defaultValue = "2") Long adminUserId) {
        return ResponseEntity.ok(noticeService.createOne(adminUserId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeDto> show(@PathVariable Long id) {
        return ResponseEntity.ok(noticeService.getOne(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BasicResponse> update(@PathVariable Long id, @RequestBody NoticeRequest request) {
        return ResponseEntity.ok(noticeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BasicResponse> delete(@PathVariable Long id) {
        return ResponseEntity.ok(noticeService.deleteOne(id));
    }
}
