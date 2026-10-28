package com.college.placement.dto;

import com.college.placement.entity.ApplicationStatus;
import java.time.LocalDateTime;

/**
 * View of an application as returned by the API.
 */
public record ApplicationResponse(
        Long id,
        Long studentUserId,
        String studentName,
        String registrationNumber,
        String department,
        java.math.BigDecimal cgpa,
        Long companyId,
        String companyName,
        String jobRole,
        ApplicationStatus status,
        LocalDateTime appliedAt
) {
    public static ApplicationResponse from(com.college.placement.entity.Application application) {
        return new ApplicationResponse(
                application.getId(),
                application.getStudent().getUser().getId(),
                application.getStudent().getUser().getFullName(),
                application.getStudent().getRegistrationNumber(),
                application.getStudent().getDepartment(),
                application.getStudent().getCgpa(),
                application.getCompany().getId(),
                application.getCompany().getName(),
                application.getCompany().getJobRole(),
                application.getStatus(),
                application.getAppliedAt()
        );
    }
}
