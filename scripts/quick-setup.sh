#!/bin/bash

# TaskDay - Setup Rápido de Branches
echo "🚀 TaskDay - Setup Rápido"
echo "========================="

# Cores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${BLUE}📋 Branches disponíveis:${NC}"
git branch -a

echo ""
echo -e "${YELLOW}🔒 Para configurar proteção das branches:${NC}"
echo "1. Vá para: https://github.com/gabrielcsaturnino/taskday/settings/branches"
echo "2. Clique em 'Add rule'"
echo "3. Configure para 'main':"
echo "   ✅ Require pull request reviews (2 reviewers)"
echo "   ✅ Require status checks (code-quality, test, security)"
echo "   ✅ Require branches to be up to date"
echo "   ✅ Restrict pushes to matching branches"
echo ""
echo "4. Configure para 'develop':"
echo "   ✅ Require pull request reviews (1 reviewer)"
echo "   ✅ Require status checks (code-quality, test)"
echo "   ✅ Require branches to be up to date"
echo ""

echo -e "${GREEN}✅ Setup básico concluído!${NC}"
echo ""
echo -e "${BLUE}🧪 Teste criando uma feature:${NC}"
echo "./scripts/create-feature.sh minha-primeira-feature"
echo ""
echo -e "${GREEN}🎉 Pronto para desenvolvimento seguro!${NC}"
