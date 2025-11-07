package com.financer.dto.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing the different types of accounts in the system.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
public enum AccountType {
    
    /**
     * Checking account for daily transactions
     */
    CHECKING("checking", "Conta Corrente"),
    
    /**
     * Savings account for storing money with interest
     */
    SAVINGS("savings", "Conta Poupança"),
    
    /**
     * Investment account for financial applications
     */
    INVESTMENT("investment", "Conta Investimento"),
    
    /**
     * Digital wallet account
     */
    DIGITAL_WALLET("digital_wallet", "Carteira Digital"),
    
    /**
     * Business account for companies
     */
    BUSINESS("business", "Conta Empresarial");

    private final String code;
    private final String description;

    AccountType(String code, String description) {
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
    public static AccountType fromCode(String code) {
        for (AccountType type : AccountType.values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid account type: " + code);
    }

    @Override
    public String toString() {
        return code;
    }
}
