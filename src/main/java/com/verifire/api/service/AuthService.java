package com.verifire.api.service;

import com.verifire.api.dto.*;
import com.verifire.api.exception.UserException;
import com.verifire.api.model.RefreshToken;
import com.verifire.api.model.User;
import com.verifire.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse loginUser(@NonNull LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserException(
                        "User not found.",
                        HttpStatus.NOT_FOUND
                ));

        if (!passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword())) {
            throw new UserException(
                    "Invalid credentials.",
                    HttpStatus.UNAUTHORIZED
            );
        }

        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

        return new AuthResponse(accessToken, refreshToken, mapToUserResponse(user));
    }

    public void createUser(@NonNull SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new UserException(
                    "Email already exists.",
                    HttpStatus.CONFLICT
            );
        }
        User user = new User();
        createUserFromRequest(user, signupRequest);
        userRepository.save(user);
    }

    public AuthResponse refreshAccessToken(RefreshRequest refreshRequest) {
        RefreshToken refreshToken = refreshTokenService.verifyToken(refreshRequest.getRefreshToken());
        User user = userRepository.findByEmail(refreshToken.getUserEmail())
                .orElseThrow(() -> new UserException(
                        "User not found.",
                        HttpStatus.NOT_FOUND
                ));
        String newAccessToken = jwtService.generateToken(user);

        return new AuthResponse(newAccessToken, refreshToken, mapToUserResponse(user));
    }

//
//    helper functions
//
    private void createUserFromRequest(@NonNull User user, @NonNull SignupRequest signupRequest) {
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setName(signupRequest.getName());
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();

        userResponse.setEmail(user.getEmail());
        userResponse.setName(user.getName());

        return userResponse;
    }
}
