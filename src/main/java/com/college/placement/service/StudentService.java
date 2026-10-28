package com.college.placement.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.college.placement.dto.CompanyResponse;
import com.college.placement.dto.EligibilityResponse;
import com.college.placement.dto.PreparationScoreRequest;
import com.college.placement.dto.PreparationScoreResponse;
import com.college.placement.dto.StudentDashboardResponse;
import com.college.placement.dto.StudentProfileRequest;
import com.college.placement.dto.StudentProfileResponse;
import com.college.placement.entity.Company;
import com.college.placement.entity.PreparationScore;
import com.college.placement.entity.Skill;
import com.college.placement.entity.StudentProfile;
import com.college.placement.entity.StudentSkill;
import com.college.placement.entity.User;
import com.college.placement.exception.ResourceNotFoundException;
import com.college.placement.repository.ApplicationRepository;
import com.college.placement.repository.CompanyRepository;
import com.college.placement.repository.PreparationScoreRepository;
import com.college.placement.repository.SkillRepository;
import com.college.placement.repository.StudentProfileRepository;
import com.college.placement.repository.StudentSkillRepository;
import com.college.placement.security.CurrentUser;

/**
 * Everything a student manages about themselves: profile, skills,
 * preparation scores and the dashboard.
 */
@Service
public class StudentService {

    private final StudentProfileRepository studentProfileRepository;
    private final PreparationScoreRepository preparationScoreRepository;
    private final SkillRepository skillRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final ApplicationRepository applicationRepository;
    private final CompanyRepository companyRepository;
    private final CurrentUser currentUser;
    private final ApplicationService applicationService;
    private final EligibilityService eligibilityService;

    public StudentService(StudentProfileRepository studentProfileRepository,
                          PreparationScoreRepository preparationScoreRepository,
                          SkillRepository skillRepository,
                          StudentSkillRepository studentSkillRepository,
                          ApplicationRepository applicationRepository,
                          CompanyRepository companyRepository,
                          CurrentUser currentUser,
                          ApplicationService applicationService,
                          EligibilityService eligibilityService) {
        this.studentProfileRepository = studentProfileRepository;
        this.preparationScoreRepository = preparationScoreRepository;
        this.skillRepository = skillRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.applicationRepository = applicationRepository;
        this.companyRepository = companyRepository;
        this.currentUser = currentUser;
        this.applicationService = applicationService;
        this.eligibilityService = eligibilityService;
    }

    // ---------- Profile ----------

    public StudentProfileResponse getMyProfile() {
        StudentProfile profile = findMyProfile();
        return StudentProfileResponse.from(profile, getSkillNames(profile));
    }

