package com.financer.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Request DTO for updating user information.
 * All fields are optional - only provided fields will be updated.
 * 
 * @author Financer Development Team
 * @version 1.0.0
 * @since 2025-11-10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * User's email address (optional update)
     */
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    /**
     * User's full name (optional update)
     */
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    /**
     * User's phone number (optional update)
     */
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    @Pattern(regexp = "^[+]?[0-9\\s-()]*$", 
             message = "Phone number must be valid")
    private String phoneNumber;

    /**
     * New password (optional - for password change)
     * Must meet security requirements
     */
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
             message = "Password must contain at least one uppercase letter, one lowercase letter, and one digit")
    private String newPassword;

    /**
     * Current password (required if changing password)
     */
    private String currentPassword;

    /**
     * User's role (admin only - for role changes)
     */
    private UserRole role;

    /**
     * User's status (admin only - for status changes)
     */
    private UserStatus status;

    /**
     * Two-factor authentication enabled flag
     */
    private Boolean twoFactorEnabled;

    /**
     * Version for optimistic locking
     */
    private Long version;

    /**
     * Check if this is a password change request.
     * 
     * @return true if new password is provided
     */
    public boolean isPasswordChangeRequest() {
        return newPassword != null && !newPassword.isBlank();
    }

    /**
     * Check if request has any update fields set.
     * 
     * @return true if at least one field is set
     */
    public boolean hasUpdates() {
        return email != null || fullName != null || phoneNumber != null || 
               newPassword != null || role != null || status != null || 
               twoFactorEnabled != null;
    }

    /**
     * Clear sensitive data after processing.
     */
    public void clearSensitiveData() {
        this.newPassword = null;
        this.currentPassword = null;
    }
}
