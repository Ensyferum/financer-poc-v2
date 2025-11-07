# 👋 Bem-vindo ao Projeto Financer!

**Este arquivo é para você que está começando agora no projeto.**

---

## 🎯 Início Rápido (10 minutos)

### Passo 1: Entenda o Projeto (2 min)

Você está trabalhando no **Financer**, um sistema de gestão financeira pessoal baseado em microserviços.

**O que já está pronto:**
- ✅ Sistema completo de versionamento de banco de dados (Flyway)
- ✅ Infraestrutura Docker (PostgreSQL, MongoDB, Kafka)
- ✅ Schema inicial (5 tabelas + auditoria automática)
- ✅ Scripts de gestão automatizados
- ✅ Documentação profissional completa

**Status atual:** 🟢 Fase 1 completa, pronto para Fase 2

---

### Passo 2: Navegue pela Documentação (3 min)

**Comece por aqui (nesta ordem):**

1. **[PROJECT-STATUS.md](PROJECT-STATUS.md)** - Status visual do projeto (1 min)
2. **[README.md](README.md)** - Visão geral e quick start (2 min)
3. **[INDEX.md](INDEX.md)** - Índice completo da documentação (para referência)

**Para trabalhar com banco de dados:**
- **[db/README.md](db/README.md)** - Guia completo de migrations

**Para entender a arquitetura:**
- **[ARCHITECTURE.md](ARCHITECTURE.md)** - 9 diagramas visuais

**Para saber o que fazer a seguir:**
- **[ROADMAP.md](ROADMAP.md)** - Próximas tarefas detalhadas

---

### Passo 3: Configure seu Ambiente (3 min)

```bash
# 1. Certifique-se de que Docker está rodando
docker --version

# 2. Execute o setup inicial (isso faz TUDO)
db-setup.bat

# 3. Aguarde a mensagem de sucesso
# [SUCCESS] Database setup completed successfully!

# 🎉 Pronto! Seu ambiente está configurado
```

**O que foi criado:**
- PostgreSQL rodando na porta 5432
- MongoDB rodando na porta 27017
- 5 tabelas com dados de exemplo
- Sistema de auditoria ativo

---

### Passo 4: Valide a Instalação (2 min)

```bash
# Teste o menu de gestão
db-migrate.bat

# Escolha: 3 (Info)
# Você deve ver V1 e V2 aplicadas com sucesso

# Escolha: 8 (Exit)
```

**Se viu as migrations aplicadas:** ✅ Tudo certo!

---

## 🎓 Primeiras Tarefas

### Se você vai trabalhar com BANCO DE DADOS:

1. Leia **[db/README.md](db/README.md)** - Seção "Uso Básico"
2. Pratique criar uma migration de teste:
   ```bash
   db-new-migration.bat
   # Digite: test_my_first_migration
   ```
3. Edite o arquivo criado e adicione SQL simples
4. Aplique com `db-migrate.bat` (opção 1)

### Se você vai DESENVOLVER MICROSERVIÇOS:

