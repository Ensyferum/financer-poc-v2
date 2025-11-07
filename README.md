# 💰 Financer - Sistema de Gestão Financeira

Sistema completo de gestão de finanças pessoais baseado em **arquitetura de microserviços** com Java 21, Spring Boot 3.2 e Docker.

---

## 🚀 Quick Start (2 minutos)

```batch
# 1. Inicie a infraestrutura
docker-compose -f docker-compose.infrastructure.yml up -d

# 2. Execute as migrations
cd scripts\database
run-migrations.bat

# 🎉 Pronto! Sistema rodando com banco configurado
```

---

## 📋 Estrutura do Projeto

```
financer2/
├── 📂 db/                          # Database schemas e migrations
│   ├── migrations/postgresql/      # SQL migrations (Flyway)
│   ├── migrations/mongodb/         # MongoDB migrations (futuro)
│   └── seeds/                      # Dados de exemplo
│
├── 📂 scripts/                     # Scripts de automação
│   └── database/
│       ├── run-migrations.bat      # 🔄 Executa migrations (serverless)
│       └── clean-database.bat      # 🗑️ Limpa banco de dados
│
├── 📂 docs/                        # 📚 Documentação completa
│   ├── ARCHITECTURE.md             # Diagramas e arquitetura
│   ├── DEVELOPMENT-GUIDE.md        # Guia de desenvolvimento
│   ├── MIGRATION-GUIDE.md          # Guia de migrations
│   └── ROADMAP.md                  # Próximos passos
│
├── 📂 logs/                        # Logs de execução (git ignored)
│
├── 📄 docker-compose.yml           # Compose principal
├── 📄 docker-compose.infrastructure.yml  # Infraestrutura (DB, Kafka, etc)
├── 📄 .env                         # Variáveis de ambiente
└── 📄 README.md                    # Este arquivo
```

---

## 🗄️ Database Migrations

### Executar Migrations

```batch
cd scripts\database
run-migrations.bat
```

**O script faz:**
- ✅ Verifica Docker rodando
- ✅ Inicia PostgreSQL se necessário
- ✅ Executa Flyway em modo serverless
- ✅ Gera logs detalhados em `logs/migration-*.log`
- ✅ Mostra versão, status e data de execução
- ✅ Em caso de erro, exibe motivo detalhado

### Limpar Banco de Dados

```batch
cd scripts\database
clean-database.bat
```

**⚠️ CUIDADO:** Este script deleta TODOS os dados!

---

## 🏗️ Infraestrutura

### Serviços Disponíveis

| Serviço | Porta | Descrição | Status |
|---------|-------|-----------|--------|
| **PostgreSQL** | 5432 | Banco de dados principal | ✅ |
| **MongoDB** | 27017 | NoSQL database | ✅ |
| **Kafka** | 9092 | Message broker | ✅ |
| **Schema Registry** | 8082 | Schema management | ✅ |
| **Kafka UI** | 8080 | Interface web Kafka | ✅ |
| **Zookeeper** | 2181 | Coordenação distribuída | ✅ |

### Comandos Docker

```batch
# Iniciar infraestrutura
docker-compose -f docker-compose.infrastructure.yml up -d

# Parar infraestrutura
docker-compose -f docker-compose.infrastructure.yml down

# Ver logs
docker-compose -f docker-compose.infrastructure.yml logs -f postgres

# Conectar ao PostgreSQL
docker exec -it financer-postgres psql -U financer_user -d financer
```

---

## 📊 Schema do Banco de Dados

### Tabelas Principais

#### 📁 **accounts** - Contas Bancárias
```sql
- id (UUID)
- account_name, account_type (CHECKING, SAVINGS, INVESTMENT)
- bank_name, bank_code, account_number
- balance, currency
- is_active (soft delete)
```

#### 💳 **cards** - Cartões de Crédito/Débito
```sql
- id (UUID)
- account_id (FK)
- card_type (CREDIT, DEBIT)
- card_brand (VISA, MASTERCARD, ELO)
- credit_limit, available_limit
- due_day, closing_day
```

#### 💸 **transactions** - Transações Financeiras
```sql
- id (UUID)
- account_id, card_id (FK)
- transaction_type (PIX, TED, CREDIT_CARD, etc)
- amount, currency, transaction_date
- installments, recurrence_pattern
- status (PENDING, COMPLETED, CANCELLED)
```

#### 📝 **Tabelas de Auditoria**
- `account_audit` - Histórico completo de mudanças em contas
- `transaction_audit` - Histórico de transações

**Ver detalhes:** [docs/DATABASE-SCHEMA.md](docs/DATABASE-SCHEMA.md)

---

## 🎨 Funcionalidades Implementadas

### ✅ Auditoria Automática
Triggers PostgreSQL capturam automaticamente TODAS as mudanças (INSERT, UPDATE, DELETE) com snapshots JSON completos.

### ✅ Soft Delete
Dados nunca são deletados fisicamente. Flag `is_active` controla visibilidade.

