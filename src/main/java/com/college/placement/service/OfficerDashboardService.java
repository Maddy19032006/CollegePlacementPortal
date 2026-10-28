package com.college.placement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.college.placement.dto.ApplicationResponse;
import com.college.placement.dto.OfficerDashboardResponse;
import com.college.placement.entity.ApplicationStatus;
import com.college.placement.entity.Role;
import com.college.placement.repository.ApplicationRepository;
import com.college.placement.repository.CompanyRepository;
import com.college.placement.repository.StudentProfileRepository;
import com.college.placement.repository.UserRepository;

/**
 * Aggregated numbers for the placement officer dashboard.
 */
@Service
public class OfficerDashboardService {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final CompanyRepository companyRepository;
    private final ApplicationRepository applicationRepository;

    public OfficerDashboardService(UserRepository userRepository,
                                   StudentProfileRepository studentProfileRepository,
                                   CompanyRepository companyRepository,
                                   ApplicationRepository applicationRepository) {
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.companyRepository = companyRepository;
        this.applicationRepository = applicationRepository;
    }

    public OfficerDashboardResponse getDashboard() {
        long students = studentProfileRepository.count();
        long companies = companyRepository.count();
        long applications = applicationRepository.count();
        long selected = applicationRepository.findByStatusOrderByAppliedAtDesc(ApplicationStatus.SELECTED).size();
        long rejected = applicationRepository.findByStatusOrderByAppliedAtDesc(ApplicationStatus.REJECTED).size();

        List<ApplicationResponse> recent = applicationRepository.findAllByOrderByAppliedAtDesc().stream()
                .limit(5)
                .map(ApplicationResponse::from)
                .toList();

        return new OfficerDashboardResponse(students, companies, applications, selected, rejected, recent);
    }

    public long countStudents() {
        return userRepository.findByRoleAndRemovedFalse(Role.STUDENT).size();
    }
}
