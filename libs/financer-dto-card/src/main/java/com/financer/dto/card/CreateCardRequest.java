package com.financer.dto.card;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for creating a new card.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardRequest {

    /**
     * User ID who will own this card
     */
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;

    /**
     * Associated account ID
     */
    @NotNull(message = "Account ID is required")
    @Positive(message = "Account ID must be positive")
    private Long accountId;

    /**
     * Card number (will be encrypted in storage)
     */
    @NotBlank(message = "Card number is required")
    @Pattern(
        regexp = "^[0-9]{13,19}$",
        message = "Card number must be between 13 and 19 digits"
    )
    private String cardNumber;

    /**
     * Cardholder name
     */
    @NotBlank(message = "Holder name is required")
    @Size(min = 3, max = 100, message = "Holder name must be between 3 and 100 characters")
    private String holderName;

    /**
     * Type of the card
     */
    @NotNull(message = "Card type is required")
    private CardType type;

    /**
     * Card brand
     */
    @NotNull(message = "Card brand is required")
    private CardBrand brand;

    /**
     * Expiration month (1-12)
     */
    @NotNull(message = "Expiry month is required")
    @Min(value = 1, message = "Expiry month must be between 1 and 12")
    @Max(value = 12, message = "Expiry month must be between 1 and 12")
    private Integer expiryMonth;

    /**
     * Expiration year (YYYY)
     */
    @NotNull(message = "Expiry year is required")
    @Min(value = 2025, message = "Expiry year must be current year or later")
    private Integer expiryYear;

    /**
     * CVV (Card Verification Value) - not stored
     */
    @NotBlank(message = "CVV is required")
    @Pattern(regexp = "^[0-9]{3,4}$", message = "CVV must be 3 or 4 digits")
    private String cvv;

    /**
     * Credit limit (for credit cards)
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Credit limit cannot be negative")
    private BigDecimal creditLimit;

    /**
     * Interest rate (percentage, for credit cards)
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Interest rate cannot be negative")
    @DecimalMax(value = "100.0", inclusive = true, message = "Interest rate cannot exceed 100%")
    private BigDecimal interestRate;

    /**
     * Annual fee
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Annual fee cannot be negative")
    private BigDecimal annualFee;

    /**
     * Whether this should be the primary card
     */
    private Boolean isPrimary;

    /**
     * Enable contactless payment
     */
    private Boolean contactlessEnabled;

    /**
     * Enable online purchases
     */
    private Boolean onlinePurchasesEnabled;

    /**
     * Enable international purchases
     */
    private Boolean internationalPurchasesEnabled;

    /**
     * Daily spending limit
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Daily limit cannot be negative")
    private BigDecimal dailyLimit;

    /**
     * Monthly spending limit
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "Monthly limit cannot be negative")
    private BigDecimal monthlyLimit;

    /**
     * Card nickname
     */
    @Size(max = 50, message = "Nickname must not exceed 50 characters")
    private String nickname;

    /**
     * Color code (hex format, e.g., #FF5733)
     */
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color code must be in hex format (#RRGGBB)")
    private String colorCode;

    /**
     * Additional metadata
     */
    private String metadata;
}
