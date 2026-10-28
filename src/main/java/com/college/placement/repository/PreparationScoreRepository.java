package com.college.placement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.college.placement.entity.PreparationScore;
import com.college.placement.entity.StudentProfile;

public interface PreparationScoreRepository extends JpaRepository<PreparationScore, Long> {

    Optional<PreparationScore> findByStudentUserId(Long userId);
    
    void deleteByStudent(StudentProfile student);
}
