package startervalley.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import startervalley.backend.dto.lunchbus.LunchbusDto;
import startervalley.backend.dto.lunchbus.LunchbusInsertRequest;
import startervalley.backend.dto.lunchbus.LunchbusResponse;
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
    public ResponseEntity<LunchbusResponse> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody LunchbusInsertRequest request) {
        return ResponseEntity.ok(lunchbusService.saveLunchbus(userDetails.getId(), request));
    }

    @GetMapping
    public ResponseEntity<List<LunchbusSimpleDto>> list(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(lunchbusService.findAllActive(userDetails.getUser().getGeneration().getId()));
    }

    @GetMapping("/{busId}")
    public ResponseEntity<LunchbusDto> show(@PathVariable Long busId) {
        return ResponseEntity.ok(lunchbusService.findOneById(busId));
    }
}
