# 🚀 Roadmap de Implementação - Financer

**Última Atualização:** 2025-11-07  
**Status do Projeto:** Phase 3 - Microservices Architecture (Em Desenvolvimento)

Guia completo de todas as fases, status atual e próximos passos do projeto Financer.

---

## 📊 Status Geral do Projeto

```
✅ Phase 1: Database Migration System (100%)
✅ Phase 2.1: Common & Eureka Libraries (100%)
✅ Phase 2.2: DTO Libraries (100%)
✅ Phase 3.1: Microservices Architecture (100%)
🚧 Phase 3.2: Account Service Implementation (0%) ← PRÓXIMA
⬜ Phase 3.3: Transaction Service Implementation (0%)
⬜ Phase 3.4: Card Service Implementation (0%)
⬜ Phase 3.5: User Service Implementation (0%)
⬜ Phase 3.6: API Gateway Configuration (0%)
⬜ Phase 3.7: Infrastructure Setup (0%)
```

---

## ✅ PHASE 1: Database Migration System (CONCLUÍDO)

**Status:** ✅ 100% Completo  
**Conclusão:** 2025-11-06  
**Documentação:** `db/README.md`

### Entregáveis
- ✅ Sistema de migrations serverless com Flyway
- ✅ Scripts batch automatizados (Windows)
- ✅ Migrations para schemas: accounts, cards, transactions, users
- ✅ Docker Compose para infraestrutura (PostgreSQL, MongoDB, Kafka)
- ✅ Documentação completa de uso
- ✅ Health checks configurados

### Estrutura Criada
```
db/
├── migrations/postgresql/
│   ├── V1__create_accounts_schema.sql
│   ├── V2__create_cards_transactions_schema.sql
│   └── V3__create_users_schema.sql
├── seeds/
scripts/database/
├── run-migrations.bat
└── clean-database.bat
```

---

## ✅ PHASE 2.1: Common & Eureka Libraries (CONCLUÍDO)

**Status:** ✅ 100% Completo  
**Conclusão:** 2025-11-07  
**Documentação:** `docs/phase-2/phase-2.1-shared-libraries.md`

### Entregáveis
- ✅ **financer-common v1.0.0** - Utilities, exceptions, health checks, logging
- ✅ **financer-eureka-client v1.0.0** - Service discovery configuration
- ✅ Parent POM configurado com dependency management
- ✅ Maven build bem-sucedido (todos os 6 módulos)
- ✅ Artifacts publicados no Maven local

### Tecnologias
- Java 21
- Spring Boot 3.2.12
- Maven 3.9.11
- Netflix Eureka Client 4.1.3

---

## ✅ PHASE 2.2: DTO Libraries (CONCLUÍDO)

**Status:** ✅ 100% Completo  
**Conclusão:** 2025-11-07  
**Documentação:** `docs/phase-2/phase-2.2-dto-libraries.md`

### Entregáveis
- ✅ **financer-dto-account v1.0.0** (5 classes)
  - AccountDTO, AccountType, AccountStatus
  - CreateAccountRequest, UpdateAccountRequest
- ✅ **financer-dto-transaction v1.0.0** (4 classes)
  - TransactionDTO, TransactionType, TransactionStatus
  - CreateTransactionRequest
- ✅ **financer-dto-card v1.0.0** (6 classes)
  - CardDTO, CardType, CardBrand, CardStatus
  - CreateCardRequest, UpdateCardRequest
- ✅ Maven build: 12.5s, 16 artifacts, 30+ classes

### Características
- Jakarta Bean Validation
- Lombok para boilerplate
- Jackson para JSON serialization
- Enums completos e bem documentados
- Security considerations (CVV não armazenado)

---

## ✅ PHASE 3.1: Microservices Architecture (CONCLUÍDO)

**Status:** ✅ 100% Completo  
**Conclusão:** 2025-11-07  
**Documentação:** `docs/phase-3/phase-3.1-architecture.md`

### Entregáveis
- ✅ Definição completa de 7 microserviços
- ✅ Padrões de comunicação (REST + Kafka)
- ✅ Estratégias de segurança (JWT + RBAC)
- ✅ Estratégias de resiliência (Circuit Breaker, Rate Limiting)
- ✅ Database per Service pattern
- ✅ Observability stack (ELK, Prometheus, Zipkin)
- ✅ Estrutura de APIs documentada

