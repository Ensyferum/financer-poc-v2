package com.financer.dto.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Account entity.
 * Represents a financial account in the system.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    /**
     * Unique identifier of the account
     */
    private Long id;

    /**
     * User ID who owns this account
     */
    private Long userId;

    /**
     * Account number (unique within the system)
     */
    private String accountNumber;

    /**
     * Type of the account
     */
    private AccountType type;

    /**
     * Current status of the account
     */
    private AccountStatus status;

    /**
     * Current balance of the account
     */
    private BigDecimal balance;

    /**
     * Currency code (e.g., BRL, USD, EUR)
     */
    private String currency;

    /**
     * Name or description of the account
     */
    private String name;

    /**
     * Bank code (for external accounts)
     */
    private String bankCode;

    /**
     * Bank name
     */
    private String bankName;

    /**
     * Agency number
     */
    private String agency;

    /**
     * Account digit for validation
     */
    private String digit;

    /**
     * Credit limit for the account (if applicable)
     */
    private BigDecimal creditLimit;

    /**
     * Available credit (credit limit - used credit)
     */
    private BigDecimal availableCredit;

    /**
     * Interest rate for savings accounts
     */
    private BigDecimal interestRate;

    /**
     * Whether the account is the default/primary account
     */
    private Boolean isDefault;

    /**
     * Timestamp when the account was created
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * Timestamp of the last update
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    /**
     * Timestamp when the account was closed (if applicable)
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime closedAt;

    /**
     * Additional metadata as JSON string
     */
    private String metadata;
}
