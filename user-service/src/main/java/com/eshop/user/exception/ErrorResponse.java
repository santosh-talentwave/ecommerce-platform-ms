package com.eshop.user.exception;

import org.springframework.validation.FieldError;

import java.time.Instant;
import java.util.List;

public record ErrorResponse (
        boolean success,
        int status,
        String error,
        String message,
        String path,
        List<FieldErrorItem> fieldErrors,
        Instant timestamp
){

    public record  FieldErrorItem(String field, String message){}

    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(false, status, error, message, path, null, Instant.now());
    }

    public static ErrorResponse of(int status, String error, String message, String path, List<FieldErrorItem> fieldErrors) {
        return new ErrorResponse(false, status, error, message, path, fieldErrors, Instant.now());
    }
}
