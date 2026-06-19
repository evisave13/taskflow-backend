package com.taskflow_backend.service;

import com.taskflow_backend.entity.User;
import com.taskflow_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Register new user
    public User registerUser(String username, 
                            String email, 
                            String password) {

        // Check karo already exist toh nahi karta
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException(
                "Email already registered!");
        }

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException(
                "Username already taken!");
        }

        // User banao
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        // Password encrypt karke save karo
        user.setPassword(
            passwordEncoder.encode(password));

        return userRepository.save(user);
    }

    // Email se user dhundo
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> 
                new RuntimeException("User not found!"));
    }
}
