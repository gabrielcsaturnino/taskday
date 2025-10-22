#!/bin/bash

# TaskDay - Script para criar nova feature
# Uso: ./scripts/create-feature.sh nome-da-feature

# Cores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# Verificar parâmetros
if [ $# -eq 0 ]; then
    echo -e "${RED}❌ Erro: Forneça o nome da feature${NC}"
    echo "Uso: ./scripts/create-feature.sh nome-da-feature"
    echo "Exemplo: ./scripts/create-feature.sh user-authentication"
    exit 1
fi

FEATURE_NAME=$1
BRANCH_NAME="feature/$FEATURE_NAME"

echo -e "${BLUE}🌿 Criando nova feature: ${FEATURE_NAME}${NC}"
echo "========================================"

# Verificar se já existe
if git show-ref --verify --quiet refs/heads/$BRANCH_NAME; then
    echo -e "${RED}❌ Erro: Branch ${BRANCH_NAME} já existe!${NC}"
    exit 1
fi

# Verificar se estamos na develop
current_branch=$(git branch --show-current)
if [ "$current_branch" != "develop" ]; then
    echo -e "${YELLOW}⚠️  Mudando para branch develop...${NC}"
    git checkout develop
    git pull origin develop
fi

# Criar branch
echo -e "${BLUE}📝 Criando branch: ${BRANCH_NAME}${NC}"
git checkout -b "$BRANCH_NAME"

# Push para origin
git push -u origin "$BRANCH_NAME"

echo -e "${GREEN}✅ Feature branch criada com sucesso!${NC}"
echo ""
echo -e "${YELLOW}📋 Próximos passos:${NC}"
echo "1. Desenvolva sua feature"
echo "2. Faça commits frequentes"
echo "3. Teste localmente"
echo "4. Crie Pull Request para develop"
echo ""
echo -e "${BLUE}🚀 Comandos úteis:${NC}"
echo "   git add ."
echo "   git commit -m 'feat: descrição da feature'"
echo "   git push origin $BRANCH_NAME"
echo ""
echo -e "${GREEN}✅ Pronto para desenvolvimento!${NC}"
