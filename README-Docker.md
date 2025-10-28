# Jooby - Docker Setup

Este documento explica como configurar e executar o Jooby usando Docker.

## 🐳 Serviços Incluídos

### 1. PostgreSQL Database
- **Imagem**: postgres:15-alpine
- **Porta**: 5432
- **Database**: joobydb
- **Usuário**: gabriel
- **Senha**: 123

### 2. pgAdmin
- **Imagem**: dpage/pgadmin4:latest
- **Porta**: 8081
- **Email**: admin@jooby.com
- **Senha**: admin123

### 3. Jooby Application (Opcional)
- **Porta**: 8080
- **Build**: Dockerfile personalizado

## 🚀 Como Executar

### Opção 1: Apenas Banco de Dados e pgAdmin

```bash
# Iniciar PostgreSQL e pgAdmin
docker-compose up postgres pgadmin -d

# Verificar se os serviços estão rodando
docker-compose ps
```

### Opção 2: Aplicação Completa (Incluindo Spring Boot)

```bash
# Iniciar todos os serviços
docker-compose --profile app up -d

# Verificar se todos os serviços estão rodando
docker-compose ps
```

### Opção 3: Apenas para Desenvolvimento (sem a aplicação)

```bash
# Iniciar apenas banco e pgAdmin
docker-compose up postgres pgadmin -d

# Executar a aplicação localmente
./mvnw spring-boot:run
```

## 📊 Acessando os Serviços

### PostgreSQL
```bash
# Conectar via psql
docker exec -it jooby-postgres psql -U gabriel -d joobydb

# Ou via cliente externo
Host: localhost
Port: 5432
Database: joobydb
Username: gabriel
Password: 123
```

### pgAdmin
1. Acesse: http://localhost:8081
2. Login: admin@jooby.com
3. Senha: admin123
4. Adicionar servidor:
   - **Nome**: Jooby DB
   - **Host**: postgres
   - **Port**: 5432
   - **Database**: joobydb
   - **Username**: gabriel
   - **Password**: 123

### Jooby API (se rodando via Docker)
- **API**: http://localhost:8080
- **Swagger**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/actuator/health

## 🛠️ Comandos Úteis

### Gerenciar Containers
```bash
# Parar todos os serviços
docker-compose down

# Parar e remover volumes (CUIDADO: apaga dados!)
docker-compose down -v

# Ver logs
docker-compose logs -f postgres
docker-compose logs -f jooby-app

# Rebuild da aplicação
docker-compose build jooby-app
```

### Backup e Restore
```bash
# Backup do banco
docker exec jooby-postgres pg_dump -U gabriel joobydb > backup.sql

# Restore do banco
docker exec -i jooby-postgres psql -U gabriel joobydb < backup.sql
```

### Limpar Dados
```bash
# Remover todos os containers e volumes
docker-compose down -v
docker system prune -f

# Remover imagens
docker rmi $(docker images -q)
```

## 📁 Estrutura de Arquivos

```
jooby/
├── docker-compose.yml              # Configuração principal
├── docker-compose.override.yml     # Override para desenvolvimento
├── Dockerfile                      # Build da aplicação
├── init-db/
│   └── 01-init.sql                 # Script de inicialização do banco
├── env.example                     # Exemplo de variáveis de ambiente
└── README-Docker.md               # Este arquivo
```

## 🔧 Configurações Personalizadas

### Variáveis de Ambiente
Copie o arquivo `env.example` para `.env` e configure:

```bash
cp env.example .env
```

### Modificar Portas
Edite o arquivo `docker-compose.yml`:

```yaml
services:
  postgres:
    ports:
      - "5433:5432"  # Muda porta externa para 5433
```

### Adicionar Dados de Teste
Edite o arquivo `init-db/01-init.sql` para adicionar mais dados de exemplo.

## 🐛 Troubleshooting

### Problema: Porta já em uso
```bash
# Verificar qual processo está usando a porta
sudo netstat -tulpn | grep :5432

# Matar o processo
sudo kill -9 <PID>
```

### Problema: Container não inicia
```bash
# Ver logs detalhados
docker-compose logs postgres

# Rebuild sem cache
docker-compose build --no-cache
```

### Problema: Banco não conecta
```bash
# Verificar se o PostgreSQL está rodando
docker-compose ps postgres

# Verificar logs do PostgreSQL
docker-compose logs postgres

# Testar conexão
docker exec jooby-postgres pg_isready -U gabriel
```

## 📈 Monitoramento

### Health Checks
```bash
# Verificar status dos serviços
docker-compose ps

# Verificar health do PostgreSQL
docker exec jooby-postgres pg_isready -U gabriel -d joobydb
```

### Logs em Tempo Real
```bash
# Todos os serviços
docker-compose logs -f

# Serviço específico
docker-compose logs -f postgres
```

## 🔒 Segurança

### Para Produção
1. **Altere as senhas padrão** no `docker-compose.yml`
2. **Use variáveis de ambiente** para senhas sensíveis
3. **Configure firewall** para restringir acesso às portas
4. **Use SSL/TLS** para conexões de banco
5. **Configure backup automático**

### Exemplo de Configuração Segura
```yaml
services:
  postgres:
    environment:
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      POSTGRES_USER: ${POSTGRES_USER}
```

---

**Jooby Docker Setup** - Desenvolvido com ❤️ para facilitar o desenvolvimento!

