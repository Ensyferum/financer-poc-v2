package com.financer.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Response DTO for successful authentication.
 * Contains JWT tokens and user information.
 * 
 * @author Financer Development Team
 * @version 1.0.0
 * @since 2025-11-10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * JWT access token for API authentication
     * Short-lived token (e.g., 15 minutes)
     */
    private String accessToken;

    /**
     * JWT refresh token for obtaining new access tokens
     * Long-lived token (e.g., 7 days)
     */
    private String refreshToken;

    /**
     * Token type (typically "Bearer")
     */
    @Builder.Default
    private String tokenType = "Bearer";

    /**
     * Access token expiration time in seconds
     */
    private Long expiresIn;

    /**
     * Refresh token expiration time in seconds
     */
    private Long refreshExpiresIn;

    /**
     * User information (without sensitive data)
     */
    private UserDTO user;

    /**
     * Timestamp of this login
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Builder.Default
    private LocalDateTime loginAt = LocalDateTime.now();

    /**
     * Session ID for tracking
     */
    private String sessionId;

    /**
     * Get full authorization header value.
     * 
     * @return "Bearer {accessToken}"
     */
    public String getAuthorizationHeader() {
        return tokenType + " " + accessToken;
    }

    /**
     * Check if tokens are about to expire (within 5 minutes).
     * 
     * @return true if token needs refresh soon
     */
    public boolean needsRefresh() {
        return expiresIn != null && expiresIn < 300; // 5 minutes
    }

    /**
     * Check if refresh token is about to expire (within 1 hour).
     * 
     * @return true if refresh token needs renewal soon
     */
    public boolean refreshTokenNeedsRenewal() {
        return refreshExpiresIn != null && refreshExpiresIn < 3600; // 1 hour
    }
}
