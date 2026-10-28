package com.college.placement.entity;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * Lifecycle of a student's application.
 *
 * APPLIED -> SHORTLISTED -> ONLINE_TEST -> TECHNICAL_INTERVIEW -> HR_INTERVIEW -> SELECTED
 * REJECTED is allowed from any active stage.
 */
public enum ApplicationStatus {
    APPLIED,
    SHORTLISTED,
    ONLINE_TEST,
    TECHNICAL_INTERVIEW,
    HR_INTERVIEW,
    SELECTED,
    REJECTED;

    /**
     * Allowed forward transitions. REJECTED is reachable from every active stage,
     * and no status is terminal (an officer may correct a mistake).
     */
    private static final Map<ApplicationStatus, Set<ApplicationStatus>> ALLOWED_TRANSITIONS = new EnumMap<>(Map.of(
            APPLIED, Set.of(SHORTLISTED, REJECTED),
            SHORTLISTED, Set.of(ONLINE_TEST, REJECTED),
            ONLINE_TEST, Set.of(TECHNICAL_INTERVIEW, REJECTED),
            TECHNICAL_INTERVIEW, Set.of(HR_INTERVIEW, REJECTED),
            HR_INTERVIEW, Set.of(SELECTED, REJECTED),
            SELECTED, Set.of(REJECTED),
            REJECTED, Set.of(APPLIED, SHORTLISTED, ONLINE_TEST, TECHNICAL_INTERVIEW, HR_INTERVIEW, SELECTED)
    ));

    public boolean canTransitionTo(ApplicationStatus target) {
        return ALLOWED_TRANSITIONS.getOrDefault(this, Set.of()).contains(target);
    }
}
