package com.eshop.user.dto;

public record AuthResponse(
        String token,
        UserResponse user
) {
}
