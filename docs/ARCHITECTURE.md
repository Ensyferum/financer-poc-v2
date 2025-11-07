# 🏗️ Arquitetura - Financer System

Documentação visual da arquitetura do sistema Financer.

---

## 📊 Diagrama de Arquitetura Geral

```mermaid
graph TB
    subgraph "Client Layer"
        WEB[Web Browser]
        MOBILE[Mobile App]
    end

    subgraph "API Gateway Layer"
        GATEWAY[API Gateway<br/>Spring Cloud Gateway]
    end

    subgraph "Service Discovery"
        EUREKA[Eureka Server<br/>Service Registry]
        CONFIG[Config Server<br/>Centralized Config]
    end

    subgraph "Microservices Layer"
        ACCOUNT[Account Service<br/>Port: 8081]
        TRANSACTION[Transaction Service<br/>Port: 8082]
        CARD[Card Service<br/>Port: 8083]
        BALANCE[Balance Service<br/>Port: 8084]
        AUDIT[Audit Service<br/>Port: 8085]
    end

    subgraph "Database Layer"
        PG[(PostgreSQL<br/>Port: 5432)]
        MONGO[(MongoDB<br/>Port: 27017)]
    end

    subgraph "Messaging Layer"
        KAFKA[Apache Kafka<br/>Port: 9092]
        ZK[Zookeeper<br/>Port: 2181]
        SR[Schema Registry<br/>Port: 8082]
    end

    subgraph "Migration & Management"
        FLYWAY[Flyway<br/>Migration Tool]
        KAFKA_UI[Kafka UI<br/>Port: 8080]
    end

    WEB --> GATEWAY
    MOBILE --> GATEWAY
    
    GATEWAY --> ACCOUNT
    GATEWAY --> TRANSACTION
    GATEWAY --> CARD
    GATEWAY --> BALANCE
    
    ACCOUNT --> EUREKA
    TRANSACTION --> EUREKA
    CARD --> EUREKA
    BALANCE --> EUREKA
    AUDIT --> EUREKA
    
    ACCOUNT --> CONFIG
    TRANSACTION --> CONFIG
    CARD --> CONFIG
    BALANCE --> CONFIG
    
    ACCOUNT --> PG
    TRANSACTION --> PG
    CARD --> PG
    BALANCE --> PG
    AUDIT --> MONGO
    
    FLYWAY --> PG
    
    ACCOUNT --> KAFKA
    TRANSACTION --> KAFKA
    CARD --> KAFKA
    
    AUDIT --> KAFKA
    
    KAFKA --> ZK
    KAFKA --> SR
    KAFKA_UI --> KAFKA
```

---

## 🗄️ Diagrama de Banco de Dados

```mermaid
erDiagram
    ACCOUNTS ||--o{ CARDS : "has"
    ACCOUNTS ||--o{ TRANSACTIONS : "contains"
    CARDS ||--o{ TRANSACTIONS : "used_in"
    TRANSACTIONS ||--o{ TRANSACTIONS : "installments"
    ACCOUNTS ||--o{ ACCOUNT_AUDIT : "audits"
    TRANSACTIONS ||--o{ TRANSACTION_AUDIT : "audits"

    ACCOUNTS {
        uuid id PK
        varchar account_name
        varchar account_type
        varchar bank_name
        varchar bank_code
        varchar account_number
        varchar agency
        decimal balance
        varchar currency
        boolean is_active
        timestamp created_at
        timestamp updated_at
        int version
    }

    CARDS {
        uuid id PK
        uuid account_id FK
        varchar card_number_masked
        varchar card_holder_name
        varchar card_type
        varchar card_brand
        date expiry_date
        decimal credit_limit
        decimal available_limit
        int due_day
        int closing_day
        boolean is_active
        boolean is_blocked
        timestamp created_at
        timestamp updated_at
    }

    TRANSACTIONS {
        uuid id PK
        uuid account_id FK
        uuid card_id FK
        varchar transaction_type
        varchar category
        text description
        decimal amount
        timestamp transaction_date
        date due_date
        boolean is_recurring
        varchar recurrence_pattern
        int installments
        int installment_number
        uuid parent_transaction_id FK
        varchar status
        timestamp created_at
        timestamp updated_at
    }

    ACCOUNT_AUDIT {
        bigint audit_id PK
        uuid account_id FK
        varchar operation
        jsonb old_value
        jsonb new_value
        varchar changed_by
        timestamp changed_at
        varchar ip_address
    }

    TRANSACTION_AUDIT {
        bigint audit_id PK
        uuid transaction_id FK
        varchar operation
        jsonb old_value
        jsonb new_value
        varchar changed_by
        timestamp changed_at
        varchar ip_address
    }
```

