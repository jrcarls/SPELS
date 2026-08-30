package com.example.backend.auth;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class AuthCookieService {
    public static final String ACCESS_TOKEN_COOKIE = "ACCESS_TOKEN";
    private final long expirationSeconds;
    private final boolean secure;

    public AuthCookieService(@Value("${jwt.expiration}") long expirationMilliseconds,
                             @Value("${app.security.cookie.secure:false}") boolean secure) {
        this.expirationSeconds = expirationMilliseconds / 1_000;
        this.secure = secure;
    }

    public void addAccessToken(HttpServletResponse response, String token) {
        addCookie(response, ResponseCookie.from(ACCESS_TOKEN_COOKIE, token)
                .httpOnly(true)
                .secure(secure)
                .sameSite("Lax")
                .path("/")
                .maxAge(expirationSeconds)
                .build());
    }

    public void clearAccessToken(HttpServletResponse response) {
        addCookie(response, ResponseCookie.from(ACCESS_TOKEN_COOKIE, "")
                .httpOnly(true)
                .secure(secure)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build());
    }

    private void addCookie(HttpServletResponse response, ResponseCookie cookie) {
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
