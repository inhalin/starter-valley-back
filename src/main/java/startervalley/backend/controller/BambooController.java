package startervalley.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import startervalley.backend.dto.request.BambooRequestDto;
import startervalley.backend.dto.request.PageRequestDto;
import startervalley.backend.dto.response.BambooResponseDto;
import startervalley.backend.dto.response.PageResultDTO;
import startervalley.backend.entity.Bamboo;
import startervalley.backend.security.auth.CustomUserDetails;
import startervalley.backend.service.BambooService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bamboos")
public class BambooController {

    private final BambooService bambooService;

    @GetMapping
    public ResponseEntity<PageResultDTO<Bamboo, BambooResponseDto>> getBambooList(@Valid @ModelAttribute PageRequestDto pageRequestDto) {
        PageResultDTO<Bamboo, BambooResponseDto> result = bambooService.findBambooList(pageRequestDto);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Long> createBamboo(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @Valid @RequestBody BambooRequestDto bambooRequestDto) {
        Long userId = userDetails.getId();
        Long result = bambooService.createBamboo(userId, bambooRequestDto);
        return ResponseEntity.ok(result);
    }
}
