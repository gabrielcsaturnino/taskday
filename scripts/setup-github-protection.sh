#!/bin/bash

# TaskDay - Script para configurar proteção de branches no GitHub
# Este script cria as regras de proteção necessárias

echo "🔒 TaskDay - Configuração de Proteção de Branches"
echo "================================================="

# Cores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${BLUE}📋 Este script irá configurar as regras de proteção para:${NC}"
echo "   🔒 main (Produção) - Proteção máxima"
echo "   🔒 develop (Desenvolvimento) - Proteção alta"
echo "   🔒 staging (Teste) - Proteção média"
echo ""

echo -e "${YELLOW}⚠️  IMPORTANTE: Execute este script após criar as branches!${NC}"
echo ""

# Verificar se estamos no diretório correto
if [ ! -f "pom.xml" ]; then
    echo -e "${RED}❌ Erro: Execute este script no diretório raiz do projeto TaskDay${NC}"
    exit 1
fi

# Verificar se o GitHub CLI está instalado
if ! command -v gh &> /dev/null; then
    echo -e "${RED}❌ Erro: GitHub CLI (gh) não está instalado!${NC}"
    echo "Instale com: https://cli.github.com/"
    exit 1
fi

# Verificar se está logado no GitHub
if ! gh auth status &> /dev/null; then
    echo -e "${RED}❌ Erro: Não está logado no GitHub CLI!${NC}"
    echo "Execute: gh auth login"
    exit 1
fi

echo -e "${BLUE}🚀 Configurando proteção de branches...${NC}"
echo ""

# 1. Proteger branch MAIN (Produção)
echo -e "${BLUE}🔒 Configurando proteção para branch MAIN...${NC}"
gh api repos/:owner/:repo/branches/main/protection \
  --method PUT \
  --field required_status_checks='{"strict":true,"contexts":["code-quality","test","security"]}' \
  --field enforce_admins=true \
  --field required_pull_request_reviews='{"required_approving_review_count":2,"dismiss_stale_reviews":true,"require_code_owner_reviews":true}' \
  --field restrictions='{"users":[],"teams":[]}' \
  --field allow_force_pushes=false \
  --field allow_deletions=false

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Proteção da branch MAIN configurada!${NC}"
else
    echo -e "${RED}❌ Erro ao configurar proteção da branch MAIN${NC}"
fi

# 2. Proteger branch DEVELOP
echo -e "${BLUE}🔒 Configurando proteção para branch DEVELOP...${NC}"
gh api repos/:owner/:repo/branches/develop/protection \
  --method PUT \
  --field required_status_checks='{"strict":true,"contexts":["code-quality","test"]}' \
  --field enforce_admins=false \
  --field required_pull_request_reviews='{"required_approving_review_count":1,"dismiss_stale_reviews":true}' \
  --field restrictions='{"users":[],"teams":[]}' \
  --field allow_force_pushes=false \
  --field allow_deletions=false

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Proteção da branch DEVELOP configurada!${NC}"
else
    echo -e "${RED}❌ Erro ao configurar proteção da branch DEVELOP${NC}"
fi

# 3. Proteger branch STAGING
echo -e "${BLUE}🔒 Configurando proteção para branch STAGING...${NC}"
gh api repos/:owner/:repo/branches/staging/protection \
  --method PUT \
  --field required_status_checks='{"strict":false,"contexts":["code-quality"]}' \
  --field enforce_admins=false \
  --field required_pull_request_reviews='{"required_approving_review_count":1}' \
  --field restrictions='{"users":[],"teams":[]}' \
  --field allow_force_pushes=false \
  --field allow_deletions=false

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Proteção da branch STAGING configurada!${NC}"
else
    echo -e "${RED}❌ Erro ao configurar proteção da branch STAGING${NC}"
fi

echo ""
echo -e "${GREEN}🎉 Configuração de proteção concluída!${NC}"
echo ""
echo -e "${YELLOW}📋 Resumo das proteções:${NC}"
echo "   🔒 main: 2 revisores, CI obrigatório, admins protegidos"
echo "   🔒 develop: 1 revisor, CI obrigatório"
echo "   🔒 staging: 1 revisor, CI básico"
echo ""
echo -e "${BLUE}📚 Próximos passos:${NC}"
echo "1. Teste criando uma feature branch"
echo "2. Tente fazer push direto para main (deve falhar)"
echo "3. Crie um Pull Request para testar"
echo "4. Verifique se as regras estão funcionando"
echo ""
echo -e "${GREEN}✅ Branches protegidas com sucesso!${NC}"
