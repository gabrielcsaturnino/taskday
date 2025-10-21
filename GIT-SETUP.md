# 🔧 Configuração do Git e GitHub

## 📋 **Passo a Passo Completo**

### **1. Configuração Inicial do Git**

```bash
# Configurar usuário global
git config --global user.name "Seu Nome Completo"
git config --global user.email "seu.email@exemplo.com"

# Configurar editor padrão
git config --global core.editor "code --wait"

# Configurar branch padrão
git config --global init.defaultBranch main

# Configurar autenticação
git config --global credential.helper store
```

### **2. Inicializar Repositório Local**

```bash
# Navegar para o diretório do projeto
cd /home/gabriel/Documentos/taskday

# Inicializar repositório Git
git init

# Adicionar todos os arquivos
git add .

# Fazer primeiro commit
git commit -m "feat: configuração inicial do projeto TaskDay

- Configuração do Spring Boot
- Entidades JPA (User, Client, Contractor, Job)
- Configuração de segurança JWT
- Swagger UI configurado
- Docker Compose para desenvolvimento
- Pipeline CI/CD configurado"

# Verificar status
git status
```

### **3. Configurar Repositório Remoto no GitHub**

#### **Opção A: Criar repositório no GitHub (Recomendado)**

1. Acesse [GitHub.com](https://github.com)
2. Clique em "New repository"
3. Nome: `taskday`
4. Descrição: `Plataforma de freelancers TaskDay`
5. Marque como "Public" ou "Private"
6. **NÃO** marque "Add a README file"
7. Clique em "Create repository"

#### **Opção B: Usar GitHub CLI (se instalado)**

```bash
# Instalar GitHub CLI (Ubuntu/Debian)
curl -fsSL https://cli.github.com/packages/githubcli-archive-keyring.gpg | sudo dd of=/usr/share/keyrings/githubcli-archive-keyring.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/githubcli-archive-keyring.gpg] https://cli.github.com/packages stable main" | sudo tee /etc/apt/sources.list.d/github-cli.list > /dev/null
sudo apt update
sudo apt install gh

# Fazer login no GitHub
gh auth login

# Criar repositório
gh repo create taskday --public --description "Plataforma de freelancers TaskDay"
```

### **4. Conectar Repositório Local com GitHub**

```bash
# Adicionar remote origin
git remote add origin https://github.com/SEU-USUARIO/taskday.git

# Verificar remotes
git remote -v

# Fazer push inicial
git push -u origin main
```

### **5. Configurar Git Flow (Fluxo de Branches)**

```bash
# Instalar git-flow (Ubuntu/Debian)
sudo apt install git-flow

# Inicializar git-flow no projeto
git flow init

# Aceitar as configurações padrão:
# - Branch para produção: main
# - Branch para desenvolvimento: develop
# - Prefixo para features: feature/
# - Prefixo para releases: release/
# - Prefixo para hotfixes: hotfix/
```

### **6. Estrutura de Branches Recomendada**

```
main (produção)
├── develop (desenvolvimento)
├── feature/nova-funcionalidade
├── feature/autenticacao-oauth
├── feature/sistema-pagamentos
├── hotfix/correcao-urgente
└── release/versao-1.0.0
```

### **7. Comandos do Git Flow**

#### **Criar Feature Branch:**
```bash
# Iniciar nova feature
git flow feature start nova-funcionalidade

# Fazer commits normalmente
git add .
git commit -m "feat: implementa nova funcionalidade"

# Finalizar feature (merge para develop)
git flow feature finish nova-funcionalidade
```

#### **Criar Release:**
```bash
# Iniciar release
git flow release start 1.0.0

# Fazer ajustes finais
git add .
git commit -m "chore: prepara release 1.0.0"

# Finalizar release (merge para main e develop)
git flow release finish 1.0.0
```

#### **Criar Hotfix:**
```bash
# Iniciar hotfix
git flow hotfix start correcao-urgente

# Fazer correção
git add .
git commit -m "fix: corrige bug crítico"

# Finalizar hotfix
git flow hotfix finish correcao-urgente
```

### **8. Configurar GitHub Actions Secrets**

1. Acesse seu repositório no GitHub
2. Vá em **Settings** → **Secrets and variables** → **Actions**
3. Clique em **New repository secret**
4. Adicione os seguintes secrets:

```
DATABASE_URL=postgresql://user:pass@host:5432/db
JWT_SECRET=sua-chave-secreta-jwt-super-segura
MAIL_USERNAME=seu-email@gmail.com
MAIL_PASSWORD=sua-senha-de-app
SLACK_WEBHOOK=https://hooks.slack.com/services/...
```

### **9. Configurar Branch Protection Rules**

1. Vá em **Settings** → **Branches**
2. Clique em **Add rule**
3. Configure para branch `main`:
   - ✅ Require a pull request before merging
   - ✅ Require status checks to pass before merging
   - ✅ Require branches to be up to date before merging
   - ✅ Restrict pushes that create files larger than 100MB

### **10. Comandos Úteis do Git**

```bash
# Ver status
git status

# Ver histórico
git log --oneline

# Ver diferenças
git diff

# Ver branches
git branch -a

# Mudar de branch
git checkout nome-da-branch

# Criar nova branch
git checkout -b nova-branch

# Fazer merge
git merge nome-da-branch

# Fazer rebase
git rebase nome-da-branch

# Ver remotes
git remote -v

# Fazer pull
git pull origin main

# Fazer push
git push origin nome-da-branch

# Ver configurações
git config --list

# Desfazer último commit (mantendo alterações)
git reset --soft HEAD~1

# Desfazer último commit (perdendo alterações)
git reset --hard HEAD~1
```

### **11. Workflow de Desenvolvimento**

#### **Para Nova Funcionalidade:**
```bash
# 1. Criar feature branch
git flow feature start nova-funcionalidade

# 2. Desenvolver
# ... fazer alterações ...

# 3. Commit
git add .
git commit -m "feat: implementa nova funcionalidade"

# 4. Push para GitHub
git push origin feature/nova-funcionalidade

# 5. Criar Pull Request no GitHub
# 6. Após aprovação, finalizar feature
git flow feature finish nova-funcionalidade
```

#### **Para Correção Urgente:**
```bash
# 1. Criar hotfix
git flow hotfix start correcao-urgente

# 2. Corrigir
# ... fazer correções ...

# 3. Commit
git add .
git commit -m "fix: corrige bug crítico"

# 4. Finalizar hotfix
git flow hotfix finish correcao-urgente
```

### **12. Configuração de Hooks (Opcional)**

```bash
# Criar diretório de hooks
mkdir -p .git/hooks

# Hook de pre-commit (verificar código antes do commit)
cat > .git/hooks/pre-commit << 'EOF'
#!/bin/bash
echo "🔍 Executando verificações de código..."

# Executar checkstyle
./mvnw checkstyle:check
if [ $? -ne 0 ]; then
    echo "❌ Checkstyle falhou!"
    exit 1
fi

# Executar testes
./mvnw test
if [ $? -ne 0 ]; then
    echo "❌ Testes falharam!"
    exit 1
fi

echo "✅ Todas as verificações passaram!"
EOF

# Tornar executável
chmod +x .git/hooks/pre-commit
```

### **13. Troubleshooting**

#### **Problema: "Permission denied (publickey)"**
```bash
# Gerar chave SSH
ssh-keygen -t ed25519 -C "seu.email@exemplo.com"

# Adicionar chave ao ssh-agent
eval "$(ssh-agent -s)"
ssh-add ~/.ssh/id_ed25519

# Copiar chave pública
cat ~/.ssh/id_ed25519.pub

# Adicionar no GitHub: Settings → SSH and GPG keys
```

#### **Problema: "Repository not found"**
```bash
# Verificar remote
git remote -v

# Corrigir URL se necessário
git remote set-url origin https://github.com/SEU-USUARIO/taskday.git
```

#### **Problema: "Merge conflicts"**
```bash
# Ver arquivos em conflito
git status

# Resolver conflitos manualmente
# ... editar arquivos ...

# Adicionar arquivos resolvidos
git add arquivo-resolvido.java

# Finalizar merge
git commit -m "resolve: merge conflicts"
```

---

## 🎯 **Checklist de Configuração**

- [ ] Git configurado com nome e email
- [ ] Repositório inicializado
- [ ] Primeiro commit feito
- [ ] Repositório GitHub criado
- [ ] Remote origin configurado
- [ ] Push inicial realizado
- [ ] Git flow inicializado
- [ ] Secrets configurados no GitHub
- [ ] Branch protection rules ativadas
- [ ] Hooks configurados (opcional)

---

*Guia criado em 21/10/2025 - TaskDay Git Setup*
