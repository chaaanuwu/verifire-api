package com.verifire.api.controller;

import com.verifire.api.dto.LoginRequest;
import com.verifire.api.dto.SignupRequest;
import com.verifire.api.service.UserService;
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
public class UserController {

    private final UserService userService;

    @PostMapping("login")
    public ResponseEntity<String> signin(@RequestBody LoginRequest loginRequest) {
        userService.loginUser(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Logged in Successfully.");
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest signupRequest) {
        userService.createUser(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Account created successfully.");
    }
}
