package startervalley.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import startervalley.backend.dto.auth.AuthRequest;
import startervalley.backend.dto.auth.AuthResponse;
import startervalley.backend.dto.auth.JwtTokenDto;
import startervalley.backend.dto.auth.SignupRequest;
import startervalley.backend.security.auth.CustomUserDetails;
import startervalley.backend.service.auth.AuthService;
import startervalley.backend.service.auth.GithubAuthService;

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
    public ResponseEntity<JwtTokenDto> signup(Authentication authentication, @RequestBody SignupRequest request) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        authService.signup(userDetails.getUser(), userDetails.getAttributes(), request);
        return ResponseEntity.ok(authService.createJwtToken(authentication));
    }

    @PostMapping("/signup/validate")
    public ResponseEntity<Void> validateCode(@RequestBody SignupRequest request) {
        authService.validateGenerationCode(request.getCode(), request.getGenerationId());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