1. Leia **[ROADMAP.md](ROADMAP.md)** - Seção "Quick Wins"
2. A próxima task é: **Account Service**
3. Siga o checklist em **[ROADMAP.md - Task 2.1](ROADMAP.md#task-21-account-service)**
4. Use os templates em `db/pom-flyway-template.xml`

### Se você vai trabalhar com FRONTEND:

1. Aguarde Fase 2 ser concluída (microserviços base)
2. Consulte **[ROADMAP.md - Fase 7](ROADMAP.md#-fase-7-frontend-angular)**
3. Por enquanto, familiarize-se com o schema do banco

---

## 📚 Mapa Mental da Documentação

```
START HERE (você está aqui!)
    │
    ├─→ PROJECT-STATUS.md ────→ Visão geral do projeto
    │
    ├─→ README.md ────────────→ Quick start e features
    │
    ├─→ INDEX.md ─────────────→ Navegação completa
    │
    ├─→ QUICK-TEST.md ────────→ Teste em 5 passos
    │
    └─→ Depois escolha:
         │
         ├─→ Trabalhar com DB?
         │   └─→ db/README.md
         │
         ├─→ Entender arquitetura?
         │   └─→ ARCHITECTURE.md
         │
         ├─→ Desenvolver features?
         │   └─→ ROADMAP.md
         │
         └─→ Validar tudo?
             └─→ VALIDATION-CHECKLIST.md
```

---

## 🛠️ Ferramentas que Você Vai Usar

### Scripts Principais

| Script | Quando Usar | Frequência |
|--------|-------------|------------|
| `db-setup.bat` | Setup inicial, reset ambiente | Raro |
| `db-migrate.bat` | Aplicar/validar migrations | Diário |
| `db-new-migration.bat` | Criar nova migration | Conforme necessário |

### Comandos Docker

```bash
# Ver containers rodando
docker ps

# Ver logs do PostgreSQL
docker logs financer-postgres

# Conectar ao banco
docker exec -it financer-postgres psql -U financer_user -d financer

# Parar tudo
docker-compose -f docker-compose.infrastructure.yml down

# Reiniciar
docker-compose -f docker-compose.infrastructure.yml up -d
```

---

## 🎯 Padrões do Projeto

### Ao Criar Migration

✅ **USE:** `db-new-migration.bat` (cria com versão automática)  
❌ **NÃO:** Crie arquivo manualmente

✅ **NOME:** `add_user_table` (snake_case)  
❌ **NOME:** `Add User Table` (espaços)

✅ **SQL:** Use `IF NOT EXISTS`  
❌ **SQL:** CREATE sem verificação

### Ao Fazer Commit

```bash
# Formato: tipo: descrição
git commit -m "feat: adiciona tabela de usuários"
git commit -m "fix: corrige índice duplicado"
git commit -m "docs: atualiza README"

# Tipos: feat, fix, docs, refactor, test, chore
```

### Ao Criar Branch

```bash
# Formato: tipo/descrição-curta
git checkout -b feature/user-authentication
git checkout -b fix/database-migration-error
git checkout -b docs/api-documentation
```

---

## ⚠️ Coisas Importantes

### ❌ NUNCA Faça Isso:

- ❌ Alterar migration já aplicada (crie nova ao invés)
- ❌ Commitar `.env.production` (está no .gitignore)
- ❌ Deletar dados sem soft delete (`is_active = false`)
- ❌ Pular testes (mínimo 80% coverage)
- ❌ Fazer PR sem atualizar documentação

### ✅ SEMPRE Faça Isso:

- ✅ Criar branch feature antes de começar
- ✅ Escrever testes para código novo
- ✅ Atualizar documentação relevante
- ✅ Seguir padrões de logging (ver [prompt.md](prompt.md))
- ✅ Validar migrations localmente antes de PR

---

## 🆘 Ajuda Rápida

### Problema: Docker não inicia

```bash
# Verifique se Docker Desktop está rodando
# Windows: Procure ícone do Docker na bandeja
# Se não estiver, inicie o Docker Desktop
```

### Problema: Porta 5432 ocupada

```bash
# Opção 1: Pare PostgreSQL local
# Opção 2: Mude a porta em docker-compose.infrastructure.yml
```

### Problema: Migration falhou

```bash
# 1. Veja o erro nos logs
docker logs financer-flyway

# 2. Corrija o SQL da migration

# 3. Faça repair se necessário
db-migrate.bat
# Opção 4: Repair
```

### Dúvida: Como fazer X?

1. **Consulte INDEX.md** - Busque por tópico
2. **Leia documentação específica** - db/README, ARCHITECTURE, etc
3. **Veja exemplos** - Migrations existentes são exemplos
4. **Pergunte** - Abra issue ou pergunte ao time

---

## 📝 Checklist do Primeiro Dia

**Conclua estas tarefas no seu primeiro dia:**

- [ ] Li PROJECT-STATUS.md
- [ ] Li README.md
- [ ] Executei db-setup.bat com sucesso
- [ ] Testei db-migrate.bat
- [ ] Conectei ao PostgreSQL e vi as tabelas
- [ ] Criei uma migration de teste
- [ ] Li db/README.md (seções principais)
- [ ] Vi os diagramas em ARCHITECTURE.md
- [ ] Li ROADMAP.md (próximas tasks)
- [ ] Entendi os padrões de commit/branch
- [ ] Configurei meu editor (VSCode recomendado)
- [ ] Clonei o repositório Git

**Quando terminar:** Você está pronto para começar a desenvolver! 🚀

---

## 🎯 Seu Primeiro Código

### Task Sugerida: Criar Migration de Teste

```bash
# 1. Crie a migration
db-new-migration.bat
# Digite: add_test_user_preferences

# 2. Edite o arquivo criado (será V3__)
# db/migrations/postgresql/V3__add_test_user_preferences.sql
```

```sql
SET search_path TO financer;

CREATE TABLE IF NOT EXISTS user_preferences (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(100) NOT NULL,
    preference_key VARCHAR(100) NOT NULL,
    preference_value TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT uk_user_preference UNIQUE (user_id, preference_key)
);

CREATE INDEX idx_user_preferences_user ON user_preferences(user_id);

COMMENT ON TABLE user_preferences IS 'User-specific preferences and settings';
```

```bash
# 3. Aplique a migration
db-migrate.bat
# Opção 1: Run Migrations

# 4. Verifique
db-migrate.bat
# Opção 3: Info
# Você deve ver V1, V2, V3 aplicadas

# 5. Conecte ao banco e veja a tabela
docker exec -it financer-postgres psql -U financer_user -d financer
\d financer.user_preferences
\q
```

**✅ Parabéns! Você criou sua primeira migration!**

---

## 🚀 Próximos Passos

Depois de completar o checklist acima:

1. **Consulte [ROADMAP.md](ROADMAP.md)** para ver próximas tasks
2. **Escolha uma task** (recomendado: começar com Account Service)
3. **Crie branch feature:** `git checkout -b feature/account-service`
4. **Desenvolva seguindo padrões:** Ver [prompt.md](prompt.md)
5. **Teste localmente:** Sempre antes de PR
6. **Atualize docs:** Se mudou algo significativo
7. **Faça PR:** Com descrição clara do que foi feito

---

## 🤝 Cultura do Time

### Valorizamos:

- ✅ **Qualidade sobre velocidade**
- ✅ **Documentação clara e atualizada**
- ✅ **Testes abrangentes (80%+ coverage)**
- ✅ **Code review construtivo**
- ✅ **Comunicação proativa**
- ✅ **Aprendizado contínuo**

### Processo:

```
1. Entender requirement
2. Planejar solução
3. Criar branch
4. Desenvolver + testar
5. Atualizar docs
6. PR + code review
7. Merge + deploy
8. Validar em dev
```

---

## 📞 Contatos e Recursos

### Documentação
- **Completa:** Todos arquivos .md no repositório
- **Navegação:** INDEX.md
- **Status:** PROJECT-STATUS.md

### Ferramentas
- **Docker Desktop:** https://www.docker.com/products/docker-desktop
- **PostgreSQL Client:** Incluído no container
- **VSCode:** Recomendado

### Suporte
- **Dúvidas técnicas:** Consulte documentação primeiro
- **Problemas bloqueantes:** Abra issue
- **Sugestões:** Bem-vindas via PR

---

## 🎉 Mensagem Final

Bem-vindo ao time! Este projeto tem:

- ✅ Documentação de alta qualidade
- ✅ Padrões bem definidos
- ✅ Infraestrutura pronta
- ✅ Testes automatizados (em andamento)
- ✅ Roadmap claro

**Você tem tudo que precisa para ter sucesso aqui!**

Se tiver dúvidas, consulte a documentação primeiro (está muito completa!), e não hesite em perguntar quando precisar.

**Boa codificação!** 🚀

---

**Dica final:** Adicione este arquivo aos seus favoritos. Você vai consultá-lo várias vezes no início!
