package com.college.placement.controller;

import com.college.placement.dto.CompanyRequest;
import com.college.placement.dto.CompanyResponse;
import com.college.placement.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * GET endpoints are for any logged-in user; write endpoints require
 * PLACEMENT_OFFICER or ADMIN (enforced in SecurityConfig).
 */
@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department) {
        return ResponseEntity.ok(companyService.search(name, department));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<CompanyResponse>> upcoming(@RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(companyService.getUpcomingDeadlines(days));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PLACEMENT_OFFICER','ADMIN')")
    public ResponseEntity<CompanyResponse> create(@Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('PLACEMENT_OFFICER','ADMIN')")
    public ResponseEntity<CompanyResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.ok(companyService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('PLACEMENT_OFFICER','ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
