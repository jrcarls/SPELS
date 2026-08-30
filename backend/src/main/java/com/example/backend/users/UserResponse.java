package com.example.backend.users;

public record UserResponse(Long id, String name, String email, PlatformRole platformRole, boolean active) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getPlatformRole(), user.isActive());
    }
}
