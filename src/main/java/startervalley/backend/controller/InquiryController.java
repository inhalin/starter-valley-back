package startervalley.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import startervalley.backend.dto.common.BasicResponse;
import startervalley.backend.dto.inquiry.InquiryRequest;
import startervalley.backend.dto.inquiry.InquiryResponse;
import startervalley.backend.security.auth.CustomUserDetails;
import startervalley.backend.service.InquiryService;
import startervalley.backend.service.auth.AuthService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inquiries")
public class InquiryController {

    private final InquiryService inquiryService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<BasicResponse> create(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @Valid @RequestBody InquiryRequest request) {
        return ResponseEntity.ok(inquiryService.createOne(userDetails.getId(), request));
    }

    @GetMapping
    public ResponseEntity<List<InquiryResponse>> list(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(inquiryService.listWithoutAnonymous(userDetails.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InquiryResponse> show(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @PathVariable Long id) {
        return ResponseEntity.ok(inquiryService.getOne(id, userDetails.getId(), authService.fetchRole(userDetails)));
    }
}
