#!/bin/bash

# Script de deploy para produção
set -e

echo "🚀 Iniciando deploy de produção do TaskDay..."

# Verificar se arquivo .env.prod existe
if [ ! -f ".env.prod" ]; then
    echo "❌ Arquivo .env.prod não encontrado!"
    echo "📋 Copie env.prod.example para .env.prod e configure as variáveis:"
    echo "   cp env.prod.example .env.prod"
    echo "   nano .env.prod"
    exit 1
fi

# Verificar se Docker está rodando
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker não está rodando!"
    exit 1
fi

# Fazer backup do banco de dados
echo "💾 Fazendo backup do banco de dados..."
docker-compose -f docker-compose.prod.yml exec postgres pg_dump -U taskday_user taskdaydb > backup_$(date +%Y%m%d_%H%M%S).sql

# Parar containers existentes
echo "🛑 Parando containers de produção..."
docker-compose -f docker-compose.prod.yml down

# Fazer pull das imagens mais recentes
echo "📥 Fazendo pull das imagens..."
docker-compose -f docker-compose.prod.yml pull

# Subir os serviços
echo "⬆️ Subindo serviços de produção..."
docker-compose -f docker-compose.prod.yml up -d

# Aguardar aplicação ficar pronta
echo "⏳ Aguardando aplicação ficar pronta..."
sleep 60

# Verificar saúde da aplicação
echo "🏥 Verificando saúde da aplicação..."
if curl -f http://localhost:8080/actuator/health; then
    echo "✅ Deploy de produção realizado com sucesso!"
    echo "🌐 Aplicação disponível em: http://localhost:8080"
    echo "📚 Swagger UI: http://localhost:8080/swagger-ui/index.html"
    echo "🗄️ pgAdmin: http://localhost:8081"
    echo "📊 Métricas: http://localhost:8080/actuator/metrics"
else
    echo "❌ Falha no deploy - aplicação não está respondendo"
    echo "📋 Verificando logs..."
    docker-compose -f docker-compose.prod.yml logs app
    exit 1
fi

# Verificar logs de erro
echo "📋 Verificando logs de erro..."
docker-compose -f docker-compose.prod.yml logs --tail=50 app | grep -i error || echo "✅ Nenhum erro encontrado nos logs"

echo "🎉 Deploy de produção concluído com sucesso!"
