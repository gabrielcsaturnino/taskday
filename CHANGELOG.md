# Changelog - Jooby

## [2.0.0] - 2024-12-19

### 🎉 Nova Marca: Jooby
- **BREAKING CHANGE**: Renomeação completa de "TaskDay" para "Jooby"
- Atualização de todos os arquivos de configuração e documentação
- Renomeação de containers Docker
- Atualização de banco de dados (taskdaydb → joobydb)
- Renomeação de pacotes Java (com.example.taskday → com.example.jooby)

### 🌐 Frontend Web
- **Adicionado**: Frontend web em React + TypeScript
- **Tecnologias**: React 19, TypeScript, Styled Components, React Router
- **Funcionalidades**: Autenticação, Dashboard, Jobs, Perfil

### 📱 Mobile Atualizado
- **Renomeado**: TaskDayMobile → JoobyMobile
- **Atualizado**: Configurações e referências

### 🔧 Backend Atualizado
- **Renomeado**: TaskdayApplication → JoobyApplication
- **Atualizado**: Estrutura de pacotes Java
- **Banco**: taskdaydb → joobydb

### 🚀 Scripts e Documentação
- **Simplificado**: README principal focado no essencial
- **Atualizado**: Scripts de desenvolvimento
- **Melhorado**: Documentação consolidada

## [1.0.0] - Versão Anterior (TaskDay)

### ✅ Funcionalidades Base
- Backend Spring Boot completo
- API REST com autenticação JWT
- Mobile React Native
- Docker e CI/CD
- Banco PostgreSQL
- Sistema de jobs e freelancers

---

## 🎯 Próximos Passos

### Melhorias Planejadas
- [ ] Integração completa frontend web com API
- [ ] Chat em tempo real (WebSocket)
- [ ] Upload de arquivos
- [ ] Notificações push
- [ ] PWA (Progressive Web App)
- [ ] Cache Redis
- [ ] Métricas avançadas