### ✅ Optimistic Locking
Campo `version` previne conflitos de concorrência.

### ✅ Timestamps Automáticos
`created_at` e `updated_at` são gerenciados por triggers.

### ✅ Migrations Serverless
Flyway executa sob demanda via script, sem container permanente. Ideal para CI/CD.

---

## 📚 Documentação

### Guias Disponíveis

| Documento | Descrição |
|-----------|-----------|
| **[ARCHITECTURE.md](docs/ARCHITECTURE.md)** | Arquitetura do sistema, diagramas Mermaid |
| **[MIGRATION-GUIDE.md](docs/MIGRATION-GUIDE.md)** | Como criar e gerenciar migrations |
| **[DEVELOPMENT-GUIDE.md](docs/DEVELOPMENT-GUIDE.md)** | Padrões de desenvolvimento, boas práticas |
| **[ROADMAP.md](docs/ROADMAP.md)** | Próximas fases e tasks |
| **[DATABASE-SCHEMA.md](docs/DATABASE-SCHEMA.md)** | Schema completo do banco de dados |

### Para Desenvolvedores Novos

1. Leia este README
2. Execute o Quick Start
3. Consulte [DEVELOPMENT-GUIDE.md](docs/DEVELOPMENT-GUIDE.md)
4. Veja [MIGRATION-GUIDE.md](docs/MIGRATION-GUIDE.md) se for trabalhar com banco

---

## 🛠️ Stack Tecnológico

### Backend (Planejado)
- **Java 21** com Spring Boot 3.2
- **Spring Cloud** 2023.0.0 (Config, Eureka, Gateway)
- **Maven** 3.9+ multi-módulo

### Database
- **PostgreSQL 16** (principal)
- **MongoDB 7** (NoSQL)
- **Flyway 10** (migrations)

### Messaging
- **Apache Kafka 7.5.0**
- **Schema Registry**

### DevOps
- **Docker & Docker Compose**
- **GitHub Actions** (CI/CD planejado)

---

## 🚀 Próximos Passos

### Fase Atual: Migration System ✅ (Completa)

### Próxima Fase: Microserviços Base 📋

1. **Config Server** - Configuração centralizada
2. **Eureka Server** - Service discovery
3. **Account Service** - Gestão de contas
4. **Transaction Service** - Gestão de transações
5. **API Gateway** - Gateway unificado

**Ver roadmap completo:** [docs/ROADMAP.md](docs/ROADMAP.md)

---

## 🧪 Testes e Validação

### Validar Migration

```batch
# Execute migration
cd scripts\database
run-migrations.bat

# Verifique o log gerado em logs/
# Deve mostrar: versão, status SUCCESS, data de execução
```

### Conectar ao Banco

```batch
docker exec -it financer-postgres psql -U financer_user -d financer
```

```sql
-- Ver tabelas
\dt financer.*;

-- Ver histórico de migrations
SELECT version, description, installed_on, success 
FROM financer.flyway_schema_history 
ORDER BY installed_rank;

-- Ver dados de exemplo
SELECT * FROM financer.accounts;
```

---

## 🔒 Segurança

- ✅ Variáveis de ambiente (nunca hardcoded)
- ✅ `.gitignore` protege arquivos sensíveis
- ✅ Logs não commitados (em `.gitignore`)
- ✅ Clean disabled no Flyway (proteção)
- ✅ Soft delete (dados preservados)

---

## 📝 Convenções

### Commits
```
feat: adiciona nova funcionalidade
fix: corrige bug
docs: atualiza documentação
refactor: refatora código
test: adiciona testes
chore: tarefas de manutenção
```

### Branches
```
feature/nome-da-feature
fix/nome-do-bug
docs/nome-da-doc
```

### Migrations
```
V{VERSION}__{DESCRIPTION}.sql

Exemplo: V1__create_accounts_schema.sql
```

---

## 🆘 Troubleshooting

### Migration falhou?

```batch
# 1. Verifique o log em logs/migration-*.log
# 2. Veja o erro específico do Flyway
# 3. Corrija o SQL
# 4. Execute novamente
```

### Porta 5432 ocupada?

```batch
# Pare PostgreSQL local ou mude a porta em docker-compose.infrastructure.yml
```

### Docker não inicia?

```batch
# Certifique-se que Docker Desktop está rodando
# Windows: Verifique ícone na bandeja do sistema
```

---

## 📞 Suporte e Contribuição

- **Issues:** Abra issue no repositório
- **Documentação:** Consulte `docs/`
- **Logs:** Verifique `logs/` para debugging

---

## 📄 Licença

MIT License

---

## ✅ Status do Projeto

**Versão:** 1.0.0  
**Fase:** 1 de 8 (Migration System) - ✅ **COMPLETA**  
**Próxima Fase:** Microserviços Base  
**Última Atualização:** 2025-11-07

---

**🚀 Pronto para começar? Execute o Quick Start acima!**
