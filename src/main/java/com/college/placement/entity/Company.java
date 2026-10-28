package com.college.placement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A recruiting company and its eligibility requirements.
 */
@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank
    @Column(name = "job_role", nullable = false, length = 100)
    private String jobRole;

    /** Annual package in LPA (lakhs per annum). */
    @Column(name = "package_ctc", precision = 6, scale = 2)
    private BigDecimal packageCtc;

    @DecimalMin(value = "0.0", message = "Minimum CGPA must be between 0 and 10")
    @Column(name = "min_cgpa", nullable = false, precision = 4, scale = 2)
    private BigDecimal minCgpa;

    /** Comma-separated list of eligible departments; empty means all departments. */
    @Column(name = "required_departments", length = 300)
    private String requiredDepartments;

    /** Comma-separated skills; empty means no specific skill requirement. */
    @Column(name = "required_skills", length = 500)
    private String requiredSkills;

    @Column(name = "min_graduation_year")
    private Integer minGraduationYear;

    @NotNull
    @Column(name = "application_deadline", nullable = false)
    private LocalDate applicationDeadline;

    @Column(length = 1000)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt = LocalDate.now();

    public Company() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public BigDecimal getPackageCtc() {
        return packageCtc;
    }

    public void setPackageCtc(BigDecimal packageCtc) {
        this.packageCtc = packageCtc;
    }

    public BigDecimal getMinCgpa() {
        return minCgpa;
    }

    public void setMinCgpa(BigDecimal minCgpa) {
        this.minCgpa = minCgpa;
    }

    public String getRequiredDepartments() {
        return requiredDepartments;
    }

    public void setRequiredDepartments(String requiredDepartments) {
        this.requiredDepartments = requiredDepartments;
    }

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(String requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public Integer getMinGraduationYear() {
        return minGraduationYear;
    }

    public void setMinGraduationYear(Integer minGraduationYear) {
        this.minGraduationYear = minGraduationYear;
    }

    public LocalDate getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(LocalDate applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
}