---

## 🔄 Fluxo de Migration

```mermaid
sequenceDiagram
    participant Dev as Developer
    participant Script as db-new-migration.bat
    participant File as Migration File
    participant Docker as Docker Compose
    participant Flyway as Flyway Container
    participant PG as PostgreSQL

    Dev->>Script: Execute db-new-migration.bat
    Script->>Script: Calculate next version (V3)
    Script->>File: Create V3__description.sql
    Script->>Dev: Return file path
    
    Dev->>File: Edit SQL statements
    Dev->>Script: Execute db-migrate.bat
    
    Script->>Docker: docker-compose up postgres
    Docker->>PG: Start PostgreSQL container
    PG-->>Docker: Health check OK
    
    Script->>Docker: docker-compose run flyway migrate
    Docker->>Flyway: Start Flyway container
    Flyway->>PG: Connect to database
    
    Flyway->>PG: Check flyway_schema_history
    Flyway->>File: Read migration files
    Flyway->>Flyway: Calculate checksums
    Flyway->>PG: Apply V3 migration
    
    alt Migration Success
        PG-->>Flyway: Success
        Flyway->>PG: Insert to flyway_schema_history
        Flyway-->>Dev: Migration applied successfully
    else Migration Failed
        PG-->>Flyway: Error
        Flyway-->>Dev: Migration failed with error
    end
```

---

## 🎯 Fluxo de Transação

```mermaid
sequenceDiagram
    participant Client as Client
    participant Gateway as API Gateway
    participant Trans as Transaction Service
    participant Acc as Account Service
    participant Kafka as Kafka
    participant PG as PostgreSQL
    participant Audit as Audit Service

    Client->>Gateway: POST /api/transactions
    Gateway->>Trans: Create Transaction
    
    Trans->>PG: Begin Transaction
    Trans->>PG: Insert into transactions
    
    Trans->>Kafka: Publish TransactionCreated Event
    
    Trans->>Acc: Request Account Update
    Acc->>PG: Update account balance
    Acc-->>Trans: Balance Updated
    
    Trans->>PG: Commit Transaction
    Trans-->>Gateway: Transaction Created (201)
    Gateway-->>Client: Success Response
    
    Kafka->>Audit: Consume TransactionCreated
    Audit->>PG: Log Audit Trail
    
    Note over PG: Trigger executes automatically
    PG->>PG: Insert into transaction_audit
```

---

## 🐳 Docker Compose Stack

```mermaid
graph TB
    subgraph "Docker Network: financer-network"
        subgraph "Databases"
            PG[PostgreSQL<br/>Container]
            MONGO[MongoDB<br/>Container]
        end
        
        subgraph "Messaging"
            ZK[Zookeeper<br/>Container]
            KAFKA[Kafka<br/>Container]
            SR[Schema Registry<br/>Container]
            KUI[Kafka UI<br/>Container]
        end
        
        subgraph "Migration"
            FLY[Flyway<br/>Container]
        end
        
        subgraph "Services (Future)"
            EUREKA[Eureka Server]
            CONFIG[Config Server]
            GATEWAY[API Gateway]
            MS1[Account Service]
            MS2[Transaction Service]
        end
    end
    
    subgraph "Persistent Volumes"
        PGV[postgres-data]
        MONGOV[mongodb-data]
        KAFKAV[kafka-data]
        ZKV[zookeeper-data]
    end
    
    FLY -->|Depends On| PG
    KAFKA -->|Depends On| ZK
    SR -->|Depends On| KAFKA
    KUI -->|Depends On| KAFKA
    KUI -->|Depends On| SR
    
    PG --> PGV
    MONGO --> MONGOV
    KAFKA --> KAFKAV
    ZK --> ZKV
    
    MS1 -.->|Future| PG
    MS2 -.->|Future| PG
    MS1 -.->|Future| KAFKA
    MS2 -.->|Future| KAFKA
```

