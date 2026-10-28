package com.college.placement.dto;

import java.util.List;

/**
 * Summary for the student dashboard.
 */
public record StudentDashboardResponse(
        StudentProfileResponse profile,
        PreparationScoreResponse preparation,
        int totalApplications,
        long selectedCount,
        List<ApplicationResponse> recentApplications,
        List<CompanyResponse> upcomingDeadlines
) {
}
