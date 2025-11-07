package com.financer.dto.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing the type of transaction.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
public enum TransactionType {
    
    /**
     * Credit transaction (money in)
     */
    CREDIT("credit", "Crédito"),
    
    /**
     * Debit transaction (money out)
     */
    DEBIT("debit", "Débito"),
    
    /**
     * Transfer between accounts
     */
    TRANSFER("transfer", "Transferência"),
    
    /**
     * Payment transaction
     */
    PAYMENT("payment", "Pagamento"),
    
    /**
     * Deposit transaction
     */
    DEPOSIT("deposit", "Depósito"),
    
    /**
     * Withdrawal transaction
     */
    WITHDRAWAL("withdrawal", "Saque"),
    
    /**
     * Fee or charge
     */
    FEE("fee", "Taxa"),
    
    /**
     * Refund transaction
     */
    REFUND("refund", "Estorno"),
    
    /**
     * Interest earned
     */
    INTEREST("interest", "Juros"),
    
    /**
     * Adjustment transaction
     */
    ADJUSTMENT("adjustment", "Ajuste");

    private final String code;
    private final String description;

    TransactionType(String code, String description) {
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
    public static TransactionType fromCode(String code) {
        for (TransactionType type : TransactionType.values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid transaction type: " + code);
    }

    @Override
    public String toString() {
        return code;
    }
}
