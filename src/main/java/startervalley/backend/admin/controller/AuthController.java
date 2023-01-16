package startervalley.backend.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import startervalley.backend.admin.dto.auth.AuthLoginRequest;
import startervalley.backend.admin.dto.auth.AuthPasswordRequest;
import startervalley.backend.admin.dto.auth.AuthRegisterRequest;
import startervalley.backend.admin.service.AuthService;
import startervalley.backend.dto.auth.JwtRefreshDto;
import startervalley.backend.dto.auth.JwtTokenDto;
import startervalley.backend.dto.common.BasicResponse;
import startervalley.backend.security.auth.AdminUserDetails;

import javax.validation.Valid;

@RestController(value = "AuthControllerBO")
@RequiredArgsConstructor
@RequestMapping("/admin/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDto> login(@Valid @RequestBody AuthLoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<BasicResponse> logout(@AuthenticationPrincipal AdminUserDetails userDetails) {
        return ResponseEntity.ok(authService.logout(userDetails.getId()));
    }

    @PostMapping("/register")
    public ResponseEntity<BasicResponse> register(@Valid @RequestBody AuthRegisterRequest request) {
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/me/password")
    public ResponseEntity<BasicResponse> changePassword(
            @AuthenticationPrincipal AdminUserDetails userDetails,
            @Valid @RequestBody AuthPasswordRequest request) {
        return ResponseEntity.ok(authService.changePassword(userDetails.getId(), request));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<JwtTokenDto> refreshToken(@RequestBody JwtRefreshDto dto) {
        return ResponseEntity.ok(authService.refreshToken(dto.getRefreshToken()));
    }
}
