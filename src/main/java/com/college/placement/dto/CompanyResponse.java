package com.college.placement.dto;

import com.college.placement.entity.Company;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * View of a company as returned by the API.
 */
public record CompanyResponse(
        Long id,
        String name,
        String jobRole,
        BigDecimal packageCtc,
        BigDecimal minCgpa,
        String requiredDepartments,
        String requiredSkills,
        Integer minGraduationYear,
        LocalDate applicationDeadline,
        String description
) {
    public static CompanyResponse from(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getJobRole(),
                company.getPackageCtc(),
                company.getMinCgpa(),
                company.getRequiredDepartments(),
                company.getRequiredSkills(),
                company.getMinGraduationYear(),
                company.getApplicationDeadline(),
                company.getDescription()
        );
    }
}
