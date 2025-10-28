#!/bin/bash

# Jooby - Setup Completo do Projeto
# Uso: ./scripts/setup.sh [opção]
# Opções: init, branches, protection, test

# Cores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# Função para mostrar ajuda
show_help() {
    echo -e "${BLUE}🚀 Jooby - Setup Completo${NC}"
    echo "============================="
    echo ""
    echo -e "${YELLOW}Uso:${NC}"
    echo "  ./scripts/setup.sh [opção]"
    echo ""
    echo -e "${YELLOW}Opções:${NC}"
    echo "  init        - Setup inicial completo"
    echo "  branches    - Criar estrutura de branches"
    echo "  protection  - Configurar proteção de branches"
    echo "  test        - Testar ambiente"
    echo "  help        - Mostrar esta ajuda"
    echo ""
    echo -e "${YELLOW}Exemplos:${NC}"
    echo "  ./scripts/setup.sh init"
    echo "  ./scripts/setup.sh branches"
    echo "  ./scripts/setup.sh test"
}

# Verificar se estamos no diretório correto
if [ ! -f "pom.xml" ]; then
    echo -e "${RED}❌ Erro: Execute este script no diretório raiz do projeto Jooby${NC}"
    exit 1
fi

# Verificar parâmetros
if [ $# -eq 0 ] || [ "$1" = "help" ] || [ "$1" = "-h" ] || [ "$1" = "--help" ]; then
    show_help
    exit 0
fi

OPTION=$1

# Função para setup inicial
setup_init() {
    echo -e "${BLUE}🚀 Setup Inicial do Jooby${NC}"
    echo "==============================="
    
    # 1. Verificar dependências
    echo -e "${YELLOW}🔍 Verificando dependências...${NC}"
    
    if ! command -v git &> /dev/null; then
        echo -e "${RED}❌ Git não está instalado!${NC}"
        exit 1
    fi
    
    if ! command -v docker &> /dev/null; then
        echo -e "${RED}❌ Docker não está instalado!${NC}"
        exit 1
    fi
    
    if ! command -v java &> /dev/null; then
        echo -e "${RED}❌ Java não está instalado!${NC}"
        exit 1
    fi
    
    if ! command -v node &> /dev/null; then
        echo -e "${RED}❌ Node.js não está instalado!${NC}"
        exit 1
    fi
    
    echo -e "${GREEN}✅ Dependências verificadas!${NC}"
    
    # 2. Configurar Git
    echo -e "${YELLOW}🔧 Configurando Git...${NC}"
    git config --global user.name "Gabriel Saturnino" 2>/dev/null || true
    git config --global user.email "gabrielcsaturnino@gmail.com" 2>/dev/null || true
    echo -e "${GREEN}✅ Git configurado!${NC}"
    
    # 3. Instalar dependências do frontend web
    echo -e "${YELLOW}🌐 Instalando dependências do frontend web...${NC}"
    if [ -d "jooby-web" ]; then
        cd jooby-web
        npm install
        cd ..
        echo -e "${GREEN}✅ Frontend web configurado!${NC}"
    fi
    
    # 4. Instalar dependências do mobile
    echo -e "${YELLOW}📱 Instalando dependências do mobile...${NC}"
    if [ -d "JoobyMobile" ]; then
        cd JoobyMobile
        npm install
        cd ..
        echo -e "${GREEN}✅ Mobile configurado!${NC}"
    fi
    
    # 5. Build da aplicação
    echo -e "${YELLOW}📦 Fazendo build da aplicação...${NC}"
    ./mvnw clean compile
    echo -e "${GREEN}✅ Build concluído!${NC}"
    
    # 6. Teste do ambiente
    echo -e "${YELLOW}🧪 Testando ambiente...${NC}"
    ./scripts/setup.sh test
    
    echo -e "${GREEN}🎉 Setup inicial concluído!${NC}"
    echo ""
    echo -e "${YELLOW}📋 Próximos passos:${NC}"
    echo "1. Execute: ./scripts/start-all.sh"
    echo "2. Acesse: http://localhost:3000 (Web) e http://localhost:8080 (API)"
}

# Função para criar branches
setup_branches() {
    echo -e "${BLUE}🌿 Criando Estrutura de Branches${NC}"
    echo "=================================="
    
    # Verificar se estamos na develop
    current_branch=$(git branch --show-current)
    if [ "$current_branch" != "develop" ]; then
        echo -e "${YELLOW}⚠️  Mudando para branch develop...${NC}"
        git checkout develop
    fi
    
    # Criar branches necessárias
    branches=("staging" "hotfix" "release" "docs" "chore")
    
    for branch in "${branches[@]}"; do
        if ! git show-ref --verify --quiet refs/heads/$branch; then
            echo -e "${YELLOW}📝 Criando branch: $branch${NC}"
            git checkout -b $branch
            git push -u origin $branch
            echo -e "${GREEN}✅ Branch $branch criada!${NC}"
        else
            echo -e "${YELLOW}⚠️  Branch $branch já existe!${NC}"
        fi
    done
    
    # Voltar para develop
    git checkout develop
    
    echo -e "${GREEN}🎉 Estrutura de branches criada!${NC}"
    echo ""
    echo -e "${YELLOW}📋 Branches disponíveis:${NC}"
    git branch -a
}

# Função para configurar proteção
setup_protection() {
    echo -e "${BLUE}🔒 Configuração de Proteção de Branches${NC}"
    echo "============================================="
    
    echo -e "${YELLOW}📋 Para configurar proteção das branches:${NC}"
    echo "1. Vá para: https://github.com/gabrielcsaturnino/taskday/settings/branches"
    echo "2. Clique em 'Add rule'"
    echo ""
    echo -e "${YELLOW}🔒 Para branch MAIN (Produção):${NC}"
    echo "   ✅ Require pull request reviews (2 reviewers)"
    echo "   ✅ Require status checks (code-quality, test, security)"
    echo "   ✅ Require branches to be up to date"
    echo "   ✅ Restrict pushes to matching branches"
    echo ""
    echo -e "${YELLOW}🔒 Para branch DEVELOP (Desenvolvimento):${NC}"
    echo "   ✅ Require pull request reviews (1 reviewer)"
    echo "   ✅ Require status checks (code-quality, test)"
    echo "   ✅ Require branches to be up to date"
    echo ""
    echo -e "${GREEN}✅ Instruções de proteção fornecidas!${NC}"
}

# Função para testar ambiente
setup_test() {
    echo -e "${BLUE}🧪 Testando Ambiente Jooby${NC}"
    echo "==============================="
    
    # 1. Teste de compilação
    echo -e "${YELLOW}📦 Testando compilação...${NC}"
    if ./mvnw clean compile -q; then
        echo -e "${GREEN}✅ Compilação OK!${NC}"
    else
        echo -e "${RED}❌ Erro na compilação!${NC}"
        return 1
    fi
    
    # 2. Teste de Docker
    echo -e "${YELLOW}🐳 Testando Docker...${NC}"
    if docker --version &> /dev/null; then
        echo -e "${GREEN}✅ Docker OK!${NC}"
    else
        echo -e "${RED}❌ Docker não está funcionando!${NC}"
        return 1
    fi
    
    # 3. Teste de Git
    echo -e "${YELLOW}🌿 Testando Git...${NC}"
    if git status &> /dev/null; then
        echo -e "${GREEN}✅ Git OK!${NC}"
    else
        echo -e "${RED}❌ Git não está funcionando!${NC}"
        return 1
    fi
    
    # 4. Teste de Node.js
    echo -e "${YELLOW}📱 Testando Node.js...${NC}"
    if node --version &> /dev/null; then
        echo -e "${GREEN}✅ Node.js OK!${NC}"
    else
        echo -e "${RED}❌ Node.js não está funcionando!${NC}"
        return 1
    fi
    
    echo -e "${GREEN}🎉 Todos os testes passaram!${NC}"
    echo ""
    echo -e "${YELLOW}📋 Próximos passos:${NC}"
    echo "1. Execute: ./scripts/start-all.sh"
    echo "2. Acesse: http://localhost:3000 (Web) e http://localhost:8080 (API)"
    echo ""
    echo -e "${GREEN}✅ Ambiente pronto para desenvolvimento!${NC}"
}

# Executar comando baseado na opção
case $OPTION in
    "init")
        setup_init
        ;;
    "branches")
        setup_branches
        ;;
    "protection")
        setup_protection
        ;;
    "test")
        setup_test
        ;;
    *)
        echo -e "${RED}❌ Opção inválida: $OPTION${NC}"
        show_help
        exit 1
        ;;
esac
