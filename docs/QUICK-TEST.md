# 🧪 Guia Rápido de Teste - Migration System

**Tempo estimado:** 5-10 minutos  
**Objetivo:** Validar que o sistema está funcionando perfeitamente

---

## ✅ Teste Rápido em 5 Passos

### Passo 1: Verificar Docker (30 segundos)

```batch
# Verifique se Docker está rodando
docker --version
docker-compose --version

# Resultado esperado:
# Docker version 24.x.x
# Docker Compose version v2.x.x
```

✅ Docker está instalado e funcionando

---

### Passo 2: Setup Inicial (2 minutos)

```batch
# Execute o setup
db-setup.bat
```

**Aguarde as mensagens:**
```
[INFO] Starting infrastructure services...
[INFO] Waiting for databases to be ready...
[INFO] Running database migrations...
[SUCCESS] Database setup completed successfully!
```

✅ PostgreSQL iniciado  
✅ MongoDB iniciado  
✅ Migrations aplicadas com sucesso

---

### Passo 3: Verificar Containers (30 segundos)

```batch
# Liste containers rodando
docker ps

# Você deve ver:
# - financer-postgres
# - financer-mongodb
# (financer-flyway já terá executado e parado)
```

✅ Containers estão rodando

---

### Passo 4: Conectar ao Banco (1 minuto)

```batch
# Conecte ao PostgreSQL
docker exec -it financer-postgres psql -U financer_user -d financer
```

```sql
-- Liste as tabelas criadas
\dt financer.*;

-- Resultado esperado (6 tabelas):
-- accounts, account_audit, cards, transactions, 
-- transaction_audit, flyway_schema_history

-- Veja os dados de exemplo
SELECT account_name, balance FROM financer.accounts;

-- Resultado esperado (3 contas):
-- Conta Corrente Principal | 5000.00
-- Conta Poupança           | 10000.00  
-- Investimentos            | 50000.00

-- Saia
\q
```

✅ Tabelas criadas  
✅ Dados de exemplo inseridos

---

### Passo 5: Testar Menu de Migrations (1 minuto)

```batch
# Abra o menu de gestão
db-migrate.bat
```

**Teste as opções:**

1. **Opção 3 (Info)** - Ver histórico de migrations
   - ✅ Deve mostrar V1 e V2 aplicadas

2. **Opção 2 (Validate)** - Validar integridade
   - ✅ Deve mostrar `[SUCCESS] All migrations are valid!`

3. **Opção 8 (Exit)** - Sair

✅ Menu funciona perfeitamente

---

## 🎉 Sistema Validado!

Se todos os ✅ acima estão marcados, o sistema está **100% funcional**!

---

## 🚀 Próximos Passos

### Teste Avançado (Opcional)

```batch
# 1. Crie uma nova migration de teste
db-new-migration.bat
# Digite: test_new_feature

# 2. Edite o arquivo criado em:
# db/migrations/postgresql/V3__test_new_feature.sql

# 3. Adicione SQL simples:
```

```sql
SET search_path TO financer;

CREATE TABLE IF NOT EXISTS test_table (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL
);
```

```batch
# 4. Aplique a migration
db-migrate.bat
# Opção 1: Run Migrations

# 5. Verifique no banco
docker exec -it financer-postgres psql -U financer_user -d financer
```

```sql
\d financer.test_table
\q
```

✅ Nova migration aplicada com sucesso!

---

## 📊 Testes de Auditoria (Opcional)

```batch
# Conecte ao banco
docker exec -it financer-postgres psql -U financer_user -d financer
```

```sql
-- Faça uma mudança em uma conta
UPDATE financer.accounts 
SET balance = 7000.00 
WHERE account_name = 'Conta Corrente Principal';

-- Veja o registro de auditoria automático
SELECT 
    operation,
    old_value->>'balance' as saldo_anterior,
    new_value->>'balance' as saldo_novo,
    changed_at
FROM financer.account_audit
ORDER BY changed_at DESC
LIMIT 1;

-- Resultado esperado:
-- operation | saldo_anterior | saldo_novo | changed_at
-- ----------+----------------+------------+------------
-- UPDATE    | 5000.00        | 7000.00    | 2025-11-07...

\q
```

✅ Auditoria automática funcionando!

---

## 🔍 Verificação de Health Checks

```batch
# Verifique health dos containers
docker inspect financer-postgres | findstr "Health"
```

**Resultado esperado:** `"Health": "healthy"`

---

## 📝 Checklist Final

Marque conforme testa:

- [ ] Docker rodando
- [ ] db-setup.bat executou com sucesso
- [ ] PostgreSQL acessível
- [ ] 6 tabelas criadas
- [ ] 3 contas de exemplo inseridas
- [ ] Menu db-migrate.bat funciona
- [ ] Info mostra V1 e V2
- [ ] Validate passa sem erros
- [ ] Nova migration criada (V3)
- [ ] Auditoria automática funciona

---

## ❌ Problemas Comuns

### "Docker is not running"
**Solução:** Inicie o Docker Desktop

### "Port 5432 already in use"
**Solução:** 
```batch
# Pare PostgreSQL local
# Ou mude a porta em docker-compose.infrastructure.yml
```

### "Migration failed"
**Solução:**
```batch
db-migrate.bat
# Opção 4: Repair
```

### "Permission denied"
**Solução:** Execute o terminal como Administrador

---

## 📖 Documentação Completa

Para informações detalhadas, consulte:

- **db/README.md** - Guia completo de migrations
- **VALIDATION-CHECKLIST.md** - 10 testes detalhados
- **ARCHITECTURE.md** - Diagramas e arquitetura
- **ROADMAP.md** - Próximos passos do projeto

---

## ✅ Conclusão

Se chegou até aqui com todos os testes passando:

🎉 **Parabéns! O sistema de migration está funcionando perfeitamente!**

Você está pronto para:
1. ✅ Criar novas migrations
2. ✅ Desenvolver microserviços
3. ✅ Evoluir o schema do banco
4. ✅ Seguir para próxima fase (Account Service)

---

**Próximo passo:** Leia `ROADMAP.md` para ver o que vem a seguir!
