package com.financer.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Request DTO for creating a new user account.
 * Contains all required fields for user registration.
 * 
 * @author Financer Development Team
 * @version 1.0.0
 * @since 2025-11-10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique username for authentication
     * Must be 3-50 characters, alphanumeric with underscores and hyphens
     */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", 
             message = "Username can only contain letters, numbers, underscores and hyphens")
    private String username;

    /**
     * User's email address (must be unique)
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    /**
     * User's password
     * Must be at least 8 characters with at least one uppercase, one lowercase, one digit
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
             message = "Password must contain at least one uppercase letter, one lowercase letter, and one digit")
    private String password;

    /**
     * Password confirmation (must match password)
     */
    @NotBlank(message = "Password confirmation is required")
    private String confirmPassword;

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
    @Pattern(regexp = "^[+]?[0-9\\s-()]*$", 
             message = "Phone number must be valid")
    private String phoneNumber;

    /**
     * User's role (defaults to USER if not specified)
     */
    @NotNull(message = "User role is required")
    @Builder.Default
    private UserRole role = UserRole.USER;

    /**
     * Validate that password and confirmPassword match.
     * 
     * @return true if passwords match
     */
    public boolean passwordsMatch() {
        return password != null && password.equals(confirmPassword);
    }

    /**
     * Clear sensitive data after processing.
     * Should be called after successful registration.
     */
    public void clearSensitiveData() {
        this.password = null;
        this.confirmPassword = null;
    }
}
