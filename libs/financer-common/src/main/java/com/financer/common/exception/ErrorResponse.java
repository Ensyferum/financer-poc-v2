package com.financer.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error response structure for all Financer APIs.
 * Provides consistent error format across all microservices.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String errorCode;
    private String path;
    private Map<String, String> validationErrors;

    public static ErrorResponse of(FinancerException exception, String path) {
        return ErrorResponse.builder()
                .timestamp(exception.getTimestamp())
                .status(exception.getHttpStatus().value())
                .error(exception.getHttpStatus().getReasonPhrase())
                .message(exception.getMessage())
                .errorCode(exception.getErrorCode())
                .path(path)
                .build();
    }
}
