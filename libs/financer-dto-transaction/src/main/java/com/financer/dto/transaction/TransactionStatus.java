package com.financer.dto.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing the status of a transaction.
 * 
 * @author Financer Team
 * @version 1.0.0
 * @since 2025-11-07
 */
public enum TransactionStatus {
    
    /**
     * Transaction is pending processing
     */
    PENDING("pending", "Pendente"),
    
    /**
     * Transaction is being processed
     */
    PROCESSING("processing", "Processando"),
    
    /**
     * Transaction completed successfully
     */
    COMPLETED("completed", "Concluída"),
    
    /**
     * Transaction failed
     */
    FAILED("failed", "Falhou"),
    
    /**
     * Transaction was cancelled
     */
    CANCELLED("cancelled", "Cancelada"),
    
    /**
     * Transaction was reversed/refunded
     */
    REVERSED("reversed", "Estornada"),
    
    /**
     * Transaction is on hold for review
     */
    ON_HOLD("on_hold", "Em Análise"),
    
    /**
     * Transaction scheduled for future processing
     */
    SCHEDULED("scheduled", "Agendada");

    private final String code;
    private final String description;

    TransactionStatus(String code, String description) {
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
    public static TransactionStatus fromCode(String code) {
        for (TransactionStatus status : TransactionStatus.values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid transaction status: " + code);
    }

    @Override
    public String toString() {
        return code;
    }
}
