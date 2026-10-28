package com.college.placement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.college.placement.entity.Application;
import com.college.placement.entity.ApplicationStatus;
import com.college.placement.entity.StudentProfile;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @EntityGraph(attributePaths = {"company", "student", "student.user"})
    List<Application> findByStudentUserIdOrderByAppliedAtDesc(Long userId);

    @EntityGraph(attributePaths = {"company", "student", "student.user"})
    List<Application> findAllByOrderByAppliedAtDesc();

    @EntityGraph(attributePaths = {"company", "student", "student.user"})
    List<Application> findByStatusOrderByAppliedAtDesc(ApplicationStatus status);

    @EntityGraph(attributePaths = {"company", "student", "student.user"})
    Optional<Application> findById(Long id);

    boolean existsByStudentUserIdAndCompanyId(Long userId, Long companyId);

    long countByCompanyId(Long companyId);
    
    void deleteByStudent(StudentProfile student);
}
