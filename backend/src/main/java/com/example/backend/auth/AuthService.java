package com.example.backend.auth;

import com.example.backend.users.User;
import com.example.backend.users.UserRepository;
import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        String email = normalizedEmail(request.email());
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        User user = new User(request.name().trim(), email, passwordEncoder.encode(request.password()));
        userRepository.save(user);
        return new AuthResponse(jwtService.generateToken(user));
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(normalizedEmail(request.email()))
                .orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas"));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Credenciais inválidas");
        }
        return new AuthResponse(jwtService.generateToken(user));
    }

    private String normalizedEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
