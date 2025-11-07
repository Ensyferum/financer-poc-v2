package com.financer.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Base exception class for all Financer business exceptions.
 * Provides consistent error handling across all microservices.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@Getter
public class FinancerException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final LocalDateTime timestamp;

    public FinancerException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.timestamp = LocalDateTime.now();
    }

    public FinancerException(String message, String errorCode, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.timestamp = LocalDateTime.now();
    }
}
