package com.college.placement.dto;

import java.util.List;

/**
 * Result of the eligibility check between one student and one company.
 */
public record EligibilityResponse(
        boolean eligible,
        List<String> failedCriteria,
        String message
) {
}
