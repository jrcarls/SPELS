package com.example.backend.users;

import com.example.backend.common.ResourceNotFoundException;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    public User findByPublicId(UUID publicId) {
        return userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    public User updateStatus(UUID publicId, boolean active) {
        User user = findByPublicId(publicId);
        user.setActive(active);
        return userRepository.save(user);
    }

    public User updatePlatformRole(UUID publicId, PlatformRole role) {
        User user = findByPublicId(publicId);
        user.setPlatformRole(role);
        return userRepository.save(user);
    }
}
