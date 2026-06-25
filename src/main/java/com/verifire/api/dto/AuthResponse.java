package com.verifire.api.dto;

import com.verifire.api.model.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private RefreshToken refreshToken;
    private UserResponse user;
}
