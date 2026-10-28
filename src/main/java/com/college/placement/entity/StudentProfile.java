package com.college.placement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

/**
 * Placement profile of a student. One-to-one with User.
 */
@Entity
@Table(name = "student_profiles",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_student_user", columnNames = "user_id"),
                @UniqueConstraint(name = "uq_student_reg_number", columnNames = "registration_number")
        })
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Owning side of the one-to-one relation with the login account. */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "registration_number", nullable = false, length = 30)
    private String registrationNumber;

    @Column(length = 15)
    private String phone;

    @Column(nullable = false, length = 60)
    private String department;

    @Column(nullable = false, length = 60)
    private String program;

    @Column(name = "graduation_year", nullable = false)
    private int graduationYear;

    @Column(name = "cgpa", nullable = false, precision = 4, scale = 2)
    private BigDecimal cgpa;

    /** Comma-separated text kept simple for this course project. */
    @Column(length = 500)
    private String skills;

    @Column(length = 500)
    private String certifications;

    @Column(length = 1000)
    private String projects;

    /** Work experience, internships, workshops or industrial training. */
    @Column(name = "internship_experience", length = 1500)
    private String internshipExperience;

    /** Clubs, leadership, volunteering, sports and other extracurricular achievements. */
    @Column(name = "extracurricular_activities", length = 1500)
    private String extracurricularActivities;

    @Column(name = "resume_summary", length = 1000)
    private String resumeSummary;

    public StudentProfile() {
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(int graduationYear) {
        this.graduationYear = graduationYear;
    }

    public BigDecimal getCgpa() {
        return cgpa;
    }

    public void setCgpa(BigDecimal cgpa) {
        this.cgpa = cgpa;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getCertifications() {
        return certifications;
    }

    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

    public String getInternshipExperience() {
        return internshipExperience;
    }

    public void setInternshipExperience(String internshipExperience) {
        this.internshipExperience = internshipExperience;
    }

    public String getExtracurricularActivities() {
        return extracurricularActivities;
    }

    public void setExtracurricularActivities(String extracurricularActivities) {
        this.extracurricularActivities = extracurricularActivities;
    }

    public String getResumeSummary() {
        return resumeSummary;
    }

    public void setResumeSummary(String resumeSummary) {
        this.resumeSummary = resumeSummary;
    }
}
