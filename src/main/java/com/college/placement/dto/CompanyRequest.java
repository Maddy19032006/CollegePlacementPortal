package com.college.placement.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Body of POST /api/companies and PUT /api/companies/{id}.
 */
public record CompanyRequest(
        @NotBlank(message = "Company name is required")
        @Size(max = 100)
        String name,

        @NotBlank(message = "Job role is required")
        @Size(max = 100)
        String jobRole,

        @DecimalMin(value = "0.0", message = "Package must be positive")
        BigDecimal packageCtc,

        @NotNull(message = "Minimum CGPA is required")
        @jakarta.validation.constraints.DecimalMax(value = "10.0", message = "Minimum CGPA must be between 0 and 10")
        BigDecimal minCgpa,

        @Size(max = 300, message = "Required departments must be 300 characters or fewer")
        String requiredDepartments,

        @Size(max = 500, message = "Required skills must be 500 characters or fewer")
        String requiredSkills,

        Integer minGraduationYear,

        @NotNull(message = "Application deadline is required")
        LocalDate applicationDeadline,

        @Size(max = 1000)
        String description
) {
}
