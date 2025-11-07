package com.financer.dto.account;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for updating an existing account.
 * All fields are optional - only provided fields will be updated.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequest {

    /**
     * New status for the account
     */
    private AccountStatus status;

    /**
     * New name or description of the account
     */
    @Size(min = 3, max = 100, message = "Account name must be between 3 and 100 characters")
    private String name;

    /**
     * New credit limit
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Credit limit cannot be negative")
    private BigDecimal creditLimit;

    /**
     * New interest rate (percentage)
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Interest rate cannot be negative")
    @DecimalMax(value = "100.0", inclusive = true, message = "Interest rate cannot exceed 100%")
    private BigDecimal interestRate;

    /**
     * Update whether this is the default account
     */
    private Boolean isDefault;

    /**
     * New bank name
     */
    @Size(max = 100, message = "Bank name must not exceed 100 characters")
    private String bankName;

    /**
     * Update metadata
     */
    private String metadata;
}
