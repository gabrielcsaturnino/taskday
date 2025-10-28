#!/bin/bash

# Script para testar a integração frontend-backend

echo "🧪 Testando Integração Jooby Frontend-Backend"
echo "=============================================="

# Verificar se o backend está rodando
echo "🔧 Verificando backend..."
if curl -s http://localhost:8080/actuator/health > /dev/null; then
    echo "✅ Backend está rodando na porta 8080"
else
    echo "❌ Backend não está rodando. Inicie com: mvn spring-boot:run"
    exit 1
fi

# Verificar se o frontend está rodando
echo "🌐 Verificando frontend..."
if curl -s http://localhost:3000 > /dev/null; then
    echo "✅ Frontend está rodando na porta 3000"
else
    echo "❌ Frontend não está rodando. Inicie com: cd jooby-web && npm start"
    exit 1
fi

# Testar endpoints da API
echo "📡 Testando endpoints da API..."

# Testar endpoint de jobs ativos
echo "  - Testando /api/v1/jobs/active..."
if curl -s http://localhost:8080/api/v1/jobs/active > /dev/null; then
    echo "    ✅ Endpoint de jobs ativos funcionando"
else
    echo "    ❌ Endpoint de jobs ativos com problema"
fi

# Testar endpoint de busca
echo "  - Testando /api/v1/jobs/search..."
if curl -s "http://localhost:8080/api/v1/jobs/search" > /dev/null; then
    echo "    ✅ Endpoint de busca funcionando"
else
    echo "    ❌ Endpoint de busca com problema"
fi

# Testar Swagger
echo "  - Testando Swagger UI..."
if curl -s http://localhost:8080/swagger-ui.html > /dev/null; then
    echo "    ✅ Swagger UI funcionando"
else
    echo "    ❌ Swagger UI com problema"
fi

echo ""
echo "🎉 Teste de integração concluído!"
echo ""
echo "📱 Acessos disponíveis:"
echo "  - Frontend: http://localhost:3000"
echo "  - Backend API: http://localhost:8080"
echo "  - Swagger UI: http://localhost:8080/swagger-ui.html"
echo "  - Health Check: http://localhost:8080/actuator/health"

