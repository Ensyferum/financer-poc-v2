package com.financer.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a requested resource is not found.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
public class ResourceNotFoundException extends FinancerException {

    public ResourceNotFoundException(String resource, String id) {
        super(
                "%s not found with id: %s".formatted(resource, id),
            "RESOURCE_NOT_FOUND",
            HttpStatus.NOT_FOUND
        );
    }

    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}
