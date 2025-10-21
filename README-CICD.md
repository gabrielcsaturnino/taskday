# 🚀 CI/CD Pipeline - TaskDay

## 📋 **Resumo do que foi configurado**

### ✅ **Arquivos Criados/Modificados:**

1. **Tutorial Completo:** `TUTORIAL-CICD.md`
2. **Configuração Git:** `GIT-SETUP.md`
3. **Dockerfile Otimizado:** `Dockerfile`
4. **GitHub Actions:** `.github/workflows/ci-cd.yml`
5. **Monitoramento:** `.github/workflows/monitoring.yml`
6. **Dependabot:** `.github/dependabot.yml`
7. **Scripts de Deploy:** `scripts/deploy.sh`, `scripts/rollback.sh`, `scripts/deploy-prod.sh`
8. **Docker Compose Produção:** `docker-compose.prod.yml`
9. **Nginx Config:** `nginx/nginx.conf`
10. **Checkstyle:** `checkstyle.xml`
11. **Configurações de Ambiente:** `application-prod.properties`, `application-test.properties`
12. **Variáveis de Ambiente:** `env.prod.example`
13. **Git Ignore:** `.gitignore`
14. **Maven Plugins:** Atualizado `pom.xml` com plugins de qualidade

---

## 🔧 **Como Usar o Pipeline**

### **1. Configuração Inicial**

```bash
# 1. Configurar Git
git config --global user.name "Seu Nome"
git config --global user.email "seu.email@exemplo.com"

# 2. Inicializar repositório
cd /home/gabriel/Documentos/taskday
git init
git add .
git commit -m "feat: configuração inicial com CI/CD"

# 3. Criar repositório no GitHub e conectar
git remote add origin https://github.com/SEU-USUARIO/taskday.git
git push -u origin main
```

### **2. Configurar Secrets no GitHub**

Vá em **Settings** → **Secrets and variables** → **Actions** e adicione:

```
DATABASE_URL=postgresql://user:pass@host:5432/db
JWT_SECRET=sua-chave-secreta-jwt
MAIL_USERNAME=seu-email@gmail.com
MAIL_PASSWORD=sua-senha-app
SLACK_WEBHOOK=https://hooks.slack.com/services/...
```

### **3. Fluxo de Desenvolvimento**

```bash
# Criar feature branch
git flow feature start nova-funcionalidade

# Desenvolver
# ... fazer alterações ...

# Commit
git add .
git commit -m "feat: implementa nova funcionalidade"

# Push
git push origin feature/nova-funcionalidade

# Criar Pull Request no GitHub
# Após aprovação, merge automático
```

---

## 🐳 **Docker Commands**

### **Desenvolvimento:**
```bash
# Subir ambiente de desenvolvimento
docker-compose up -d

# Ver logs
docker-compose logs -f app

# Parar
docker-compose down
```

### **Produção:**
```bash
# Configurar variáveis
cp env.prod.example .env.prod
nano .env.prod

# Deploy de produção
./scripts/deploy-prod.sh

# Verificar status
docker-compose -f docker-compose.prod.yml ps
```

---

## 🔍 **Pipeline de CI/CD**

### **O que acontece automaticamente:**

1. **Push para `develop` ou `main`:**
   - ✅ Análise de código (SpotBugs, Checkstyle)
   - ✅ Testes unitários e integração
   - ✅ Scan de segurança (Trivy)
   - ✅ Build da aplicação
   - ✅ Build da imagem Docker
   - ✅ Push para GitHub Container Registry

2. **Push para `main` (produção):**
   - ✅ Deploy automático para staging
   - ✅ Smoke tests
   - ✅ Deploy para produção (se tudo OK)

### **Monitoramento:**
- ✅ Health checks a cada 6 horas
- ✅ Notificações no Slack em caso de falha
- ✅ Métricas e logs centralizados

---

## 📊 **Qualidade de Código**

### **Plugins Configurados:**
- **JaCoCo:** Cobertura de testes
- **SpotBugs:** Análise estática de bugs
- **Checkstyle:** Padrões de código
- **Surefire:** Testes unitários
- **Failsafe:** Testes de integração

### **Comandos Locais:**
```bash
# Executar todos os testes
./mvnw clean test

# Verificar qualidade de código
./mvnw checkstyle:check
./mvnw spotbugs:check

# Gerar relatório de cobertura
./mvnw jacoco:report
```

---

## 🚀 **Deploy Manual**

### **Desenvolvimento:**
```bash
# Deploy local
./scripts/deploy.sh
```

### **Produção:**
```bash
# Deploy de produção
./scripts/deploy-prod.sh

# Rollback se necessário
./scripts/rollback.sh
```

---

## 🔧 **Troubleshooting**

### **Problema: Pipeline falha**
```bash
# Ver logs do GitHub Actions
# Vá em Actions → Seu workflow → Ver detalhes

# Testar localmente
./mvnw clean test
./mvnw checkstyle:check
./mvnw spotbugs:check
```

### **Problema: Docker não inicia**
```bash
# Verificar se Docker está rodando
docker info

# Limpar containers antigos
docker-compose down
docker system prune -f

# Rebuild
docker-compose up -d --build
```

### **Problema: Banco de dados**
```bash
# Verificar logs do PostgreSQL
docker-compose logs postgres

# Conectar ao banco
docker-compose exec postgres psql -U gabriel -d taskdaydb
```

---

## 📈 **Próximos Passos**

1. **Configurar ambiente de staging**
2. **Implementar testes de carga**
3. **Configurar alertas de monitoramento**
4. **Implementar blue-green deployment**
5. **Configurar backup automático**
6. **Implementar feature flags**

---

## 🎯 **Checklist de Configuração**

- [ ] Git configurado
- [ ] Repositório GitHub criado
- [ ] Secrets configurados
- [ ] Branch protection rules ativadas
- [ ] Pipeline funcionando
- [ ] Deploy de desenvolvimento funcionando
- [ ] Deploy de produção funcionando
- [ ] Monitoramento ativo

---

## 📞 **Suporte**

- **Documentação:** `TUTORIAL-CICD.md`
- **Git Setup:** `GIT-SETUP.md`
- **Changelog:** `CHANGELOG.md`
- **Docker:** `README-Docker.md`

---

*Pipeline CI/CD configurado em 21/10/2025 - TaskDay*
