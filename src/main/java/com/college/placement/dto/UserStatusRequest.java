package com.college.placement.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Body of PUT /api/admin/users/{id}/status.
 */
public record UserStatusRequest(
        @NotNull(message = "Enabled flag is required")
        Boolean enabled
) {
}
