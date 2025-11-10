# Financer DTO User Library

**Version:** 1.0.0  
**Package:** `com.financer.dto.user`

---

## 📋 Overview

This library provides Data Transfer Objects (DTOs) for the **User domain** in the Financer application. It includes DTOs for authentication, authorization, and user management operations.

## 🎯 Purpose

- **User Management**: DTOs for creating, updating, and retrieving user information
- **Authentication**: Login/logout request and response models
- **Authorization**: Role-Based Access Control (RBAC) enums and utilities
- **Account Status**: User account lifecycle management

## 📦 Classes

### DTOs

| Class | Description | Use Case |
|-------|-------------|----------|
| `UserDTO` | Complete user information (without password) | User profile, user list responses |
| `CreateUserRequest` | User registration data | User registration/sign-up |
| `UpdateUserRequest` | User update data (partial) | Profile updates, admin user management |
| `LoginRequest` | Authentication credentials | User login |
| `LoginResponse` | JWT tokens + user info | Login success response |

### Enums

| Enum | Description | Values |
|------|-------------|--------|
| `UserRole` | User roles for RBAC | `ADMIN`, `USER`, `MANAGER`, `GUEST` |
| `UserStatus` | Account status | `ACTIVE`, `SUSPENDED`, `LOCKED`, `PENDING`, `INACTIVE` |

---

## 🔧 Usage Examples

### Create User Request
```java
CreateUserRequest request = CreateUserRequest.builder()
    .username("john.doe")
    .email("john@example.com")
    .password("SecurePass123")
    .confirmPassword("SecurePass123")
    .fullName("John Doe")
    .phoneNumber("+1234567890")
    .role(UserRole.USER)
    .build();

// Validate passwords match
if (!request.passwordsMatch()) {
    throw new ValidationException("Passwords do not match");
}

// Clear sensitive data after processing
request.clearSensitiveData();
```

### User DTO
```java
UserDTO user = UserDTO.builder()
    .id(1L)
    .username("john.doe")
    .email("john@example.com")
    .fullName("John Doe")
    .role(UserRole.USER)
    .status(UserStatus.ACTIVE)
    .emailVerified(true)
    .twoFactorEnabled(false)
    .lastLoginAt(LocalDateTime.now())
    .createdAt(LocalDateTime.now())
    .build();

// Check permissions
boolean isAdmin = user.isAdmin(); // false
boolean canLogin = user.canLogin(); // true
String displayName = user.getDisplayName(); // "John Doe"
```

### Login Request & Response
```java
// Login Request
LoginRequest loginRequest = LoginRequest.builder()
    .username("john.doe")
    .password("SecurePass123")
    .rememberMe(true)
    .build();

// Login Response
LoginResponse loginResponse = LoginResponse.builder()
    .accessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    .refreshToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    .tokenType("Bearer")
    .expiresIn(900L) // 15 minutes
    .refreshExpiresIn(604800L) // 7 days
    .user(user)
    .sessionId("session-123")
    .build();

// Get authorization header
String authHeader = loginResponse.getAuthorizationHeader();
// "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### User Roles
```java
UserRole role = UserRole.ADMIN;

String code = role.getCode(); // "ROLE_ADMIN"
String displayName = role.getDisplayName(); // "Administrator"
boolean isAdmin = role.isAdmin(); // true
boolean canManage = role.canManageUsers(); // true

// From JSON
UserRole parsedRole = UserRole.fromCode("ROLE_USER");
```

### User Status
```java
UserStatus status = UserStatus.ACTIVE;

boolean canLogin = status.canLogin(); // true
boolean isDisabled = status.isDisabled(); // false
boolean requiresAction = status.requiresAction(); // false

// From JSON
UserStatus parsedStatus = UserStatus.fromCode("LOCKED");
```

---

## 🔒 Security Features

### Password Validation
- Minimum 8 characters
- At least one uppercase letter
- At least one lowercase letter
- At least one digit
- Pattern enforced at DTO level

### Username Validation
- 3-50 characters
- Alphanumeric with underscores and hyphens only
- Pattern: `^[a-zA-Z0-9_-]+$`

### Sensitive Data Handling
All request DTOs with sensitive data (passwords, tokens) have a `clearSensitiveData()` method:

```java
CreateUserRequest request = ...;
// Process request
request.clearSensitiveData(); // Clears password fields
```

### JWT Token Management
- Access Token: Short-lived (e.g., 15 minutes)
- Refresh Token: Long-lived (e.g., 7 days)
- Automatic expiration tracking

---

## 📊 RBAC (Role-Based Access Control)

### Role Hierarchy
```
ADMIN > MANAGER > USER > GUEST
```

### Permissions Matrix

| Permission | ADMIN | MANAGER | USER | GUEST |
|-----------|-------|---------|------|-------|
| Manage all users | ✅ | ✅ | ❌ | ❌ |
| Full system access | ✅ | ❌ | ❌ | ❌ |
| Manage own account | ✅ | ✅ | ✅ | ❌ |
| View own data | ✅ | ✅ | ✅ | ✅ |
| Create/Update/Delete | ✅ | ✅ | ✅ | ❌ |

---

## 🧪 Testing

The library includes comprehensive validation tests. Example:

```java
@Test
void testCreateUserRequestValidation() {
    CreateUserRequest request = CreateUserRequest.builder()
        .username("ab") // Too short
        .email("invalid-email")
        .password("weak")
        .build();
    
    Set<ConstraintViolation<CreateUserRequest>> violations = 
        validator.validate(request);
    
    assertFalse(violations.isEmpty());
}
```

---

## 📝 Dependencies

```xml
<dependencies>
    <!-- Jakarta Bean Validation -->
    <dependency>
        <groupId>jakarta.validation</groupId>
        <artifactId>jakarta.validation-api</artifactId>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
    
    <!-- Jackson -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-annotations</artifactId>
    </dependency>
</dependencies>
```

---

## 🏗️ Build

```bash
cd libs/financer-dto-user
mvn clean install
```

---

## 📚 Related Libraries

- [`financer-dto-account`](../financer-dto-account/README.md) - Account domain DTOs
- [`financer-dto-transaction`](../financer-dto-transaction/README.md) - Transaction domain DTOs
- [`financer-dto-card`](../financer-dto-card/README.md) - Card domain DTOs
- [`financer-common`](../financer-common/README.md) - Common utilities

---

## 📄 License

Internal use only - Financer Project

---

**Author:** Financer Development Team  
**Created:** 2025-11-10  
**Last Updated:** 2025-11-10
