#!/bin/bash

echo "🔄 Iniciando rollback..."

# Fazer backup do estado atual
echo "💾 Fazendo backup..."
docker-compose exec postgres pg_dump -U gabriel taskdaydb > backup_$(date +%Y%m%d_%H%M%S).sql

# Voltar para versão anterior
echo "⬅️ Voltando para versão anterior..."
git checkout HEAD~1

# Rebuild e restart
echo "🔨 Rebuild da aplicação..."
docker-compose down
docker-compose up -d --build

echo "✅ Rollback concluído!"