### Microserviços Definidos
1. **Eureka Server** (8761) - Service Discovery
2. **Config Server** (8888) - Configurações centralizadas
3. **API Gateway** (8080) - Entry point, JWT, rate limiting
4. **Account Service** (8081) - Gestão de contas
5. **Transaction Service** (8082) - Transações + Saga
6. **Card Service** (8083) - Cartões + Tokenização
7. **User Service** (8084) - Autenticação + RBAC

### Decisões Arquiteturais
- **Comunicação Síncrona:** REST + OpenFeign
- **Comunicação Assíncrona:** Apache Kafka
- **Database:** PostgreSQL 16 (um banco por serviço)
- **Cache:** Redis 7
- **Event Format:** JSON com estrutura padronizada
- **Security:** JWT com refresh tokens, BCrypt para senhas
- **Resilience:** Resilience4j para circuit breaker e retry

---

## 🚧 PHASE 3.2: Account Service Implementation (PRÓXIMA)

**Status:** 🚧 0% - Não Iniciado  
**Prioridade:** 🔴 Alta  
**Estimativa:** 3-4 dias  
**Dependências:** financer-dto-account v1.0.0

### Objetivo
Implementar microserviço completo para gestão de contas financeiras usando financer-dto-account.

### Checklist de Implementação

#### Checklist Detalhado

**Setup Inicial:**
- [ ] Criar estrutura Maven do projeto (`services/account-service/`)
- [ ] Configurar POM com dependências:
  - financer-dto-account v1.0.0
  - financer-common v1.0.0
  - financer-eureka-client v1.0.0
  - Spring Boot Web, Data JPA, PostgreSQL
  - Spring Cloud OpenFeign, Kafka
  - Resilience4j
- [ ] Configurar `application.yml` (porta 8081, Eureka, database)
- [ ] Copiar migrations para `src/main/resources/db/migration`

**Domain Layer:**
- [ ] Implementar entidades JPA:
  - Account (id, userId, type, status, balance, currency, etc.)
  - AccountBalanceHistory (histórico de saldos)
- [ ] Criar repositories (JpaRepository)
- [ ] Implementar mappers (Entity ↔ DTO)

**Business Layer:**
- [ ] Implementar AccountService interface + implementation
- [ ] Regras de negócio:
  - Validação de saldo mínimo
  - Verificação de ownership (userId)
  - Cálculo de saldo disponível
  - Histórico de mudanças
- [ ] Implementar Feign client para User Service (validação)
- [ ] Implementar event publishers (Kafka):
  - AccountCreated, AccountUpdated, BalanceChanged

**API Layer:**
- [ ] Criar AccountController com endpoints:
  - POST /api/v1/accounts
  - GET /api/v1/accounts/{id}
  - GET /api/v1/accounts
  - PUT /api/v1/accounts/{id}
  - DELETE /api/v1/accounts/{id}
  - GET /api/v1/accounts/{id}/balance
  - GET /api/v1/accounts/{id}/history
- [ ] Implementar GlobalExceptionHandler
- [ ] Adicionar validações Bean Validation
- [ ] Configurar OpenAPI/Swagger

**Quality Assurance:**
- [ ] Testes unitários (services, mappers) - 80%+ coverage
- [ ] Testes de integração (Testcontainers + PostgreSQL)
- [ ] Teste de Feign clients (WireMock)
- [ ] Teste de eventos Kafka

**DevOps:**
- [ ] Criar Dockerfile multi-stage
- [ ] Adicionar ao docker-compose.services.yml
- [ ] Configurar health checks
- [ ] Testar registro no Eureka

**Documentação:**
- [ ] README.md do serviço
- [ ] Documentação de APIs (OpenAPI)
- [ ] Diagramas de fluxo (Mermaid)

