package com.college.placement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * Join entity: a skill that a student has. Replacing the student's skills
 * deletes and recreates these rows, which is simple and fine at this scale.
 */
@Entity
@Table(name = "student_skills",
        uniqueConstraints = @UniqueConstraint(name = "uq_student_skill", columnNames = {"student_id", "skill_id"}))
public class StudentSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentProfile student;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    public StudentSkill() {
    }

    public StudentSkill(StudentProfile student, Skill skill) {
        this.student = student;
        this.skill = skill;
    }

    public Long getId() {
        return id;
    }

    public StudentProfile getStudent() {
        return student;
    }

    public Skill getSkill() {
        return skill;
    }
}
