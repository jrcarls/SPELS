package com.example.backend.auth;

import java.util.UUID;

public record AuthResponse(String accessToken, UUID organizationId) { }
