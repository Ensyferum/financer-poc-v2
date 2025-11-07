# 📚 Financer - Shared Libraries

**Version:** 1.0.0  
**Status:** ✅ **BUILD SUCCESS** (2025-11-07)  
**Java:** 21  
**Spring Boot:** 3.2.12  
**Spring Cloud:** 2023.0.0  
**Build Time:** 6.868s

Bibliotecas compartilhadas para os microserviços do projeto Financer.

---

## 🎯 Status do Build

```
✅ BUILD SUCCESS
─────────────────────────────────────────────────
Reactor Build Order:
├── financer-libs-parent ......... SUCCESS [ 0.599s ]
├── financer-common .............. SUCCESS [ 4.162s ]
└── financer-eureka-client ....... SUCCESS [ 1.708s ]
─────────────────────────────────────────────────
Total: 6.868s | Artifacts: 7 | Size: 45.32 KB
```

📄 **Relatórios:**
- [BUILD_SUCCESS_SUMMARY.md](./BUILD_SUCCESS_SUMMARY.md) - Resumo executivo
- [BUILD_VALIDATION_REPORT.md](./BUILD_VALIDATION_REPORT.md) - Relatório detalhado

---

## 🗂️ Estrutura

```
libs/
├── pom.xml                          # Parent POM (financer-libs-parent)
│
├── financer-common/                 # ✅ Common utilities & configs
│   ├── health-check (versão dinâmica)
│   ├── logging (logback-spring.xml)
│   ├── exception handling global
│   └── Jackson configuration
│
├── financer-eureka-client/          # ✅ Service Discovery
│   ├── Eureka client auto-config
│   ├── Load balancing (Ribbon)
│   └── Metadata customizado
│
├── financer-dto-account/            # 🚧 Account Domain DTOs
│   └── (em desenvolvimento)
│
├── financer-dto-transaction/        # 🚧 Transaction Domain DTOs
│   └── (em desenvolvimento)
│
└── financer-dto-card/               # 🚧 Card Domain DTOs
    └── (em desenvolvimento)
```

---

## 📦 Bibliotecas Disponíveis

### 1️⃣ **financer-common** (v1.0.0) ✅
Utilitários comuns, health checks, logs e exception handling.

**Recursos:**
- ✅ Health check customizado com versão dinâmica do POM
- ✅ Logback configurado (console + arquivos rotativos)
- ✅ Exception handling global (`@RestControllerAdvice`)
- ✅ Exceções padronizadas (`FinancerException`, `ResourceNotFoundException`, `BusinessValidationException`)
- ✅ Jackson configuration (ISO-8601, Java 8 Date/Time)
- ✅ ErrorResponse padronizado

**Instalação:**
```xml
<dependency>
    <groupId>com.financer</groupId>
    <artifactId>financer-common</artifactId>
    <version>1.0.0</version>
</dependency>
```

**Documentação:** [financer-common/README.md](financer-common/README.md)

---

### 2️⃣ **financer-eureka-client** (v1.0.0) ✅
Configuração padronizada para Eureka Service Discovery.

**Recursos:**
- ✅ Auto-configuration do Eureka Client (`@EnableEurekaClient`)
- ✅ Configurações otimizadas (registry fetch, lease renewal)
- ✅ Load balancing com Ribbon
- ✅ Metadata customizado (versão, profile)
- ✅ Logging de serviços registrados
- ✅ Health checks integrados

**Instalação:**
```xml
<dependency>
    <groupId>com.financer</groupId>
    <artifactId>financer-eureka-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

**Documentação:** [financer-eureka-client/README.md](financer-eureka-client/README.md)

---

### 3️⃣ **financer-dto-account** (v1.0.0) 🚧
DTOs para o domínio de contas (Account, AccountType, etc).

**Status:** Em desenvolvimento

---

### 4️⃣ **financer-dto-transaction** (v1.0.0) 🚧
DTOs para o domínio de transações (Transaction, TransactionType, etc).

**Status:** Em desenvolvimento

---

### 5️⃣ **financer-dto-card** (v1.0.0) 🚧
DTOs para o domínio de cartões (Card, CardType, CardBrand, etc).

**Status:** Em desenvolvimento

---

## 🚀 Como Usar

### 1. Adicionar Parent POM (opcional)
Se estiver criando um novo microserviço, pode usar o parent POM:

```xml
<parent>
    <groupId>com.financer</groupId>
    <artifactId>financer-libs-parent</artifactId>
    <version>1.0.0</version>
    <relativePath>../libs/pom.xml</relativePath>
