package com.college.placement.service;

import com.college.placement.dto.EligibilityResponse;
import com.college.placement.entity.Company;
import com.college.placement.entity.StudentProfile;
import com.college.placement.entity.StudentSkill;
import com.college.placement.entity.Skill;
import com.college.placement.repository.StudentSkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the eligibility rules using a mocked repository.
 */
@ExtendWith(MockitoExtension.class)
class EligibilityServiceTest {

    @Mock
    private StudentSkillRepository studentSkillRepository;

    private EligibilityService eligibilityService;

    private StudentProfile student;
    private Company company;

    @BeforeEach
    void setUp() {
        eligibilityService = new EligibilityService(studentSkillRepository);

        student = new StudentProfile();
        student.setRegistrationNumber("CS2021001");
        student.setDepartment("CSE");
        student.setGraduationYear(2026);
        student.setCgpa(new BigDecimal("8.5"));

        company = new Company();
        company.setName("TestCorp");
        company.setJobRole("Developer");
        company.setMinCgpa(new BigDecimal("7.0"));
        company.setApplicationDeadline(LocalDate.now().plusDays(10));
    }

    @Test
    void studentWithNoRequirementsIsEligible() {
        EligibilityResponse response = eligibilityService.check(student, company);
        assertTrue(response.eligible());
        assertEquals(0, response.failedCriteria().size());
    }

    @Test
    void lowCgpaFailsEligibility() {
        student.setCgpa(new BigDecimal("6.0"));
        EligibilityResponse response = eligibilityService.check(student, company);
        assertFalse(response.eligible());
        assertTrue(response.failedCriteria().get(0).contains("CGPA"));
    }

    @Test
    void boundaryCgpaExactlyAtMinimumIsEligible() {
        company.setMinCgpa(new BigDecimal("8.5"));
        EligibilityResponse response = eligibilityService.check(student, company);
        assertTrue(response.eligible());
    }

    @Test
    void wrongDepartmentFailsEligibility() {
        company.setRequiredDepartments("IT, ECE");
        EligibilityResponse response = eligibilityService.check(student, company);
        assertFalse(response.eligible());
        assertTrue(response.failedCriteria().get(0).contains("Department"));
    }

    @Test
    void matchingDepartmentCaseIsIgnored() {
        company.setRequiredDepartments("cse, it");
        EligibilityResponse response = eligibilityService.check(student, company);
        assertTrue(response.eligible());
    }

    @Test
    void earlyGraduationYearFailsEligibility() {
        company.setMinGraduationYear(2027);
        EligibilityResponse response = eligibilityService.check(student, company);
        assertFalse(response.eligible());
        assertTrue(response.failedCriteria().get(0).contains("Graduation year"));
    }

    @Test
    void missingSkillFailsEligibility() {
        company.setRequiredSkills("Java, Spring Boot");
        when(studentSkillRepository.findByStudent(any())).thenReturn(List.of(
                new StudentSkill(student, new Skill("Java"))));

        EligibilityResponse response = eligibilityService.check(student, company);
        assertFalse(response.eligible());
        assertTrue(response.failedCriteria().stream().anyMatch(reason -> reason.contains("Spring Boot")));
    }

    @Test
    void allCriteriaMetIsEligible() {
        company.setRequiredDepartments("CSE");
        company.setRequiredSkills("Java");
        company.setMinGraduationYear(2026);
        when(studentSkillRepository.findByStudent(any())).thenReturn(List.of(
                new StudentSkill(student, new Skill("Java"))));

        EligibilityResponse response = eligibilityService.check(student, company);
        assertTrue(response.eligible());
    }
}
