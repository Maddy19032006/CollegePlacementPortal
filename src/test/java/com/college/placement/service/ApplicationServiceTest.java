package com.college.placement.service;

import com.college.placement.dto.ApplicationResponse;
import com.college.placement.dto.ApplicationStatusRequest;
import com.college.placement.dto.EligibilityResponse;
import com.college.placement.entity.Application;
import com.college.placement.entity.ApplicationStatus;
import com.college.placement.entity.Company;
import com.college.placement.entity.StudentProfile;
import com.college.placement.entity.User;
import com.college.placement.exception.BusinessException;
import com.college.placement.repository.ApplicationRepository;
import com.college.placement.repository.CompanyRepository;
import com.college.placement.repository.StudentProfileRepository;
import com.college.placement.security.CurrentUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the application business rules with mocked repositories:
 * duplicate applications, deadlines and status transitions.
 */
@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private StudentProfileRepository studentProfileRepository;
    @Mock
    private EligibilityService eligibilityService;
    @Mock
    private CurrentUser currentUser;

    private ApplicationService applicationService;
    private StudentProfile student;
    private Company company;

    @BeforeEach
    void setUp() {
        applicationService = new ApplicationService(
                applicationRepository, companyRepository, studentProfileRepository,
                eligibilityService, currentUser);

        User user = new User("arjun", "arjun@college.edu", "hash", "Arjun", com.college.placement.entity.Role.STUDENT);
        student = new StudentProfile();
        student.setUser(user);
        student.setCgpa(new BigDecimal("8.5"));
        student.setDepartment("CSE");
        student.setGraduationYear(2026);

        company = new Company();
        company.setName("TestCorp");
        company.setJobRole("Developer");
        company.setMinCgpa(new BigDecimal("7.0"));
        company.setApplicationDeadline(LocalDate.now().plusDays(5));
    }

    @Test
    void eligibleStudentCanApply() {
        when(currentUser.get()).thenReturn(user(student));
        when(studentProfileRepository.findByUserId(user(student).getId())).thenReturn(Optional.of(student));
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(applicationRepository.existsByStudentUserIdAndCompanyId(any(), any())).thenReturn(false);
        when(eligibilityService.check(student, company))
                .thenReturn(new EligibilityResponse(true, List.of(), "eligible"));
        when(applicationRepository.save(any(Application.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ApplicationResponse response = applicationService.apply(1L);

        assertEquals(ApplicationStatus.APPLIED, response.status());
        assertEquals("TestCorp", response.companyName());
    }

    @Test
    void duplicateApplicationIsRejected() {
        when(currentUser.get()).thenReturn(user(student));
        when(studentProfileRepository.findByUserId(any())).thenReturn(Optional.of(student));
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(applicationRepository.existsByStudentUserIdAndCompanyId(any(), any())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> applicationService.apply(1L));

        assertTrue(exception.getMessage().contains("already applied"));
        verify(applicationRepository, never()).save(any());
    }

    @Test
    void applicationAfterDeadlineIsRejected() {
        company.setApplicationDeadline(LocalDate.now().minusDays(1));
        when(currentUser.get()).thenReturn(user(student));
        when(studentProfileRepository.findByUserId(any())).thenReturn(Optional.of(student));
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(applicationRepository.existsByStudentUserIdAndCompanyId(any(), any())).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> applicationService.apply(1L));

        assertTrue(exception.getMessage().contains("deadline"));
        verify(applicationRepository, never()).save(any());
    }

    @Test
    void ineligibleStudentIsRejected() {
        when(currentUser.get()).thenReturn(user(student));
        when(studentProfileRepository.findByUserId(any())).thenReturn(Optional.of(student));
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(applicationRepository.existsByStudentUserIdAndCompanyId(any(), any())).thenReturn(false);
        when(eligibilityService.check(student, company))
                .thenReturn(new EligibilityResponse(false, List.of("CGPA too low"), "not eligible"));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> applicationService.apply(1L));

        assertTrue(exception.getMessage().contains("not eligible"));
        verify(applicationRepository, never()).save(any());
    }

    @Test
    void invalidStatusTransitionIsBlocked() {
        Application application = new Application();
        application.setStatus(ApplicationStatus.APPLIED);
        when(applicationRepository.findById(10L)).thenReturn(Optional.of(application));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> applicationService.updateStatus(10L, new ApplicationStatusRequest(ApplicationStatus.HR_INTERVIEW)));

        assertTrue(exception.getMessage().contains("Invalid status transition"));
    }

    @Test
    void validStatusTransitionIsAllowed() {
        Application application = new Application();
        application.setStatus(ApplicationStatus.APPLIED);
        application.setStudent(student);
        application.setCompany(company);
        when(applicationRepository.findById(10L)).thenReturn(Optional.of(application));
        when(applicationRepository.save(any(Application.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ApplicationResponse response =
                applicationService.updateStatus(10L, new ApplicationStatusRequest(ApplicationStatus.SHORTLISTED));

        assertEquals(ApplicationStatus.SHORTLISTED, response.status());
    }

    private User user(StudentProfile profile) {
        return profile.getUser();
    }
}
