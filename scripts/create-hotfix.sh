#!/bin/bash

# TaskDay - Script para criar hotfix urgente
# Uso: ./scripts/create-hotfix.sh descricao-do-hotfix

# Cores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# Verificar parâmetros
if [ $# -eq 0 ]; then
    echo -e "${RED}❌ Erro: Forneça a descrição do hotfix${NC}"
    echo "Uso: ./scripts/create-hotfix.sh descricao-do-hotfix"
    echo "Exemplo: ./scripts/create-hotfix.sh critical-security-fix"
    exit 1
fi

HOTFIX_DESCRIPTION=$1
BRANCH_NAME="hotfix/$HOTFIX_DESCRIPTION"

echo -e "${RED}🚨 Criando HOTFIX urgente: ${HOTFIX_DESCRIPTION}${NC}"
echo "========================================"
echo -e "${YELLOW}⚠️  ATENÇÃO: Hotfix é para correções URGENTES em produção!${NC}"
echo ""

# Verificar se já existe
if git show-ref --verify --quiet refs/heads/$BRANCH_NAME; then
    echo -e "${RED}❌ Erro: Branch ${BRANCH_NAME} já existe!${NC}"
    exit 1
fi

# Verificar se estamos na main
current_branch=$(git branch --show-current)
if [ "$current_branch" != "main" ]; then
    echo -e "${YELLOW}⚠️  Mudando para branch main...${NC}"
    git checkout main
    git pull origin main
fi

# Criar branch
echo -e "${BLUE}📝 Criando branch: ${BRANCH_NAME}${NC}"
git checkout -b "$BRANCH_NAME"

# Push para origin
git push -u origin "$BRANCH_NAME"

echo -e "${GREEN}✅ Hotfix branch criada com sucesso!${NC}"
echo ""
echo -e "${YELLOW}📋 Próximos passos URGENTES:${NC}"
echo "1. Corrija o problema CRÍTICO"
echo "2. Teste extensivamente"
echo "3. Faça commit e push"
echo "4. Merge IMEDIATO para main"
echo "5. Deploy IMEDIATO para produção"
echo ""
echo -e "${BLUE}🚀 Comandos úteis:${NC}"
echo "   git add ."
echo "   git commit -m 'hotfix: descrição da correção urgente'"
echo "   git push origin $BRANCH_NAME"
echo ""
echo -e "${RED}🚨 LEMBRE-SE: Hotfix deve ser mergeado para main IMEDIATAMENTE!${NC}"
echo -e "${GREEN}✅ Pronto para correção urgente!${NC}"
