package startervalley.backend.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import startervalley.backend.admin.dto.user.AdminUserDto;
import startervalley.backend.admin.service.UserService;

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
}