#### Estrutura Esperada
```
services/account-service/
├── src/
│   ├── main/
│   │   ├── java/com/financer/account/
│   │   │   ├── AccountServiceApplication.java
│   │   │   ├── entity/                    # JPA entities
│   │   │   │   ├── Account.java
│   │   │   │   └── AccountBalanceHistory.java
│   │   │   ├── repository/
│   │   │   │   ├── AccountRepository.java
│   │   │   │   └── AccountBalanceHistoryRepository.java
│   │   │   ├── service/
│   │   │   │   ├── AccountService.java
│   │   │   │   └── AccountServiceImpl.java
│   │   │   ├── controller/
│   │   │   │   └── AccountController.java
│   │   │   ├── mapper/                    # Entity <-> DTO
│   │   │   │   └── AccountMapper.java
│   │   │   ├── client/                    # Feign clients
│   │   │   │   └── UserClient.java
│   │   │   ├── event/                     # Kafka producers
│   │   │   │   ├── AccountEventPublisher.java
│   │   │   │   └── model/
│   │   │   │       ├── AccountCreatedEvent.java
│   │   │   │       └── BalanceChangedEvent.java
│   │   │   ├── config/
│   │   │   │   ├── FeignConfig.java
│   │   │   │   ├── KafkaConfig.java
│   │   │   │   └── OpenApiConfig.java
│   │   │   └── exception/
│   │   │       ├── GlobalExceptionHandler.java
│   │   │       ├── AccountNotFoundException.java
│   │   │       └── InsufficientBalanceException.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-prod.yml
│   │       └── db/migration/
│   │           └── V1__create_accounts_schema.sql
│   └── test/
│       ├── java/com/financer/account/
│       │   ├── service/
│       │   │   └── AccountServiceTest.java
│       │   ├── controller/
│       │   │   └── AccountControllerTest.java
│       │   └── integration/
│       │       └── AccountIntegrationTest.java
│       └── resources/
│           └── application-test.yml
├── Dockerfile
├── pom.xml
└── README.md
```

### APIs Definidas (Referência)
Ver documentação completa em: `docs/phase-3/phase-3.1-architecture.md` seção "Account Service"

---

## ⬜ PHASE 3.3: Transaction Service Implementation

**Status:** ⬜ 0% - Não Iniciado  
**Prioridade:** 🔴 Alta  
**Estimativa:** 4-5 dias  
**Dependências:** financer-dto-transaction v1.0.0, Account Service

### Objetivo
Implementar microserviço para processamento de transações financeiras com suporte a Saga pattern.

### Checklist
- [ ] Criar estrutura Maven do projeto
- [ ] Configurar dependências (dto-transaction, dto-account, common, eureka)
- [ ] Implementar entidades: Transaction, ScheduledTransaction, TransactionMetadata
- [ ] Implementar repositories
- [ ] Implementar services com validações de saldo
- [ ] Implementar Saga orchestration (transações distribuídas)
- [ ] Implementar controllers REST
- [ ] Configurar Feign clients (Account Service, Card Service)
- [ ] Implementar event publishers (Kafka)
- [ ] Implementar idempotência (chave única por transação)
- [ ] Implementar retry com backoff exponencial
- [ ] Testes unitários + integração
- [ ] Dockerfile + docker-compose
- [ ] Documentação

**Funcionalidades Especiais:**
- Agendamento de transações futuras
- Rastreamento de geolocalização e device
- Detecção básica de fraude
- Reversão de transações (estorno)

---

## ⬜ PHASE 3.4: Card Service Implementation

**Status:** ⬜ 0% - Não Iniciado  
**Prioridade:** 🟡 Média  
**Estimativa:** 3-4 dias  
**Dependências:** financer-dto-card v1.0.0

### Objetivo
Implementar microserviço para gestão de cartões com segurança PCI-DSS.

### Checklist
- [ ] Criar estrutura Maven do projeto
- [ ] Implementar entidades: Card, CardToken, CardLimit
- [ ] Implementar tokenização de números de cartão
- [ ] Implementar mascaramento de dados sensíveis
- [ ] Implementar gestão de limites
- [ ] Implementar bloqueio/desbloqueio
- [ ] Controllers REST
- [ ] Feign clients (Account Service)
- [ ] Event publishers
- [ ] Auditoria de operações sensíveis
- [ ] Testes + segurança
- [ ] Dockerfile + docker-compose
- [ ] Documentação PCI-DSS considerations

---

## ⬜ PHASE 3.5: User Service Implementation

**Status:** ⬜ 0% - Não Iniciado  
**Prioridade:** 🔴 Alta  
**Estimativa:** 4-5 dias  
**Dependências:** Criar financer-dto-user primeiro

### Pré-requisito
- [ ] Criar financer-dto-user v1.0.0
  - UserDTO, CreateUserRequest, UpdateUserRequest
  - LoginRequest, LoginResponse (com JWT)
  - Enums: UserRole, UserStatus

