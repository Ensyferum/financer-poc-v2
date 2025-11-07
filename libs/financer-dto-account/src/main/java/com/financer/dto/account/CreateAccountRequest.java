package com.financer.dto.account;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for creating a new account.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {

    /**
     * User ID who will own this account
     */
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;

    /**
     * Type of the account
     */
    @NotNull(message = "Account type is required")
    private AccountType type;

    /**
     * Name or description of the account
     */
    @NotBlank(message = "Account name is required")
    @Size(min = 3, max = 100, message = "Account name must be between 3 and 100 characters")
    private String name;

    /**
     * Currency code (e.g., BRL, USD, EUR)
     */
    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be a 3-letter code")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be uppercase letters")
    private String currency;

    /**
     * Initial balance (optional, defaults to 0)
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Initial balance cannot be negative")
    private BigDecimal initialBalance;

    /**
     * Bank code (for external accounts)
     */
    @Size(max = 10, message = "Bank code must not exceed 10 characters")
    private String bankCode;

    /**
     * Bank name
     */
    @Size(max = 100, message = "Bank name must not exceed 100 characters")
    private String bankName;

    /**
     * Agency number
     */
    @Size(max = 20, message = "Agency must not exceed 20 characters")
    private String agency;

    /**
     * Account number (for external accounts)
     */
    @Size(max = 20, message = "Account number must not exceed 20 characters")
    private String accountNumber;

    /**
     * Account digit for validation
     */
    @Size(max = 2, message = "Digit must not exceed 2 characters")
    private String digit;

    /**
     * Credit limit (optional, for credit accounts)
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Credit limit cannot be negative")
    private BigDecimal creditLimit;

    /**
     * Interest rate for savings accounts (percentage)
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Interest rate cannot be negative")
    @DecimalMax(value = "100.0", inclusive = true, message = "Interest rate cannot exceed 100%")
    private BigDecimal interestRate;

    /**
     * Whether this should be the default/primary account
     */
    private Boolean isDefault;

    /**
     * Additional metadata as JSON string
     */
    private String metadata;
}
