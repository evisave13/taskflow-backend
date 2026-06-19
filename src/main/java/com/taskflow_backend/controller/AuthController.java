package com.taskflow_backend.controller;

import com.taskflow_backend.entity.User;
import com.taskflow_backend.security.JwtUtil;
import com.taskflow_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    // Register API
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody Map<String, String> request) {

        User user = userService.registerUser(
            request.get("username"),
            request.get("email"),
            request.get("password")
        );

        return ResponseEntity.ok(
            Map.of(
                "message", "User registered successfully!",
                "username", user.getUsername()
            )
        );
    }

    // Login API
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> request) {

        Authentication auth =
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.get("email"),
                    request.get("password")
                )
            );

        String token = jwtUtil.generateToken(
            request.get("email"));

        return ResponseEntity.ok(
            Map.of("token", token)
        );
    }
}