package com.college.placement.service;

import com.college.placement.dto.ApplicationResponse;
import com.college.placement.dto.ApplicationStatusRequest;
import com.college.placement.dto.EligibilityResponse;
import com.college.placement.entity.Application;
import com.college.placement.entity.ApplicationStatus;
import com.college.placement.entity.Company;
import com.college.placement.entity.StudentProfile;
import com.college.placement.exception.AccessDeniedException;
import com.college.placement.exception.BusinessException;
import com.college.placement.exception.ResourceNotFoundException;
import com.college.placement.repository.ApplicationRepository;
import com.college.placement.repository.CompanyRepository;
import com.college.placement.repository.StudentProfileRepository;
import com.college.placement.security.CurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Application rules:
 * 1. A student cannot apply twice to the same company.
 * 2. A student cannot apply after the deadline.
 * 3. The student must meet all eligibility criteria.
 * 4. The student must have saved a profile first (CGPA, department, etc.).
 */
@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final CompanyRepository companyRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final EligibilityService eligibilityService;
    private final CurrentUser currentUser;

    public ApplicationService(ApplicationRepository applicationRepository,
                              CompanyRepository companyRepository,
                              StudentProfileRepository studentProfileRepository,
                              EligibilityService eligibilityService,
                              CurrentUser currentUser) {
        this.applicationRepository = applicationRepository;
        this.companyRepository = companyRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.eligibilityService = eligibilityService;
        this.currentUser = currentUser;
    }

    /** Applications of the logged-in student. */
    public List<ApplicationResponse> getMyApplications() {
        Long userId = currentUser.get().getId();
        return applicationRepository.findByStudentUserIdOrderByAppliedAtDesc(userId).stream()
                .map(ApplicationResponse::from)
                .toList();
    }

    /** All applications (officer/admin view), optionally filtered by status. */
    public List<ApplicationResponse> getAll(ApplicationStatus status) {
        List<Application> applications = (status == null)
                ? applicationRepository.findAllByOrderByAppliedAtDesc()
                : applicationRepository.findByStatusOrderByAppliedAtDesc(status);
        return applications.stream().map(ApplicationResponse::from).toList();
    }

    /** Applies to a company after running every business rule. */
    @Transactional
    public ApplicationResponse apply(Long companyId) {
        StudentProfile student = studentProfileRepository.findByUserId(currentUser.get().getId())
                .orElseThrow(() -> new BusinessException("Save your profile before applying to companies"));

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));

        if (applicationRepository.existsByStudentUserIdAndCompanyId(student.getUser().getId(), companyId)) {
            throw new BusinessException("You have already applied to this company");
        }
        if (company.getApplicationDeadline().isBefore(LocalDate.now())) {
            throw new BusinessException("The application deadline for " + company.getName() + " has passed");
        }

        EligibilityResponse eligibility = eligibilityService.check(student, company);
        if (!eligibility.eligible()) {
            throw new BusinessException("Student is not eligible for this company: "
                    + String.join("; ", eligibility.failedCriteria()));
        }

        Application application = new Application();
        application.setStudent(student);
        application.setCompany(company);
        application.setStatus(ApplicationStatus.APPLIED);
        return ApplicationResponse.from(applicationRepository.save(application));
    }

    /** A student may cancel while the application is still APPLIED. */
    @Transactional
    public void cancel(Long applicationId) {
        Application application = findEntity(applicationId);
        Long myUserId = currentUser.get().getId();
        if (!application.getStudent().getUser().getId().equals(myUserId)) {
            throw new AccessDeniedException("You can only cancel your own applications");
        }
        if (application.getStatus() != ApplicationStatus.APPLIED) {
            throw new BusinessException("Only applications in APPLIED status can be cancelled");
        }
        applicationRepository.delete(application);
    }

    /** Officer changes the status; transitions are validated against the flow. */
    @Transactional
    public ApplicationResponse updateStatus(Long applicationId, ApplicationStatusRequest request) {
        Application application = findEntity(applicationId);
        ApplicationStatus current = application.getStatus();
        ApplicationStatus target = request.status();

        if (!current.canTransitionTo(target)) {
            throw new BusinessException("Invalid status transition: " + current + " to " + target
                    + ". Allowed: " + ApplicationStatus.APPLIED.canTransitionTo(target));
        }
        application.setStatus(target);
        application.setLastUpdatedAt(LocalDateTime.now());
        return ApplicationResponse.from(applicationRepository.save(application));
    }

    /** Applications for one company (used by the officer view). */
    public List<ApplicationResponse> getByCompany(Long companyId) {
        return applicationRepository.findAllByOrderByAppliedAtDesc().stream()
                .filter(application -> application.getCompany().getId().equals(companyId))
                .map(ApplicationResponse::from)
                .toList();
    }

    private Application findEntity(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id " + id));
    }
}
