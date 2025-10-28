#!/bin/bash

# Jooby - Script de Teste Completo
# Uso: ./scripts/test-all.sh

# Cores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${BLUE}🧪 Jooby - Teste Completo${NC}"
echo "============================="

# Verificar se estamos no diretório correto
if [ ! -f "pom.xml" ]; then
    echo -e "${RED}❌ Execute este script no diretório raiz do projeto Jooby${NC}"
    exit 1
fi

# Contador de testes
TESTS_PASSED=0
TESTS_FAILED=0

# Função para executar teste
run_test() {
    local test_name="$1"
    local test_command="$2"
    
    echo -e "${YELLOW}🔍 Executando: $test_name${NC}"
    
    if eval "$test_command"; then
        echo -e "${GREEN}✅ $test_name - PASSOU${NC}"
        ((TESTS_PASSED++))
    else
        echo -e "${RED}❌ $test_name - FALHOU${NC}"
        ((TESTS_FAILED++))
    fi
    echo ""
}

# 1. Teste do ambiente
run_test "Verificação do Ambiente" "./scripts/setup.sh test"

# 2. Teste do backend
run_test "Compilação do Backend" "./mvnw clean compile -q"
run_test "Testes Unitários do Backend" "./mvnw test -q"
run_test "Análise de Código (Checkstyle)" "./mvnw checkstyle:check -q"
run_test "Análise de Código (SpotBugs)" "./mvnw spotbugs:check -q"

# 3. Teste do frontend web
if [ -d "jooby-web" ]; then
    run_test "Testes do Frontend Web" "cd jooby-web && npm test -- --watchAll=false && cd .."
    run_test "Build do Frontend Web" "cd jooby-web && npm run build && cd .."
else
    echo -e "${YELLOW}⚠️  Frontend web não encontrado, pulando testes${NC}"
fi

# 4. Teste do mobile
if [ -d "JoobyMobile" ]; then
    run_test "Testes do Mobile" "cd JoobyMobile && npm test -- --watchAll=false && cd .."
else
    echo -e "${YELLOW}⚠️  Mobile não encontrado, pulando testes${NC}"
fi

# 5. Teste de integração (se disponível)
if [ -f "scripts/test-integration.sh" ]; then
    run_test "Teste de Integração" "./scripts/test-integration.sh"
else
    echo -e "${YELLOW}⚠️  Script de integração não encontrado, pulando${NC}"
fi

# 6. Verificar se Docker está funcionando
run_test "Docker" "docker --version"

# 7. Verificar se Git está funcionando
run_test "Git" "git status"

# Resumo final
echo -e "${BLUE}📊 Resumo dos Testes${NC}"
echo "=================="
echo -e "${GREEN}✅ Testes que passaram: $TESTS_PASSED${NC}"
echo -e "${RED}❌ Testes que falharam: $TESTS_FAILED${NC}"

if [ $TESTS_FAILED -eq 0 ]; then
    echo -e "${GREEN}🎉 Todos os testes passaram! Pronto para push!${NC}"
    echo ""
    echo -e "${YELLOW}📋 Próximos passos:${NC}"
    echo "1. git add ."
    echo "2. git commit -m 'feat: descrição das mudanças'"
    echo "3. git push origin nome-da-branch"
    exit 0
else
    echo -e "${RED}❌ Alguns testes falharam! Corrija antes de fazer push.${NC}"
    exit 1
fi
