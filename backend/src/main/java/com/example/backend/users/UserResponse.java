package com.example.backend.users;

import java.util.UUID;

public record UserResponse(UUID id, String name, String email, PlatformRole platformRole, boolean active) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getPublicId(), user.getName(), user.getEmail(), user.getPlatformRole(), user.isActive());
    }
}
