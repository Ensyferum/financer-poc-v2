package com.financer.dto.card;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for updating an existing card.
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
public class UpdateCardRequest {

    /**
     * New status for the card
     */
    private CardStatus status;

    /**
     * Update cardholder name
     */
    @Size(min = 3, max = 100, message = "Holder name must be between 3 and 100 characters")
    private String holderName;

    /**
     * Update credit limit
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Credit limit cannot be negative")
    private BigDecimal creditLimit;

    /**
     * Update interest rate
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Interest rate cannot be negative")
    @DecimalMax(value = "100.0", inclusive = true, message = "Interest rate cannot exceed 100%")
    private BigDecimal interestRate;

    /**
     * Update whether this is the primary card
     */
    private Boolean isPrimary;

    /**
     * Enable/disable contactless payment
     */
    private Boolean contactlessEnabled;

    /**
     * Enable/disable online purchases
     */
    private Boolean onlinePurchasesEnabled;

    /**
     * Enable/disable international purchases
     */
    private Boolean internationalPurchasesEnabled;

    /**
     * Update daily limit
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Daily limit cannot be negative")
    private BigDecimal dailyLimit;

    /**
     * Update monthly limit
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Monthly limit cannot be negative")
    private BigDecimal monthlyLimit;

    /**
     * Update nickname
     */
    @Size(max = 50, message = "Nickname must not exceed 50 characters")
    private String nickname;

    /**
     * Update color code
     */
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color code must be in hex format (#RRGGBB)")
    private String colorCode;

    /**
     * Update metadata
     */
    private String metadata;
}
