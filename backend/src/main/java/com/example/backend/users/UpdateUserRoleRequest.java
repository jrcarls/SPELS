package com.example.backend.users;

import jakarta.validation.constraints.NotNull;

public record UpdateUserRoleRequest(@NotNull PlatformRole role) { }
