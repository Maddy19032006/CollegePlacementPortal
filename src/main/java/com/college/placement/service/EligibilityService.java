package com.college.placement.service;

import com.college.placement.dto.EligibilityResponse;
import com.college.placement.entity.Company;
import com.college.placement.entity.StudentProfile;
import com.college.placement.repository.StudentProfileRepository;
import com.college.placement.repository.StudentSkillRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Checks whether a student meets a company's requirements
 * (CGPA, graduation year, department, skills).
 */
@Service
public class EligibilityService {

    private final StudentSkillRepository studentSkillRepository;

    public EligibilityService(StudentSkillRepository studentSkillRepository) {
        this.studentSkillRepository = studentSkillRepository;
    }

    /** Evaluates all criteria and returns them together with an overall verdict. */
    public EligibilityResponse check(StudentProfile student, Company company) {
        List<String> failed = new ArrayList<>();

        if (student.getCgpa().compareTo(company.getMinCgpa()) < 0) {
            failed.add("CGPA " + student.getCgpa() + " is below the required " + company.getMinCgpa());
        }
        if (company.getMinGraduationYear() != null
                && student.getGraduationYear() < company.getMinGraduationYear()) {
            failed.add("Graduation year " + student.getGraduationYear()
                    + " is before the required " + company.getMinGraduationYear());
        }
        if (company.getRequiredDepartments() != null && !company.getRequiredDepartments().isBlank()) {
            List<String> allowed = split(company.getRequiredDepartments()).stream()
                    .map(token -> token.toLowerCase(Locale.ROOT))
                    .toList();
            if (!allowed.contains(student.getDepartment().toLowerCase(Locale.ROOT))) {
                failed.add("Department " + student.getDepartment() + " is not in the allowed list: "
                        + String.join(", ", allowed));
            }
        }
        if (company.getRequiredSkills() != null && !company.getRequiredSkills().isBlank()) {
            List<String> requiredSkills = split(company.getRequiredSkills());
            List<String> studentSkills = studentSkillRepository.findByStudent(student).stream()
                    .map(studentSkill -> studentSkill.getSkill().getName().toLowerCase(Locale.ROOT))
                    .toList();
            for (String skill : requiredSkills) {
                if (!studentSkills.contains(skill.toLowerCase(Locale.ROOT))) {
                    failed.add("Missing required skill: " + skill);
                }
            }
        }

        boolean eligible = failed.isEmpty();
        return new EligibilityResponse(eligible, failed,
                eligible ? "Student meets all eligibility criteria"
                        : "Student does not meet " + failed.size() + " eligibility criteria");
    }

    /** Splits a comma-separated requirement string into trimmed tokens (case preserved). */
    private List<String> split(String csv) {
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(token -> !token.isEmpty())
                .toList();
    }
}