</parent>
```

### 2. Adicionar Dependências
Adicione apenas as bibliotecas necessárias:

```xml
<dependencies>
    <!-- Common utilities -->
    <dependency>
        <groupId>com.financer</groupId>
        <artifactId>financer-common</artifactId>
        <version>1.0.0</version>
    </dependency>

    <!-- Eureka Client -->
    <dependency>
        <groupId>com.financer</groupId>
        <artifactId>financer-eureka-client</artifactId>
        <version>1.0.0</version>
    </dependency>

    <!-- DTOs (quando disponíveis) -->
    <dependency>
        <groupId>com.financer</groupId>
        <artifactId>financer-dto-account</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### 3. Configurar application.yml
```yaml
spring:
  application:
    name: financer-account-service
    version: 1.0.0

# Eureka (se usar financer-eureka-client)
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka

# Logging (defaults já vêm do financer-common)
logging:
  level:
    com.financer: DEBUG
```

---

## 🔧 Build

### Compilar Todas as Libs
```bash
cd libs
mvn clean install
```

### Compilar Lib Específica
```bash
cd libs/financer-common
mvn clean install
```

### Instalar no Repositório Local
```bash
mvn clean install
```

As libs estarão disponíveis em `~/.m2/repository/com/financer/`

---

## 🔄 Versionamento Semântico

Todas as bibliotecas seguem **Semantic Versioning 2.0.0**:

```
MAJOR.MINOR.PATCH

MAJOR: Breaking changes (incompatível)
MINOR: Novos recursos (compatível)
PATCH: Bug fixes (compatível)
```

**Exemplo:**
- `1.0.0` → `1.0.1`: Bug fix
- `1.0.1` → `1.1.0`: Nova feature
- `1.1.0` → `2.0.0`: Breaking change

---

## 📋 Dependências Gerenciadas (Parent POM)

| Dependência | Versão |
|-------------|--------|
| **Java** | 21 |
| **Spring Boot** | 3.2.12 |
| **Spring Cloud** | 2023.0.0 |
| **Lombok** | 1.18.30 |
| **MapStruct** | 1.5.5.Final |
| **Logback** | 1.4.14 |
| **SLF4J** | 2.0.9 |
| **Jackson** | 2.16.1 |
| **JUnit 5** | 5.10.1 |
| **Mockito** | 5.8.0 |

---

## 🧪 Testes

### Executar Testes
```bash
# Todas as libs
mvn test

# Lib específica
cd financer-common
mvn test
```

---

## 📊 Estrutura de Código

### Package Convention
```
com.financer.{lib-name}.{sub-package}

Exemplos:
- com.financer.common.config
- com.financer.common.health
- com.financer.common.exception
- com.financer.eureka.config
- com.financer.dto.account
```

### Auto-Configuration
Todas as libs usam Spring Boot Auto-Configuration:

```
src/main/resources/META-INF/spring/
└── org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

---

## 🤝 Contribuindo

1. **Criar branch:** `feature/lib-name-feature`
2. **Implementar:** Seguir padrões estabelecidos
3. **Testes:** Cobertura mínima de 80%
4. **Documentação:** Atualizar README.md
5. **Versão:** Incrementar seguindo semver
6. **Changelog:** Documentar mudanças

---

## 📝 Changelog

### [1.0.0] - 2025-11-07
#### Added
- ✅ financer-common v1.0.0
  - Health checks com versão dinâmica
  - Logging padronizado (logback)
  - Exception handling global
  - Jackson configuration
- ✅ financer-eureka-client v1.0.0
  - Eureka Client auto-configuration
  - Load balancing (Ribbon)
  - Metadata customizado
- ✅ Parent POM com dependency management

#### In Progress
- 🚧 financer-dto-account
- 🚧 financer-dto-transaction
- 🚧 financer-dto-card

---

## 📞 Suporte

Para dúvidas ou problemas:
1. Consulte o README de cada lib
2. Verifique os exemplos de uso
3. Abra uma issue no repositório

---

## 📄 Licença

MIT License - Financer Team © 2025