### Checklist
- [ ] Criar estrutura Maven do projeto
- [ ] Implementar entidades: User, UserCredential, UserRole, UserSession
- [ ] Implementar Spring Security configuration
- [ ] Implementar autenticação JWT (geração + validação)
- [ ] Implementar refresh tokens
- [ ] Implementar BCrypt para senhas (strength 12)
- [ ] Implementar RBAC (roles e permissions)
- [ ] Controllers REST (register, login, logout, profile)
- [ ] Rate limiting em login (proteção contra brute force)
- [ ] Event publishers
- [ ] Testes de segurança
- [ ] Dockerfile + docker-compose
- [ ] Documentação de fluxos de autenticação

---

## ⬜ PHASE 3.6: API Gateway Configuration

**Status:** ⬜ 0% - Não Iniciado  
**Prioridade:** 🔴 Alta  
**Estimativa:** 2-3 dias  
**Dependências:** Eureka Server, User Service (JWT)

### Objetivo
Configurar gateway como ponto de entrada único com segurança e resiliência.

### Checklist

- [ ] Criar projeto Spring Cloud Gateway
- [ ] Configurar rotas para todos os serviços
- [ ] Implementar JWT validation filter
- [ ] Implementar RBAC (role-based routing)
- [ ] Configurar rate limiting (Redis)
  - 100 req/min por usuário
  - 1000 req/min por IP
  - Burst capacity: 20
- [ ] Configurar CORS policies
- [ ] Implementar circuit breaker (Resilience4j)
- [ ] Configurar retry policies
- [ ] Implementar request/response logging
- [ ] Adicionar ao docker-compose
- [ ] Testes de carga
- [ ] Documentação de rotas

**Rotas Configuradas:**
```yaml
/api/v1/accounts/**      → Account Service (8081)
/api/v1/transactions/**  → Transaction Service (8082)
/api/v1/cards/**         → Card Service (8083)
/api/v1/users/**         → User Service (8084)
```

---

## ⬜ PHASE 3.7: Infrastructure Setup

**Status:** ⬜ 0% - Não Iniciado  
**Prioridade:** 🟡 Média  
**Estimativa:** 3-4 dias  
**Dependências:** Todos os serviços implementados

### Objetivo
Configurar infraestrutura completa de suporte (Config Server, Kafka, Redis, etc.)

### Task 3.7.1: Config Server
**Estimativa:** 1 dia

#### Checklist
- [ ] Criar projeto Spring Cloud Config Server (porta 8888)
- [ ] Criar repositório Git para configurações
- [ ] Configurar configs por ambiente:
  - application.yml (global)
  - application-dev.yml
  - application-prod.yml
  - account-service.yml
  - transaction-service.yml
  - card-service.yml
  - user-service.yml
  - gateway.yml
- [ ] Configurar encryption de secrets
- [ ] Adicionar ao docker-compose
- [ ] Configurar refresh automático (Spring Cloud Bus)
- [ ] Testar endpoints
- [ ] Documentação

### Task 3.7.2: Eureka Server
**Estimativa:** 1 dia

#### Checklist
- [ ] Criar projeto Eureka Server (porta 8761)
- [ ] Configurar dashboard UI
- [ ] Configurar self-preservation mode
- [ ] Configurar health checks
- [ ] Adicionar ao docker-compose
- [ ] Testar registro de todos os serviços
- [ ] Documentar naming conventions

### Task 3.7.3: Kafka Topics Provisioning
**Estimativa:** 1 dia

#### Checklist
- [ ] Definir schemas Avro para eventos
- [ ] Registrar schemas no Schema Registry
- [ ] Criar script de provisionamento de topics:
  - account.events
  - transaction.events
  - card.events
  - user.events
  - notification.events
- [ ] Configurar partições (3 por topic)
- [ ] Configurar replicação (factor 2)
- [ ] Configurar retention (7 dias)
- [ ] Testar producers/consumers
- [ ] Documentação de estrutura de eventos

### Task 3.7.4: Redis Setup
**Estimativa:** 0.5 dia

#### Checklist
- [ ] Adicionar Redis 7 ao docker-compose
- [ ] Configurar persistence (RDB + AOF)
- [ ] Configurar max memory policy (allkeys-lru)
- [ ] Integrar no Gateway (rate limiting)
- [ ] Integrar nos serviços (cache)
- [ ] Testar TTL configurations
- [ ] Documentação de uso

---

## ⬜ PHASE 4: Observability & Monitoring

**Status:** ⬜ 0% - Não Iniciado  
**Prioridade:** 🟡 Média  
**Estimativa:** 5-7 dias

### Task 4.1: Logging Centralizado (ELK Stack)
**Estimativa:** 2-3 dias

