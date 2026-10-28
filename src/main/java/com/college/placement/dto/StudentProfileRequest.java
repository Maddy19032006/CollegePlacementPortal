package com.college.placement.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * Body of PUT /api/students/profile.
 */
public record StudentProfileRequest(
        @NotBlank(message = "Registration number is required")
        @Size(max = 30)
        String registrationNumber,

        @Pattern(regexp = "^[0-9+\\- ]{6,15}$", message = "Phone must be 6-15 digits (optional + and -)")
        String phone,

        @NotBlank(message = "Department is required")
        @Size(max = 60)
        String department,

        @NotBlank(message = "Program is required")
        @Size(max = 60)
        String program,

        @NotNull(message = "Graduation year is required")
        @Min(value = 2024, message = "Graduation year must be 2024 or later")
        @Max(value = 2035, message = "Graduation year must be 2035 or earlier")
        Integer graduationYear,

        @NotNull(message = "CGPA is required")
        @DecimalMin(value = "0.0", message = "CGPA must be between 0 and 10")
        @DecimalMax(value = "10.0", message = "CGPA must be between 0 and 10")
        BigDecimal cgpa,

        @Size(max = 1000)
        String certifications,

        @Size(max = 1000)
        String projects,

        @Size(max = 1500)
        String internshipExperience,

        @Size(max = 1500)
        String extracurricularActivities,

        @Size(max = 1000)
        String resumeSummary,

        /** Skill names; they are stored in the skills/student_skills tables. */
        List<@NotBlank(message = "Skill names cannot be blank") @Size(max = 60) String> skills
) {
}
