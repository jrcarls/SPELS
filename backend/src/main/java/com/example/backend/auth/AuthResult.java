package com.example.backend.auth;

import java.util.UUID;

record AuthResult(String accessToken, UUID organizationId) { }
