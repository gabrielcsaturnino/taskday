#!/bin/bash

# Script para testar o cadastro de usuários

echo "🧪 Testando Cadastro de Usuários - Jooby"
echo "========================================"

# Verificar se o backend está rodando
echo "🔧 Verificando backend..."
if curl -s http://localhost:8080/actuator/health > /dev/null; then
    echo "✅ Backend está rodando"
else
    echo "❌ Backend não está rodando. Inicie com: mvn spring-boot:run"
    exit 1
fi

# Testar cadastro de cliente
echo "👤 Testando cadastro de cliente..."
CLIENT_DATA='{
  "firstName": "João",
  "lastName": "Silva",
  "email": "joao.teste@example.com",
  "phone": "11999999999",
  "password": "12345678",
  "cpf": "123.456.789-01",
  "rgDocument": "1234567890",
  "dateOfBirthday": "1990-01-01",
  "createAddressRequestDTO": {
    "street": "Rua das Flores",
    "number": "123",
    "neighborhood": "Centro",
    "city": "São Paulo",
    "state": "SP",
    "zipCode": "01234-567"
  }
}'

CLIENT_RESPONSE=$(curl -s -X POST http://localhost:8080/api/v1/clients \
  -H "Content-Type: application/json" \
  -d "$CLIENT_DATA")

if echo "$CLIENT_RESPONSE" | grep -q "id"; then
    echo "    ✅ Cadastro de cliente funcionando"
else
    echo "    ❌ Erro no cadastro de cliente"
    echo "    Resposta: $CLIENT_RESPONSE"
fi

# Testar cadastro de freelancer
echo "💼 Testando cadastro de freelancer..."
CONTRACTOR_DATA='{
  "firstName": "Maria",
  "lastName": "Santos",
  "email": "maria.teste@example.com",
  "phone": "11888888888",
  "password": "12345678",
  "cpf": "987.654.321-01",
  "rgDocument": "0987654321",
  "dateOfBirthday": "1985-05-15",
  "description": "Desenvolvedora Full Stack com 5 anos de experiência",
  "createAddressRequestDTO": {
    "street": "Avenida Paulista",
    "number": "456",
    "neighborhood": "Bela Vista",
    "city": "São Paulo",
    "state": "SP",
    "zipCode": "01310-100"
  }
}'

CONTRACTOR_RESPONSE=$(curl -s -X POST http://localhost:8080/api/v1/contractors \
  -H "Content-Type: application/json" \
  -d "$CONTRACTOR_DATA")

if echo "$CONTRACTOR_RESPONSE" | grep -q "id"; then
    echo "    ✅ Cadastro de freelancer funcionando"
else
    echo "    ❌ Erro no cadastro de freelancer"
    echo "    Resposta: $CONTRACTOR_RESPONSE"
fi

# Testar autenticação
echo "🔐 Testando autenticação..."
AUTH_RESPONSE=$(curl -s -X POST http://localhost:8080/authenticate \
  -H "Content-Type: application/json" \
  -d '{"email": "joao.teste@example.com", "password": "12345678"}')

if echo "$AUTH_RESPONSE" | grep -q "eyJ"; then
    echo "    ✅ Autenticação funcionando"
else
    echo "    ❌ Erro na autenticação"
    echo "    Resposta: $AUTH_RESPONSE"
fi

echo ""
echo "🎉 Teste de cadastro concluído!"
echo ""
echo "📱 Acessos disponíveis:"
echo "  - Frontend: http://localhost:3000"
echo "  - Backend API: http://localhost:8080"
echo "  - Swagger UI: http://localhost:8080/swagger-ui.html"


