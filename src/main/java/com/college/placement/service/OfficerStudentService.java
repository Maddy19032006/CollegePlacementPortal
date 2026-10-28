package com.college.placement.service;

import com.college.placement.dto.EligibilityResponse;
import com.college.placement.dto.StudentProfileResponse;
import com.college.placement.entity.Company;
import com.college.placement.entity.StudentProfile;
import com.college.placement.exception.ResourceNotFoundException;
import com.college.placement.repository.CompanyRepository;
import com.college.placement.repository.StudentProfileRepository;
import com.college.placement.repository.StudentSkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Officer view of students and their eligibility for a company.
 */
@Service
public class OfficerStudentService {

    private final StudentProfileRepository studentProfileRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final CompanyRepository companyRepository;
    private final EligibilityService eligibilityService;

    public OfficerStudentService(StudentProfileRepository studentProfileRepository,
                                 StudentSkillRepository studentSkillRepository,
                                 CompanyRepository companyRepository,
                                 EligibilityService eligibilityService) {
        this.studentProfileRepository = studentProfileRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.companyRepository = companyRepository;
        this.eligibilityService = eligibilityService;
    }

    public List<StudentProfileResponse> getAllStudents() {
        return studentProfileRepository.findAll().stream()
                .map(profile -> StudentProfileResponse.from(profile,
                        studentSkillRepository.findByStudent(profile).stream()
                                .map(studentSkill -> studentSkill.getSkill().getName())
                                .toList()))
                .toList();
    }

    /** Eligibility of every student for one company (for the officer screen). */
    public List<StudentEligibilityView> getEligibleStudents(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        return studentProfileRepository.findAll().stream()
                .map(profile -> new StudentEligibilityView(
                        StudentProfileResponse.from(profile,
                                studentSkillRepository.findByStudent(profile).stream()
                                        .map(studentSkill -> studentSkill.getSkill().getName())
                                        .toList()),
                        eligibilityService.check(profile, company)))
                .toList();
    }

    /** One student joined with their eligibility verdict for a company. */
    public record StudentEligibilityView(StudentProfileResponse profile, EligibilityResponse eligibility) {
    }
}
