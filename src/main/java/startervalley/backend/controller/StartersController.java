package startervalley.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import startervalley.backend.dto.user.UserProfileReadDto;
import startervalley.backend.dto.user.UserProfileUpdateDto;
import startervalley.backend.dto.response.BaseResponseDto;
import startervalley.backend.dto.user.UserCardDto;
import startervalley.backend.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/starters")
public class StartersController {

    private final UserService userService;

    @GetMapping({"/", ""})
    public BaseResponseDto<List<UserCardDto>> list(@RequestParam Long generationId) {
        return userService.findUsersByGenerationId(generationId);
    }

    @GetMapping("/{id}")
    public BaseResponseDto<UserProfileReadDto> show(@PathVariable Long id) {
        return userService.showUserProfile(id);
    }

    @PutMapping("/{id}")
    public BaseResponseDto<UserProfileUpdateDto> update(@PathVariable Long id, @RequestBody UserProfileUpdateDto userProfileUpdateDto) {
        // TODO: 본인이 아닌 경우 예외 발생
        return userService.updateUserProfile(id, userProfileUpdateDto);
    }
}
