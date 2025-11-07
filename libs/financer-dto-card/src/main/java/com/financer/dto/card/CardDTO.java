package com.financer.dto.card;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Card entity.
 * Represents a payment card (credit/debit) in the system.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {

    /**
     * Unique identifier of the card
     */
    private Long id;

    /**
     * User ID who owns this card
     */
    private Long userId;

    /**
     * Associated account ID
     */
    private Long accountId;

    /**
     * Card number (masked for security, e.g., **** **** **** 1234)
     */
    private String cardNumber;

    /**
     * Last 4 digits of the card (for display)
     */
    private String lastFourDigits;

    /**
     * Cardholder name
     */
    private String holderName;

    /**
     * Type of the card
     */
    private CardType type;

    /**
     * Card brand
     */
    private CardBrand brand;

    /**
     * Current status of the card
     */
    private CardStatus status;

    /**
     * Expiration month (1-12)
     */
    private Integer expiryMonth;

    /**
     * Expiration year (YYYY)
     */
    private Integer expiryYear;

    /**
     * Expiration date
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    /**
     * CVV (Card Verification Value) - should never be stored, only for requests
     * @deprecated Do not store CVV
     */
    @Deprecated
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String cvv;

    /**
     * Credit limit (for credit cards)
     */
    private BigDecimal creditLimit;

    /**
     * Available credit
     */
    private BigDecimal availableCredit;

    /**
     * Current balance/debt (for credit cards)
     */
    private BigDecimal currentBalance;

    /**
     * Minimum payment due
     */
    private BigDecimal minimumPayment;

    /**
     * Payment due date
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate paymentDueDate;

    /**
     * Statement closing date
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate statementClosingDate;

    /**
     * Interest rate (percentage)
     */
    private BigDecimal interestRate;

    /**
     * Annual fee
     */
    private BigDecimal annualFee;

    /**
     * Whether the card is the primary/default card
     */
    private Boolean isPrimary;

    /**
     * Whether contactless payment is enabled
     */
    private Boolean contactlessEnabled;

    /**
     * Whether online purchases are enabled
     */
    private Boolean onlinePurchasesEnabled;

    /**
     * Whether international purchases are enabled
     */
    private Boolean internationalPurchasesEnabled;

    /**
     * Daily spending limit
     */
    private BigDecimal dailyLimit;

    /**
     * Monthly spending limit
     */
    private BigDecimal monthlyLimit;

    /**
     * Color code of the card (for UI)
     */
    private String colorCode;

    /**
     * Card nickname
     */
    private String nickname;

    /**
     * Timestamp when the card was issued
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime issuedAt;

    /**
     * Timestamp when the card was activated
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime activatedAt;

    /**
     * Timestamp when the card was created
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * Timestamp of the last update
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    /**
     * Timestamp when the card was cancelled
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime cancelledAt;

    /**
     * Additional metadata as JSON string
     */
    private String metadata;
}
