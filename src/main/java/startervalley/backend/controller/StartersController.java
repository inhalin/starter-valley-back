package startervalley.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import startervalley.backend.dto.response.BaseResponseDto;
import startervalley.backend.dto.user.UserCardListDto;
import startervalley.backend.dto.user.UserProfileReadDto;
import startervalley.backend.dto.user.UserProfileUpdateDto;
import startervalley.backend.security.auth.CustomUserDetails;
import startervalley.backend.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/starters")
public class StartersController {

    private final UserService userService;

    @GetMapping({"/", ""})
    public ResponseEntity<UserCardListDto> list(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        Long generationId = userDetails.getUser().getGeneration().getId();

        return ResponseEntity.ok(userService.findUsersByGeneration(userId, generationId));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserProfileReadDto> show(@PathVariable String username) {
        return ResponseEntity.ok(userService.showUserProfile(username));
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserProfileUpdateDto> update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UserProfileUpdateDto userProfileUpdateDto,
            @PathVariable String username) {
        Long id = userDetails.getId();

        userService.validateUser(id, username);

        return ResponseEntity.ok(userService.updateUserProfile(id, userProfileUpdateDto));
    }
}
