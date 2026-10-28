package com.college.placement.dto;

import com.college.placement.entity.User;
import java.time.LocalDateTime;

/**
 * Safe view of a user account: never includes the password hash.
 */
public record UserResponse(
        Long id,
        String username,
        String email,
        String fullName,
        String role,
        boolean enabled,
        LocalDateTime createdAt,
        String studentId,
        String department,
        String degree,
        String companyName,
        String designation,
        String employeeId
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRole().name(),
                user.isEnabled(),
                user.getCreatedAt(),
                user.getStudentId(),
                user.getDepartment(),
                user.getDegree(),
                user.getCompanyName(),
                user.getDesignation(),
                user.getEmployeeId()
        );
    }
}
