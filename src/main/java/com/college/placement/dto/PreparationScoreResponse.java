package com.college.placement.dto;

import com.college.placement.entity.PreparationScore;

/**
 * View of preparation scores including the computed readiness score.
 */
public record PreparationScoreResponse(
        Integer aptitude,
        Integer coding,
        Integer technical,
        Integer projects,
        Integer communication,
        double readinessScore
) {
    public static PreparationScoreResponse from(PreparationScore score) {
        return new PreparationScoreResponse(
                score.getAptitude(),
                score.getCoding(),
                score.getTechnical(),
                score.getProjects(),
                score.getCommunication(),
                score.getReadinessScore()
        );
    }

    public static PreparationScoreResponse empty() {
        return new PreparationScoreResponse(0, 0, 0, 0, 0, 0.0);
    }
}
