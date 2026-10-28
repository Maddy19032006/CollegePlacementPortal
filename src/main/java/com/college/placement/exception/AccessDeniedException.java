package com.college.placement.exception;

/**
 * Thrown when the authenticated user tries to access data that is not theirs (mapped to 403).
 * Named separately from Spring's AccessDeniedException to keep the handling explicit.
 */
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String message) {
        super(message);
    }
}
