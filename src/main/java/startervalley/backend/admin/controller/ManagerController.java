package startervalley.backend.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import startervalley.backend.admin.dto.user.AdminUserDto;
import startervalley.backend.admin.service.UserService;
import startervalley.backend.security.auth.AdminUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/managers")
public class ManagerController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<AdminUserDto>> listAdminUsers() {
        return ResponseEntity.ok(userService.findAdminUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdminUser(@AuthenticationPrincipal AdminUserDetails userDetails,
                                                @PathVariable Long id) {
        userService.deleteAdminUser(userDetails.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
