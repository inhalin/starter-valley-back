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
    public ResponseEntity<List<LunchbusSimpleDto>> listActive() {
        return ResponseEntity.ok(lunchbusService.findAllActiveLunchbuses());
    }

    @GetMapping("/past")
    public ResponseEntity<List<LunchbusSimpleDto>> listPast() {
        final int LIMITED_DAYS = 3;
        return ResponseEntity.ok(lunchbusService.findPastLunchbusesInLimitedDays(LIMITED_DAYS));
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
    public ResponseEntity<BasicResponse> join(@PathVariable Long busId) {
        return ResponseEntity.ok(lunchbusService.joinLunchbus(busId));
    }

    @PostMapping("/{busId}/leave")
    public ResponseEntity<BasicResponse> leave(@PathVariable Long busId) {
        return ResponseEntity.ok(lunchbusService.leaveLunchbus(busId));
    }
}
