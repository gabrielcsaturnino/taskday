# 🚀 Tutorial Completo: CI/CD para TaskDay

## 📋 **O que é CI/CD?**

**CI (Continuous Integration)** = Integração Contínua
- Automatiza testes e builds quando código é enviado
- Detecta problemas rapidamente
- Mantém código sempre funcional

**CD (Continuous Deployment)** = Deploy Contínuo
- Automatiza o deploy da aplicação
- Entrega rápida de novas funcionalidades
- Reduz erros manuais

## 🏗️ **Arquitetura do Pipeline**

```
Git Push → GitHub Actions → Build → Test → Docker Build → Deploy
    ↓
1. Lint & Format Check
2. Unit Tests
3. Integration Tests
4. Security Scan
5. Build Application
6. Build Docker Image
7. Push to Registry
8. Deploy to Environment
```

---

## 🔧 **Configuração do Ambiente**

### **1. Estrutura de Branches (Git Flow)**

```
main (produção)
├── develop (desenvolvimento)
├── feature/nova-funcionalidade
├── hotfix/correcao-urgente
└── release/versao-1.0.0
```

### **2. Configuração do Git**

```bash
# Configurar usuário
git config --global user.name "Seu Nome"
git config --global user.email "seu.email@exemplo.com"

# Inicializar repositório
git init
git remote add origin https://github.com/seu-usuario/taskday.git
```

---

## 🐳 **Dockerfile Otimizado para Produção**

Vou criar um Dockerfile multi-stage para otimizar o build:

```dockerfile
# Multi-stage build para otimização
FROM maven:3.9.9-openjdk-17-slim AS builder

WORKDIR /app
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies (cache layer)
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jre-slim

WORKDIR /app

# Criar usuário não-root para segurança
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Copiar JAR do build stage
COPY --from=builder /app/target/*.jar app.jar

# Configurar permissões
RUN chown -R appuser:appuser /app
USER appuser

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## ⚙️ **GitHub Actions - Pipeline Completo**

### **1. Workflow Principal (.github/workflows/ci-cd.yml)**

```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]

env:
  REGISTRY: ghcr.io
  IMAGE_NAME: ${{ github.repository }}

jobs:
  # Job 1: Análise de Código
  code-quality:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Cache Maven dependencies
        uses: actions/cache@v3
        with:
          path: ~/.m2
          key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
          restore-keys: ${{ runner.os }}-m2

      - name: Run SpotBugs
        run: ./mvnw spotbugs:check

      - name: Run Checkstyle
        run: ./mvnw checkstyle:check

      - name: Generate JaCoCo Report
        run: ./mvnw jacoco:report

      - name: Upload coverage to Codecov
        uses: codecov/codecov-action@v3
        with:
          file: ./target/site/jacoco/jacoco.xml

  # Job 2: Testes
  test:
    runs-on: ubuntu-latest
    services:
      postgres:
        image: postgres:15
        env:
          POSTGRES_PASSWORD: postgres
          POSTGRES_DB: taskday_test
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5
        ports:
          - 5432:5432

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Cache Maven dependencies
        uses: actions/cache@v3
        with:
          path: ~/.m2
          key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}

      - name: Run unit tests
        run: ./mvnw test
        env:
          SPRING_PROFILES_ACTIVE: test
          SPRING_DATASOURCE_URL: jdbc:postgresql://localhost:5432/taskday_test
          SPRING_DATASOURCE_USERNAME: postgres
          SPRING_DATASOURCE_PASSWORD: postgres

      - name: Run integration tests
        run: ./mvnw verify
        env:
          SPRING_PROFILES_ACTIVE: test
          SPRING_DATASOURCE_URL: jdbc:postgresql://localhost:5432/taskday_test
          SPRING_DATASOURCE_USERNAME: postgres
          SPRING_DATASOURCE_PASSWORD: postgres

  # Job 3: Security Scan
  security:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Run Trivy vulnerability scanner
        uses: aquasecurity/trivy-action@master
        with:
          scan-type: 'fs'
          scan-ref: '.'
          format: 'sarif'
          output: 'trivy-results.sarif'

      - name: Upload Trivy scan results
        uses: github/codeql-action/upload-sarif@v2
        with:
          sarif_file: 'trivy-results.sarif'

  # Job 4: Build e Deploy (apenas na branch main)
  build-and-deploy:
    needs: [code-quality, test, security]
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Cache Maven dependencies
        uses: actions/cache@v3
        with:
          path: ~/.m2
          key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}

      - name: Build application
        run: ./mvnw clean package -DskipTests

      - name: Log in to Container Registry
        uses: docker/login-action@v3
        with:
          registry: ${{ env.REGISTRY }}
          username: ${{ github.actor }}
          password: ${{ secrets.GITHUB_TOKEN }}

      - name: Extract metadata
        id: meta
        uses: docker/metadata-action@v5
        with:
          images: ${{ env.REGISTRY }}/${{ env.IMAGE_NAME }}
          tags: |
            type=ref,event=branch
            type=ref,event=pr
            type=sha,prefix={{branch}}-
            type=raw,value=latest,enable={{is_default_branch}}

      - name: Build and push Docker image
        uses: docker/build-push-action@v5
        with:
          context: .
          push: true
          tags: ${{ steps.meta.outputs.tags }}
          labels: ${{ steps.meta.outputs.labels }}

      - name: Deploy to staging
        run: |
          echo "Deploying to staging environment..."
          # Aqui você adicionaria comandos para deploy real
          # Exemplo: kubectl apply -f k8s/staging/
          # Ou: docker-compose -f docker-compose.staging.yml up -d

      - name: Run smoke tests
        run: |
          echo "Running smoke tests..."
          # Testes básicos para verificar se a aplicação está funcionando
          # curl -f http://staging.taskday.com/actuator/health

      - name: Deploy to production
        if: success()
        run: |
          echo "Deploying to production..."
          # Deploy para produção após testes de staging passarem