---

## 📈 Estado Atual do Sistema

```mermaid
graph LR
    subgraph "Implementado ✅"
        A[Database Schema]
        B[Migration System]
        C[Docker Infrastructure]
        D[Auditoria Automática]
        E[Scripts de Gestão]
    end
    
    subgraph "Em Progresso 🚧"
        F[Account Service]
        G[Config Server]
        H[Eureka Server]
    end
    
    subgraph "Planejado 📋"
        I[Transaction Service]
        J[API Gateway]
        K[Frontend Angular]
        L[CI/CD Pipeline]
    end
    
    A --> B
    B --> C
    C --> D
    D --> E
    
    E -.-> F
    F -.-> G
    G -.-> H
    
    H -.-> I
    I -.-> J
    J -.-> K
    K -.-> L
    
    style A fill:#90EE90
    style B fill:#90EE90
    style C fill:#90EE90
    style D fill:#90EE90
    style E fill:#90EE90
    
    style F fill:#FFD700
    style G fill:#FFD700
    style H fill:#FFD700
    
    style I fill:#87CEEB
    style J fill:#87CEEB
    style K fill:#87CEEB
    style L fill:#87CEEB
```

---

## 🔐 Segurança e Auditoria

```mermaid
graph TD
    A[User Action] --> B{Authentication}
    B -->|Valid| C[API Request]
    B -->|Invalid| D[401 Unauthorized]
    
    C --> E{Authorization}
    E -->|Allowed| F[Execute Operation]
    E -->|Denied| G[403 Forbidden]
    
    F --> H[Database Operation]
    H --> I[Trigger Fires]
    
    I --> J[Capture Old Value]
    J --> K[Capture New Value]
    K --> L[Record Audit Entry]
    
    L --> M[audit_account_changes]
    L --> N[audit_transaction_changes]
    
    M --> O[account_audit table]
    N --> P[transaction_audit table]
    
    O --> Q[Audit Log Complete]
    P --> Q
    
    Q --> R[Return Success]
    R --> S[Client Response]
```

---

## 📊 Monitoramento (Futuro)

```mermaid
graph TB
    subgraph "Application Metrics"
        APP[Microservices]
        APP --> METRICS[Micrometer Metrics]
    end
    
    subgraph "Infrastructure Metrics"
        DOCKER[Docker Containers]
        DB[Databases]
        KAFKA_M[Kafka]
    end
    
    subgraph "Collection"
        PROMETHEUS[Prometheus]
        METRICS --> PROMETHEUS
        DOCKER --> PROMETHEUS
        DB --> PROMETHEUS
        KAFKA_M --> PROMETHEUS
    end
    
    subgraph "Visualization"
        GRAFANA[Grafana Dashboards]
        PROMETHEUS --> GRAFANA
    end
    
    subgraph "APM"
        DYNATRACE[Dynatrace]
        APP --> DYNATRACE
    end
    
    subgraph "Alerting"
        ALERTS[Alert Manager]
        PROMETHEUS --> ALERTS
        ALERTS --> EMAIL[Email]
        ALERTS --> SLACK[Slack]
    end
```

---

## 🚀 CI/CD Pipeline (Planejado)

```mermaid
graph LR
    A[Git Push] --> B[GitHub Actions]
    
    B --> C[Build Stage]
    C --> D[Maven Build]
    C --> E[Docker Build]
    
    D --> F[Unit Tests]
    E --> F
    
    F --> G{Tests Pass?}
    
    G -->|No| H[Notify Developer]
    G -->|Yes| I[Integration Tests]
    
    I --> J{Tests Pass?}
    J -->|No| H
    J -->|Yes| K[Run Migrations]
    
    K --> L{Migration OK?}
    L -->|No| H
    L -->|Yes| M[Deploy to Dev]
    
    M --> N[Smoke Tests]
    N --> O{All OK?}
    
    O -->|No| H
    O -->|Yes| P[Deploy to Staging]
    
    P --> Q[Manual Approval]
    Q --> R[Deploy to Production]
    
    R --> S[Health Check]
    S --> T[Success]
```

---

**Versão:** 1.0.0  
**Última Atualização:** 2025-11-07
