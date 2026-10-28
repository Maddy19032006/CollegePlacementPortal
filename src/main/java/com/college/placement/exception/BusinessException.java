package com.college.placement.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when business rules are violated (e.g. duplicate application, deadline passed).
 * Carries the HTTP status so the global handler can map it.
 */
public class BusinessException extends RuntimeException {

    private final HttpStatus status;

    public BusinessException(String message) {
        this(HttpStatus.BAD_REQUEST, message);
    }

    public BusinessException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
