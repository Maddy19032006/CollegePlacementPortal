package com.college.placement.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.college.placement.dto.AdminStatsResponse;
import com.college.placement.dto.RegisterRequest;
import com.college.placement.dto.UserResponse;
import com.college.placement.dto.UserStatusRequest;
import com.college.placement.entity.ApplicationStatus;
import com.college.placement.entity.Role;
import com.college.placement.entity.User;
import com.college.placement.exception.BusinessException;
import com.college.placement.exception.ResourceNotFoundException;
import com.college.placement.repository.ApplicationRepository;
import com.college.placement.repository.CompanyRepository;
import com.college.placement.repository.PreparationScoreRepository;
import com.college.placement.repository.StudentSkillRepository;
import com.college.placement.repository.UserRepository;

/**
 * Admin functionality: view users, enable/disable accounts and view statistics.
 */
@Service
public class AdminService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final ApplicationRepository applicationRepository;
    private final com.college.placement.repository.StudentProfileRepository studentProfileRepository;
    private final PreparationScoreRepository preparationScoreRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public AdminService(UserRepository userRepository,
                        CompanyRepository companyRepository,
                        ApplicationRepository applicationRepository,
                        com.college.placement.repository.StudentProfileRepository studentProfileRepository,
                        PreparationScoreRepository preparationScoreRepository,
                        StudentSkillRepository studentSkillRepository,
                        org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.applicationRepository = applicationRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.preparationScoreRepository = preparationScoreRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findByRemovedFalse().stream().map(UserResponse::from).toList();
    }

    public List<UserResponse> getUsersByRole(Role role) {
        return userRepository.findByRoleAndRemovedFalse(role).stream().map(UserResponse::from).toList();
    }

    /** Creates a user from the protected administration area. */
    @Transactional
    public UserResponse createUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new BusinessException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email is already registered");
        }

        if (request.role() == Role.STUDENT) {
            if (request.studentId() == null || request.studentId().isBlank()) {
                throw new BusinessException("Student ID / Roll Number is required for students");
            }
            if (request.department() == null || request.department().isBlank()) {
                throw new BusinessException("Department is required for students");
            }
            if (request.degree() == null || request.degree().isBlank()) {
                throw new BusinessException("Degree is required for students");
            }
        }

        if (request.role() == Role.PLACEMENT_OFFICER) {
            if (request.employeeId() == null || request.employeeId().isBlank()) {
                throw new BusinessException("Employee ID is required for placement officers");
            }
            if (request.department() == null || request.department().isBlank()) {
                throw new BusinessException("Department is required for placement officers");
            }
            if (request.companyName() == null || request.companyName().isBlank()) {
                throw new BusinessException("Company Name is required for placement officers");
            }
            if (request.designation() == null || request.designation().isBlank()) {
                throw new BusinessException("Designation is required for placement officers");
            }
        }

        User user = new User(
                request.username().trim(),
                request.email().trim(),
                passwordEncoder.encode(request.password()),
                request.fullName().trim(),
                request.role()
        );

        if (request.role() == Role.PLACEMENT_OFFICER) {
            user.setEmployeeId(request.employeeId());
            user.setDepartment(request.department());
            user.setCompanyName(request.companyName());
            user.setDesignation(request.designation());
        } else if (request.role() == Role.STUDENT) {
            user.setStudentId(request.studentId());
            user.setDepartment(request.department());
            user.setDegree(request.degree());
        }

        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public UserResponse setEnabled(Long userId, UserStatusRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        if (!request.enabled() && user.getRole() == Role.ADMIN) {
            throw new BusinessException("Admin accounts cannot be disabled");
        }
        user.setEnabled(request.enabled());
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        if (user.getRole() == Role.ADMIN) {
            throw new BusinessException("Admin accounts cannot be deleted");
        }

        studentProfileRepository.findByUserId(userId).ifPresent(profile -> {
            applicationRepository.deleteByStudent(profile);
            studentSkillRepository.deleteByStudent(profile);
            preparationScoreRepository.deleteByStudent(profile);
            studentProfileRepository.delete(profile);
        });
        user.setEnabled(false);
        user.setRemoved(true);
        userRepository.save(user);
    }

    public AdminStatsResponse getStats() {
        List<User> users = userRepository.findByRemovedFalse();
        Map<String, Long> byStatus = new LinkedHashMap<>();
        for (ApplicationStatus status : ApplicationStatus.values()) {
            byStatus.put(status.name(), (long) applicationRepository.findByStatusOrderByAppliedAtDesc(status).size());
        }

        List<AdminStatsResponse.DepartmentStat> byDepartment =
                studentProfileRepository.findAll().stream()
                        .collect(java.util.stream.Collectors.groupingBy(
                                com.college.placement.entity.StudentProfile::getDepartment,
                                java.util.stream.Collectors.counting()))
                        .entrySet().stream()
                        .map(entry -> new AdminStatsResponse.DepartmentStat(entry.getKey(), entry.getValue()))
                        .toList();

        return new AdminStatsResponse(
                users.size(),
                users.stream().filter(u -> u.getRole() == Role.STUDENT).count(),
                users.stream().filter(u -> u.getRole() == Role.PLACEMENT_OFFICER).count(),
                users.stream().filter(u -> u.getRole() == Role.ADMIN).count(),
                companyRepository.count(),
                applicationRepository.count(),
                byStatus.get(ApplicationStatus.SELECTED.name()),
                byStatus.get(ApplicationStatus.REJECTED.name()),
                byStatus,
                byDepartment
        );
    }
}
