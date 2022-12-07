package startervalley.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import startervalley.backend.dto.auth.*;
import startervalley.backend.security.auth.CustomUserDetails;
import startervalley.backend.service.auth.AuthService;
import startervalley.backend.service.auth.GithubAuthService;

import javax.validation.Valid;

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
}
