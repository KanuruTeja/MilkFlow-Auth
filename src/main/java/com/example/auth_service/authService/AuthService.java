package com.example.auth_service.authService;

import com.example.auth_service.authEntity.Role;
import com.example.auth_service.authEntity.User;
import com.example.auth_service.authRepo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ✅ Register user
    public User registerUser(String name, String email, String password, String phone, String roleStr) {
        // Check for duplicate email
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User with email already exists: " + email);
        }

        // ✅ Convert role safely (case-insensitive)
        Role role;
        try {
            role = Role.valueOf(roleStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role provided: " + roleStr);
        }

        // Encrypt password
        String encryptedPassword = passwordEncoder.encode(password);

        // Create user entity
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(encryptedPassword);
        user.setPhone(phone);
        user.setRole(role);

        // Save to DB
        return userRepository.save(user);
    }

    // ✅ Login user
    public String login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid credentials: user not found");
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        // Return dummy token (for now)
        return "TOKEN-" + user.getId() + "-" + user.getRole();
    }
}
