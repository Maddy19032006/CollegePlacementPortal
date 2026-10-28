package com.college.placement.service;

import com.college.placement.dto.CompanyRequest;
import com.college.placement.entity.Company;
import com.college.placement.exception.BusinessException;
import com.college.placement.repository.ApplicationRepository;
import com.college.placement.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Tests company validation: deadlines and graduation year bounds.
 */
@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private ApplicationRepository applicationRepository;

    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        companyService = new CompanyService(companyRepository, applicationRepository);
    }

    private CompanyRequest request(LocalDate deadline) {
        return new CompanyRequest("TestCorp", "Developer", new BigDecimal("6.5"),
                new BigDecimal("7.0"), "CSE", "Java", 2026, deadline, "desc");
    }

    @Test
    void companyWithPastDeadlineIsRejected() {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> companyService.create(request(LocalDate.now().minusDays(1))));
        assertTrue(exception.getMessage().contains("deadline"));
    }

    @Test
    void companyWithTodayDeadlineIsAccepted() {
        when(companyRepository.save(org.mockito.ArgumentMatchers.any(Company.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = companyService.create(request(LocalDate.now()));
        assertTrue(response.applicationDeadline().isEqual(LocalDate.now()));
    }

    @Test
    void companyWithInvalidGraduationYearIsRejected() {
        CompanyRequest invalid = new CompanyRequest("TestCorp", "Developer", new BigDecimal("6.5"),
                new BigDecimal("7.0"), "CSE", "Java", 1999, LocalDate.now().plusDays(5), "desc");

        BusinessException exception = assertThrows(BusinessException.class, () -> companyService.create(invalid));
        assertTrue(exception.getMessage().contains("graduation year"));
    }

    @Test
    void deletingCompanyWithApplicationsIsRejected() {
        Company company = new Company();
        company.setName("TestCorp");
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(applicationRepository.countByCompanyId(1L)).thenReturn(3L);

        BusinessException exception = assertThrows(BusinessException.class, () -> companyService.delete(1L));
        assertTrue(exception.getMessage().contains("application"));
    }
}
