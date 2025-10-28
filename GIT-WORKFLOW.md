# Git Workflow - Jooby

## 🚀 Setup Inicial

### Configuração do Git
```bash
# Configurar usuário
git config --global user.name "Seu Nome"
git config --global user.email "seu.email@exemplo.com"

# Configurar branch padrão
git config --global init.defaultBranch main
```

### Inicializar Repositório
```bash
# No diretório do projeto
git init
git add .
git commit -m "feat: configuração inicial do Jooby"

# Conectar ao GitHub
git remote add origin https://github.com/SEU-USUARIO/jooby.git
git push -u origin main
```

## 🌿 Estrutura de Branches

| Branch | Propósito | Proteção |
|--------|-----------|----------|
| `main` | Produção | 🔒 Máxima |
| `develop` | Desenvolvimento | 🔒 Alta |
| `feature/*` | Novas funcionalidades | 🔓 Baixa |
| `bugfix/*` | Correções de bugs | 🔓 Baixa |
| `hotfix/*` | Correções urgentes | 🔒 Alta |

## 🛠️ Scripts Disponíveis

### Criar Branches
```bash
# Nova feature
./scripts/create-feature.sh nome-da-feature

# Correção de bug
./scripts/create-bugfix.sh descricao-do-bug

# Hotfix urgente
./scripts/create-hotfix.sh descricao-do-hotfix
```

### Merge Seguro
```bash
# Merge para develop
./scripts/merge-to-develop.sh nome-da-branch
```

## 📋 Fluxo de Desenvolvimento

### 1. Nova Funcionalidade
```bash
# Criar feature
./scripts/create-feature.sh user-authentication

# Desenvolver
git add .
git commit -m "feat: implementa autenticação de usuário"
git push origin feature/user-authentication

# Merge para develop
./scripts/merge-to-develop.sh feature/user-authentication
```

### 2. Correção de Bug
```bash
# Criar bugfix
./scripts/create-bugfix.sh login-validation-error

# Corrigir
git add .
git commit -m "fix: corrige validação de login"
git push origin bugfix/login-validation-error

# Merge para develop
./scripts/merge-to-develop.sh bugfix/login-validation-error
```

### 3. Hotfix Urgente
```bash
# Criar hotfix
./scripts/create-hotfix.sh critical-security-fix

# Corrigir URGENTEMENTE
git add .
git commit -m "hotfix: corrige vulnerabilidade crítica"
git push origin hotfix/critical-security-fix

# Merge IMEDIATO para main
git checkout main
git merge hotfix/critical-security-fix
git push origin main
```

## 🔒 Regras de Proteção

### Branch `main` (Produção)
- ✅ Requer Pull Request
- ✅ Requer aprovação de 2 revisores
- ✅ Requer status checks (CI/CD)
- ✅ Requer branch atualizada

### Branch `develop` (Desenvolvimento)
- ✅ Requer Pull Request
- ✅ Requer aprovação de 1 revisor
- ✅ Requer status checks (CI/CD)

## 🚨 Cenários de Emergência

### Bug Crítico em Produção
```bash
# 1. Criar hotfix
./scripts/create-hotfix.sh critical-bug-fix

# 2. Corrigir IMEDIATAMENTE
git add .
git commit -m "hotfix: corrige bug crítico"
git push origin hotfix/critical-bug-fix

# 3. Merge para main
git checkout main
git merge hotfix/critical-bug-fix
git push origin main
```

### Rollback de Produção
```bash
# 1. Identificar commit anterior
git log --oneline

# 2. Reverter para commit anterior
git revert <commit-hash>

# 3. Push para main
git push origin main
```

## 📊 Comandos Úteis

```bash
# Ver status
git status

# Ver histórico
git log --oneline

# Ver branches
git branch -a

# Mudar de branch
git checkout nome-da-branch

# Fazer pull
git pull origin main

# Fazer push
git push origin nome-da-branch
```

## 🎯 Melhores Práticas

### Commits
- Use mensagens descritivas
- Siga o padrão: `tipo: descrição`
- Exemplos: `feat:`, `fix:`, `docs:`, `refactor:`

### Branches
- Nomes descritivos e curtos
- Use kebab-case: `feature/user-auth`
- Delete branches após merge

### Pull Requests
- Título claro e descritivo
- Descrição detalhada das mudanças
- Screenshots para mudanças de UI

---

**Jooby** - Desenvolvimento seguro e organizado! 🛡️🌿
