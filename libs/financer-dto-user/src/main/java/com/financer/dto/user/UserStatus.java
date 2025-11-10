package com.financer.dto.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Enum representing user account status.
 * Controls access and account lifecycle management.
 * 
 * @author Financer Development Team
 * @version 1.0.0
 * @since 2025-11-10
 */
@Getter
public enum UserStatus {
    
    /**
     * Account is active and user can login
     */
    ACTIVE("ACTIVE", "Active", "User account is active and operational"),
    
    /**
     * Account is temporarily suspended
     */
    SUSPENDED("SUSPENDED", "Suspended", "User account is temporarily suspended"),
    
    /**
     * Account is locked (e.g., after too many failed login attempts)
     */
    LOCKED("LOCKED", "Locked", "User account is locked due to security reasons"),
    
    /**
     * Account is pending activation (e.g., email verification pending)
     */
    PENDING("PENDING", "Pending", "User account is pending activation"),
    
    /**
     * Account is inactive (soft delete)
     */
    INACTIVE("INACTIVE", "Inactive", "User account is inactive");

    /**
     * Status code for database storage and JSON serialization
     */
    private final String code;
    
    /**
     * Human-readable status name
     */
    private final String displayName;
    
    /**
     * Detailed description of the status
     */
    private final String description;

    /**
     * Constructor for UserStatus enum.
     *
     * @param code        Status code
     * @param displayName Human-readable name
     * @param description Detailed status description
     */
    UserStatus(String code, String displayName, String description) {
        this.code = code;
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Get the status code for JSON serialization.
     * 
     * @return status code
     */
    @JsonValue
    public String getCode() {
        return code;
    }

    /**
     * Create UserStatus from code (for JSON deserialization).
     * 
     * @param code the status code
     * @return UserStatus enum value
     * @throws IllegalArgumentException if code is invalid
     */
    @JsonCreator
    public static UserStatus fromCode(String code) {
        for (UserStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid user status code: " + code);
    }

    /**
     * Check if user can login with this status.
     * 
     * @return true if user can login
     */
    public boolean canLogin() {
        return this == ACTIVE;
    }

    /**
     * Check if account requires action (pending, locked, suspended).
     * 
     * @return true if account requires action
     */
    public boolean requiresAction() {
        return this == PENDING || this == LOCKED || this == SUSPENDED;
    }

    /**
     * Check if account is effectively disabled.
     * 
     * @return true if account is locked, suspended or inactive
     */
    public boolean isDisabled() {
        return this == LOCKED || this == SUSPENDED || this == INACTIVE;
    }
}
