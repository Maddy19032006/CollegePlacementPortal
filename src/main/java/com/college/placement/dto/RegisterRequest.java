package com.college.placement.dto;

import com.college.placement.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Body of POST /api/auth/register.
 * Password rules are enforced here so the API rejects weak passwords early.
 */
public record RegisterRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 4, max = 50, message = "Username must be 4-50 characters")
        @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username may contain only letters, digits, dot, underscore and hyphen")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Full name is required")
        @Size(max = 100)
        String fullName,

        @NotBlank(message = "Password is required")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#]).{8,}$",
                message = "Password must be at least 8 characters and contain upper case, lower case, a digit and a special character"
        )
        String password,

        @NotNull(message = "Role is required")
        Role role,

        @Size(max = 50)
        String employeeId,

        @Size(max = 100)
        String department,

        @Size(max = 100)
        String companyName,

        @Size(max = 100)
        String designation,

        @Size(max = 50)
        String studentId,

        @Size(max = 100)
        String degree
) {
}
