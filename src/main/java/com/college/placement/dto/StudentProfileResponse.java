package com.college.placement.dto;

import com.college.placement.entity.StudentProfile;
import java.math.BigDecimal;
import java.util.List;

/**
 * Full view of a student profile, including the skill names.
 */
public record StudentProfileResponse(
        Long userId,
        String username,
        String fullName,
        String email,
        String registrationNumber,
        String phone,
        String department,
        String program,
        int graduationYear,
        BigDecimal cgpa,
        String certifications,
        String projects,
        String internshipExperience,
        String extracurricularActivities,
        String resumeSummary,
        List<String> skills
) {
    public static StudentProfileResponse from(StudentProfile profile, List<String> skillNames) {
        return new StudentProfileResponse(
                profile.getUser().getId(),
                profile.getUser().getUsername(),
                profile.getUser().getFullName(),
                profile.getUser().getEmail(),
                profile.getRegistrationNumber(),
                profile.getPhone(),
                profile.getDepartment(),
                profile.getProgram(),
                profile.getGraduationYear(),
                profile.getCgpa(),
                profile.getCertifications(),
                profile.getProjects(),
                profile.getInternshipExperience(),
                profile.getExtracurricularActivities(),
                profile.getResumeSummary(),
                skillNames
        );
    }
}
