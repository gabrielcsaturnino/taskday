#!/bin/bash

# TaskDay - Script para merge seguro para develop
# Uso: ./scripts/merge-to-develop.sh nome-da-branch

# Cores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# Verificar parâmetros
if [ $# -eq 0 ]; then
    echo -e "${RED}❌ Erro: Forneça o nome da branch${NC}"
    echo "Uso: ./scripts/merge-to-develop.sh nome-da-branch"
    echo "Exemplo: ./scripts/merge-to-develop.sh feature/user-auth"
    exit 1
fi

BRANCH_NAME=$1

echo -e "${BLUE}🔄 Fazendo merge seguro para develop${NC}"
echo "========================================"

# Verificar se a branch existe
if ! git show-ref --verify --quiet refs/heads/$BRANCH_NAME; then
    echo -e "${RED}❌ Erro: Branch ${BRANCH_NAME} não existe!${NC}"
    exit 1
fi

# Verificar se estamos na develop
current_branch=$(git branch --show-current)
if [ "$current_branch" != "develop" ]; then
    echo -e "${YELLOW}⚠️  Mudando para branch develop...${NC}"
    git checkout develop
    git pull origin develop
fi

# Verificar se há mudanças não commitadas
if ! git diff-index --quiet HEAD --; then
    echo -e "${RED}❌ Erro: Há mudanças não commitadas!${NC}"
    echo "Faça commit ou stash das mudanças antes de continuar."
    exit 1
fi

# Executar testes antes do merge
echo -e "${BLUE}🧪 Executando testes antes do merge...${NC}"
./mvnw clean test

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Erro: Testes falharam! Merge cancelado.${NC}"
    exit 1
fi

# Executar análise de código
echo -e "${BLUE}🔍 Executando análise de código...${NC}"
./mvnw spotbugs:check
./mvnw checkstyle:check

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Erro: Análise de código falhou! Merge cancelado.${NC}"
    exit 1
fi

# Fazer merge
echo -e "${BLUE}🔄 Fazendo merge de ${BRANCH_NAME} para develop...${NC}"
git merge --no-ff $BRANCH_NAME -m "Merge $BRANCH_NAME into develop"

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Erro: Merge falhou! Resolva os conflitos manualmente.${NC}"
    exit 1
fi

# Push para origin
echo -e "${BLUE}📤 Fazendo push para origin...${NC}"
git push origin develop

# Deletar branch local (opcional)
echo -e "${YELLOW}🗑️  Deseja deletar a branch local ${BRANCH_NAME}? (y/n)${NC}"
read -r response
if [[ "$response" =~ ^[Yy]$ ]]; then
    git branch -d $BRANCH_NAME
    echo -e "${GREEN}✅ Branch local deletada!${NC}"
fi

echo -e "${GREEN}✅ Merge realizado com sucesso!${NC}"
echo ""
echo -e "${YELLOW}📋 Próximos passos:${NC}"
echo "1. Verifique se o CI/CD passou"
echo "2. Teste em ambiente de staging"
echo "3. Prepare para merge para main"
echo ""
echo -e "${GREEN}🎉 Branch ${BRANCH_NAME} integrada com sucesso!${NC}"
