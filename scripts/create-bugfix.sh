#!/bin/bash

# TaskDay - Script para criar nova correção de bug
# Uso: ./scripts/create-bugfix.sh descricao-do-bug

# Cores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# Verificar parâmetros
if [ $# -eq 0 ]; then
    echo -e "${RED}❌ Erro: Forneça a descrição do bug${NC}"
    echo "Uso: ./scripts/create-bugfix.sh descricao-do-bug"
    echo "Exemplo: ./scripts/create-bugfix.sh login-validation-error"
    exit 1
fi

BUG_DESCRIPTION=$1
BRANCH_NAME="bugfix/$BUG_DESCRIPTION"

echo -e "${BLUE}🐛 Criando correção de bug: ${BUG_DESCRIPTION}${NC}"
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

echo -e "${GREEN}✅ Bugfix branch criada com sucesso!${NC}"
echo ""
echo -e "${YELLOW}📋 Próximos passos:${NC}"
echo "1. Identifique e corrija o bug"
echo "2. Adicione testes para o bug"
echo "3. Faça commits descritivos"
echo "4. Crie Pull Request para develop"
echo ""
echo -e "${BLUE}🚀 Comandos úteis:${NC}"
echo "   git add ."
echo "   git commit -m 'fix: descrição da correção'"
echo "   git push origin $BRANCH_NAME"
echo ""
echo -e "${GREEN}✅ Pronto para correção!${NC}"
