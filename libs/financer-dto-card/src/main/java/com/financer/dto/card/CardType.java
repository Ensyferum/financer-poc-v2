package com.financer.dto.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing the type of card.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
public enum CardType {
    
    /**
     * Credit card
     */
    CREDIT("credit", "Crédito"),
    
    /**
     * Debit card
     */
    DEBIT("debit", "Débito"),
    
    /**
     * Prepaid card
     */
    PREPAID("prepaid", "Pré-pago"),
    
    /**
     * Virtual card for online purchases
     */
    VIRTUAL("virtual", "Virtual"),
    
    /**
     * Physical card
     */
    PHYSICAL("physical", "Físico");

    private final String code;
    private final String description;

    CardType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static CardType fromCode(String code) {
        for (CardType type : CardType.values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid card type: " + code);
    }

    @Override
    public String toString() {
        return code;
    }
}
