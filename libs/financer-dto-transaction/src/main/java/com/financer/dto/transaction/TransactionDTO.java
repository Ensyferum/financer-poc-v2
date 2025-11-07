package com.financer.dto.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Transaction entity.
 * Represents a financial transaction in the system.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    /**
     * Unique identifier of the transaction
     */
    private Long id;

    /**
     * User ID who owns this transaction
     */
    private Long userId;

    /**
     * Source account ID (debit from)
     */
    private Long fromAccountId;

    /**
     * Destination account ID (credit to)
     */
    private Long toAccountId;

    /**
     * Type of transaction
     */
    private TransactionType type;

    /**
     * Current status of the transaction
     */
    private TransactionStatus status;

    /**
     * Transaction amount
     */
    private BigDecimal amount;

    /**
     * Currency code (e.g., BRL, USD, EUR)
     */
    private String currency;

    /**
     * Transaction description
     */
    private String description;

    /**
     * Category of the transaction
     */
    private String category;

    /**
     * Tags for the transaction (comma-separated or JSON array)
     */
    private String tags;

    /**
     * Reference ID for external systems
     */
    private String referenceId;

    /**
     * Transaction fee (if applicable)
     */
    private BigDecimal fee;

    /**
     * Balance after transaction (from account)
     */
    private BigDecimal balanceAfter;

    /**
     * Scheduled date for future transactions
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime scheduledAt;

    /**
     * Timestamp when the transaction was processed
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime processedAt;

    /**
     * Timestamp when the transaction was created
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * Timestamp of the last update
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    /**
     * Failure reason (if status is FAILED)
     */
    private String failureReason;

    /**
     * Additional metadata as JSON string
     */
    private String metadata;

    /**
     * Geolocation data (lat,long or JSON)
     */
    private String location;

    /**
     * Device information
     */
    private String deviceInfo;

    /**
     * IP address from which transaction was initiated
     */
    private String ipAddress;
}
