package com.financer.dto.transaction;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Request DTO for creating a new transaction.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequest {

    /**
     * User ID who is creating the transaction
     */
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;

    /**
     * Source account ID (for debit/transfer)
     */
    @Positive(message = "From account ID must be positive")
    private Long fromAccountId;

    /**
     * Destination account ID (for credit/transfer)
     */
    @Positive(message = "To account ID must be positive")
    private Long toAccountId;

    /**
     * Type of transaction
     */
    @NotNull(message = "Transaction type is required")
    private TransactionType type;

    /**
     * Transaction amount
     */
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    /**
     * Currency code
     */
    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be a 3-letter code")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be uppercase letters")
    private String currency;

    /**
     * Transaction description
     */
    @NotBlank(message = "Description is required")
    @Size(min = 3, max = 500, message = "Description must be between 3 and 500 characters")
    private String description;

    /**
     * Category (optional)
     */
    @Size(max = 50, message = "Category must not exceed 50 characters")
    private String category;

    /**
     * Tags (optional)
     */
    @Size(max = 200, message = "Tags must not exceed 200 characters")
    private String tags;

    /**
     * Reference ID for external systems (optional)
     */
    @Size(max = 100, message = "Reference ID must not exceed 100 characters")
    private String referenceId;

    /**
     * Transaction fee (optional)
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Fee cannot be negative")
    private BigDecimal fee;

    /**
     * Scheduled date for future transactions (optional)
     */
    @Future(message = "Scheduled date must be in the future")
    private LocalDateTime scheduledAt;

    /**
     * Additional metadata (optional)
     */
    private String metadata;

    /**
     * Geolocation data (optional)
     */
    @Size(max = 200, message = "Location must not exceed 200 characters")
    private String location;

    /**
     * Device information (optional)
     */
    @Size(max = 200, message = "Device info must not exceed 200 characters")
    private String deviceInfo;

    /**
     * IP address (optional)
     */
    @Pattern(
        regexp = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$",
        message = "Invalid IP address format"
    )
    private String ipAddress;
}
