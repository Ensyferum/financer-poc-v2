# 📦 financer-dto-account

**Version:** 1.0.0  
**Type:** DTO Library  
**Domain:** Account Management

Data Transfer Objects para o domínio de contas financeiras.

---

## 📋 Descrição

Esta biblioteca contém todos os DTOs, Enums e Request/Response objects relacionados ao domínio de **contas** (accounts) no sistema Financer.

---

## 📦 Instalação

### Maven

```xml
<dependency>
    <groupId>com.financer</groupId>
    <artifactId>financer-dto-account</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```gradle
implementation 'com.financer:financer-dto-account:1.0.0'
```

---

## 🗂️ Conteúdo

### DTOs

#### AccountDTO
DTO completo representando uma conta financeira no sistema.

**Campos:**
- `id`: Identificador único
- `userId`: ID do usuário proprietário
- `accountNumber`: Número da conta (único)
- `type`: Tipo da conta (AccountType)
- `status`: Status da conta (AccountStatus)
- `balance`: Saldo atual
- `currency`: Código da moeda (BRL, USD, EUR)
- `name`: Nome/descrição da conta
- `bankCode`: Código do banco
- `bankName`: Nome do banco
- `agency`: Agência
- `digit`: Dígito verificador
- `creditLimit`: Limite de crédito
- `availableCredit`: Crédito disponível
- `interestRate`: Taxa de juros
- `isDefault`: Conta padrão/principal
- `createdAt`: Data de criação
- `updatedAt`: Data de atualização
- `closedAt`: Data de encerramento
- `metadata`: Metadados adicionais (JSON)

**Exemplo:**
```java
AccountDTO account = AccountDTO.builder()
    .userId(1L)
    .accountNumber("12345-6")
    .type(AccountType.CHECKING)
    .status(AccountStatus.ACTIVE)
    .balance(new BigDecimal("1500.00"))
    .currency("BRL")
    .name("Minha Conta Corrente")
    .isDefault(true)
    .build();
```

---

### Request Objects

#### CreateAccountRequest
DTO para criação de nova conta.

**Validações:**
- `userId`: Obrigatório, positivo
- `type`: Obrigatório
- `name`: Obrigatório, 3-100 caracteres
- `currency`: Obrigatório, 3 letras maiúsculas (ex: BRL)
- `initialBalance`: Opcional, >= 0
- `creditLimit`: Opcional, >= 0
- `interestRate`: Opcional, 0-100%

**Exemplo:**
```java
CreateAccountRequest request = CreateAccountRequest.builder()
    .userId(1L)
    .type(AccountType.CHECKING)
    .name("Conta Corrente Principal")
    .currency("BRL")
    .initialBalance(new BigDecimal("1000.00"))
    .isDefault(true)
    .build();
```

#### UpdateAccountRequest
DTO para atualização de conta existente. Todos os campos são opcionais.

**Exemplo:**
```java
UpdateAccountRequest request = UpdateAccountRequest.builder()
    .status(AccountStatus.INACTIVE)
    .name("Nova Descrição")
    .creditLimit(new BigDecimal("5000.00"))
    .build();
```

---

### Enums

#### AccountType
Tipos de contas disponíveis no sistema.

**Valores:**
- `CHECKING` - Conta Corrente
- `SAVINGS` - Conta Poupança
- `INVESTMENT` - Conta Investimento
- `DIGITAL_WALLET` - Carteira Digital
- `BUSINESS` - Conta Empresarial

**Serialização JSON:**
```json
{
  "type": "checking"
}
```

**Uso:**
```java
AccountType type = AccountType.CHECKING;
String code = type.getCode(); // "checking"
String desc = type.getDescription(); // "Conta Corrente"

// Deserialização
AccountType parsed = AccountType.fromCode("checking");
```

#### AccountStatus
Status possíveis de uma conta.

**Valores:**
- `ACTIVE` - Ativa (operacional)
- `INACTIVE` - Inativa (temporário)
- `BLOCKED` - Bloqueada (sem transações)
- `FROZEN` - Congelada (ordem bancária/judicial)
- `CLOSED` - Encerrada (permanente)
- `PENDING_APPROVAL` - Pendente de Aprovação

**Exemplo:**
```java
AccountStatus status = AccountStatus.ACTIVE;
String code = status.getCode(); // "active"

