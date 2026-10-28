package com.college.placement.service;

import com.college.placement.entity.ApplicationStatus;
import com.college.placement.entity.PreparationScore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pure unit tests for the readiness formula and the status flow.
 */
class ReadinessScoreCalculationTest {

    private PreparationScore scores(int aptitude, int coding, int technical, int projects, int communication) {
        return new PreparationScore(null, aptitude, coding, technical, projects, communication);
    }

    @Test
    void readinessScoreUsesTheDocumentedWeights() {
        PreparationScore score = scores(80, 70, 90, 60, 50);
        // 80*0.20 + 70*0.30 + 90*0.20 + 60*0.20 + 50*0.10 = 16 + 21 + 18 + 12 + 5 = 72
        assertEquals(72.0, score.getReadinessScore(), 0.0001);
    }

    @Test
    void perfectScoresGive100() {
        assertEquals(100.0, scores(100, 100, 100, 100, 100).getReadinessScore(), 0.0001);
    }

    @Test
    void zeroScoresGive0() {
        assertEquals(0.0, scores(0, 0, 0, 0, 0).getReadinessScore(), 0.0001);
    }

    @Test
    void codingWeightIsHighest() {
        PreparationScore onlyCoding = scores(0, 100, 0, 0, 0);
        assertEquals(30.0, onlyCoding.getReadinessScore(), 0.0001);
    }

    @Test
    void validStatusTransitionsAreAllowed() {
        assertTrue(ApplicationStatus.APPLIED.canTransitionTo(ApplicationStatus.SHORTLISTED));
        assertTrue(ApplicationStatus.SHORTLISTED.canTransitionTo(ApplicationStatus.ONLINE_TEST));
        assertTrue(ApplicationStatus.ONLINE_TEST.canTransitionTo(ApplicationStatus.TECHNICAL_INTERVIEW));
        assertTrue(ApplicationStatus.TECHNICAL_INTERVIEW.canTransitionTo(ApplicationStatus.HR_INTERVIEW));
        assertTrue(ApplicationStatus.HR_INTERVIEW.canTransitionTo(ApplicationStatus.SELECTED));
        assertTrue(ApplicationStatus.APPLIED.canTransitionTo(ApplicationStatus.REJECTED));
    }

    @Test
    void invalidStatusTransitionsAreBlocked() {
        assertFalse(ApplicationStatus.APPLIED.canTransitionTo(ApplicationStatus.SELECTED));
        assertFalse(ApplicationStatus.SHORTLISTED.canTransitionTo(ApplicationStatus.APPLIED));
        assertFalse(ApplicationStatus.SELECTED.canTransitionTo(ApplicationStatus.HR_INTERVIEW));
    }
}