#### Checklist
- [ ] Adicionar Elasticsearch 8 ao docker-compose
- [ ] Adicionar Logstash 8 ao docker-compose
- [ ] Adicionar Kibana 8 ao docker-compose
- [ ] Configurar Logback em todos serviços (JSON format)
- [ ] Implementar correlation IDs (trace context)
- [ ] Criar dashboards no Kibana:
  - Error rates por serviço
  - Latency distribution
  - Request volume
  - Top endpoints
- [ ] Configurar alertas
- [ ] Documentação de queries úteis

### Task 4.2: Metrics & Dashboards (Prometheus + Grafana)
**Estimativa:** 2-3 dias

#### Checklist
- [ ] Adicionar Prometheus ao docker-compose
- [ ] Adicionar Grafana ao docker-compose
- [ ] Configurar Micrometer em todos serviços
- [ ] Expor /actuator/prometheus em todos serviços
- [ ] Criar dashboards:
  - JVM Metrics (heap, GC, threads)
  - HTTP Metrics (rate, latency, errors)
  - Database Metrics (connections, query time)
  - Kafka Metrics (lag, throughput)
  - Business Metrics (transações/min, saldo total)
- [ ] Configurar alertas:
  - Error rate > 5%
  - P95 latency > 1s
  - Memory usage > 80%
  - Disk usage > 85%
- [ ] Documentação de dashboards

### Task 4.3: Distributed Tracing (Zipkin)
**Estimativa:** 1-2 dias

#### Checklist
- [ ] Adicionar Zipkin ao docker-compose
- [ ] Configurar Spring Cloud Sleuth em todos serviços
- [ ] Implementar trace propagation (B3 headers)
- [ ] Criar service dependency graph
- [ ] Testar traces end-to-end
- [ ] Documentação de uso

---

## ⬜ PHASE 5: CI/CD Pipeline

**Status:** ⬜ 0% - Não Iniciado  
**Prioridade:** 🟡 Média  
**Estimativa:** 3-4 dias

### Task 5.1: GitHub Actions Workflows
**Estimativa:** 2-3 dias

#### Workflows Necessários
```
.github/workflows/
├── libs-build.yml              # Build das bibliotecas compartilhadas
├── service-build.yml           # Build de um microserviço
├── integration-tests.yml       # Testes de integração
├── deploy-dev.yml              # Deploy automático em dev
├── deploy-staging.yml          # Deploy manual em staging
├── deploy-prod.yml             # Deploy manual em prod com approvals
└── security-scan.yml           # SAST/DAST scans
```

#### Checklist
- [ ] Criar workflow de build Maven para libs
- [ ] Criar workflow de build Maven para services
- [ ] Configurar cache de dependências Maven
- [ ] Adicionar code coverage (Jacoco) - 80% minimum
- [ ] Integrar SonarQube (quality gate)
- [ ] Build de imagens Docker multi-stage
- [ ] Push para Docker Hub/AWS ECR
- [ ] Scan de segurança (Trivy, Snyk)
- [ ] Deploy automático em dev (on push to master)
- [ ] Deploy manual em staging (manual trigger)
- [ ] Deploy manual em prod (manual approval + smoke tests)
- [ ] Notificações (Slack/Email)
- [ ] Documentação de pipelines

### Task 5.2: Environment Configuration
**Estimativa:** 1 dia

#### Checklist
- [ ] Criar docker-compose-dev.yml
- [ ] Criar docker-compose-staging.yml
- [ ] Criar docker-compose-prod.yml
- [ ] Configurar secrets management (GitHub Secrets)
- [ ] Implementar smoke tests pós-deploy
- [ ] Configurar rollback automático em falhas
- [ ] Documentar processo de release

---

## ⬜ PHASE 6: Advanced Features

**Status:** ⬜ 0% - Não Iniciado  
**Prioridade:** � Baixa  
**Estimativa:** 8-10 dias

### Task 6.1: Balance Service (Event Consumer)
**Estimativa:** 2 dias

#### Checklist
- [ ] Criar projeto Balance Service
- [ ] Consumir eventos de transações (Kafka)
- [ ] Calcular saldos consolidados
- [ ] Implementar projeções futuras
- [ ] Cache com Redis (TTL 30s)
- [ ] APIs de consulta
- [ ] Testes de carga

### Task 6.2: Audit Service (Event Consumer)
**Estimativa:** 2 dias

#### Checklist
- [ ] Criar projeto Audit Service
- [ ] Consumir todos eventos de domínio
- [ ] Armazenar em MongoDB (time-series)
- [ ] APIs de consulta de histórico
- [ ] Relatórios de auditoria
- [ ] Política de retenção (90 dias)
- [ ] Testes completos

