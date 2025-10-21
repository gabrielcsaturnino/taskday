# TaskDay - Plataforma de Freelancers

Uma plataforma completa para conectar clientes e freelancers, desenvolvida com Spring Boot.

## 🚀 Funcionalidades Implementadas

### ✅ **Melhorias de Infraestrutura**
- **Spring Boot 3.2.0** (atualizado da versão 4.0.0-SNAPSHOT)
- **Dependências atualizadas** e organizadas
- **Flyway habilitado** para migrações de banco
- **Cache configurado** com Spring Cache
- **OpenAPI/Swagger** para documentação da API
- **Actuator** para monitoramento
- **Configuração de email** para notificações
- **Upload de arquivos** configurado

### ✅ **Segurança Melhorada**
- **JWT com expiração** configurada
- **Endpoints públicos** bem definidos
- **Autorização por recurso** implementada
- **Configuração de CORS** adequada

### ✅ **Entidades e DTOs**
- **Enums separados** em arquivos próprios
- **DTOs de Response, Update e Search** criados
- **Validações melhoradas** com Caelum Stella para CPF
- **Campos de auditoria** (created_at, updated_at)

### ✅ **Services Aprimorados**

#### **ClientService**
- ✅ `updateClient()` - Atualizar perfil do cliente
- ✅ `changePassword()` - Alterar senha
- ✅ `deactivateAccount()` / `activateAccount()` - Gerenciar conta
- ✅ `findById()` - Buscar cliente por ID
- ✅ Validações de unicidade de email/telefone

#### **ContractorService**
- ✅ `updateContractor()` - Atualizar perfil do contratante
- ✅ `changePassword()` - Alterar senha
- ✅ `deactivateAccount()` / `activateAccount()` - Gerenciar conta
- ✅ `searchContractors()` - Buscar contratantes
- ✅ Cache implementado com `@Cacheable` e `@CacheEvict`

#### **JobService**
- ✅ `updateJob()` - Atualizar job
- ✅ `deleteJob()` - Deletar job (soft delete)
- ✅ `closeJob()` - Fechar job
- ✅ `searchJobs()` - Buscar jobs com filtros
- ✅ `findJobsByLocation()` - Buscar por localização
- ✅ `findJobsByPriceRange()` - Buscar por faixa de preço

#### **MessageService**
- ✅ `markAsRead()` - Marcar mensagens como lidas
- ✅ `findUnreadMessages()` - Buscar mensagens não lidas
- ✅ `deleteMessage()` - Deletar mensagem

### ✅ **Controllers Completos**

#### **ClientController**
- ✅ `GET /api/v1/clients/{id}` - Buscar cliente por ID
- ✅ `PUT /api/v1/clients/profile` - Atualizar perfil
- ✅ `PUT /api/v1/clients/password` - Alterar senha
- ✅ `PUT /api/v1/clients/{id}/deactivate` - Desativar conta
- ✅ `PUT /api/v1/clients/{id}/activate` - Ativar conta

#### **ContractorController**
- ✅ `GET /api/v1/contractors/{id}` - Buscar contratante por ID
- ✅ `GET /api/v1/contractors/search` - Buscar contratantes
- ✅ `PUT /api/v1/contractors/profile` - Atualizar perfil
- ✅ `PUT /api/v1/contractors/password` - Alterar senha
- ✅ `PUT /api/v1/contractors/{id}/deactivate` - Desativar conta
- ✅ `PUT /api/v1/contractors/{id}/activate` - Ativar conta

#### **JobController**
- ✅ `PUT /api/v1/jobs/{id}` - Atualizar job
- ✅ `DELETE /api/v1/jobs/{id}` - Deletar job
- ✅ `PUT /api/v1/jobs/{id}/close` - Fechar job
- ✅ `GET /api/v1/jobs/search` - Buscar jobs
- ✅ `GET /api/v1/jobs/active` - Jobs ativos

### ✅ **Novos Services e Controllers**

#### **SearchService & SearchController**
- ✅ Busca avançada de jobs
- ✅ Busca avançada de contratantes
- ✅ Filtros por localização, preço, rating

#### **NotificationService**
- ✅ Envio de emails
- ✅ Notificações de candidatura
- ✅ Notificações de aceitação/rejeição

#### **FileService & FileController**
- ✅ Upload de arquivos
- ✅ Download de arquivos
- ✅ Exclusão de arquivos

#### **MetricsController**
- ✅ Métricas do dashboard
- ✅ Estatísticas de aplicações
- ✅ Métricas de jobs ativos

### ✅ **Testes Implementados**
- ✅ **ClientServiceTest** - Testes unitários do ClientService
- ✅ **JobApplicationIT** - Testes de integração de candidaturas
- ✅ **Testes existentes** mantidos e melhorados

### ✅ **Configurações Avançadas**
- ✅ **CacheConfig** - Configuração de cache
- ✅ **OpenApiConfig** - Documentação da API
- ✅ **Application.properties** - Configurações completas
- ✅ **Logging** configurado adequadamente

## 🛠️ Tecnologias Utilizadas

