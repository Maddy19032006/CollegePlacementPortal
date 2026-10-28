package com.college.placement.controller;

import com.college.placement.dto.ApplicationResponse;
import com.college.placement.dto.ApplicationStatusRequest;
import com.college.placement.entity.ApplicationStatus;
import com.college.placement.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * Students apply and view their own applications; officers list all
 * applications and change statuses.
 */
@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    /** Student: apply to a company. */
    @PostMapping
    public ResponseEntity<ApplicationResponse> apply(@RequestParam Long companyId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(applicationService.apply(companyId));
    }

    /** Officer/admin: all applications, optionally filtered by status. */
    @GetMapping
    @PreAuthorize("hasAnyRole('PLACEMENT_OFFICER','ADMIN')")
    public ResponseEntity<List<ApplicationResponse>> all(
            @RequestParam(required = false) ApplicationStatus status) {
        return ResponseEntity.ok(applicationService.getAll(status));
    }

    /** Officer/admin: update an application's status (validated transition). */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('PLACEMENT_OFFICER','ADMIN')")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationStatusRequest request) {
        return ResponseEntity.ok(applicationService.updateStatus(id, request));
    }
}
