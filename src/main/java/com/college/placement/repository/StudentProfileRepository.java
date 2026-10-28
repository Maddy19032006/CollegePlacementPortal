package com.college.placement.repository;

import com.college.placement.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

    Optional<StudentProfile> findByUserId(Long userId);

    boolean existsByRegistrationNumber(String registrationNumber);

    boolean existsByUserId(Long userId);
}