- **Spring Boot 3.2.0**
- **Spring Security** com JWT
- **Spring Data JPA**
- **PostgreSQL**
- **Flyway** para migrações
- **Spring Cache**
- **Spring Mail**
- **OpenAPI/Swagger**
- **Caelum Stella** para validação de CPF
- **JUnit 5** para testes
- **Mockito** para mocks

## 📋 Endpoints da API

### **Autenticação**
- `POST /authenticate` - Login

### **Clientes**
- `POST /api/v1/clients` - Criar cliente
- `GET /api/v1/clients/profile` - Perfil do cliente logado
- `GET /api/v1/clients/{id}` - Buscar cliente por ID
- `PUT /api/v1/clients/profile` - Atualizar perfil
- `PUT /api/v1/clients/password` - Alterar senha
- `PUT /api/v1/clients/{id}/deactivate` - Desativar conta
- `PUT /api/v1/clients/{id}/activate` - Ativar conta

### **Contratantes**
- `POST /api/v1/contractors` - Criar contratante
- `GET /api/v1/contractors/profile` - Perfil do contratante logado
- `GET /api/v1/contractors/{id}` - Buscar contratante por ID
- `GET /api/v1/contractors/search` - Buscar contratantes
- `PUT /api/v1/contractors/profile` - Atualizar perfil
- `PUT /api/v1/contractors/password` - Alterar senha
- `PUT /api/v1/contractors/{id}/deactivate` - Desativar conta
- `PUT /api/v1/contractors/{id}/activate` - Ativar conta

### **Jobs**
- `POST /api/v1/jobs` - Criar job
- `GET /api/v1/jobs/{id}` - Buscar job por ID
- `GET /api/v1/jobs/client/{clientId}` - Jobs de um cliente
- `GET /api/v1/jobs/my-jobs` - Meus jobs
- `GET /api/v1/jobs/search` - Buscar jobs
- `GET /api/v1/jobs/active` - Jobs ativos
- `PUT /api/v1/jobs/{id}` - Atualizar job
- `DELETE /api/v1/jobs/{id}` - Deletar job
- `PUT /api/v1/jobs/{id}/close` - Fechar job

### **Candidaturas**
- `POST /api/v1/job-applications/apply/{jobId}` - Candidatar-se
- `GET /api/v1/job-applications/{id}` - Buscar candidatura
- `PUT /api/v1/job-applications/{id}/status` - Atualizar status

### **Chat**
- `GET /api/v1/chat-rooms/{id}` - Buscar chat room
- `GET /api/v1/chat-rooms/{id}/messages` - Mensagens do chat
- `PUT /api/v1/chat-rooms/{id}/status` - Atualizar status
- `GET /api/v1/chat-rooms/my-chats` - Meus chats

### **Busca**
- `GET /api/v1/search/jobs` - Buscar jobs
- `GET /api/v1/search/contractors` - Buscar contratantes

### **Arquivos**
- `POST /api/v1/files/upload` - Upload de arquivo
- `GET /api/v1/files/{filename}` - Download de arquivo
- `DELETE /api/v1/files/{filename}` - Deletar arquivo

### **Métricas**
- `GET /api/v1/metrics/dashboard` - Métricas do dashboard
- `GET /api/v1/metrics/jobs/active` - Métricas de jobs ativos

## 🚀 Como Executar

1. **Configurar PostgreSQL**
   ```bash
   # Criar banco de dados
   createdb taskdaydb
   ```

2. **Configurar variáveis de ambiente**
   ```bash
   export MAIL_USERNAME=seu-email@gmail.com
   export MAIL_PASSWORD=sua-senha-app
   ```

3. **Executar a aplicação**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Acessar a documentação**
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - API Docs: http://localhost:8080/api-docs

## 📊 Monitoramento

- **Health Check**: http://localhost:8080/actuator/health
- **Métricas**: http://localhost:8080/actuator/metrics
- **Info**: http://localhost:8080/actuator/info

## 🧪 Testes

```bash
# Executar todos os testes
./mvnw test

# Executar testes de integração
./mvnw test -Dtest=*IT

# Executar testes unitários
./mvnw test -Dtest=*Test
```

## 📈 Melhorias Implementadas

### **Performance**
- ✅ Cache implementado nos services
- ✅ Queries otimizadas
- ✅ Paginação preparada

### **Segurança**
- ✅ JWT com expiração
- ✅ Validações robustas
- ✅ Autorização por recurso

### **Manutenibilidade**
- ✅ Código bem estruturado
- ✅ DTOs separados
- ✅ Enums organizados
- ✅ Testes abrangentes

### **Funcionalidades**
- ✅ CRUD completo
- ✅ Busca avançada
- ✅ Notificações por email
- ✅ Upload de arquivos
- ✅ Métricas e dashboard

## 🎯 Próximos Passos Sugeridos

1. **Implementar paginação** nos endpoints de listagem
2. **Adicionar filtros avançados** nas buscas
3. **Implementar sistema de avaliações**
4. **Adicionar geolocalização** para jobs
5. **Implementar sistema de pagamentos**
6. **Adicionar notificações push**
7. **Implementar relatórios avançados**

---

**TaskDay** - Conectando talentos e oportunidades! 🚀
