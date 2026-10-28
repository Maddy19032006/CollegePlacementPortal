package com.college.placement.dto;

import jakarta.validation.constraints.NotBlank;
import com.college.placement.entity.Role;

/**
 * Body of POST /api/auth/login.
 */
public record LoginRequest(
        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Password is required")
        String password,
        
        Role role
) {
}
