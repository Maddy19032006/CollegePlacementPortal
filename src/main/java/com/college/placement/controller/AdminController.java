package com.college.placement.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.college.placement.dto.AdminStatsResponse;
import com.college.placement.dto.RegisterRequest;
import com.college.placement.dto.UserResponse;
import com.college.placement.dto.UserStatusRequest;
import com.college.placement.entity.Role;
import com.college.placement.service.AdminService;

import jakarta.validation.Valid;

/**
 * Admin-only endpoints (/api/admin/**).
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> users(@RequestParam(required = false) Role role) {
        List<UserResponse> users = (role == null)
                ? adminService.getAllUsers()
                : adminService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED)
                .body(adminService.createUser(request));
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<UserResponse> setUserStatus(
            @PathVariable Long id,
            @Valid @RequestBody UserStatusRequest request) {
        return ResponseEntity.ok(adminService.setEnabled(id, request));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsResponse> stats() {
        return ResponseEntity.ok(adminService.getStats());
    }

    @GetMapping("/agile-metrics")
    public ResponseEntity<com.college.placement.dto.AgileMetricsResponse> agileMetrics() {
        return ResponseEntity.ok(adminService.getAgileMetrics());
    }
}