### Task 6.3: Notification Service
**Estimativa:** 2-3 dias

#### Checklist
- [ ] Criar projeto Notification Service
- [ ] Consumir eventos de notificação
- [ ] Integração com providers:
  - Email (SendGrid/AWS SES)
  - SMS (Twilio)
  - Push (Firebase)
- [ ] Templates de mensagens
- [ ] Preferências de usuário
- [ ] Retry logic e DLQ
- [ ] Testes

### Task 6.4: Frontend Angular
**Estimativa:** 7-10 dias

#### Checklist
- [ ] Criar projeto Angular 17+
- [ ] Configurar Angular Material
- [ ] Implementar telas:
  - Dashboard
  - Contas (CRUD)
  - Transações (lista + filtros)
  - Cartões (CRUD)
  - Relatórios
  - Perfil de usuário
- [ ] Implementar autenticação (JWT)
- [ ] Implementar guards e interceptors
- [ ] Testes unitários (Jasmine/Karma)
- [ ] Build de produção
- [ ] Deploy (Nginx container ou S3+CloudFront)

---

## ⬜ PHASE 7: Security Hardening

**Status:** ⬜ 0% - Não Iniciado  
**Prioridade:** 🔴 Alta (antes de produção)  
**Estimativa:** 3-4 dias

### Checklist
- [ ] OWASP Top 10 compliance check
- [ ] API rate limiting avançado (por endpoint)
- [ ] Input validation em todos endpoints
- [ ] SQL injection prevention (prepared statements)
- [ ] XSS prevention
- [ ] CSRF protection
- [ ] Secrets encryption at rest
- [ ] TLS/SSL em todas comunicações
- [ ] Security headers (HSTS, CSP, X-Frame-Options)
- [ ] Audit logging de operações sensíveis
- [ ] Penetration testing
- [ ] Vulnerability scanning (Snyk, OWASP Dependency Check)

---

---

## 📊 Timeline Estimado

```
✅ Phase 1: Database Migration     → CONCLUÍDO
✅ Phase 2.1: Common Libraries     → CONCLUÍDO
✅ Phase 2.2: DTO Libraries        → CONCLUÍDO
✅ Phase 3.1: Architecture         → CONCLUÍDO

🚧 Phase 3.2: Account Service      → 3-4 dias
⬜ Phase 3.3: Transaction Service  → 4-5 dias
⬜ Phase 3.4: Card Service         → 3-4 dias
⬜ Phase 3.5: User Service         → 4-5 dias (+ criar dto-user)
⬜ Phase 3.6: API Gateway          → 2-3 dias
⬜ Phase 3.7: Infrastructure       → 3-4 dias

⬜ Phase 4: Observability          → 5-7 dias
⬜ Phase 5: CI/CD Pipeline         → 3-4 dias
⬜ Phase 6: Advanced Features      → 8-10 dias
⬜ Phase 7: Security Hardening     → 3-4 dias

TOTAL RESTANTE: 38-53 dias úteis (~2-2.5 meses)
```

---

## 🎯 MVP Path (Caminho Crítico para Produção)

Para ter um sistema funcional em produção **o mais rápido possível**:

### Sprint 1: Core Services (10-12 dias)
1. ✅ Phase 3.1: Architecture (FEITO)
2. 🚧 Phase 3.5: User Service + dto-user (4-5 dias) ← **PRIORIDADE 1**
3. ⬜ Phase 3.2: Account Service (3-4 dias)
4. ⬜ Phase 3.3: Transaction Service (4-5 dias)

### Sprint 2: Infrastructure (5-6 dias)
5. ⬜ Phase 3.7.1: Config Server (1 dia)
6. ⬜ Phase 3.7.2: Eureka Server (1 dia)
7. ⬜ Phase 3.6: API Gateway (2-3 dias)
8. ⬜ Phase 3.7.3: Kafka Topics (1 dia)

### Sprint 3: Observability Básica (3-4 dias)
9. ⬜ Phase 4.1: ELK Stack básico (2 dias)
10. ⬜ Phase 4.2: Prometheus + Grafana básico (2 dias)

### Sprint 4: Security & Deploy (4-5 dias)
11. ⬜ Phase 7: Security Hardening (3-4 dias)
12. ⬜ Phase 5.1: CI/CD básico (2 dias)

