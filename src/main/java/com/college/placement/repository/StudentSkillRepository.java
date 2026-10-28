package com.college.placement.repository;

import com.college.placement.entity.StudentProfile;
import com.college.placement.entity.StudentSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface StudentSkillRepository extends JpaRepository<StudentSkill, Long> {

    List<StudentSkill> findByStudent(StudentProfile student);

    @Modifying
    void deleteByStudent(StudentProfile student);
}
