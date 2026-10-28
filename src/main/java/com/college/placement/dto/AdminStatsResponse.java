package com.college.placement.dto;

import java.util.List;
import java.util.Map;

/**
 * Statistics for the admin dashboard / reports page.
 */
public record AdminStatsResponse(
        long totalUsers,
        long totalStudents,
        long totalOfficers,
        long totalAdmins,
        long totalCompanies,
        long totalApplications,
        long selectedCount,
        long rejectedCount,
        Map<String, Long> applicationsByStatus,
        List<DepartmentStat> studentsByDepartment
) {
    public record DepartmentStat(String department, long students) {
    }
}
