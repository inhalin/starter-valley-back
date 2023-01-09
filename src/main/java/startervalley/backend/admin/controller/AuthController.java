package startervalley.backend.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import startervalley.backend.admin.dto.auth.AuthPasswordRequest;
import startervalley.backend.admin.dto.auth.AuthRegisterRequest;
import startervalley.backend.admin.dto.auth.AuthLoginRequest;
import startervalley.backend.admin.dto.auth.AuthLoginResponse;
import startervalley.backend.admin.service.AdminAuthService;
import startervalley.backend.dto.common.BasicResponse;
import startervalley.backend.security.auth.AdminUserDetails;

import javax.validation.Valid;

@RestController(value = "AuthControllerBO")
@RequiredArgsConstructor
@RequestMapping("/admin/auth")
public class AuthController {

    private final AdminAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@Valid @RequestBody AuthLoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
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
}
