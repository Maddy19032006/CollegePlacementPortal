package com.college.placement.repository;

import com.college.placement.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {

    List<Company> findByNameContainingIgnoreCase(String name);

    List<Company> findByApplicationDeadlineAfterOrderByApplicationDeadlineAsc(java.time.LocalDate date);
}
