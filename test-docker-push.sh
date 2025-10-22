#!/bin/bash

# Script para testar push do Docker para GHCR
# Execute este script para testar o push localmente

echo "🐳 Testando push do Docker para GHCR..."
echo "========================================"

# Verificar se está logado no Docker
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker não está rodando ou você não está logado"
    exit 1
fi

# Verificar se está logado no GHCR
if ! docker images | grep -q "ghcr.io"; then
    echo "🔐 Fazendo login no GitHub Container Registry..."
    echo "Por favor, insira seu token do GitHub:"
    echo "1. Vá para: https://github.com/settings/tokens"
    echo "2. Crie um token com permissões 'write:packages'"
    echo "3. Cole o token abaixo:"
    read -s GITHUB_TOKEN
    
    echo "$GITHUB_TOKEN" | docker login ghcr.io -u gabrielcsaturnino --password-stdin
fi

# Build da imagem
echo "🔨 Fazendo build da imagem..."
docker build -t ghcr.io/gabrielcsaturnino/taskday:latest .

if [ $? -ne 0 ]; then
    echo "❌ Erro no build da imagem"
    exit 1
fi

# Tag da imagem
echo "🏷️  Criando tags..."
docker tag ghcr.io/gabrielcsaturnino/taskday:latest ghcr.io/gabrielcsaturnino/taskday:main

# Push da imagem
echo "📤 Fazendo push para GHCR..."
docker push ghcr.io/gabrielcsaturnino/taskday:latest
docker push ghcr.io/gabrielcsaturnino/taskday:main

if [ $? -eq 0 ]; then
    echo "✅ Push realizado com sucesso!"
    echo "🌐 Imagem disponível em: https://github.com/gabrielcsaturnino/taskday/pkgs/container/taskday"
else
    echo "❌ Erro no push da imagem"
    exit 1
fi

echo "🎉 Teste concluído com sucesso!"
