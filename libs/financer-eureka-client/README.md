# 🔍 Financer Eureka Client Library

**Version:** 1.0.0  
**Java:** 21  
**Spring Boot:** 3.2.12  
**Spring Cloud:** 2023.0.0

Biblioteca para integração com Eureka Service Discovery em todos os microserviços do Financer.

---

## 🎯 Recursos

### ✅ Service Discovery
- **@EnableEurekaClient**: Auto-configuração automática
- Registro automático de serviços no Eureka Server
- Health checks integrados
- Metadata customizado (versão, profile)

### ✅ Configurações Padrão
- **URL Padrão**: `http://localhost:8761/eureka`
- **Registry Fetch**: 30 segundos
- **Lease Renewal**: 30 segundos
- **Lease Expiration**: 90 segundos
- **Instance ID**: `${app-name}:${random-id}`

### ✅ Load Balancing (Ribbon)
- Retry automático configurado
- Timeouts otimizados
- 1 retry por servidor, 2 servidores

---

## 📦 Instalação

### Maven
```xml
<dependency>
    <groupId>com.financer</groupId>
    <artifactId>financer-eureka-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

## 🚀 Uso

### 1. Adicionar Dependência
A biblioteca é auto-configurada automaticamente. Basta adicionar no `pom.xml`.

### 2. Configurar application.yml
```yaml
spring:
  application:
    name: financer-account-service

# Eureka Configuration (opcional - já tem defaults)
eureka:
  client:
    service-url:
      defaultZone: ${EUREKA_SERVER_URL:http://localhost:8761/eureka}
  instance:
    prefer-ip-address: true  # Use IP ao invés de hostname
```

### 3. Executar
O serviço será registrado automaticamente no Eureka ao iniciar:

```
========================================================
Eureka Client Configuration Initialized
========================================================
Application Name: financer-account-service
Eureka Server URL: http://localhost:8761/eureka
Prefer IP Address: true
========================================================
```

---

## 🔧 Configurações Avançadas

### Customizar Instance ID
```yaml
eureka:
  instance:
    instance-id: ${spring.application.name}:${server.port}
```

### Desabilitar Eureka (para testes locais)
```yaml
eureka:
  client:
    enabled: false
```

### Adicionar Metadata Customizado
```yaml
eureka:
  instance:
    metadata-map:
      zone: us-east-1
      environment: production
      team: backend
```

### Configurar Health Check Path
```yaml
eureka:
  instance:
    health-check-url-path: /actuator/health
    status-page-url-path: /actuator/info
```

---

## 🧪 Testando Service Discovery

### 1. Ver Serviços Registrados
```java
@RestController
@RequiredArgsConstructor
public class DiscoveryController {
    
    private final DiscoveryClient discoveryClient;
    
    @GetMapping("/services")
    public List<String> getServices() {
        return discoveryClient.getServices();
    }
    
    @GetMapping("/services/{serviceName}")
    public List<ServiceInstance> getInstances(@PathVariable String serviceName) {
        return discoveryClient.getInstances(serviceName);
    }
}
```

### 2. Chamar Outro Serviço via Service Name
```java
@Service
@RequiredArgsConstructor
public class TransactionService {
    
    private final RestTemplate restTemplate;  // Com @LoadBalanced
    
    public Account getAccount(UUID accountId) {
        String url = "http://financer-account-service/api/v1/accounts/" + accountId;
        return restTemplate.getForObject(url, Account.class);
    }
}
```

### 3. Configurar RestTemplate com Load Balancing
```java
@Configuration
public class RestTemplateConfig {
    
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

---

## 📊 Monitoramento

### Eureka Dashboard
Acesse: `http://localhost:8761`

Você verá:
- Todos os serviços registrados
- Status de cada instância
- Metadata customizado
- Health checks

### Actuator Endpoints
```bash
# Ver informações de discovery
curl http://localhost:8080/actuator/health

# Response
{
  "status": "UP",
  "components": {
    "eureka": {
      "status": "UP",
      "details": {
        "applications": {
          "FINANCER-ACCOUNT-SERVICE": 2,
          "FINANCER-TRANSACTION-SERVICE": 1
        }
      }
    }
  }
}
```

---

## 🐳 Docker / Kubernetes

### Docker Compose
```yaml
services:
  account-service:
    image: financer-account-service:1.0.0
    environment:
      - EUREKA_SERVER_URL=http://eureka-server:8761/eureka
      - EUREKA_INSTANCE_PREFER_IP_ADDRESS=true
```

### Kubernetes
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: eureka-config
data:
  EUREKA_SERVER_URL: "http://eureka-server.default.svc.cluster.local:8761/eureka"
```

---

## 🔄 Versionamento

**Semantic Versioning:** MAJOR.MINOR.PATCH

- **1.0.0** (2025-11-07): Versão inicial
  - Auto-configuration Eureka Client
  - Configurações padrão otimizadas
  - Ribbon load balancing
  - Metadata com versão e profile

---

## 📝 Changelog

### [1.0.0] - 2025-11-07
#### Added
- ✅ @EnableEurekaClient auto-configuration
- ✅ Configurações padrão para registro e discovery
- ✅ Health checks integrados
- ✅ Metadata customizado (version, profile)
- ✅ Ribbon load balancing configurado
- ✅ Logging de serviços registrados

---

## 📄 Licença

MIT License - Financer Team © 2025
