package com.college.placement.exception;

import java.time.Instant;
import java.util.List;

/**
 * Standard error body for every failed API call:
 * { timestamp, status, error, message, path, fieldErrors? }
 */
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldError> fieldErrors
) {
    public record FieldError(String field, String message) {
    }

    public static ApiError of(int status, String error, String message, String path) {
        return new ApiError(Instant.now(), status, error, message, path, null);
    }
}