```

---

## 🔒 **Configuração de Segurança**

### **1. Secrets do GitHub**

Configure estes secrets no GitHub (Settings → Secrets and variables → Actions):

```
DATABASE_URL=postgresql://user:pass@host:5432/db
JWT_SECRET=sua-chave-secreta-jwt
MAIL_USERNAME=seu-email@gmail.com
MAIL_PASSWORD=sua-senha-app
```

### **2. Arquivo de Segurança (.github/dependabot.yml)**

```yaml
version: 2
updates:
  - package-ecosystem: "maven"
    directory: "/"
    schedule:
      interval: "weekly"
    open-pull-requests-limit: 10

  - package-ecosystem: "docker"
    directory: "/"
    schedule:
      interval: "weekly"
```

---

## 📊 **Monitoramento e Métricas**

### **1. Workflow de Monitoramento (.github/workflows/monitoring.yml)**

```yaml
name: Monitoring

on:
  schedule:
    - cron: '0 */6 * * *'  # A cada 6 horas
  workflow_dispatch:

jobs:
  health-check:
    runs-on: ubuntu-latest
    steps:
      - name: Check application health
        run: |
          curl -f https://taskday.com/actuator/health || exit 1
          
      - name: Check database connectivity
        run: |
          # Verificar conectividade com banco
          echo "Database health check"
          
      - name: Send notification on failure
        if: failure()
        uses: 8398a7/action-slack@v3
        with:
          status: failure
          webhook_url: ${{ secrets.SLACK_WEBHOOK }}
```

---

## 🚀 **Scripts de Deploy**

### **1. Script de Deploy Local (deploy.sh)**

```bash
#!/bin/bash

# Script de deploy local
set -e

echo "🚀 Iniciando deploy do TaskDay..."

# Verificar se Docker está rodando
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker não está rodando!"
    exit 1
fi

# Parar containers existentes
echo "🛑 Parando containers existentes..."
docker-compose down

# Fazer pull das imagens mais recentes
echo "📥 Fazendo pull das imagens..."
docker-compose pull

# Subir os serviços
echo "⬆️ Subindo serviços..."
docker-compose up -d

# Aguardar aplicação ficar pronta
echo "⏳ Aguardando aplicação ficar pronta..."
sleep 30

# Verificar saúde da aplicação
echo "🏥 Verificando saúde da aplicação..."
if curl -f http://localhost:8080/actuator/health; then
    echo "✅ Deploy realizado com sucesso!"
    echo "🌐 Aplicação disponível em: http://localhost:8080"
    echo "📚 Swagger UI: http://localhost:8080/swagger-ui/index.html"
    echo "🗄️ pgAdmin: http://localhost:8081"
else
    echo "❌ Falha no deploy - aplicação não está respondendo"
    exit 1
fi
```

### **2. Script de Rollback (rollback.sh)**

```bash
#!/bin/bash

echo "🔄 Iniciando rollback..."

# Fazer backup do estado atual
echo "💾 Fazendo backup..."
docker-compose exec postgres pg_dump -U gabriel taskdaydb > backup_$(date +%Y%m%d_%H%M%S).sql

# Voltar para versão anterior
echo "⬅️ Voltando para versão anterior..."
git checkout HEAD~1

# Rebuild e restart
echo "🔨 Rebuild da aplicação..."
docker-compose down
docker-compose up -d --build

echo "✅ Rollback concluído!"
```

---

## 📋 **Checklist de Deploy**

### **Antes do Deploy:**
- [ ] Todos os testes passando
- [ ] Código revisado
- [ ] Documentação atualizada
- [ ] Backup do banco de dados
- [ ] Variáveis de ambiente configuradas

### **Durante o Deploy:**
- [ ] Monitorar logs da aplicação
- [ ] Verificar saúde dos serviços
- [ ] Testar endpoints críticos
- [ ] Verificar conectividade com banco

### **Após o Deploy:**
- [ ] Smoke tests executados
- [ ] Monitoramento ativo
- [ ] Notificação de sucesso
- [ ] Documentação de incidentes (se houver)

---

## 🔧 **Comandos Úteis**

### **Git Workflow:**
```bash
# Criar feature branch
git checkout -b feature/nova-funcionalidade

# Fazer commit
git add .
git commit -m "feat: adiciona nova funcionalidade"

# Push para GitHub
git push origin feature/nova-funcionalidade

# Criar Pull Request no GitHub
# Após aprovação, merge para develop/main
```

### **Docker Commands:**
```bash
# Build local
docker build -t taskday:latest .

# Run local
docker run -p 8080:8080 taskday:latest

# Ver logs
docker-compose logs -f app

# Executar testes
docker-compose exec app ./mvnw test
```

### **Monitoramento:**
```bash
# Verificar saúde
curl http://localhost:8080/actuator/health

# Ver métricas
curl http://localhost:8080/actuator/metrics

# Ver logs da aplicação
docker-compose logs -f app
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

*Tutorial criado em 21/10/2025 - TaskDay CI/CD Pipeline*
