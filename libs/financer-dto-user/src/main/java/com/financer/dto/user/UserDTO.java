package com.financer.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for User information.
 * Represents user data without sensitive fields like password.
 * 
 * @author Financer Development Team
 * @version 1.0.0
 * @since 2025-11-10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for the user
     */
    private Long id;

    /**
     * Unique username for authentication
     */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    /**
     * User's email address
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    /**
     * User's full name
     */
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    /**
     * User's phone number (optional)
     */
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;

    /**
     * User's role in the system
     */
    @NotNull(message = "User role is required")
    private UserRole role;

    /**
     * Current status of the user account
     */
    @NotNull(message = "User status is required")
    private UserStatus status;

    /**
     * Indicates if email is verified
     */
    @Builder.Default
    private Boolean emailVerified = false;

    /**
     * Indicates if two-factor authentication is enabled
     */
    @Builder.Default
    private Boolean twoFactorEnabled = false;

    /**
     * Last login timestamp
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastLoginAt;

    /**
     * Account creation timestamp
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * Last update timestamp
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    /**
     * Version for optimistic locking
     */
    @JsonIgnore
    private Long version;

    /**
     * Check if user is active and can login.
     * 
     * @return true if user can login
     */
    public boolean canLogin() {
        return status != null && status.canLogin();
    }

    /**
     * Check if user has admin role.
     * 
     * @return true if user is admin
     */
    public boolean isAdmin() {
        return role != null && role.isAdmin();
    }

    /**
     * Check if user can manage other users.
     * 
     * @return true if user can manage users
     */
    public boolean canManageUsers() {
        return role != null && role.canManageUsers();
    }

    /**
     * Get user's display name (full name or username).
     * 
     * @return display name
     */
    public String getDisplayName() {
        return fullName != null && !fullName.isBlank() ? fullName : username;
    }
}
