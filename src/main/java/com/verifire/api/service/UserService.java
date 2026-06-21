package com.verifire.api.service;

import com.verifire.api.dto.LoginRequest;
import com.verifire.api.dto.SignupRequest;
import com.verifire.api.exception.UserException;
import com.verifire.api.model.User;
import com.verifire.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void loginUser(@NonNull LoginRequest loginRequest) {
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
    }

    public void createUser(@NonNull SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new UserException(
                    "Email already exists.",
                    HttpStatus.CONFLICT
            );
        }
        User user = new User();
        updateUserFromRequest(user, signupRequest);
        userRepository.save(user);
    }

    private void updateUserFromRequest(@NonNull User user, @NonNull SignupRequest signupRequest) {
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setName(signupRequest.getName());
    }
}
