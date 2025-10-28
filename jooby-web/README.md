# Jooby Web - Frontend Web

Frontend web da plataforma Jooby, desenvolvido com React e TypeScript.

## 🚀 Tecnologias

- **React 19** - Biblioteca principal
- **TypeScript** - Tipagem estática
- **Styled Components** - CSS-in-JS
- **React Router** - Roteamento
- **TanStack Query** - Gerenciamento de estado servidor
- **Axios** - Cliente HTTP

## 📦 Instalação

```bash
# Instalar dependências
npm install

# Iniciar servidor de desenvolvimento
npm start

# Build para produção
npm run build

# Executar testes
npm test
```

## 🌐 Configuração

O frontend se conecta automaticamente com o backend Spring Boot na porta 8080.

Para configurar uma URL diferente, crie um arquivo `.env.local`:

```env
REACT_APP_API_URL=http://localhost:8080
```

## 🎨 Funcionalidades

### ✅ Implementadas
- **Autenticação** - Login e registro
- **Dashboard** - Painel principal
- **Jobs** - Listagem de oportunidades
- **Perfil** - Gerenciamento de perfil
- **Design Responsivo** - Interface moderna

### 🔄 Em Desenvolvimento
- **Chat em Tempo Real** - WebSocket integration
- **Upload de Arquivos** - Gestão de documentos
- **Notificações** - Sistema de alertas
- **Filtros Avançados** - Busca refinada

## 🏗️ Estrutura

```
src/
├── components/     # Componentes reutilizáveis
├── contexts/      # Context API (Auth)
├── pages/         # Páginas da aplicação
├── services/      # Serviços de API
├── config/        # Configurações
└── types/         # Definições TypeScript
```

## 🔗 Integração com Backend

O frontend consome a API REST do backend Spring Boot:

- **Base URL**: `http://localhost:8080`
- **Autenticação**: JWT Bearer Token
- **Endpoints**: `/api/v1/*`

## 🚀 Deploy

```bash
# Build de produção
npm run build

# Os arquivos estáticos ficam em build/
# Podem ser servidos por qualquer servidor web
```

## 🧪 Testes

```bash
# Executar testes
npm test

# Executar testes com coverage
npm run test -- --coverage
```

## 📱 Mobile

O projeto também inclui uma versão mobile em React Native na pasta `JoobyMobile/`.