package com.financer.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for all Financer microservices.
 * Catches and handles exceptions with consistent error responses.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles FinancerException and its subclasses.
     */
    @ExceptionHandler(FinancerException.class)
    public ResponseEntity<ErrorResponse> handleFinancerException(
            FinancerException ex,
            HttpServletRequest request) {
        
        log.error("Financer exception occurred: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = ErrorResponse.of(ex, request.getRequestURI());
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }

    /**
     * Handles validation errors from @Valid annotations.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        
        log.error("Validation exception occurred: {}", ex.getMessage());
        
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation failed")
                .errorCode("VALIDATION_ERROR")
                .path(request.getRequestURI())
                .validationErrors(validationErrors)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handles all other uncaught exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {
        
        log.error("Unexpected exception occurred: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("An unexpected error occurred")
                .errorCode("INTERNAL_SERVER_ERROR")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
