# Jooby - Plataforma de Freelancers

Uma plataforma completa para conectar clientes e freelancers, desenvolvida com Spring Boot, React Web e React Native.

## 🚀 Início Rápido

### Pré-requisitos
- Java 17+
- Node.js 18+
- Docker & Docker Compose
- PostgreSQL (ou use Docker)

### Setup Completo
```bash
# 1. Clone o repositório
git clone <repository-url>
cd jooby

# 2. Setup inicial (instala dependências e configura ambiente)
./scripts/setup.sh init

# 3. Iniciar todo o ecossistema
./scripts/start-all.sh
```

### URLs de Acesso
- **Frontend Web**: http://localhost:3000
- **Backend API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **pgAdmin**: http://localhost:8081

## 🛠️ Desenvolvimento

### Backend (Spring Boot)
```bash
# Executar apenas o backend
./mvnw spring-boot:run

# Executar testes
./mvnw test

# Build
./mvnw clean package
```

### Frontend Web (React)
```bash
# Executar apenas o frontend web
./scripts/dev-web.sh

# Ou manualmente
cd jooby-web
npm install
npm start
```

### Mobile (React Native)
```bash
# Setup inicial do mobile
./scripts/mobile-dev.sh setup

# Iniciar ambiente mobile
./scripts/mobile-dev.sh start

# Executar no Android
./scripts/mobile-dev.sh android

# Executar no iOS
./scripts/mobile-dev.sh ios
```

## 🐳 Docker

### Desenvolvimento Completo
```bash
# Iniciar todos os serviços
docker-compose up -d

# Verificar status
docker-compose ps

# Parar serviços
docker-compose down
```

### Apenas Mobile
```bash
# Ambiente mobile completo
docker-compose -f docker-compose.mobile.yml up -d
```

## 📊 Banco de Dados

O banco PostgreSQL é inicializado automaticamente com:
- Schema completo criado via Flyway migrations
- Dados de exemplo para testes
- Triggers para timestamps automáticos

### Reset do Banco
```bash
# Parar e remover volumes (CUIDADO: apaga dados!)
docker-compose down -v
docker-compose up postgres -d
```

## 🧪 Testes

```bash
# Todos os testes
./mvnw test

# Testes de integração
./mvnw test -Dtest=*IT

# Testes unitários
./mvnw test -Dtest=*Test
```

## 📱 Tecnologias

### Backend
- Spring Boot 3.2.0
- Spring Security + JWT
- PostgreSQL + Flyway
- Docker

### Frontend Web
- React 19 + TypeScript
- Styled Components
- React Router

### Mobile
- React Native + TypeScript
- Metro Bundler

## 🔧 Scripts Úteis

```bash
# Criar nova feature
./scripts/create-feature.sh nome-da-feature

# Criar bugfix
./scripts/create-bugfix.sh nome-do-bugfix

# Gerenciar banco de dados
./scripts/init-db.sh status
./scripts/init-db.sh reset

# Deploy
./scripts/deploy.sh

# Teste de integração
./scripts/test-integration.sh
```

## 📚 Documentação Adicional

- [Configuração Docker](README-Docker.md)
- [CI/CD](README-CICD.md)
- [Workflow Git](GIT-WORKFLOW.md)

---

**Jooby** - Conectando talentos e oportunidades! 🚀
