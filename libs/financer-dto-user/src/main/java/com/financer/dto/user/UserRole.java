package com.financer.dto.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Enum representing user roles in the system.
 * Implements Role-Based Access Control (RBAC).
 * 
 * @author Financer Development Team
 * @version 1.0.0
 * @since 2025-11-10
 */
@Getter
public enum UserRole {
    
    /**
     * Administrator role - Full system access
     */
    ADMIN("ROLE_ADMIN", "Administrator", "Full system access with all permissions"),
    
    /**
     * User role - Standard user access
     */
    USER("ROLE_USER", "User", "Standard user with basic permissions"),
    
    /**
     * Manager role - Enhanced user access
     */
    MANAGER("ROLE_MANAGER", "Manager", "Enhanced access for managing user accounts"),
    
    /**
     * Guest role - Limited read-only access
     */
    GUEST("ROLE_GUEST", "Guest", "Limited read-only access");

    /**
     * Role code used in security contexts (Spring Security format)
     */
    private final String code;
    
    /**
     * Human-readable role name
     */
    private final String displayName;
    
    /**
     * Detailed description of the role
     */
    private final String description;

    /**
     * Constructor for UserRole enum.
     *
     * @param code        Role code (Spring Security format with ROLE_ prefix)
     * @param displayName Human-readable name
     * @param description Detailed role description
     */
    UserRole(String code, String displayName, String description) {
        this.code = code;
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Get the role code for JSON serialization.
     * 
     * @return role code
     */
    @JsonValue
    public String getCode() {
        return code;
    }

    /**
     * Create UserRole from code (for JSON deserialization).
     * 
     * @param code the role code
     * @return UserRole enum value
     * @throws IllegalArgumentException if code is invalid
     */
    @JsonCreator
    public static UserRole fromCode(String code) {
        for (UserRole role : values()) {
            if (role.code.equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid user role code: " + code);
    }

    /**
     * Check if this role has admin privileges.
     * 
     * @return true if this is an admin role
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }

    /**
     * Check if this role can manage users.
     * 
     * @return true if this role can manage users
     */
    public boolean canManageUsers() {
        return this == ADMIN || this == MANAGER;
    }

    /**
     * Check if this is a read-only role.
     * 
     * @return true if this is a guest role
     */
    public boolean isReadOnly() {
        return this == GUEST;
    }
}