**Total MVP:** ~22-27 dias úteis (~1-1.5 mês)

**⚠️ NOTA IMPORTANTE:** User Service deve ser implementado ANTES de Account Service para ter autenticação funcionando!

---

## 📝 Decisões Arquiteturais Importantes

### Comunicação Entre Serviços
- **Síncrona:** REST + OpenFeign para operações que precisam resposta imediata
- **Assíncrona:** Apache Kafka para eventos, notificações e auditoria
- **Padrão:** Event-Driven Architecture com Saga pattern para transações distribuídas

### Database Strategy
- **Pattern:** Database per Service (isolamento completo)
- **SGBD:** PostgreSQL 16 para todos os serviços
- **Migrations:** Flyway integrado em cada serviço
- **Evitar:** JOINs cross-database (usar eventos para sincronização)

### Segurança
- **Autenticação:** JWT gerado por User Service
- **Autorização:** RBAC (Role-Based Access Control)
- **Gateway:** Valida tokens e injeta userId nos headers
- **Serviços:** Confiam no header userId (rede interna segura)
- **Senhas:** BCrypt com strength 12

### Resiliência
- **Circuit Breaker:** Resilience4j
- **Retry:** Backoff exponencial
- **Rate Limiting:** Redis no Gateway (100 req/min por user, 1000 por IP)
- **Timeout:** 3s default para chamadas síncronas
- **Fallback:** Métodos alternativos para operações críticas

### Observability
- **Logging:** ELK Stack com structured JSON logs
- **Metrics:** Prometheus + Grafana
- **Tracing:** Zipkin com Spring Cloud Sleuth
- **Correlation:** Trace IDs propagados via B3 headers
- **Formato Log:** `[DOMAIN].[FUNCTION].[STEP] - Description`

### Eventos Kafka
- **Formato:** JSON com envelope padronizado
- **Estrutura:**
  ```json
  {
    "eventId": "uuid",
    "eventType": "AccountCreated",
    "timestamp": "2025-11-07T10:00:00Z",
    "aggregateId": "123",
    "aggregateType": "Account",
    "version": 1,
    "payload": {...},
    "metadata": {
      "correlationId": "uuid",
      "causationId": "uuid",
      "userId": 456
    }
  }
  ```
- **Topics:** Separados por domínio (account.events, transaction.events, etc.)
- **Partições:** 3 por topic
- **Replicação:** Factor 2
- **Retention:** 7 dias

### Convenções de Desenvolvimento
- **Testes:** Mínimo 80% coverage (JUnit 5 + AssertJ + Testcontainers)
- **Commits:** Padrão "Phase X.Y: Description - Summary"
- **Branches:** feature/, bugfix/, hotfix/ prefixes
- **Code Style:** Google Java Style Guide
- **Documentação:** OpenAPI 3.0 para todas as APIs

---

## 📋 Checklist Pré-Produção

Antes de colocar o sistema em produção, garantir que:

### Funcional
- [ ] Todos os serviços core implementados (User, Account, Transaction)
- [ ] APIs documentadas com OpenAPI
- [ ] Testes de integração passando (80%+ coverage)
- [ ] Fluxos end-to-end validados

### Infraestrutura
- [ ] Config Server configurado com profiles
- [ ] Eureka Server rodando e todos serviços registrados
- [ ] API Gateway com rotas e rate limiting
- [ ] Kafka topics provisionados
- [ ] Redis configurado para cache e rate limiting
- [ ] Databases separados por serviço

### Segurança
- [ ] JWT authentication funcionando
- [ ] RBAC implementado e testado
- [ ] Senhas encriptadas (BCrypt)
- [ ] HTTPS/TLS em todas comunicações
- [ ] Secrets management configurado
- [ ] OWASP Top 10 compliance check realizado
- [ ] Penetration testing executado

### Observability
- [ ] Logs centralizados (ELK)
- [ ] Métricas coletadas (Prometheus)
- [ ] Dashboards criados (Grafana)
- [ ] Alertas configurados
- [ ] Distributed tracing funcionando (Zipkin)
- [ ] Health checks em todos endpoints

### DevOps
- [ ] CI/CD pipeline configurado
- [ ] Docker images buildando corretamente
- [ ] Ambientes separados (dev, staging, prod)
- [ ] Rollback strategy definida
- [ ] Backup strategy implementada
- [ ] Disaster recovery plan documentado

---

## 🆘 Troubleshooting Comum

