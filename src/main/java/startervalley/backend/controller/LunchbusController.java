package startervalley.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import startervalley.backend.dto.common.BasicResponse;
import startervalley.backend.dto.lunchbus.LunchbusDto;
import startervalley.backend.dto.lunchbus.LunchbusInsertRequest;
import startervalley.backend.dto.lunchbus.LunchbusSimpleDto;
import startervalley.backend.security.auth.CustomUserDetails;
import startervalley.backend.service.LunchbusService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lunchbuses")
public class LunchbusController {

    private final LunchbusService lunchbusService;

    @PostMapping
    public ResponseEntity<BasicResponse> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody LunchbusInsertRequest request) {
        return ResponseEntity.ok(lunchbusService.saveLunchbus(userDetails.getId(), request));
    }

    @GetMapping
    public ResponseEntity<List<LunchbusSimpleDto>> listActive(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(lunchbusService.findAllActiveLunchbuses(userDetails.getId()));
    }

    @GetMapping("/past")
    public ResponseEntity<List<LunchbusSimpleDto>> listPast(@AuthenticationPrincipal CustomUserDetails userDetails) {
        final int LIMITED_DAYS = 3;
        return ResponseEntity.ok(lunchbusService.findPastLunchbusesInLimitedDays(LIMITED_DAYS, userDetails.getUser().getGeneration().getId()));
    }

    @GetMapping("/{busId}")
    public ResponseEntity<LunchbusDto> show(@PathVariable Long busId) {
        return ResponseEntity.ok(lunchbusService.findLunchbus(busId));
    }

    @DeleteMapping("/{busId}")
    public ResponseEntity<BasicResponse> delete(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long busId) {
        lunchbusService.validateDriver(userDetails.getId(), busId);

        return ResponseEntity.ok(lunchbusService.deleteLunchbus(busId));
    }

    @PostMapping("/{busId}/join")
    public ResponseEntity<BasicResponse> join(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long busId) {
        return ResponseEntity.ok(lunchbusService.joinLunchbus(userDetails.getId(), busId));
    }

    @PostMapping("/{busId}/leave")
    public ResponseEntity<BasicResponse> leave(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long busId) {
        return ResponseEntity.ok(lunchbusService.leaveLunchbus(userDetails.getId(), busId));
    }

    @PostMapping("/{busId}/close")
    public ResponseEntity<BasicResponse> close(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long busId) {
        lunchbusService.validateDriver(userDetails.getId(), busId);

        return ResponseEntity.ok(lunchbusService.closeLunchbus(busId));
    }
}
