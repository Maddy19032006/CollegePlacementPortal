package com.college.placement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Catalog of known skills (e.g. "Java", "SQL"). Linked to students through StudentSkill.
 */
@Entity
@Table(name = "skills",
        uniqueConstraints = @UniqueConstraint(name = "uq_skill_name", columnNames = "name"))
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 60)
    private String name;

    public Skill() {
    }

    public Skill(String name) {
        this.name = name;
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
}
