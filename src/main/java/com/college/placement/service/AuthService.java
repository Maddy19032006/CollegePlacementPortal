package com.college.placement.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.college.placement.dto.LoginRequest;
import com.college.placement.dto.RegisterRequest;
import com.college.placement.dto.UserResponse;
import com.college.placement.entity.Role;
import com.college.placement.entity.User;
import com.college.placement.exception.BusinessException;
import com.college.placement.repository.UserRepository;
import com.college.placement.security.CurrentUser;

/**
 * Registration and login. After login the user is authenticated in the HTTP
 * session, so subsequent requests carry the JSESSIONID cookie.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUser currentUser;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, CurrentUser currentUser) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.currentUser = currentUser;
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new BusinessException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email is already taken!");
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
        // Public registration allows student and officer accounts. Admin must never be selectable.
        if (request.role() == Role.ADMIN) {
            throw new BusinessException("Admin accounts cannot be created through public registration");
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
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password()),
                request.fullName(),
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

    /** Verifies credentials and stores the authentication in the security context. */
    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BusinessException(org.springframework.http.HttpStatus.UNAUTHORIZED,
                        "Invalid username or password"));

        if (user.isRemoved()) {
            throw new BusinessException(org.springframework.http.HttpStatus.FORBIDDEN,
                "Your account has been removed by an administrator. Please contact the administrator.");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException(org.springframework.http.HttpStatus.UNAUTHORIZED,
                    "Invalid username or password");
        }
        if (request.role() != null && user.getRole() != request.role()) {
            throw new BusinessException(org.springframework.http.HttpStatus.FORBIDDEN,
                    "Invalid gateway for your account role. Please use the correct login portal.");
        }
        if (!user.isEnabled()) {
            throw new BusinessException(org.springframework.http.HttpStatus.FORBIDDEN,
                    "Your account has been disabled. Contact the administrator.");
        }

        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
                user.getUsername(), null,
                java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority(
                        "ROLE_" + user.getRole().name())));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return UserResponse.from(user);
    }

    /** Returns the currently logged-in user. */
    public UserResponse me() {
        return UserResponse.from(currentUser.get());
    }

    /** Clears the security context to log the user out. */
    @Transactional
    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
