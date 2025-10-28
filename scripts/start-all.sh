#!/bin/bash

# Script para iniciar todo o ecossistema Jooby

echo "🚀 Iniciando Jooby - Plataforma de Freelancers"
echo "================================================"

# Verificar se Docker está rodando
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker não está rodando. Inicie o Docker primeiro."
    exit 1
fi

# Verificar se estamos no diretório correto
if [ ! -f "pom.xml" ]; then
    echo "❌ Execute este script no diretório raiz do projeto Jooby"
    exit 1
fi

echo "🐳 Iniciando containers Docker..."
docker-compose up -d

echo "⏳ Aguardando banco de dados inicializar..."
sleep 15

echo "🔧 Iniciando backend Spring Boot..."
./mvnw spring-boot:run &
BACKEND_PID=$!

echo "⏳ Aguardando backend inicializar..."
sleep 20

# Verificar se backend está funcionando
echo "🔍 Verificando se backend está funcionando..."
for i in {1..10}; do
    if curl -f http://localhost:8080/actuator/health &> /dev/null; then
        echo "✅ Backend funcionando!"
        break
    fi
    echo "⏳ Aguardando backend... ($i/10)"
    sleep 5
done

echo "🌐 Iniciando frontend web..."
cd jooby-web
npm start &
WEB_PID=$!
cd ..

echo ""
echo "✅ Jooby está rodando!"
echo "🌐 Frontend Web: http://localhost:3000"
echo "🔧 Backend API: http://localhost:8080"
echo "📊 Swagger UI: http://localhost:8080/swagger-ui.html"
echo "📊 pgAdmin: http://localhost:8081"
echo "📱 Mobile: cd JoobyMobile && npm start"
echo ""
echo "Pressione Ctrl+C para parar todos os serviços"

# Função para parar todos os processos
cleanup() {
    echo ""
    echo "🛑 Parando serviços..."
    kill $BACKEND_PID $WEB_PID 2>/dev/null
    docker-compose down
    echo "✅ Serviços parados!"
    exit 0
}

# Capturar Ctrl+C
trap cleanup SIGINT

# Aguardar
wait