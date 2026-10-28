package com.college.placement.dto;

import java.util.List;
import java.util.Map;

/**
 * Telemetry and quality metrics for the Agile ALM & DevOps dashboard widget.
 * Maps to Course Outcome 5 and Theory Module 6.
 */
public record AgileMetricsResponse(
        String releaseVersion,
        String activeSprint,
        int totalStoryPoints,
        int completedStoryPoints,
        double sprintProgressPercentage,
        Map<String, Integer> velocityPerSprint,
        List<BurndownPoint> burndownData,
        TestMetrics testMetrics,
        DevOpsPipelineStatus pipelineStatus
) {
    public record BurndownPoint(int day, int idealPoints, int actualPoints) {}

    public record TestMetrics(
            int totalTests,
            int passedTests,
            int failedTests,
            double passRatePercentage,
            double instructionCoveragePercentage,
            double branchCoveragePercentage,
            double lineCoveragePercentage,
            int testSuitesCount
    ) {}

    public record DevOpsPipelineStatus(
            String jenkinsStatus,
            String dockerStatus,
            String k8sNamespace,
            String elkStatus,
            String gitBranch
    ) {}
}
