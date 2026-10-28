package com.college.placement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Preparation scores of a student (0-100 each). One-to-one with StudentProfile.
 */
@Entity
@Table(name = "preparation_scores",
        uniqueConstraints = @UniqueConstraint(name = "uq_preparation_student", columnNames = "student_id"))
public class PreparationScore {

    public static final int WEIGHT_APTITUDE = 20;
    public static final int WEIGHT_CODING = 30;
    public static final int WEIGHT_TECHNICAL = 20;
    public static final int WEIGHT_PROJECTS = 20;
    public static final int WEIGHT_COMMUNICATION = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentProfile student;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer aptitude = 0;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer coding = 0;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer technical = 0;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer projects = 0;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer communication = 0;

    public PreparationScore() {
    }

    public PreparationScore(StudentProfile student, int aptitude, int coding, int technical, int projects, int communication) {
        this.student = student;
        this.aptitude = aptitude;
        this.coding = coding;
        this.technical = technical;
        this.projects = projects;
        this.communication = communication;
    }

    /**
     * Readiness score = aptitude*0.20 + coding*0.30 + technical*0.20 + projects*0.20 + communication*0.10
     */
    public double getReadinessScore() {
        return aptitude * 0.20
                + coding * 0.30
                + technical * 0.20
                + projects * 0.20
                + communication * 0.10;
    }

    public Long getId() {
        return id;
    }

    public StudentProfile getStudent() {
        return student;
    }

    public void setStudent(StudentProfile student) {
        this.student = student;
    }

    public Integer getAptitude() {
        return aptitude;
    }

    public void setAptitude(Integer aptitude) {
        this.aptitude = aptitude;
    }

    public Integer getCoding() {
        return coding;
    }

    public void setCoding(Integer coding) {
        this.coding = coding;
    }

    public Integer getTechnical() {
        return technical;
    }

    public void setTechnical(Integer technical) {
        this.technical = technical;
    }

    public Integer getProjects() {
        return projects;
    }

    public void setProjects(Integer projects) {
        this.projects = projects;
    }

    public Integer getCommunication() {
        return communication;
    }

    public void setCommunication(Integer communication) {
        this.communication = communication;
    }
}
