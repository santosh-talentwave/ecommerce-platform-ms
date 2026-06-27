package com.eshop.user.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(final ApiException ex, HttpServletRequest req) {
        ErrorResponse body = ErrorResponse.of(
                ex.getStatus().value(), ex.getStatus().getReasonPhrase(), ex.getMessage(), req.getRequestURI()
        );
        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<ErrorResponse.FieldErrorItem> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toFieldError)
                .toList();

        ErrorResponse body = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(), "Bad Request", "Validation Field",
                req.getRequestURI(), fieldErrors
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
        ErrorResponse body = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "An unexpected error occurred",
                req.getRequestURI()
        );
        return ResponseEntity.internalServerError().body(body);
    }

    private ErrorResponse.FieldErrorItem toFieldError(FieldError fieldError) {
        return new ErrorResponse.FieldErrorItem(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
