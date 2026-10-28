package com.college.placement.dto;

import com.college.placement.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Body of PUT /api/applications/{id}/status.
 */
public record ApplicationStatusRequest(
        @NotNull(message = "Status is required")
        ApplicationStatus status
) {
}
