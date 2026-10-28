package com.college.placement.controller;

import com.college.placement.dto.OfficerDashboardResponse;
import com.college.placement.dto.StudentProfileResponse;
import com.college.placement.service.OfficerDashboardService;
import com.college.placement.service.OfficerStudentService;
import com.college.placement.service.OfficerStudentService.StudentEligibilityView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Officer-only endpoints (/api/officer/**). Admin also has access.
 */
@RestController
@RequestMapping("/api/officer")
public class OfficerController {

    private final OfficerDashboardService officerDashboardService;
    private final OfficerStudentService officerStudentService;

    public OfficerController(OfficerDashboardService officerDashboardService,
                             OfficerStudentService officerStudentService) {
        this.officerDashboardService = officerDashboardService;
        this.officerStudentService = officerStudentService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<OfficerDashboardResponse> dashboard() {
        return ResponseEntity.ok(officerDashboardService.getDashboard());
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentProfileResponse>> students() {
        return ResponseEntity.ok(officerStudentService.getAllStudents());
    }

    /** Eligibility of every student against one company. */
    @GetMapping("/companies/{id}/eligible-students")
    public ResponseEntity<List<StudentEligibilityView>> eligibleStudents(@PathVariable Long id) {
        return ResponseEntity.ok(officerStudentService.getEligibleStudents(id));
    }
}
