# 📚 Financer Common Library

**Version:** 1.0.0  
**Java:** 21  
**Spring Boot:** 3.2.12

Biblioteca comum compartilhada entre todos os microserviços do Financer com configurações, utilitários e padrões.

---

## 🎯 Recursos

### ✅ Health Checks
- **FinancerHealthIndicator**: Health check customizado com metadata da aplicação
- Integrado com Spring Boot Actuator

### ✅ Logging Padronizado
- **logback-spring.xml**: Configuração completa de logs
  - Console colorido (desenvolvimento)
  - Arquivos rotativos (produção)
  - Arquivo separado para erros
  - Async appenders para performance
  - Profiles: dev, local, prod, production

### ✅ Exception Handling
- **FinancerException**: Classe base para exceções de negócio
- **ResourceNotFoundException**: 404 - Recurso não encontrado
- **BusinessValidationException**: 400 - Erro de validação
- **GlobalExceptionHandler**: Handler global com respostas padronizadas
- **ErrorResponse**: Estrutura de erro consistente

### ✅ Configurações
- **JacksonConfig**: Serialização/desserialização JSON padronizada
  - Suporte a Java 8 Date/Time (ISO-8601)
  - Ignore unknown properties
  - Formatação consistente

---

## 📦 Instalação

### Maven
```xml
<dependency>
    <groupId>com.financer</groupId>
    <artifactId>financer-common</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

## 🚀 Uso

### Auto-Configuration
A biblioteca é auto-configurada automaticamente ao incluir a dependência. Não é necessário `@ComponentScan` adicional.

### Health Check
```bash
# Endpoint disponível automaticamente
curl http://localhost:8080/actuator/health
```

**Resposta:**
```json
{
  "status": "UP",
  "components": {
    "financerHealthIndicator": {
      "status": "UP",
      "details": {
        "status": "Application is running",
        "timestamp": "2025-11-07 18:30:00",
        "service": "Financer Service",
        "version": "1.0.0"
      }
    }
  }
}
```

### Exception Handling
```java
@Service
public class AccountService {
    
    public Account findById(UUID id) {
        return accountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Account", id.toString()));
    }
    
    public void validateBalance(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessValidationException("Amount cannot be negative");
        }
    }
}
```

**Resposta de Erro (404):**
```json
{
  "timestamp": "2025-11-07T18:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Account not found with id: 123e4567-e89b-12d3-a456-426614174000",
  "errorCode": "RESOURCE_NOT_FOUND",
  "path": "/api/v1/accounts/123e4567-e89b-12d3-a456-426614174000"
}
```

### Logging
```java
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TransactionService {
    
    public void processTransaction(Transaction tx) {
        log.info("Processing transaction: {}", tx.getId());
        log.debug("Transaction details: {}", tx);
        
        try {
            // ... process
        } catch (Exception e) {
            log.error("Failed to process transaction: {}", tx.getId(), e);
        }
    }
}
```

**Arquivos de Log Gerados:**
```
logs/
├── financer-app.log              # Todos os logs
├── financer-app-error.log        # Apenas erros
├── financer-app-2025-11-07.1.log # Arquivo rotativo
└── financer-app-error-2025-11-07.1.log
```

---

## ⚙️ Configuração

### application.yml
```yaml
spring:
  application:
    name: financer-account-service

# Health Check
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always

# Logging (opcional - sobrescreve padrões)
logging:
  level:
    com.financer: DEBUG
    org.springframework.web: INFO
```

---

## 📋 Estrutura

```
financer-common/
├── src/main/java/com/financer/common/
│   ├── config/
│   │   ├── FinancerCommonAutoConfiguration.java
│   │   └── JacksonConfig.java
│   ├── health/
│   │   └── FinancerHealthIndicator.java
│   ├── exception/
│   │   ├── FinancerException.java
│   │   ├── ResourceNotFoundException.java
│   │   ├── BusinessValidationException.java
│   │   ├── ErrorResponse.java
│   │   └── GlobalExceptionHandler.java
│   └── util/
│       └── (futuras utilidades)
├── src/main/resources/
│   ├── logback-spring.xml
│   └── META-INF/spring/
│       └── org.springframework.boot.autoconfigure.AutoConfiguration.imports
└── pom.xml
```

---

## 🔄 Versionamento

**Semantic Versioning:** MAJOR.MINOR.PATCH

- **1.0.0** (2025-11-07): Versão inicial
  - Health checks
  - Logging padronizado (logback)
  - Exception handling global
  - Jackson configuration

---

## 📝 Changelog

### [1.0.0] - 2025-11-07
#### Added
- ✅ FinancerHealthIndicator com metadata
- ✅ logback-spring.xml com profiles e rotação
- ✅ GlobalExceptionHandler com ErrorResponse
- ✅ FinancerException, ResourceNotFoundException, BusinessValidationException
- ✅ JacksonConfig com suporte a JSR-310
- ✅ Auto-configuration via Spring Boot

---

## 🤝 Contribuindo

1. Mantenha a retrocompatibilidade
2. Incremente a versão seguindo semver
3. Documente mudanças no CHANGELOG
4. Adicione testes para novos recursos

---

## 📄 Licença

MIT License - Financer Team © 2025
