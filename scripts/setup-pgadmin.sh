#!/bin/bash

echo "🔧 Configurando pgAdmin para conectar ao PostgreSQL..."

# Aguardar o pgAdmin inicializar
echo "⏳ Aguardando pgAdmin inicializar..."
sleep 30

# Verificar se o pgAdmin está rodando
if ! curl -s http://localhost:8081 > /dev/null; then
    echo "❌ pgAdmin não está acessível em http://localhost:8081"
    exit 1
fi

echo "✅ pgAdmin está rodando!"
echo ""
echo "📋 Para acessar o pgAdmin:"
echo "   URL: http://localhost:8081"
echo "   Email: admin@jooby.com"
echo "   Senha: admin123"
echo ""
echo "🔗 Para conectar ao banco PostgreSQL:"
echo "   Host: jooby-postgres"
echo "   Port: 5432"
echo "   Database: joobydb"
echo "   Username: gabriel"
echo "   Password: 123"
echo ""
echo "💡 Dica: Se o servidor não aparecer automaticamente, adicione manualmente usando as informações acima."


