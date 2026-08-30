package com.example.backend.users;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/{id}/status")
    public UserResponse updateStatus(@PathVariable UUID id,
                                     @Valid @RequestBody UpdateUserStatusRequest request) {
        return UserResponse.from(userService.updateStatus(id, request.active()));
    }

    @PatchMapping("/{id}/role")
    public UserResponse updateRole(@PathVariable UUID id,
                                   @Valid @RequestBody UpdateUserRoleRequest request) {
        return UserResponse.from(userService.updatePlatformRole(id, request.role()));
    }
}
