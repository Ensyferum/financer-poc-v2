# 🏗️ Phase 3.1 - Arquitetura de Microserviços

**Data:** 2025-11-07  
**Status:** 🚧 Em Desenvolvimento

---

## 📋 Objetivo

Definir a arquitetura completa do ecossistema de microserviços do Financer, estabelecendo:
- Estrutura dos serviços
- Padrões de comunicação
- Responsabilidades e fronteiras
- Estratégias de segurança e resiliência

---

## 🎯 Arquitetura Geral

### Visão Macro

```
┌─────────────────────────────────────────────────────────────┐
│                        API Gateway                          │
│             (Spring Cloud Gateway + JWT)                    │
│                    Port: 8080                               │
└──────────────┬──────────────────────────────────┬───────────┘
               │                                  │
       ┌───────┴────────┐                ┌───────┴────────┐
       │                │                │                │
┌──────▼──────┐  ┌─────▼──────┐  ┌─────▼──────┐  ┌─────▼──────┐
│   Account   │  │Transaction │  │    Card    │  │    User    │
│   Service   │  │  Service   │  │  Service   │  │  Service   │
│  Port: 8081 │  │Port: 8082  │  │Port: 8083  │  │Port: 8084  │
└──────┬──────┘  └─────┬──────┘  └─────┬──────┘  └─────┬──────┘
       │                │                │                │
       └────────────────┴────────────────┴────────────────┘
                              │
                    ┌─────────▼──────────┐
                    │ Service Discovery  │
                    │   (Eureka Server)  │
                    │     Port: 8761     │
                    └─────────┬──────────┘
                              │
                    ┌─────────▼──────────┐
                    │  Config Server     │
                    │     Port: 8888     │
                    └────────────────────┘
```

---

## 🔧 Componentes da Arquitetura

### 1. Service Discovery (Eureka Server) - Port: 8761

**Responsabilidades:**
- Registro automático de microserviços
- Descoberta de serviços em tempo de execução
- Health checks e monitoramento de disponibilidade
- Load balancing dinâmico

**Configuração:**
```yaml
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
  server:
    enable-self-preservation: true
```

**Dependências:**
- Spring Cloud Netflix Eureka Server
- financer-common (utils, exceptions)

---

### 2. Config Server - Port: 8888

**Responsabilidades:**
- Centralização de configurações
- Gerenciamento de perfis (dev, staging, prod)
- Refresh dinâmico de configs via Spring Cloud Bus
- Versionamento de configurações via Git

**Configuração:**
```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: ${CONFIG_REPO_URI}
          default-label: main
          search-paths: configs/{application}
```

**Arquivos de Config (por serviço):**
- `application.yml` - configs comuns
- `account-service.yml`
- `transaction-service.yml`
- `card-service.yml`
- `user-service.yml`
- `gateway.yml`

---

### 3. API Gateway - Port: 8080

**Responsabilidades:**
- Ponto de entrada único para todos os clientes
- Roteamento inteligente para microserviços
- Autenticação JWT centralizada
- Rate limiting e throttling
- CORS configuration
- Request/Response logging
- Circuit breaker patterns

