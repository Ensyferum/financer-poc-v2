# Changelog - Financer Shared Libraries

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/),
e este projeto adere ao [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.0.0] - 2025-11-07

### ✨ Adicionado

#### financer-libs-parent
- Parent POM com gerenciamento centralizado de dependências
- Configuração de plugins (compiler, surefire, source, javadoc)
- Suporte para Java 21
- Spring Boot 3.2.12 e Spring Cloud 2023.0.0
- Configuração de annotation processors (Lombok, MapStruct)

#### financer-common
- **Health Check:**
  - `FinancerHealthIndicator` com versão dinâmica do pom.xml
  - Carregamento via `@Value("${financer.common.version}")`
  - Resource filtering para substituição de `@project.version@`

- **Logging:**
  - `logback-spring.xml` completo
  - CONSOLE appender com cores
  - FILE appender com rotação time-based (10MB, 30 dias)
  - ERROR_FILE appender dedicado
  - ASYNC appenders para performance
  - Perfis dev/local/prod

- **Exception Handling:**
  - `FinancerException` - exceção base
  - `ResourceNotFoundException` - erro 404
  - `BusinessValidationException` - erro 400
  - `GlobalExceptionHandler` com `@RestControllerAdvice`
  - `ErrorResponse` DTO padronizado

- **Jackson Configuration:**
  - Suporte a JSR-310 (Java 8 Date/Time API)
  - `@JsonFormat` para datas/timestamps

- **Auto-Configuration:**
  - `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
  - Registro automático em aplicações Spring Boot

#### financer-eureka-client
- **Auto-Configuration:**
  - `FinancerEurekaClientAutoConfiguration` para service discovery
  - Registro automático (sem necessidade de `@EnableEurekaClient`)

- **Configurações Otimizadas:**
  - `eureka.client.registry-fetch-interval-seconds: 30`
  - `eureka.instance.lease-renewal-interval-in-seconds: 30`
  - `eureka.instance.lease-expiration-duration-in-seconds: 90`
  - `eureka.instance.instance-id` com UUID aleatório
  - Metadata com `version` e `profile`

- **Ribbon Load Balancing:**
  - ReadTimeout: 60000ms
  - Retry na mesma instância: 1
  - Retry em outras instâncias: 2

- **EurekaClientConfig:**
  - Logging de configuração na inicialização
  - Método `logRegisteredServices()` para debugging

#### Documentação
- README.md para cada biblioteca
- BUILD_SUCCESS_SUMMARY.md com resumo executivo
- BUILD_VALIDATION_REPORT.md com relatório detalhado
- CHANGELOG.md para versionamento

#### Automação
- `scripts/build-libs.bat` para build e validação
- Verificação de Maven instalado
- Limpeza de artefatos antigos
- Validação de JARs, POMs e sources
- Logging detalhado com timestamps

### 🔧 Corrigido

- **Spring Cloud 2023.0.0 Compatibility:**
  - Removida anotação `@EnableEurekaClient` (deprecada)
  - Service discovery agora é automático via classpath

- **Import Conflicts:**
  - Removido import de `com.netflix.discovery.EurekaClientConfig`
  - Resolvido conflito de nomes de classes

- **Maven Multi-Module:**
  - Comentados módulos DTO não implementados no parent POM
  - Adicionados comentários explicativos

### 📊 Métricas

- **Build Time:** 6.868s
- **Artifacts Generated:** 7
  - 2 JARs principais
  - 2 JARs de sources
  - 2 POMs de biblioteca
  - 1 POM parent
- **Total Size:** 45.32 KB
- **Classes Compiled:** 10 arquivos Java

### 🔨 Build Details

```
Maven: 3.9.11
Java: 21.0.8
OS: Windows 11

Reactor Summary:
├── financer-libs-parent ......... SUCCESS [ 0.599s ]
├── financer-common .............. SUCCESS [ 4.162s ]
└── financer-eureka-client ....... SUCCESS [ 1.708s ]

Total time: 6.868s
```

### 📦 Artifacts

#### financer-common-1.0.0
- JAR: 15.71 KB (8 classes)
- Sources: 10.54 KB
- POM: 4.38 KB
- **Total:** 30.63 KB

#### financer-eureka-client-1.0.0
- JAR: 6.09 KB (2 classes)
- Sources: 5.11 KB
- POM: 3.49 KB
- **Total:** 14.69 KB

---

## [Unreleased]

### 🚧 Planejado

#### financer-dto-account v1.0.0
- AccountDTO
- AccountTypeEnum
- AccountStatusEnum
- CreateAccountRequest
- UpdateAccountRequest

#### financer-dto-transaction v1.0.0
- TransactionDTO
- TransactionTypeEnum
- TransactionStatusEnum
- CreateTransactionRequest

#### financer-dto-card v1.0.0
- CardDTO
- CardTypeEnum
- CardBrandEnum
- CreateCardRequest

### 🔄 Próximas Ações
- [ ] Implementar bibliotecas DTO por domínio
- [ ] Descomentar módulos DTO no parent POM
- [ ] Atualizar microsserviços para usar as novas libs
- [ ] Remover código duplicado dos microserviços
- [ ] Adicionar testes unitários nas bibliotecas
- [ ] Configurar CI/CD para build automático

---

## Formato do Versionamento

- **MAJOR:** Mudanças incompatíveis na API
- **MINOR:** Novas funcionalidades compatíveis
- **PATCH:** Correções de bugs compatíveis

## Tipos de Mudanças

- **✨ Adicionado:** Novas funcionalidades
- **🔧 Corrigido:** Correções de bugs
- **🔄 Alterado:** Mudanças em funcionalidades existentes
- **❌ Removido:** Funcionalidades removidas
- **🔒 Segurança:** Vulnerabilidades corrigidas
- **⚠️ Deprecado:** Funcionalidades que serão removidas

---

[1.0.0]: https://github.com/financer/libs/releases/tag/v1.0.0
[Unreleased]: https://github.com/financer/libs/compare/v1.0.0...HEAD
