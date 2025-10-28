#!/bin/bash

# Script para iniciar o desenvolvimento web do Jooby

echo "🚀 Iniciando desenvolvimento do Jooby Web..."

# Verificar se o diretório existe
if [ ! -d "./jooby-web" ]; then
    echo "❌ Diretório jooby-web não encontrado!"
    exit 1
fi

# Navegar para o diretório do frontend web
cd ./jooby-web

# Verificar se node_modules existe
if [ ! -d "node_modules" ]; then
    echo "📦 Instalando dependências..."
    npm install
fi

# Iniciar o servidor de desenvolvimento
echo "🌐 Iniciando servidor de desenvolvimento..."
echo "📱 Acesse: http://localhost:3000"
echo "🔗 API Backend: http://localhost:8080"
echo ""
echo "Pressione Ctrl+C para parar o servidor"

npm start


