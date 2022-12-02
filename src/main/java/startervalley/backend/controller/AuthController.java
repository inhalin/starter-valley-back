package startervalley.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import startervalley.backend.dto.auth.AuthRequest;
import startervalley.backend.dto.auth.AuthResponse;
import startervalley.backend.dto.auth.SignupRequest;
import startervalley.backend.dto.response.BaseResponseDto;
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
    public BaseResponseDto<Void> signup(Authentication authentication, @RequestBody SignupRequest signupRequest) {
        authService.signup(authentication, signupRequest);

        return new BaseResponseDto<>("user signed up successfully", null);
    }
}
