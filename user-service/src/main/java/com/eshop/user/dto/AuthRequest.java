package com.eshop.user.dto;

public record AuthRequest(
        String username,
        String password
) {
}
