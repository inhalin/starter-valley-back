package startervalley.backend.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import startervalley.backend.dto.common.BasicResponse;
import startervalley.backend.dto.inquiry.InquiryResponse;
import startervalley.backend.security.auth.AdminUserDetails;
import startervalley.backend.service.InquiryService;
import startervalley.backend.service.auth.AuthService;

import java.util.List;

@RestController(value = "adminInquiryController")
@RequiredArgsConstructor
@RequestMapping("/admin/inquiries")
public class InquiryController {

    private final InquiryService inquiryService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<InquiryResponse>> list() {
        return ResponseEntity.ok(inquiryService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InquiryResponse> get(@AuthenticationPrincipal AdminUserDetails userDetails,
                                               @PathVariable Long id) {
        return ResponseEntity.ok(inquiryService.getOne(id, userDetails.getId(), authService.fetchRole(userDetails)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<BasicResponse> delete(@AuthenticationPrincipal AdminUserDetails userDetails,
                                                @PathVariable Long id) {
        return ResponseEntity.ok(inquiryService.deleteOne(id, authService.fetchRole(userDetails)));
    }
}
