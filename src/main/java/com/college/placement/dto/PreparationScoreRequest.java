package com.college.placement.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Body of PUT /api/students/preparation. Every score must be 0-100.
 */
public record PreparationScoreRequest(
        @NotNull(message = "Aptitude score is required")
        @Min(value = 0, message = "Aptitude score must be between 0 and 100")
        @Max(value = 100, message = "Aptitude score must be between 0 and 100")
        Integer aptitude,

        @NotNull(message = "Coding score is required")
        @Min(value = 0, message = "Coding score must be between 0 and 100")
        @Max(value = 100, message = "Coding score must be between 0 and 100")
        Integer coding,

        @NotNull(message = "Technical score is required")
        @Min(value = 0, message = "Technical score must be between 0 and 100")
        @Max(value = 100, message = "Technical score must be between 0 and 100")
        Integer technical,

        @NotNull(message = "Projects score is required")
        @Min(value = 0, message = "Projects score must be between 0 and 100")
        @Max(value = 100, message = "Projects score must be between 0 and 100")
        Integer projects,

        @NotNull(message = "Communication score is required")
        @Min(value = 0, message = "Communication score must be between 0 and 100")
        @Max(value = 100, message = "Communication score must be between 0 and 100")
        Integer communication
) {
}
