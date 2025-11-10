package com.financer.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Request DTO for user login/authentication.
 * Contains credentials for authentication.
 * 
 * @author Financer Development Team
 * @version 1.0.0
 * @since 2025-11-10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Username or email for authentication
     */
    @NotBlank(message = "Username or email is required")
    private String username;

    /**
     * User's password
     */
    @NotBlank(message = "Password is required")
    private String password;

    /**
     * Two-factor authentication code (if enabled)
     */
    private String twoFactorCode;

    /**
     * Remember me flag for extended session
     */
    @Builder.Default
    private Boolean rememberMe = false;

    /**
     * Clear sensitive data after processing.
     * Should be called after authentication attempt.
     */
    public void clearSensitiveData() {
        this.password = null;
        this.twoFactorCode = null;
    }

    /**
     * Check if two-factor authentication is being used.
     * 
     * @return true if 2FA code is provided
     */
    public boolean hasTwoFactorCode() {
        return twoFactorCode != null && !twoFactorCode.isBlank();
    }
}
