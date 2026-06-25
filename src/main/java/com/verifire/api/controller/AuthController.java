package com.verifire.api.controller;

import com.verifire.api.dto.LoginRequest;
import com.verifire.api.dto.AuthResponse;
import com.verifire.api.dto.RefreshRequest;
import com.verifire.api.dto.SignupRequest;
import com.verifire.api.service.AuthService;
import com.verifire.api.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.loginUser(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest signupRequest) {
        authService.createUser(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Account created successfully.");
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest refreshRequest) {
        AuthResponse response = authService.refreshAccessToken(refreshRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshRequest refreshRequest) {
        refreshTokenService.logoutUser(refreshRequest.getRefreshToken());
        return ResponseEntity.ok("Logged out successfully");
    }
}
