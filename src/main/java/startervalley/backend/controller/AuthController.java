package startervalley.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import startervalley.backend.dto.auth.*;
import startervalley.backend.security.auth.CustomUserDetails;
import startervalley.backend.service.auth.AuthService;
import startervalley.backend.service.auth.GithubAuthService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final GithubAuthService githubAuthService;

    @PostMapping("/login/github")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(githubAuthService.login(authRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<JwtTokenDto> signup(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody SignupRequest request) {
        return ResponseEntity.ok(authService.signup(userDetails.getAttributes(), request));
    }

    @PostMapping("/signup/validate")
    public ResponseEntity<Void> validateCode(@Valid @RequestBody CodeValidationRequest request) {
        authService.validateGenerationCode(request.getCode(), request.getGenerationId());
        return ResponseEntity.ok(null);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {
        authService.logout(userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/available/generations")
    public ResponseEntity<List<Long>> listAvailableGenerations() {
        return ResponseEntity.ok(authService.getAvailableGenerations());
    }

    @GetMapping("/available/devparts")
    public ResponseEntity<List<AvailableDevpart>> listAvailableDevparts(@RequestParam("generation") Long generationId) {
        return ResponseEntity.ok(authService.getAvailableDevparts(generationId));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<JwtTokenDto> refreshToken(@RequestBody JwtRefreshDto dto) {
        return ResponseEntity.ok(authService.refreshToken(dto.getRefreshToken()));
    }
}
