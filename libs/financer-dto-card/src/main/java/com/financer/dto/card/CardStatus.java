package com.financer.dto.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing the status of a card.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
public enum CardStatus {
    
    /**
     * Card is active and can be used
     */
    ACTIVE("active", "Ativo"),
    
    /**
     * Card is inactive
     */
    INACTIVE("inactive", "Inativo"),
    
    /**
     * Card is blocked (temporarily)
     */
    BLOCKED("blocked", "Bloqueado"),
    
    /**
     * Card is cancelled (permanently)
     */
    CANCELLED("cancelled", "Cancelado"),
    
    /**
     * Card is expired
     */
    EXPIRED("expired", "Expirado"),
    
    /**
     * Card is pending activation
     */
    PENDING_ACTIVATION("pending_activation", "Pendente de Ativação"),
    
    /**
     * Card was lost and should be replaced
     */
    LOST("lost", "Perdido"),
    
    /**
     * Card was stolen
     */
    STOLEN("stolen", "Roubado");

    private final String code;
    private final String description;

    CardStatus(String code, String description) {
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
    public static CardStatus fromCode(String code) {
        for (CardStatus status : CardStatus.values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid card status: " + code);
    }

    @Override
    public String toString() {
        return code;
    }
}
