# 🗄️ Database Migration System - Financer

Sistema completo de **versionamento e migração de banco de dados** para o projeto Financer usando **Flyway** + **Docker Compose**.

---

## 📋 Índice

- [Visão Geral](#-visão-geral)
- [Estrutura de Diretórios](#-estrutura-de-diretórios)
- [Instalação e Configuração](#-instalação-e-configuração)
- [Uso Básico](#-uso-básico)
- [Comandos Disponíveis](#-comandos-disponíveis)
- [Padrões de Nomenclatura](#-padrões-de-nomenclatura)
- [Boas Práticas](#-boas-práticas)
- [Troubleshooting](#-troubleshooting)

---

## 🎯 Visão Geral

O sistema de migrations garante:

✅ **Versionamento automático** de schema do banco de dados  
✅ **Rastreabilidade completa** de todas as mudanças  
✅ **Execução idempotente** via Docker Compose  
✅ **Auditoria integrada** com triggers automáticos  
✅ **Rollback controlado** através de migrations reversas  
✅ **Ambientes isolados** (dev, test, prod)  

### Tecnologias Utilizadas

- **Flyway 10**: Engine de migration
- **PostgreSQL 16**: Banco de dados relacional
- **Docker Compose**: Orquestração de containers
- **Spring Boot Flyway**: Integração com microserviços

---

## 📂 Estrutura de Diretórios

```
financer2/
├── db/
│   ├── migrations/
│   │   ├── postgresql/              # Migrations PostgreSQL
│   │   │   ├── V1__create_accounts_schema.sql
│   │   │   ├── V2__create_cards_transactions_schema.sql
│   │   │   └── V3__your_new_migration.sql
│   │   └── mongodb/                 # Migrations MongoDB (futuro)
│   ├── seeds/                       # Dados de teste/desenvolvimento
│   │   └── V999__seed_sample_data.sql
│   ├── pom-flyway-template.xml      # Template Maven com Flyway
│   └── application-flyway-template.yml  # Template Spring Boot config
├── docker-compose.yml               # Compose principal
├── docker-compose.infrastructure.yml # Infraestrutura + Flyway
├── .env                            # Variáveis de ambiente
├── db-setup.bat                    # Setup inicial rápido
├── db-migrate.bat                  # Gerenciador de migrations
└── db-new-migration.bat            # Criar nova migration
```

---

## 🚀 Instalação e Configuração

### Pré-requisitos

- **Docker Desktop** instalado e rodando
- **Docker Compose** v3.8+
- **Git** (para versionamento)

### Setup Inicial

```batch
# 1. Execute o setup inicial (cria bancos + aplica migrations)
db-setup.bat

# 2. Verifique o status das migrations
db-migrate.bat
# Escolha opção: 3 (Info)
```

**Pronto!** O banco de dados está criado com todas as tabelas.

---

## 💻 Uso Básico

### 1. Criar Nova Migration

```batch
# Execute o script de criação
db-new-migration.bat

# Digite a descrição (sem espaços, use underscore)
# Exemplo: add_payment_methods_table

# O arquivo será criado:
# db/migrations/postgresql/V3__add_payment_methods_table.sql
```

### 2. Editar o Arquivo de Migration

```sql
-- V3__add_payment_methods_table.sql
SET search_path TO financer;

CREATE TABLE IF NOT EXISTS payment_methods (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    method_name VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_payment_methods_active ON payment_methods(is_active);
```

### 3. Aplicar Migration

```batch
db-migrate.bat
# Escolha opção: 1 (Run Migrations)
```

### 4. Validar Aplicação

```batch
db-migrate.bat
# Escolha opção: 2 (Validate Migrations)
```

---

## 🛠️ Comandos Disponíveis

### `db-setup.bat`
**Setup inicial completo** - Cria bancos de dados e aplica todas migrations.

```batch
db-setup.bat
```

**Use quando:**
- Configurar ambiente pela primeira vez
- Resetar ambiente de desenvolvimento
- Provisionar novo ambiente

---

### `db-migrate.bat`
**Gerenciador principal de migrations** com menu interativo.

```batch
db-migrate.bat
```

**Opções:**

| Opção | Comando | Descrição |
|-------|---------|-----------|
| 1 | `migrate` | Aplica todas migrations pendentes |
| 2 | `validate` | Verifica integridade das migrations |
| 3 | `info` | Mostra histórico de migrations |
| 4 | `repair` | Corrige checksums incorretos |
| 5 | `clean` | **PERIGO** - Apaga todo o banco |
| 6 | `baseline` | Marca schema existente como baseline |
| 7 | `rollback` | Info sobre rollback (Teams/Enterprise) |

---

### `db-new-migration.bat`
**Cria nova migration** com template e numeração automática.

```batch
db-new-migration.bat
```

**Exemplos de uso:**
```batch
# Criar migration para nova tabela
Enter description: create_invoices_table

# Criar migration para alterar tabela existente
Enter description: add_email_to_accounts

# Criar migration para índice
Enter description: add_index_transactions_date
```

---

## 📝 Padrões de Nomenclatura

### Convenção Flyway

```
V{VERSION}__{DESCRIPTION}.sql
```

**Componentes:**
- `V`: Prefixo obrigatório (versionado)
- `{VERSION}`: Número sequencial (1, 2, 3...)
- `__`: Dois underscores como separador
- `{DESCRIPTION}`: Descrição em snake_case
- `.sql`: Extensão do arquivo

### Exemplos Válidos

✅ `V1__create_accounts_schema.sql`  
✅ `V2__add_user_authentication.sql`  
✅ `V3__create_indexes_for_performance.sql`  
✅ `V4__alter_accounts_add_currency.sql`  

### Exemplos Inválidos

❌ `1_create_accounts.sql` (falta 'V')  
❌ `V1_create_accounts.sql` (um underscore só)  
❌ `V1__Create Accounts.sql` (espaços no nome)  
❌ `V01__create_accounts.sql` (zero à esquerda)  

---

## 🎯 Boas Práticas

### 1. Nunca Altere Migrations Já Aplicadas

```sql
-- ❌ ERRADO: Editar V1__create_accounts.sql após aplicado
-- ✅ CORRETO: Criar V3__alter_accounts_add_field.sql
```

**Motivo:** Flyway valida checksums. Alterações quebram a validação.

---

### 2. Sempre Use Transações

```sql
-- ✅ Envolva DDL statements em transactions quando possível
BEGIN;

CREATE TABLE payments (
    id UUID PRIMARY KEY,
    amount DECIMAL(19,4)
);

CREATE INDEX idx_payments_amount ON payments(amount);

COMMIT;
```

---

### 3. Torne Migrations Idempotentes

```sql
-- ✅ Use IF NOT EXISTS
CREATE TABLE IF NOT EXISTS accounts (...);

-- ✅ Verifique antes de adicionar coluna
DO $$ 
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name='accounts' AND column_name='email'
    ) THEN
        ALTER TABLE accounts ADD COLUMN email VARCHAR(255);
    END IF;
END $$;
```

---

### 4. Use Schema Qualificado

```sql
-- ✅ Sempre defina o schema
SET search_path TO financer;

-- Ou use qualificação completa
CREATE TABLE financer.accounts (...);
```

---

### 5. Documente Suas Migrations

```sql
-- =====================================================
-- Migration: V5 - Add User Authentication
-- Description: Creates users table and auth tokens
-- Author: João Silva
-- Date: 2025-11-07
-- Jira: FIN-123
-- =====================================================

SET search_path TO financer;

-- Create users table for authentication
CREATE TABLE users (
    ...
);
```

---

### 6. Crie Índices para Performance

```sql
-- Índices para Foreign Keys
CREATE INDEX idx_transactions_account ON transactions(account_id);

-- Índices para buscas frequentes
CREATE INDEX idx_accounts_type ON accounts(account_type);
CREATE INDEX idx_transactions_date ON transactions(transaction_date DESC);

-- Índices compostos
CREATE INDEX idx_accounts_bank_active ON accounts(bank_code, is_active);
```

---

### 7. Implemente Auditoria

O sistema já tem triggers de auditoria automática:

```sql
-- Já implementado em V1:
CREATE TRIGGER trg_accounts_audit
    AFTER INSERT OR UPDATE OR DELETE ON accounts
    FOR EACH ROW
    EXECUTE FUNCTION audit_account_changes();
```

**Toda mudança em `accounts` é registrada automaticamente em `account_audit`.**

---

### 8. Use Soft Delete

```sql
-- ✅ Inative ao invés de deletar
UPDATE accounts 
SET is_active = FALSE, 
    updated_at = CURRENT_TIMESTAMP 
WHERE id = '...';

-- ❌ Evite DELETE quando possível
-- DELETE FROM accounts WHERE id = '...';
```

---

## 🔧 Integração com Spring Boot

### 1. Adicione Dependências no `pom.xml`

```xml
<dependencies>
    <!-- Flyway Core -->
    <dependency>
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-core</artifactId>
    </dependency>

    <!-- Flyway PostgreSQL -->
    <dependency>
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-database-postgresql</artifactId>
    </dependency>

    <!-- PostgreSQL Driver -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

### 2. Configure no `application.yml`

```yaml
spring:
  flyway:
    enabled: true
    baseline-on-migrate: true
    validate-on-migrate: true
    locations: classpath:db/migration
    schemas: financer
    
  datasource:
    url: jdbc:postgresql://${POSTGRES_HOST:localhost}:5432/financer
    username: ${POSTGRES_USER:financer_user}
    password: ${POSTGRES_PASSWORD:financer_pass}
    
  jpa:
    hibernate:
      ddl-auto: validate  # ⚠️ Importante: Flyway controla o schema!
    properties:
      hibernate:
        default_schema: financer
```

### 3. Coloque Migrations em `src/main/resources/db/migration`

```
account-service/
└── src/
    └── main/
        └── resources/
            └── db/
                └── migration/
                    ├── V1__create_accounts_schema.sql
                    └── V2__add_account_indexes.sql
```

---

## 🔍 Troubleshooting

### Problema: "Checksum mismatch"

**Causa:** Arquivo de migration foi alterado após aplicação.

**Solução:**
```batch
db-migrate.bat
# Opção 4: Repair
```

Ou reverta a alteração e crie nova migration.

---

### Problema: "Migration failed"

**Causa:** Erro de sintaxe SQL ou constraint violation.

**Solução:**
1. Veja os logs do Flyway
2. Corrija o SQL
3. Execute `repair` se necessário
4. Tente novamente

---

### Problema: "Baseline required"

**Causa:** Banco já tem tabelas mas sem histórico Flyway.

**Solução:**
```batch
db-migrate.bat
# Opção 6: Baseline
# Digite a versão atual (ex: 1)
```

---

### Problema: "Out of order migration"

**Causa:** Migration com versão anterior foi adicionada depois.

**Solução:**
- Em **desenvolvimento**: Renomeie para versão maior
- Em **produção**: Configure `out-of-order: true` (não recomendado)

---

## 🔐 Segurança

### Dados Sensíveis

❌ **NUNCA** commite no Git:
- Senhas de produção
- Dados reais de clientes
- Chaves de API

✅ **Use variáveis de ambiente:**
```yaml
# application.yml
spring:
  datasource:
    password: ${POSTGRES_PASSWORD}  # ✅ Lê do .env
```

### Backup Antes de Migrations

```batch
# Faça backup antes de rodar migrations em produção
docker exec financer-postgres pg_dump -U financer_user financer > backup_$(date +%Y%m%d).sql
```

---

## 📊 Monitoramento

### Verificar Histórico de Migrations

```sql
-- Conecte ao PostgreSQL
psql -h localhost -U financer_user -d financer

-- Veja o histórico
SELECT * FROM financer.flyway_schema_history ORDER BY installed_rank;
```

### Informações Úteis

```sql
-- Última migration aplicada
SELECT version, description, installed_on 
FROM financer.flyway_schema_history 
ORDER BY installed_rank DESC 
LIMIT 1;

-- Verificar tabelas criadas
SELECT table_name 
FROM information_schema.tables 
WHERE table_schema = 'financer';
```

---

## 🚀 Ambientes

### Desenvolvimento (Local)

```batch
# Use db-setup.bat para setup completo
db-setup.bat

# Rode migrations conforme necessário
db-migrate.bat
```

### Staging / Production

```yaml
# docker-compose.production.yml
services:
  flyway:
    environment:
      - SPRING_PROFILES_ACTIVE=production
    command: >
      -url=jdbc:postgresql://${PROD_DB_HOST}:5432/financer
      -user=${PROD_DB_USER}
      -password=${PROD_DB_PASSWORD}
      migrate
```

---

## 📚 Referências

- [Flyway Documentation](https://flywaydb.org/documentation/)
- [PostgreSQL Best Practices](https://wiki.postgresql.org/wiki/Don%27t_Do_This)
- [Spring Boot Flyway Integration](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization.migration-tool)

---

## 🤝 Contribuindo

### Criar Nova Migration

1. Execute `db-new-migration.bat`
2. Edite o arquivo SQL gerado
3. Teste localmente com `db-migrate.bat`
4. Commit e push

### Review Checklist

- [ ] Nomenclatura seguindo padrão Flyway
- [ ] SQL idempotente (IF NOT EXISTS)
- [ ] Documentação no cabeçalho
- [ ] Índices criados para FKs
- [ ] Testado localmente
- [ ] Sem dados sensíveis

---

## 📞 Suporte

**Problemas com migrations?**
1. Verifique logs: `docker logs financer-flyway`
2. Consulte este README
3. Abra issue no repositório

---

**Versão:** 1.0.0  
**Última Atualização:** 2025-11-07  
**Autor:** Financer Team
