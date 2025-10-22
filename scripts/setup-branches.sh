#!/bin/bash

# TaskDay - Setup de Branches para Desenvolvimento Seguro
# Este script cria toda a estrutura de branches necessária

echo "🌿 TaskDay - Setup de Branches Seguras"
echo "======================================="

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Função para criar branch
create_branch() {
    local branch_name=$1
    local description=$2
    
    echo -e "${BLUE}📝 Criando branch: ${branch_name}${NC}"
    echo "   Descrição: ${description}"
    
    git checkout -b "$branch_name"
    git push -u origin "$branch_name"
    echo -e "${GREEN}✅ Branch ${branch_name} criada com sucesso!${NC}"
    echo ""
}

# Verificar se estamos no diretório correto
if [ ! -f "pom.xml" ]; then
    echo -e "${RED}❌ Erro: Execute este script no diretório raiz do projeto TaskDay${NC}"
    exit 1
fi

# Verificar se estamos na branch main
current_branch=$(git branch --show-current)
if [ "$current_branch" != "main" ]; then
    echo -e "${YELLOW}⚠️  Mudando para branch main...${NC}"
    git checkout main
fi

echo -e "${BLUE}🚀 Iniciando criação da estrutura de branches...${NC}"
echo ""

# 1. BRANCH DEVELOP (Desenvolvimento Principal)
create_branch "develop" "Branch principal de desenvolvimento - integração de features"

# 2. BRANCH STAGING (Ambiente de Teste)
create_branch "staging" "Ambiente de staging para testes antes da produção"

# 3. BRANCH HOTFIX (Correções Urgentes)
create_branch "hotfix" "Branch para correções urgentes em produção"

# 4. BRANCH RELEASE (Preparação de Releases)
create_branch "release" "Branch para preparação de releases"

# 5. BRANCH FEATURE (Template para Features)
create_branch "feature/template" "Template para novas features - DELETE após uso"

# 6. BRANCH BUGFIX (Template para Correções)
create_branch "bugfix/template" "Template para correções de bugs - DELETE após uso"

# 7. BRANCH DOCS (Documentação)
create_branch "docs" "Branch para atualizações de documentação"

# 8. BRANCH CHORE (Manutenção)
create_branch "chore" "Branch para tarefas de manutenção e refatoração"

echo -e "${GREEN}🎉 Estrutura de branches criada com sucesso!${NC}"
echo ""
echo -e "${YELLOW}📋 Branches criadas:${NC}"
echo "   🌿 main          - Produção (protegida)"
echo "   🌿 develop       - Desenvolvimento principal"
echo "   🌿 staging       - Ambiente de teste"
echo "   🌿 hotfix        - Correções urgentes"
echo "   🌿 release       - Preparação de releases"
echo "   🌿 feature/*     - Novas funcionalidades"
echo "   🌿 bugfix/*      - Correções de bugs"
echo "   🌿 docs          - Documentação"
echo "   🌿 chore         - Manutenção"
echo ""

# Voltar para main
git checkout main

echo -e "${BLUE}📚 Próximos passos:${NC}"
echo "1. Configure branch protection rules no GitHub"
echo "2. Use os scripts de gerenciamento de branches"
echo "3. Siga o fluxo de trabalho documentado"
echo ""
echo -e "${GREEN}✅ Setup concluído!${NC}"