// Verificar status
if (status == AccountStatus.ACTIVE) {
    // Conta está ativa
}
```

---

## 🎯 Casos de Uso

### Criar Nova Conta

```java
// Request
CreateAccountRequest request = CreateAccountRequest.builder()
    .userId(userId)
    .type(AccountType.SAVINGS)
    .name("Poupança")
    .currency("BRL")
    .initialBalance(BigDecimal.ZERO)
    .interestRate(new BigDecimal("0.5"))
    .build();

// Validar
ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
Validator validator = factory.getValidator();
Set<ConstraintViolation<CreateAccountRequest>> violations = validator.validate(request);

if (!violations.isEmpty()) {
    // Tratar erros de validação
}
```

### Atualizar Conta

```java
UpdateAccountRequest request = UpdateAccountRequest.builder()
    .status(AccountStatus.BLOCKED)
    .build();

// PATCH /accounts/{id}
accountService.update(accountId, request);
```

### Listar Contas do Usuário

```java
List<AccountDTO> accounts = accountService.findByUserId(userId);

accounts.forEach(account -> {
    System.out.printf("%s - %s: R$ %.2f%n",
        account.getName(),
        account.getType().getDescription(),
        account.getBalance()
    );
});
```

---

## 🔧 Validações

### Bean Validation (Jakarta)

Todas as validações são feitas via anotações Jakarta Validation:

- `@NotNull`: Campo obrigatório
- `@NotBlank`: String não vazia
- `@Size`: Tamanho mínimo/máximo
- `@Pattern`: Expressão regular
- `@DecimalMin/Max`: Valores numéricos
- `@Positive`: Números positivos

### Exemplo de Tratamento

```java
@RestControllerAdvice
public class ValidationExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        return ResponseEntity.badRequest().body(
            new ErrorResponse("Validation failed", errors)
        );
    }
}
```

---

## 📊 Diagrama de Classes

```
AccountDTO
├── id: Long
├── userId: Long
├── type: AccountType
├── status: AccountStatus
└── balance: BigDecimal

AccountType (Enum)
├── CHECKING
├── SAVINGS
├── INVESTMENT
├── DIGITAL_WALLET
└── BUSINESS

AccountStatus (Enum)
├── ACTIVE
├── INACTIVE
├── BLOCKED
├── FROZEN
├── CLOSED
└── PENDING_APPROVAL

CreateAccountRequest
└── (campos obrigatórios)

UpdateAccountRequest
└── (todos campos opcionais)
```

---

## 🚀 Integração com Microserviços

### Account Service

```java
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    
    @PostMapping
    public ResponseEntity<AccountDTO> create(
            @Valid @RequestBody CreateAccountRequest request) {
        AccountDTO account = accountService.create(request);
        return ResponseEntity.ok(account);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<AccountDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAccountRequest request) {
        AccountDTO account = accountService.update(id, request);
        return ResponseEntity.ok(account);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> findById(@PathVariable Long id) {
        return accountService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
```

### Transaction Service (Consumer)

```java
// Buscar conta para validar transação
AccountDTO account = accountClient.findById(accountId);

if (account.getStatus() != AccountStatus.ACTIVE) {
    throw new BusinessException("Account is not active");
}

if (account.getBalance().compareTo(transactionAmount) < 0) {
    throw new InsufficientFundsException();
}
```

---

## 📚 Dependências

- **Spring Boot Validation**: Bean validation (Jakarta)
- **Jackson**: Serialização/deserialização JSON
- **Lombok**: Redução de boilerplate

---

## 🔄 Versionamento

Esta biblioteca segue [Semantic Versioning](https://semver.org/):

- **MAJOR**: Mudanças incompatíveis (breaking changes)
- **MINOR**: Novas funcionalidades compatíveis
- **PATCH**: Correções de bugs

**Versão Atual:** 1.0.0

---

## 📝 Changelog

### [1.0.0] - 2025-11-07

#### Adicionado
- AccountDTO completo com todos os campos
- CreateAccountRequest com validações
- UpdateAccountRequest (campos opcionais)
- AccountType enum (5 tipos)
- AccountStatus enum (6 status)
- Validações Jakarta completas
- Suporte Jackson para JSON
- Documentação completa

---

## 🤝 Contribuindo

Para adicionar novos DTOs ou campos:

1. Mantenha compatibilidade com versão atual
2. Adicione validações apropriadas
3. Atualize documentação
4. Adicione exemplos de uso
5. Incremente versão seguindo semver

---

## 📄 Licença

Proprietary - Financer Team © 2025

---

**Financer Team** | v1.0.0
