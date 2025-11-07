package com.financer.dto.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing the status of an account.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
public enum AccountStatus {
    
    /**
     * Account is active and operational
     */
    ACTIVE("active", "Ativa"),
    
    /**
     * Account is temporarily inactive
     */
    INACTIVE("inactive", "Inativa"),
    
    /**
     * Account is blocked (no transactions allowed)
     */
    BLOCKED("blocked", "Bloqueada"),
    
    /**
     * Account is frozen by bank or legal order
     */
    FROZEN("frozen", "Congelada"),
    
    /**
     * Account is closed and cannot be reactivated
     */
    CLOSED("closed", "Encerrada"),
    
    /**
     * Account is pending approval
     */
    PENDING_APPROVAL("pending_approval", "Pendente de Aprovação");

    private final String code;
    private final String description;

    AccountStatus(String code, String description) {
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
    public static AccountStatus fromCode(String code) {
        for (AccountStatus status : AccountStatus.values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid account status: " + code);
    }

    @Override
    public String toString() {
        return code;
    }
}
