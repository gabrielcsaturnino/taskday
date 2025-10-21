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