    /** Creates the profile on first save so the student needs no extra setup step. */
    @Transactional
    public StudentProfileResponse saveMyProfile(StudentProfileRequest request) {
        User user = currentUser.get();
        StudentProfile profile = studentProfileRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    StudentProfile created = new StudentProfile();
                    created.setUser(user);
                    return created;
                });

        if (profile.getRegistrationNumber() == null
                || !profile.getRegistrationNumber().equals(request.registrationNumber())) {
            if (studentProfileRepository.existsByRegistrationNumber(request.registrationNumber())) {
                throw new com.college.placement.exception.BusinessException(
                        "Registration number is already used by another student");
            }
        }

        profile.setRegistrationNumber(request.registrationNumber());
        profile.setPhone(request.phone());
        profile.setDepartment(request.department());
        profile.setProgram(request.program());
        profile.setGraduationYear(request.graduationYear());
        profile.setCgpa(request.cgpa());
        profile.setCertifications(request.certifications());
        profile.setProjects(request.projects());
        profile.setInternshipExperience(request.internshipExperience());
        profile.setExtracurricularActivities(request.extracurricularActivities());
        profile.setResumeSummary(request.resumeSummary());

        StudentProfile saved = studentProfileRepository.save(profile);
        replaceSkills(saved, request.skills() == null ? List.of() : request.skills());
        return StudentProfileResponse.from(saved, getSkillNames(saved));
    }

    // ---------- Preparation scores ----------

    public PreparationScoreResponse getMyPreparation() {
        return preparationScoreRepository.findByStudentUserId(currentUserId())
                .map(PreparationScoreResponse::from)
                .orElse(PreparationScoreResponse.empty());
    }

    @Transactional
    public PreparationScoreResponse saveMyPreparation(PreparationScoreRequest request) {
        StudentProfile profile = findMyProfile();
        PreparationScore score = preparationScoreRepository.findByStudentUserId(currentUserId())
                .orElseGet(() -> new PreparationScore());
        score.setStudent(profile);
        score.setAptitude(request.aptitude());
        score.setCoding(request.coding());
        score.setTechnical(request.technical());
        score.setProjects(request.projects());
        score.setCommunication(request.communication());
        return PreparationScoreResponse.from(preparationScoreRepository.save(score));
    }

    // ---------- Dashboard ----------

    @Transactional(readOnly = true)
    public StudentDashboardResponse getMyDashboard() {
        StudentProfile profile = findMyProfile();
        List<com.college.placement.entity.Application> applications =
                applicationRepository.findByStudentUserIdOrderByAppliedAtDesc(currentUserId());

        long selected = applications.stream()
                .filter(application -> application.getStatus()
                        == com.college.placement.entity.ApplicationStatus.SELECTED)
                .count();

        List<Company> deadlines = companyRepository
                .findByApplicationDeadlineAfterOrderByApplicationDeadlineAsc(java.time.LocalDate.now().minusDays(1))
                .stream()
                .limit(5)
                .toList();

        return new StudentDashboardResponse(
                StudentProfileResponse.from(profile, getSkillNames(profile)),
                getMyPreparation(),
                applications.size(),
                selected,
                applications.stream().map(com.college.placement.dto.ApplicationResponse::from).limit(5).toList(),
                deadlines.stream().map(com.college.placement.dto.CompanyResponse::from).toList()
        );
    }

    // ---------- Job Recommendations ----------

    /**
     * Returns a list of recommended companies for the current student, sorted by suitability.
     * Eligibility is checked (CGPA, graduation year, department, skills).
     * Sorting is primarily by package CTC (descending), then by skill match (descending),
     * with companies having no skill requirements considered best match.
     *
     * @return list of recommended companies (most recommended first)
     */
    @Transactional(readOnly = true)
    public List<CompanyResponse> getRecommendedJobs() {
        StudentProfile student = findMyProfile();
        LocalDate cutoffDate = LocalDate.now().minusDays(1); // include today and future
        List<Company> allCompanies = companyRepository.findByApplicationDeadlineAfterOrderByApplicationDeadlineAsc(cutoffDate);

        List<Company> eligibleCompanies = new ArrayList<>();
        for (Company company : allCompanies) {
            if (eligibilityService.check(student, company).eligible()) {
                eligibleCompanies.add(company);
            }
        }

        // Precompute student skills once (lowercase for case-insensitive comparison)
        List<String> studentSkills = studentSkillRepository.findByStudent(student).stream()
                .map(studentSkill -> studentSkill.getSkill().getName().toLowerCase(Locale.ROOT))
                .toList();

        // Build list of company with match score
        List<CompanyMatch> matches = new ArrayList<>();
        for (Company company : eligibleCompanies) {
            int matchCount;
            boolean noSkillReq = false;
            String requiredSkillsStr = company.getRequiredSkills();
            if (requiredSkillsStr == null || requiredSkillsStr.isBlank()) {
                noSkillReq = true;
                matchCount = Integer.MAX_VALUE; // treat as best possible skill match
            } else {
                List<String> requiredSkills = Arrays.stream(requiredSkillsStr.split(","))
                        .map(String::trim)
                        .filter(token -> !token.isEmpty())
                        .map(token -> token.toLowerCase(Locale.ROOT))
                        .toList();
                matchCount = (int) requiredSkills.stream()
                        .filter(studentSkills::contains)
                        .count();
            }
            matches.add(new CompanyMatch(company, matchCount, noSkillReq));
        }

        // Sort by package CTC descending, then by match count descending (noSkillReq already has MAX_VALUE)
        matches.sort(Comparator
                .comparing((CompanyMatch cm) -> cm.company.getPackageCtc(),
                        Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(Comparator.comparingInt((CompanyMatch cm) -> cm.matchCount).reversed()));

        // Convert to DTOs
        return matches.stream()
                .map(m -> CompanyResponse.from(m.company))
                .toList();
    }

    /** Lets a student see the exact criteria for a vacancy before applying. */
    public EligibilityResponse checkMyEligibility(Long companyId) {
        StudentProfile student = findMyProfile();
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + companyId));
        return eligibilityService.check(student, company);
    }

    // ---------- Helpers ----------

    private StudentProfile findMyProfile() {
        Long userId = currentUserId();
        return studentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Profile not found yet. Save your profile first."));
    }

    private Long currentUserId() {
        return currentUser.get().getId();
    }

    private List<String> getSkillNames(StudentProfile profile) {
        return studentSkillRepository.findByStudent(profile).stream()
                .map(studentSkill -> studentSkill.getSkill().getName())
                .toList();
    }

    /** Replaces the student's skills with the given names, reusing the skill catalog. */
    private void replaceSkills(StudentProfile profile, List<String> skillNames) {
        studentSkillRepository.deleteByStudent(profile);
        studentSkillRepository.flush();
        Map<String, String> uniqueSkills = new LinkedHashMap<>();
        for (String name : skillNames) {
            if (name != null && !name.isBlank()) {
                String trimmed = name.trim();
                uniqueSkills.putIfAbsent(trimmed.toLowerCase(Locale.ROOT), trimmed);
            }
        }
        for (String trimmed : uniqueSkills.values()) {
            Skill skill = skillRepository.findByNameIgnoreCase(trimmed)
                    .orElseGet(() -> skillRepository.save(new Skill(trimmed)));
            studentSkillRepository.save(new StudentSkill(profile, skill));
        }
    }

    /** Helper class to hold company and its skill match count for sorting. */
    private static class CompanyMatch {
        final Company company;
        final int matchCount; // higher is better
        final boolean noSkillReq; // true if company has no skill requirements

        CompanyMatch(Company company, int matchCount, boolean noSkillReq) {
            this.company = company;
            this.matchCount = matchCount;
            this.noSkillReq = noSkillReq;
        }
    }
}
