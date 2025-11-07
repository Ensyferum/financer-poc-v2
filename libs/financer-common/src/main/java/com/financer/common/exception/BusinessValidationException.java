package com.financer.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when business validation fails.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
public class BusinessValidationException extends FinancerException {

    public BusinessValidationException(String message) {
        super(message, "BUSINESS_VALIDATION_ERROR", HttpStatus.BAD_REQUEST);
    }

    public BusinessValidationException(String message, Throwable cause) {
        super(message, "BUSINESS_VALIDATION_ERROR", HttpStatus.BAD_REQUEST, cause);
    }
}