**Rotas:**
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: account-service
          uri: lb://ACCOUNT-SERVICE
          predicates:
            - Path=/api/v1/accounts/**
          filters:
            - name: RateLimiter
            - name: CircuitBreaker
        
        - id: transaction-service
          uri: lb://TRANSACTION-SERVICE
          predicates:
            - Path=/api/v1/transactions/**
        
        - id: card-service
          uri: lb://CARD-SERVICE
          predicates:
            - Path=/api/v1/cards/**
        
        - id: user-service
          uri: lb://USER-SERVICE
          predicates:
            - Path=/api/v1/users/**
```

**Segurança:**
- JWT validation filter
- Role-based access control (RBAC)
- API key validation para integrações externas

**Dependências:**
- Spring Cloud Gateway
- Spring Security
- JWT library (io.jsonwebtoken)
- financer-common

---

### 4. Account Service - Port: 8081

**Domínio:** Gerenciamento de Contas Financeiras

**Responsabilidades:**
- CRUD de contas bancárias
- Gerenciamento de saldos
- Histórico de saldos
- Limites de crédito
- Contas ativas/inativas
- Validação de propriedade de conta

**API Endpoints:**
```
POST   /api/v1/accounts                    - Criar conta
GET    /api/v1/accounts/{id}               - Buscar por ID
GET    /api/v1/accounts                    - Listar contas do usuário
PUT    /api/v1/accounts/{id}               - Atualizar conta
DELETE /api/v1/accounts/{id}               - Deletar/Inativar conta
GET    /api/v1/accounts/{id}/balance       - Consultar saldo
POST   /api/v1/accounts/{id}/balance       - Ajustar saldo (admin)
GET    /api/v1/accounts/{id}/history       - Histórico de movimentações
```

**DTOs Utilizados:**
- `financer-dto-account`
  - AccountDTO
  - CreateAccountRequest
  - UpdateAccountRequest
  - AccountType enum
  - AccountStatus enum

**Database Tables:**
- `accounts` - dados principais
- `account_balance_history` - histórico de saldos

**Comunicação Externa:**
- **Consome:** User Service (validação de userId)
- **Publica eventos:** 
  - `AccountCreated`
  - `AccountUpdated`
  - `AccountDeleted`
  - `BalanceChanged`

**Dependências:**
- financer-dto-account v1.0.0
- financer-common v1.0.0
- financer-eureka-client v1.0.0
- Spring Boot Web
- Spring Data JPA
- PostgreSQL driver
- Spring Cloud OpenFeign (para chamadas HTTP)
- Spring Kafka (para eventos)

---

### 5. Transaction Service - Port: 8082

**Domínio:** Processamento de Transações Financeiras

**Responsabilidades:**
- CRUD de transações
- Processamento de transferências
- Validação de saldo antes de débito
- Agendamento de transações futuras
- Rastreamento de localização e device
- Detecção básica de fraude
- Conciliação bancária

**API Endpoints:**
```
POST   /api/v1/transactions                - Criar transação
GET    /api/v1/transactions/{id}           - Buscar por ID
GET    /api/v1/transactions                - Listar transações (filtros)
PUT    /api/v1/transactions/{id}           - Atualizar transação
DELETE /api/v1/transactions/{id}           - Cancelar transação
POST   /api/v1/transactions/{id}/reverse   - Estornar transação
GET    /api/v1/transactions/account/{id}   - Transações por conta
POST   /api/v1/transactions/schedule       - Agendar transação
```

**DTOs Utilizados:**
- `financer-dto-transaction`
  - TransactionDTO
  - CreateTransactionRequest
  - TransactionType enum
  - TransactionStatus enum

**Database Tables:**
- `transactions` - transações realizadas
- `scheduled_transactions` - transações agendadas
- `transaction_metadata` - dados extras (geolocation, device)

**Comunicação Externa:**
- **Consome:** 
  - Account Service (validação de conta e saldo)
  - Card Service (validação de cartão)
- **Publica eventos:**
  - `TransactionCreated`
  - `TransactionCompleted`
  - `TransactionFailed`
  - `TransactionReversed`
  - `TransactionScheduled`

**Patterns Implementados:**
- **Saga Pattern** para transações distribuídas
- **Idempotency** para evitar duplicação
- **Retry com backoff** para falhas temporárias

**Dependências:**
- financer-dto-transaction v1.0.0
- financer-dto-account v1.0.0 (para validações)
- financer-common v1.0.0
- financer-eureka-client v1.0.0
- Spring Boot Web
- Spring Data JPA
- PostgreSQL driver
- Spring Cloud OpenFeign
- Spring Kafka

---

### 6. Card Service - Port: 8083

**Domínio:** Gerenciamento de Cartões de Crédito/Débito

**Responsabilidades:**
- CRUD de cartões
- Gerenciamento de limites
- Bloqueio/desbloqueio de cartões
- Controle de validade
- Virtual cards
- Segurança: tokenização e mascaramento
- Configurações de notificação

**API Endpoints:**
```
POST   /api/v1/cards                       - Criar cartão
GET    /api/v1/cards/{id}                  - Buscar por ID
GET    /api/v1/cards                       - Listar cartões do usuário
PUT    /api/v1/cards/{id}                  - Atualizar cartão
DELETE /api/v1/cards/{id}                  - Deletar cartão
POST   /api/v1/cards/{id}/block            - Bloquear cartão
POST   /api/v1/cards/{id}/unblock          - Desbloquear cartão
POST   /api/v1/cards/{id}/report-lost      - Reportar perda/roubo
GET    /api/v1/cards/{id}/transactions     - Transações do cartão
POST   /api/v1/cards/virtual               - Criar cartão virtual
```

**DTOs Utilizados:**
- `financer-dto-card`
  - CardDTO
  - CreateCardRequest
  - UpdateCardRequest
  - CardType enum
  - CardBrand enum
  - CardStatus enum

**Database Tables:**
- `cards` - dados do cartão (número mascarado)
- `card_tokens` - tokenização segura
- `card_limits` - limites configuráveis

**Comunicação Externa:**
- **Consome:** 
  - Account Service (validação de accountId)
  - User Service (validação de userId)
- **Publica eventos:**
  - `CardCreated`
  - `CardBlocked`
  - `CardUnblocked`
  - `CardReportedLost`
  - `CardExpired`

**Segurança:**
- **CVV nunca armazenado** (conforme WRITE_ONLY no DTO)
- **Card number tokenizado** após criação
- **PCI-DSS compliance** considerations
- Auditoria de todas operações sensíveis

**Dependências:**
- financer-dto-card v1.0.0
- financer-dto-account v1.0.0
- financer-common v1.0.0
- financer-eureka-client v1.0.0
- Spring Boot Web
- Spring Data JPA
- PostgreSQL driver
- Spring Cloud OpenFeign
- Spring Kafka

---

### 7. User Service - Port: 8084

**Domínio:** Gerenciamento de Usuários e Autenticação

**Responsabilidades:**
- CRUD de usuários
- Autenticação (login/logout)
- Gerenciamento de senhas
- Perfis e permissões
- Dados pessoais e KYC
- Preferências de usuário
- Auditoria de acessos

**API Endpoints:**
```
POST   /api/v1/users/register              - Registrar usuário
POST   /api/v1/users/login                 - Login (gera JWT)
POST   /api/v1/users/logout                - Logout (invalida token)
GET    /api/v1/users/{id}                  - Buscar por ID
GET    /api/v1/users/me                    - Dados do usuário logado
PUT    /api/v1/users/{id}                  - Atualizar dados
DELETE /api/v1/users/{id}                  - Deletar usuário
POST   /api/v1/users/password/reset        - Resetar senha
POST   /api/v1/users/password/change       - Trocar senha
GET    /api/v1/users/{id}/permissions      - Listar permissões
```

**DTOs:** (a criar em financer-dto-user)
- UserDTO
- CreateUserRequest
- UpdateUserRequest
- LoginRequest
- LoginResponse (com JWT)

**Database Tables:**
- `users` - dados do usuário
- `user_credentials` - senha hash (bcrypt)
- `user_roles` - papéis (USER, ADMIN, MANAGER)
- `user_permissions` - permissões granulares
- `user_sessions` - tokens ativos

**Comunicação Externa:**
- **Consumido por:** Account, Transaction, Card services
- **Publica eventos:**
  - `UserRegistered`
  - `UserUpdated`
  - `UserDeleted`
  - `UserLoggedIn`
  - `UserLoggedOut`

**Segurança:**
- Senhas com BCrypt (strength 12)
- JWT com refresh tokens
- Rate limiting em login
- Bloqueio temporário após tentativas falhas

**Dependências:**
- financer-dto-user v1.0.0 (a criar)
- financer-common v1.0.0
- financer-eureka-client v1.0.0
- Spring Boot Web
- Spring Data JPA
- Spring Security
- JWT library
- PostgreSQL driver
- Spring Kafka

---

## 🔄 Padrões de Comunicação

### Comunicação Síncrona (REST + OpenFeign)

**Quando usar:**
- Validações que precisam de resposta imediata
- Consultas de dados necessários para operação
- Operações que dependem de confirmação

**Exemplos:**
```java
// Account Service chama User Service
@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/api/v1/users/{id}")
    UserDTO getUserById(@PathVariable Long id);
}

// Transaction Service chama Account Service
@FeignClient(name = "account-service")
public interface AccountClient {
    @GetMapping("/api/v1/accounts/{id}")
    AccountDTO getAccountById(@PathVariable Long id);
    
    @PostMapping("/api/v1/accounts/{id}/balance")
    void updateBalance(@PathVariable Long id, @RequestBody BalanceUpdate update);
}
```

**Resiliência:**
- Circuit Breaker (Resilience4j)
- Retry com backoff exponencial
- Fallback methods
- Timeout configurável (3s default)

---

### Comunicação Assíncrona (Kafka Events)

**Quando usar:**
- Notificações de mudança de estado
- Auditoria e logging
- Processamento que não bloqueia a operação
- Integração com sistemas externos

**Tópicos Kafka:**
```yaml
Topics:
  - account.events          # AccountCreated, AccountUpdated, BalanceChanged
  - transaction.events      # TransactionCreated, TransactionCompleted
  - card.events            # CardCreated, CardBlocked, CardExpired
  - user.events            # UserRegistered, UserUpdated
  - notification.events    # Notificações para usuários
```

**Event Structure:**
```json
{
  "eventId": "uuid",
  "eventType": "AccountCreated",
  "timestamp": "2025-11-07T10:30:00Z",
  "aggregateId": "123",
  "aggregateType": "Account",
  "version": 1,
  "payload": {
    "userId": 456,
    "accountType": "CHECKING",
    "currency": "BRL"
  },
  "metadata": {
    "correlationId": "uuid",
    "causationId": "uuid",
    "userId": 456
  }
}
```

---

## 🛡️ Segurança

### JWT Authentication

**Estrutura do Token:**
```json
{
  "sub": "user-id-123",
  "username": "joao.silva",
  "roles": ["USER", "ADMIN"],
  "permissions": ["account:read", "account:write"],
  "iat": 1699363800,
  "exp": 1699367400
}
```

**Fluxo:**
1. User Service gera JWT após login
2. API Gateway valida token em cada request
3. Gateway injeta userId no header para microserviços
4. Microserviços confiam no header (comunicação interna segura)

---

### Authorization

**Níveis:**
- **Gateway:** Valida token e roles básicas
- **Service:** Valida permissões específicas e ownership

**Exemplo:**
```java
@PreAuthorize("hasPermission(#accountId, 'Account', 'READ')")
public AccountDTO getAccount(Long accountId, Long userId) {
    // Valida se conta pertence ao usuário
    if (!accountRepository.isOwner(accountId, userId)) {
        throw new ForbiddenException();
    }
    return accountRepository.findById(accountId);
}
```

---

## 🔍 Observability

### Logging (ELK Stack)

**Structured Logging:**
```json
{
  "timestamp": "2025-11-07T10:30:00Z",
  "level": "INFO",
  "service": "account-service",
  "traceId": "abc123",
  "spanId": "def456",
  "userId": "789",
  "message": "Account created successfully",
  "accountId": "101112"
}
```

---

### Metrics (Prometheus + Grafana)

**Métricas por serviço:**
- Request rate (RPM)
- Error rate
- Latency (p50, p95, p99)
- Database connection pool
- JVM metrics (heap, GC)

---

### Distributed Tracing (Zipkin)

**Headers:**
- X-B3-TraceId
- X-B3-SpanId
- X-B3-ParentSpanId

---

## 📊 Databases

### Database per Service Pattern

**Cada serviço tem seu próprio banco PostgreSQL:**

```
account-service     → financer_accounts_db
transaction-service → financer_transactions_db
card-service       → financer_cards_db
user-service       → financer_users_db
```

**Justificativa:**
- Isolamento de dados
- Escalabilidade independente
- Deploy independente
- Escolha de schema otimizada por domínio

**Considerações:**
- Não usar JOIN cross-database
- Usar eventos para sincronização
- Implementar eventual consistency

---

## 🚀 Resiliência e Escalabilidade

### Circuit Breaker (Resilience4j)

**Configuração:**
```yaml
resilience4j:
  circuitbreaker:
    instances:
      userService:
        failure-rate-threshold: 50
        wait-duration-in-open-state: 10s
        sliding-window-size: 10
        permitted-number-of-calls-in-half-open-state: 3
```

---

### Rate Limiting

**API Gateway:**
- 100 req/min por usuário
- 1000 req/min por IP
- Burst capacity: 20

**Redis como backend para contadores**

---

### Caching Strategy

**Níveis:**
1. **Gateway Cache:** Responses públicas (30s TTL)
2. **Service Cache (Redis):** Dados frequentes (5min TTL)
3. **Database Cache:** Query results

**Exemplos:**
- Account balance: cache 30s
- User data: cache 5min
- Card list: cache 1min

---

## 📦 Estrutura de Projeto (por microserviço)

```
account-service/
├── src/main/java/com/financer/account/
│   ├── controller/        # REST endpoints
│   ├── service/          # Business logic
│   ├── repository/       # Data access
│   ├── entity/           # JPA entities
│   ├── mapper/           # Entity <-> DTO
│   ├── client/           # Feign clients
│   ├── event/            # Event producers/consumers
│   ├── config/           # Configurations
│   └── exception/        # Custom exceptions
├── src/main/resources/
│   ├── application.yml
│   ├── application-dev.yml
│   ├── application-prod.yml
│   └── db/migration/     # Flyway scripts
├── src/test/
│   ├── java/             # Unit + Integration tests
│   └── resources/
├── Dockerfile
├── pom.xml
└── README.md
```

---

## 🔧 Tecnologias e Versões

### Core Stack
- **Java:** 21 LTS
- **Spring Boot:** 3.2.12
- **Spring Cloud:** 2023.0.x

### Service Discovery & Config
- **Eureka Server:** 4.1.x
- **Config Server:** 4.1.x

### Gateway
- **Spring Cloud Gateway:** 4.1.x

### Communication
- **OpenFeign:** 4.1.x
- **Apache Kafka:** 3.6.x

### Databases
- **PostgreSQL:** 16.x
- **Flyway:** 10.x
- **Redis:** 7.x (cache)

### Security
- **Spring Security:** 6.2.x
- **JWT:** io.jsonwebtoken:jjwt-api:0.12.x

### Resilience
- **Resilience4j:** 2.2.x

### Observability
- **Micrometer:** 1.12.x
- **Zipkin:** 2.27.x
- **ELK Stack:** 8.x

### Testing
- **JUnit 5:** 5.10.x
- **Mockito:** 5.x
- **Testcontainers:** 1.19.x

---

## 📝 Próximos Passos

### Phase 3.2 - Account Service Implementation
1. Criar estrutura do projeto
2. Configurar dependências
3. Implementar entities e repositories
4. Implementar services e controllers
5. Configurar Eureka client
6. Configurar Feign clients
7. Implementar event publishers
8. Criar Flyway migrations
9. Escrever testes
10. Criar Dockerfile

### Phase 3.3 - Transaction Service Implementation
(similar ao Account Service)

### Phase 3.4 - Card Service Implementation
(similar ao Account Service)

### Phase 3.5 - User Service Implementation
- Criar financer-dto-user primeiro
- Implementar serviço com autenticação

### Phase 3.6 - API Gateway Configuration
1. Configurar rotas
2. Implementar JWT filter
3. Configurar rate limiting
4. Configurar CORS
5. Implementar circuit breaker

### Phase 3.7 - Infrastructure Setup
1. Configurar Kafka clusters
2. Configurar Redis
3. Configurar Prometheus + Grafana
4. Configurar ELK Stack
5. Configurar Zipkin

---

## ✅ Resumo

### Microserviços (7)
1. ✅ **Service Discovery** (Eureka) - Port 8761
2. ✅ **Config Server** - Port 8888
3. 🚧 **API Gateway** - Port 8080
4. 🚧 **Account Service** - Port 8081
5. 🚧 **Transaction Service** - Port 8082
6. 🚧 **Card Service** - Port 8083
7. 🚧 **User Service** - Port 8084

### Patterns Implementados
- ✅ Service Discovery
- ✅ Centralized Configuration
- ✅ API Gateway
- ✅ Database per Service
- ✅ Event-Driven Architecture
- ✅ Circuit Breaker
- ✅ Rate Limiting
- ✅ Distributed Tracing
- ✅ Structured Logging

### Comunicação
- ✅ Síncrona: REST + OpenFeign
- ✅ Assíncrona: Kafka Events

### Segurança
- ✅ JWT Authentication
- ✅ Role-Based Access Control
- ✅ Data Encryption
- ✅ PCI-DSS considerations

---

**Financer Team** | Arquitetura v1.0 | 2025-11-07
