package com.college.placement.service;

import com.college.placement.dto.CompanyRequest;
import com.college.placement.dto.CompanyResponse;
import com.college.placement.entity.Company;
import com.college.placement.exception.BusinessException;
import com.college.placement.exception.ResourceNotFoundException;
import com.college.placement.repository.ApplicationRepository;
import com.college.placement.repository.CompanyRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Company CRUD plus search/filter for students.
 */
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final ApplicationRepository applicationRepository;

    public CompanyService(CompanyRepository companyRepository, ApplicationRepository applicationRepository) {
        this.companyRepository = companyRepository;
        this.applicationRepository = applicationRepository;
    }

    /** Search by name and/or filter by eligible department; results sorted by deadline. */
    public List<CompanyResponse> search(String name, String department) {
        Specification<Company> spec = (root, query, builder) -> builder.conjunction();

        if (name != null && !name.isBlank()) {
            spec = spec.and((root, query, builder) ->
                    builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (department != null && !department.isBlank()) {
            spec = spec.and((root, query, builder) ->
                    builder.like(builder.lower(root.get("requiredDepartments")),
                            "%" + department.toLowerCase() + "%"));
        }

        return companyRepository.findAll(spec, Sort.by(Sort.Direction.ASC, "applicationDeadline")).stream()
                .map(CompanyResponse::from)
                .toList();
    }

    public CompanyResponse getById(Long id) {
        return CompanyResponse.from(findEntity(id));
    }

    public List<CompanyResponse> getUpcomingDeadlines(int days) {
        LocalDate limit = LocalDate.now().plusDays(days);
        return companyRepository
                .findByApplicationDeadlineAfterOrderByApplicationDeadlineAsc(LocalDate.now().minusDays(1)).stream()
                .filter(company -> !company.getApplicationDeadline().isAfter(limit))
                .map(CompanyResponse::from)
                .toList();
    }

    @Transactional
    public CompanyResponse create(CompanyRequest request) {
        validate(request, null);
        Company company = new Company();
        applyRequest(company, request);
        return CompanyResponse.from(companyRepository.save(company));
    }

    @Transactional
    public CompanyResponse update(Long id, CompanyRequest request) {
        Company company = findEntity(id);
        validate(request, company);
        applyRequest(company, request);
        return CompanyResponse.from(companyRepository.save(company));
    }

    @Transactional
    public void delete(Long id) {
        Company company = findEntity(id);
        long applications = applicationRepository.countByCompanyId(id);
        if (applications > 0) {
            throw new BusinessException(
                    "Cannot delete a company that has " + applications + " application(s). Close it instead.");
        }
        companyRepository.delete(company);
    }

    private Company findEntity(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + id));
    }

    /** Shared validation for create and update. */
    private void validate(CompanyRequest request, Company existing) {
        if (request.applicationDeadline().isBefore(LocalDate.now())) {
            throw new BusinessException("Application deadline must be today or in the future");
        }
        if (existing == null && request.applicationDeadline().isBefore(LocalDate.now())) {
            throw new BusinessException("Application deadline must be in the future for a new company");
        }
        if (request.minGraduationYear() != null && (request.minGraduationYear() < 2024
                || request.minGraduationYear() > 2035)) {
            throw new BusinessException("Minimum graduation year must be between 2024 and 2035");
        }
    }

    private void applyRequest(Company company, CompanyRequest request) {
        company.setName(request.name().trim());
        company.setJobRole(request.jobRole().trim());
        company.setPackageCtc(request.packageCtc());
        company.setMinCgpa(request.minCgpa());
        company.setRequiredDepartments(request.requiredDepartments());
        company.setRequiredSkills(request.requiredSkills());
        company.setMinGraduationYear(request.minGraduationYear());
        company.setApplicationDeadline(request.applicationDeadline());
        company.setDescription(request.description());
    }
}
