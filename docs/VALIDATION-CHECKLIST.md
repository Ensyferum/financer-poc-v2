# ✅ Checklist de Validação - Sistema de Migration

Use este checklist para validar que o sistema de migration está funcionando corretamente.

---

## 🎯 Pré-requisitos

Antes de começar, certifique-se de que:

- [ ] Docker Desktop está instalado e rodando
- [ ] Você tem acesso aos scripts `.bat` no diretório raiz
- [ ] Porta 5432 está disponível (PostgreSQL)
- [ ] Porta 27017 está disponível (MongoDB)

---

## 📋 Teste 1: Setup Inicial

### Objetivo
Validar que o setup inicial funciona corretamente.

### Passos

```batch
# Execute o setup
db-setup.bat
```

### Verificações

- [ ] PostgreSQL container iniciou sem erros
- [ ] MongoDB container iniciou sem erros
- [ ] Flyway executou as migrations com sucesso
- [ ] Mensagem de sucesso apareceu: `[SUCCESS] Database setup completed successfully!`

### Validação Manual

```sql
# Conecte ao PostgreSQL
docker exec -it financer-postgres psql -U financer_user -d financer

# Liste as tabelas criadas
\dt financer.*;

# Resultado esperado:
#  Schema  |        Name         | Type  |     Owner
# ---------+---------------------+-------+---------------
#  financer| accounts            | table | financer_user
#  financer| account_audit       | table | financer_user
#  financer| cards               | table | financer_user
#  financer| transactions        | table | financer_user
#  financer| transaction_audit   | table | financer_user
#  financer| flyway_schema_history| table| financer_user

# Verifique o histórico de migrations
SELECT version, description, installed_on, success 
FROM financer.flyway_schema_history 
ORDER BY installed_rank;

# Resultado esperado:
# version |          description              | installed_on | success
# --------+-----------------------------------+--------------+---------
#   1     | create accounts schema            | 2025-11-07...| t
#   2     | create cards transactions schema  | 2025-11-07...| t

# Saia
\q
```

- [ ] Todas as 6 tabelas foram criadas
- [ ] Histórico do Flyway mostra 2 migrations aplicadas
- [ ] Todas migrations têm `success = t`

---

## 📋 Teste 2: Validar Dados de Exemplo

### Objetivo
Verificar que os dados seed foram inseridos.

### Passos

```sql
# Conecte ao PostgreSQL
docker exec -it financer-postgres psql -U financer_user -d financer

# Verifique contas
SELECT account_name, account_type, balance 
FROM financer.accounts;

# Resultado esperado: 3 contas
# Conta Corrente Principal | CHECKING | 5000.00
# Conta Poupança | SAVINGS | 10000.00
# Investimentos | INVESTMENT | 50000.00

# Verifique cartões
SELECT card_holder_name, card_type, card_brand 
FROM financer.cards;

# Resultado esperado: 2 cartões
# João da Silva | CREDIT | VISA
# Maria Santos | DEBIT | MASTERCARD

# Verifique transações
SELECT description, amount, status 
FROM financer.transactions;

# Resultado esperado: 3 transações

\q
```

### Verificações

- [ ] 3 contas foram inseridas
- [ ] 2 cartões foram inseridos
- [ ] 3 transações foram inseridas
- [ ] Valores batem com o esperado

---

## 📋 Teste 3: Criar Nova Migration

### Objetivo
Validar o processo de criação de nova migration.

### Passos

```batch
# Execute o criador
db-new-migration.bat

# Digite a descrição quando solicitado
Enter description: test_add_payment_methods
```

### Verificações

- [ ] Arquivo foi criado: `db/migrations/postgresql/V3__test_add_payment_methods.sql`
- [ ] Arquivo tem cabeçalho correto com comentários
- [ ] Versão é V3 (próxima após V2)

### Editar e Aplicar

```sql
-- Edite o arquivo V3__test_add_payment_methods.sql
SET search_path TO financer;

CREATE TABLE IF NOT EXISTS payment_methods (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    method_name VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);
```

```batch
# Aplique a migration
db-migrate.bat
# Escolha opção: 1 (Run Migrations)
```

### Validação

```sql
# Conecte ao banco
docker exec -it financer-postgres psql -U financer_user -d financer

# Verifique nova tabela
\d financer.payment_methods

# Verifique histórico
SELECT version, description 
FROM financer.flyway_schema_history 
ORDER BY installed_rank;

\q
```

- [ ] Nova tabela `payment_methods` foi criada
- [ ] V3 aparece no histórico do Flyway
- [ ] Migration marcada como `success = t`

---

## 📋 Teste 4: Validação de Integridade

### Objetivo
Verificar que a validação de checksums funciona.

### Passos

```batch
# Execute validação
db-migrate.bat
# Escolha opção: 2 (Validate Migrations)
```

### Verificações

- [ ] Validação passou sem erros
- [ ] Mensagem: `[SUCCESS] All migrations are valid!`

---

## 📋 Teste 5: Info e Status

### Objetivo
Visualizar informações detalhadas das migrations.

### Passos

```batch
# Veja o histórico
db-migrate.bat
# Escolha opção: 3 (Info)
```

### Verificações

- [ ] Lista mostra todas migrations (V1, V2, V3)
- [ ] Status de cada migration é "Success"
- [ ] Checksums são exibidos
- [ ] Data de instalação está correta

---

## 📋 Teste 6: Auditoria Automática

### Objetivo
Validar que os triggers de auditoria funcionam.

### Passos

