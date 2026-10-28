package com.college.placement.controller;

import com.college.placement.dto.ApplicationResponse;
import com.college.placement.dto.CompanyResponse;
import com.college.placement.dto.EligibilityResponse;
import com.college.placement.dto.PreparationScoreRequest;
import com.college.placement.dto.PreparationScoreResponse;
import com.college.placement.dto.StudentDashboardResponse;
import com.college.placement.dto.StudentProfileRequest;
import com.college.placement.dto.StudentProfileResponse;
import com.college.placement.service.ApplicationService;
import com.college.placement.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Student-only endpoints (role STUDENT enforced by SecurityConfig).
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final ApplicationService applicationService;

    public StudentController(StudentService studentService, ApplicationService applicationService) {
        this.studentService = studentService;
        this.applicationService = applicationService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<StudentDashboardResponse> dashboard() {
        return ResponseEntity.ok(studentService.getMyDashboard());
    }

    @GetMapping("/profile")
    public ResponseEntity<StudentProfileResponse> getProfile() {
        return ResponseEntity.ok(studentService.getMyProfile());
    }

    @PutMapping("/profile")
    public ResponseEntity<StudentProfileResponse> saveProfile(
            @Valid @RequestBody StudentProfileRequest request) {
        return ResponseEntity.ok(studentService.saveMyProfile(request));
    }

    @GetMapping("/preparation")
    public ResponseEntity<PreparationScoreResponse> getPreparation() {
        return ResponseEntity.ok(studentService.getMyPreparation());
    }

    @PutMapping("/preparation")
    public ResponseEntity<PreparationScoreResponse> savePreparation(
            @Valid @RequestBody PreparationScoreRequest request) {
        return ResponseEntity.ok(studentService.saveMyPreparation(request));
    }

    @GetMapping("/applications")
    public ResponseEntity<List<ApplicationResponse>> myApplications() {
        return ResponseEntity.ok(applicationService.getMyApplications());
    }

    @DeleteMapping("/applications/{id}")
    public ResponseEntity<Void> cancelApplication(@PathVariable Long id) {
        applicationService.cancel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recommended-jobs")
    public ResponseEntity<List<CompanyResponse>> recommendedJobs() {
        return ResponseEntity.ok(studentService.getRecommendedJobs());
    }

    @GetMapping("/companies/{companyId}/eligibility")
    public ResponseEntity<EligibilityResponse> checkEligibility(@PathVariable Long companyId) {
        return ResponseEntity.ok(studentService.checkMyEligibility(companyId));
    }
}