### Serviço não registra no Eureka
- Verificar `eureka.client.serviceUrl.defaultZone` no application.yml
- Verificar se Eureka Server está rodando (porta 8761)
- Checar logs para erros de conexão
- Validar naming convention (spring.application.name)

### Feign Client com timeout
- Aumentar timeout: `feign.client.config.default.connectTimeout` e `readTimeout`
- Verificar circuit breaker (pode estar aberto)
- Checar se serviço alvo está saudável
- Validar URL do serviço no Eureka

### Eventos Kafka não são consumidos
- Verificar se topic existe (Kafka UI)
- Checar group.id do consumer
- Validar serialização (JSON vs Avro)
- Verificar lag no consumer
- Checar logs de erro no consumer

### Gateway retorna 503
- Verificar se serviço alvo está UP no Eureka
- Checar circuit breaker status
- Validar rotas no Gateway
- Verificar load balancer configuration

---

## 📚 Documentação de Referência

### Documentos Criados
- ✅ `docs/README.md` - Índice geral da documentação
- ✅ `docs/ROADMAP.md` - Este documento (roadmap completo)
- ✅ `docs/phase-1/` - Database migration system
- ✅ `docs/phase-2/README.md` - Overview da Phase 2
- ✅ `docs/phase-2/phase-2.1-shared-libraries.md` - Common e Eureka libs
- ✅ `docs/phase-2/phase-2.2-dto-libraries.md` - DTO libraries
- ✅ `docs/phase-3/README.md` - Overview da Phase 3
- ✅ `docs/phase-3/phase-3.1-architecture.md` - Arquitetura completa
- ✅ `docs/libs/build-summary.md` - Resumo de builds
- ✅ `docs/libs/build-report.md` - Relatórios de build
- ✅ `docs/libs/changelog.md` - Histórico de mudanças
- ✅ `prompt.md` - System prompt atualizado
- ✅ `README.md` - Documentação principal do projeto

### READMEs de Bibliotecas
- ✅ `libs/README.md` - Overview das libs
- ✅ `libs/financer-common/README.md`
- ✅ `libs/financer-eureka-client/README.md`
- ✅ `libs/financer-dto-account/README.md`
- 🚧 `libs/financer-dto-transaction/README.md` (básico)
- 🚧 `libs/financer-dto-card/README.md` (básico)

### Próximos Documentos
- ⬜ `docs/phase-3/phase-3.2-account-service.md`
- ⬜ `docs/phase-3/phase-3.3-transaction-service.md`
- ⬜ `services/account-service/README.md`
- ⬜ `services/transaction-service/README.md`

---

## 🤝 Como Usar Este Roadmap

### Para Retomar o Projeto
1. Leia o **Status Geral** no topo para ver onde estamos
2. Veja a **Próxima Phase** marcada com 🚧
3. Siga o **Checklist Detalhado** da phase atual
4. Consulte **Decisões Arquiteturais** para entender o contexto
5. Use **MVP Path** se precisar priorizar features

### Para Desenvolvedores Novos
1. Leia `README.md` principal do projeto
2. Leia `docs/README.md` para entender estrutura de docs
3. Leia `docs/phase-3/phase-3.1-architecture.md` para entender arquitetura
4. Leia `prompt.md` para entender convenções e padrões
5. Escolha uma task não iniciada e comece

### Para Atualizar Este Roadmap
1. Marque checkboxes ✅ conforme tasks são concluídas
2. Atualize percentuais de conclusão das phases
3. Adicione novas decisões arquiteturais quando relevante
4. Atualize timeline se estimativas mudarem
5. Documente problemas encontrados em Troubleshooting

---

## 📞 Próximos Passos Imediatos

### Ação Imediata (Hoje)
1. ✅ Atualizar ROADMAP.md (FEITO)
2. 🚧 Atualizar prompt.md com contexto atual (EM ANDAMENTO)
3. ⬜ Commitar mudanças com mensagem apropriada

### Próxima Sessão de Desenvolvimento
1. ⬜ Criar financer-dto-user v1.0.0
2. ⬜ Iniciar Phase 3.5: User Service Implementation
3. ⬜ Implementar autenticação JWT
4. ⬜ Testar login/logout/refresh token

### Esta Semana
1. ⬜ Completar User Service
2. ⬜ Completar Account Service
3. ⬜ Iniciar Transaction Service

---

**Última Atualização:** 2025-11-07  
**Próxima Revisão:** Após completar Phase 3.2  
**Versão do Roadmap:** 2.0 (completo com todas as phases)
