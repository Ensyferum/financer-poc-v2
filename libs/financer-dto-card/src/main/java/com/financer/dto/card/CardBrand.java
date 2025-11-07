package com.financer.dto.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing the brand of the card.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
public enum CardBrand {
    
    /**
     * Visa
     */
    VISA("visa", "Visa"),
    
    /**
     * Mastercard
     */
    MASTERCARD("mastercard", "Mastercard"),
    
    /**
     * American Express
     */
    AMERICAN_EXPRESS("american_express", "American Express"),
    
    /**
     * Elo (Brazilian brand)
     */
    ELO("elo", "Elo"),
    
    /**
     * Hipercard (Brazilian brand)
     */
    HIPERCARD("hipercard", "Hipercard"),
    
    /**
     * Discover
     */
    DISCOVER("discover", "Discover"),
    
    /**
     * Diners Club
     */
    DINERS_CLUB("diners_club", "Diners Club"),
    
    /**
     * JCB
     */
    JCB("jcb", "JCB"),
    
    /**
     * Other/Unknown brand
     */
    OTHER("other", "Outro");

    private final String code;
    private final String description;

    CardBrand(String code, String description) {
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
    public static CardBrand fromCode(String code) {
        for (CardBrand brand : CardBrand.values()) {
            if (brand.code.equalsIgnoreCase(code)) {
                return brand;
            }
        }
        throw new IllegalArgumentException("Invalid card brand: " + code);
    }

    @Override
    public String toString() {
        return code;
    }
}
