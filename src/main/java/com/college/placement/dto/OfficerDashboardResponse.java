package com.college.placement.dto;

import java.util.List;

/**
 * Summary for the placement officer dashboard.
 */
public record OfficerDashboardResponse(
        long totalStudents,
        long totalCompanies,
        long totalApplications,
        long selectedCount,
        long rejectedCount,
        List<ApplicationResponse> recentApplications
) {
}