```sql
# Conecte ao banco
docker exec -it financer-postgres psql -U financer_user -d financer

# Faça uma atualização em uma conta
UPDATE financer.accounts 
SET balance = 6000.00 
WHERE account_name = 'Conta Corrente Principal';

# Verifique o registro de auditoria
SELECT 
    operation,
    old_value->>'balance' as old_balance,
    new_value->>'balance' as new_balance,
    changed_at
FROM financer.account_audit
ORDER BY changed_at DESC
LIMIT 1;

# Resultado esperado:
# operation | old_balance | new_balance | changed_at
# ----------+-------------+-------------+------------
# UPDATE    | 5000.00     | 6000.00     | 2025-11-07...

\q
```

### Verificações

- [ ] Registro de auditoria foi criado automaticamente
- [ ] Operation = "UPDATE"
- [ ] old_value contém o saldo anterior (5000.00)
- [ ] new_value contém o novo saldo (6000.00)
- [ ] Timestamp está correto

---

## 📋 Teste 7: Soft Delete

### Objetivo
Verificar que soft delete funciona (is_active flag).

### Passos

```sql
# Conecte ao banco
docker exec -it financer-postgres psql -U financer_user -d financer

# "Delete" uma conta (soft delete)
UPDATE financer.accounts 
SET is_active = FALSE 
WHERE account_name = 'Investimentos';

# Verifique que ainda existe
SELECT account_name, is_active 
FROM financer.accounts 
WHERE account_name = 'Investimentos';

# Resultado esperado:
# account_name  | is_active
# --------------+-----------
# Investimentos | f

# Verifique auditoria
SELECT operation, new_value->>'is_active' as is_active
FROM financer.account_audit
WHERE account_id = (SELECT id FROM financer.accounts WHERE account_name = 'Investimentos')
ORDER BY changed_at DESC
LIMIT 1;

\q
```

### Verificações

- [ ] Conta foi desativada (is_active = false)
- [ ] Conta ainda existe no banco (não deletada)
- [ ] Auditoria registrou a mudança

---

## 📋 Teste 8: Timestamps Automáticos

### Objetivo
Validar que updated_at é atualizado automaticamente.

### Passos

```sql
# Conecte ao banco
docker exec -it financer-postgres psql -U financer_user -d financer

# Veja timestamps atuais
SELECT account_name, created_at, updated_at 
FROM financer.accounts 
WHERE account_name = 'Conta Poupança';

# Anote o updated_at atual, depois faça uma atualização
UPDATE financer.accounts 
SET balance = 11000.00 
WHERE account_name = 'Conta Poupança';

# Veja novamente
SELECT account_name, created_at, updated_at 
FROM financer.accounts 
WHERE account_name = 'Conta Poupança';

\q
```

### Verificações

- [ ] `created_at` não mudou
- [ ] `updated_at` foi atualizado para o timestamp atual
- [ ] `version` foi incrementado (optimistic locking)

---

## 📋 Teste 9: Constraints e Validações

### Objetivo
Verificar que constraints do banco estão funcionando.

### Passos

```sql
# Conecte ao banco
docker exec -it financer-postgres psql -U financer_user -d financer

# Tente inserir account_type inválido (deve falhar)
INSERT INTO financer.accounts (account_name, account_type)
VALUES ('Teste', 'INVALID_TYPE');

# Resultado esperado: ERROR (constraint violation)

# Tente inserir transação com tipo inválido (deve falhar)
INSERT INTO financer.transactions (account_id, transaction_type, description, amount, transaction_date)
VALUES (
    (SELECT id FROM financer.accounts LIMIT 1),
    'INVALID_TYPE',
    'Test',
    100.00,
    CURRENT_TIMESTAMP
);

# Resultado esperado: ERROR (constraint violation)

\q
```

### Verificações

- [ ] Inserção com account_type inválido foi rejeitada
- [ ] Inserção com transaction_type inválido foi rejeitada
- [ ] Mensagem de erro é clara

---

## 📋 Teste 10: Limpeza (Opcional)

### Objetivo
Testar limpeza do ambiente.

### Passos

```batch
# Pare todos containers
docker-compose -f docker-compose.infrastructure.yml down

# Liste containers
docker ps -a

# Remova volumes (cuidado: apaga dados!)
docker-compose -f docker-compose.infrastructure.yml down -v
```

### Verificações

- [ ] Containers foram parados
- [ ] Volumes foram removidos (se solicitado)
- [ ] Sistema pode ser recriado com `db-setup.bat`

---

## 📊 Resumo de Validação

### Status Geral

| Teste | Status | Observações |
|-------|--------|-------------|
| 1. Setup Inicial | ☐ | |
| 2. Dados de Exemplo | ☐ | |
| 3. Criar Nova Migration | ☐ | |
| 4. Validação de Integridade | ☐ | |
| 5. Info e Status | ☐ | |
| 6. Auditoria Automática | ☐ | |
| 7. Soft Delete | ☐ | |
| 8. Timestamps Automáticos | ☐ | |
| 9. Constraints | ☐ | |
| 10. Limpeza | ☐ | |

### Resultado Final

- [ ] **Todos os testes passaram** ✅
- [ ] Sistema está pronto para uso em desenvolvimento
- [ ] Documentação está completa e correta

---

## 🐛 Problemas Encontrados

Anote aqui quaisquer problemas encontrados durante a validação:

```
Teste #: [número do teste]
Problema: [descrição do problema]
Erro: [mensagem de erro, se houver]
Solução: [como foi resolvido]
```

---

## 📝 Notas Adicionais

```
[Espaço para observações adicionais sobre o processo de validação]
```

---

**Data da Validação:** _______________  
**Validado por:** _______________  
**Ambiente:** ☐ Desenvolvimento ☐ Staging ☐ Produção  
**Versão do Sistema:** 1.0.0
