# 🌿 TaskDay - Fluxo de Trabalho com Branches

## 🛡️ **Estrutura de Branches para Desenvolvimento Seguro**

### **📋 Branches Principais**

| Branch | Propósito | Proteção | Deploy |
|--------|-----------|----------|--------|
| `main` | **Produção** | 🔒 Máxima | ✅ Automático |
| `develop` | **Desenvolvimento** | 🔒 Alta | ✅ Staging |
| `staging` | **Teste** | 🔒 Média | ✅ Manual |
| `hotfix/*` | **Correções Urgentes** | 🔒 Alta | ✅ Imediato |
| `release/*` | **Preparação Releases** | 🔒 Média | ❌ Não |
| `feature/*` | **Novas Funcionalidades** | 🔓 Baixa | ❌ Não |
| `bugfix/*` | **Correções de Bugs** | 🔓 Baixa | ❌ Não |
| `docs` | **Documentação** | 🔓 Baixa | ❌ Não |
| `chore` | **Manutenção** | 🔓 Baixa | ❌ Não |

---

## 🚀 **Fluxo de Desenvolvimento**

### **1. 🌿 Desenvolvimento de Features**

```bash
# 1. Criar nova feature
./scripts/create-feature.sh user-authentication

# 2. Desenvolver
git add .
git commit -m "feat: implementa autenticação de usuário"
git push origin feature/user-authentication

# 3. Merge para develop
./scripts/merge-to-develop.sh feature/user-authentication
```

### **2. 🐛 Correção de Bugs**

```bash
# 1. Criar correção
./scripts/create-bugfix.sh login-validation-error

# 2. Corrigir
git add .
git commit -m "fix: corrige validação de login"
git push origin bugfix/login-validation-error

# 3. Merge para develop
./scripts/merge-to-develop.sh bugfix/login-validation-error
```

### **3. 🚨 Hotfix Urgente**

```bash
# 1. Criar hotfix
./scripts/create-hotfix.sh critical-security-fix

# 2. Corrigir URGENTEMENTE
git add .
git commit -m "hotfix: corrige vulnerabilidade crítica"
git push origin hotfix/critical-security-fix

# 3. Merge IMEDIATO para main
git checkout main
git merge hotfix/critical-security-fix
git push origin main

# 4. Deploy IMEDIATO
# GitHub Actions fará deploy automático
```

---

## 🔒 **Regras de Proteção**

### **Branch `main` (Produção)**
- ✅ **Requer Pull Request**
- ✅ **Requer aprovação de 2 revisores**
- ✅ **Requer status checks (CI/CD)**
- ✅ **Requer branch atualizada**
- ✅ **Bloqueia push direto**
- ✅ **Requer assinatura de commits**

### **Branch `develop` (Desenvolvimento)**
- ✅ **Requer Pull Request**
- ✅ **Requer aprovação de 1 revisor**
- ✅ **Requer status checks (CI/CD)**
- ✅ **Requer branch atualizada**
- ✅ **Bloqueia push direto**

### **Branches `feature/*` e `bugfix/*`**
- 🔓 **Push direto permitido**
- ✅ **Requer Pull Request para merge**
- ✅ **Requer status checks (CI/CD)**

---

## 📋 **Checklist de Desenvolvimento**

### **✅ Antes de Criar Branch**
- [ ] Estou na branch correta (`develop` para features, `main` para hotfix)
- [ ] Código está atualizado (`git pull`)
- [ ] Não há mudanças não commitadas

### **✅ Durante o Desenvolvimento**
- [ ] Commits frequentes e descritivos
- [ ] Testes locais passando
- [ ] Análise de código limpa
- [ ] Documentação atualizada

### **✅ Antes do Merge**
- [ ] Todos os testes passando
- [ ] Code review aprovado
- [ ] Conflitos resolvidos
- [ ] Branch atualizada com target

### **✅ Após o Merge**
- [ ] CI/CD passando
- [ ] Deploy em staging funcionando
- [ ] Smoke tests passando
- [ ] Branch deletada (se aplicável)

---

## 🛠️ **Scripts Disponíveis**

### **Criação de Branches**
```bash
# Nova feature
./scripts/create-feature.sh nome-da-feature

# Correção de bug
./scripts/create-bugfix.sh descricao-do-bug

# Hotfix urgente
./scripts/create-hotfix.sh descricao-do-hotfix
```

### **Merge Seguro**
```bash
# Merge para develop
./scripts/merge-to-develop.sh nome-da-branch
```

### **Setup Inicial**
```bash
# Criar toda estrutura de branches
./scripts/setup-branches.sh
```

---

## 🚨 **Cenários de Emergência**

### **1. Bug Crítico em Produção**
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

# 4. Deploy automático acontece
```

### **2. Rollback de Produção**
```bash
# 1. Identificar commit anterior
git log --oneline

# 2. Reverter para commit anterior
git revert <commit-hash>

# 3. Push para main
git push origin main

# 4. Deploy automático acontece
```

### **3. Conflito de Merge**
```bash
# 1. Resolver conflitos
git status
# Editar arquivos com conflitos
git add .
git commit -m "resolve merge conflicts"

# 2. Continuar merge
git push origin <branch-name>
```

---

## 📊 **Monitoramento**

### **GitHub Actions Status**
- ✅ **CI/CD**: Verificar status em Actions
- ✅ **Deploy**: Monitorar logs de deploy
- ✅ **Health**: Verificar health checks

### **Branches Ativas**
```bash
# Listar branches locais
git branch

# Listar branches remotas
git branch -r

# Listar todas as branches
git branch -a
```

### **Histórico de Commits**
```bash
# Log detalhado
git log --oneline --graph --all

# Log de uma branch específica
git log --oneline develop

# Log de merges
git log --merges
```

---

## 🎯 **Melhores Práticas**

### **✅ Commits**
- Use mensagens descritivas
- Siga o padrão: `tipo: descrição`
- Exemplos: `feat:`, `fix:`, `docs:`, `refactor:`

### **✅ Branches**
- Nomes descritivos e curtos
- Use kebab-case: `feature/user-auth`
- Delete branches após merge

### **✅ Pull Requests**
- Título claro e descritivo
- Descrição detalhada das mudanças
- Screenshots para mudanças de UI
- Link para issues relacionadas

### **✅ Code Review**
- Revise código de outros
- Seja construtivo nas críticas
- Teste localmente se necessário
- Aprove apenas se estiver seguro

---

## 🚀 **Deploy Automático**

### **Branch `main` → Produção**
- ✅ Deploy automático via GitHub Actions
- ✅ Health checks automáticos
- ✅ Rollback automático em caso de falha

### **Branch `develop` → Staging**
- ✅ Deploy automático para ambiente de teste
- ✅ Testes de integração automáticos
- ✅ Notificações de status

---

## 📚 **Recursos Adicionais**

- [Git Flow](https://nvie.com/posts/a-successful-git-branching-model/)
- [GitHub Flow](https://guides.github.com/introduction/flow/)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [Semantic Versioning](https://semver.org/)

---

**TaskDay** - Desenvolvimento seguro e organizado! 🛡️🌿
