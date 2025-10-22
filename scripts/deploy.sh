#!/bin/bash

# TaskDay - Script de Deploy Unificado
# Uso: ./scripts/deploy.sh [ambiente] [opção]
# Exemplos:
#   ./scripts/deploy.sh dev          # Deploy desenvolvimento
#   ./scripts/deploy.sh prod         # Deploy produção
#   ./scripts/deploy.sh rollback     # Rollback
#   ./scripts/deploy.sh status       # Status dos serviços

# Cores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# Função para mostrar ajuda
show_help() {
    echo -e "${BLUE}🚀 TaskDay - Script de Deploy Unificado${NC}"
    echo "=============================================="
    echo ""
    echo -e "${YELLOW}Uso:${NC}"
    echo "  ./scripts/deploy.sh [ambiente] [opção]"
    echo ""
    echo -e "${YELLOW}Ambientes:${NC}"
    echo "  dev     - Desenvolvimento (docker-compose.yml)"
    echo "  prod    - Produção (docker-compose.prod.yml)"
    echo "  staging - Staging (docker-compose.yml + configurações especiais)"
    echo ""
    echo -e "${YELLOW}Opções:${NC}"
    echo "  build   - Apenas build (não inicia)"
    echo "  start   - Iniciar serviços"
    echo "  stop    - Parar serviços"
    echo "  restart - Reiniciar serviços"
    echo "  logs    - Ver logs"
    echo "  status  - Status dos serviços"
    echo "  rollback- Rollback para versão anterior"
    echo "  backup  - Backup do banco de dados"
    echo ""
    echo -e "${YELLOW}Exemplos:${NC}"
    echo "  ./scripts/deploy.sh dev start"
    echo "  ./scripts/deploy.sh prod build"
    echo "  ./scripts/deploy.sh rollback"
    echo "  ./scripts/deploy.sh status"
}

# Verificar se estamos no diretório correto
if [ ! -f "pom.xml" ]; then
    echo -e "${RED}❌ Erro: Execute este script no diretório raiz do projeto TaskDay${NC}"
    exit 1
fi

# Verificar parâmetros
if [ $# -eq 0 ] || [ "$1" = "help" ] || [ "$1" = "-h" ] || [ "$1" = "--help" ]; then
    show_help
    exit 0
fi

ENVIRONMENT=$1
OPTION=${2:-start}

# Função para deploy de desenvolvimento
deploy_dev() {
    echo -e "${BLUE}🔧 Deploy de Desenvolvimento${NC}"
    echo "================================"
    
    case $OPTION in
        "build")
            echo -e "${YELLOW}📦 Fazendo build da aplicação...${NC}"
            ./mvnw clean package -DskipTests
            docker-compose build
            ;;
        "start")
            echo -e "${YELLOW}🚀 Iniciando ambiente de desenvolvimento...${NC}"
            docker-compose up -d
            echo -e "${GREEN}✅ Ambiente iniciado!${NC}"
            echo "   📊 pgAdmin: http://localhost:8081"
            echo "   🚀 App: http://localhost:8080"
            echo "   📚 Swagger: http://localhost:8080/swagger-ui.html"
            ;;
        "stop")
            echo -e "${YELLOW}🛑 Parando ambiente de desenvolvimento...${NC}"
            docker-compose down
            echo -e "${GREEN}✅ Ambiente parado!${NC}"
            ;;
        "restart")
            echo -e "${YELLOW}🔄 Reiniciando ambiente de desenvolvimento...${NC}"
            docker-compose down
            docker-compose up -d
            echo -e "${GREEN}✅ Ambiente reiniciado!${NC}"
            ;;
        "logs")
            echo -e "${YELLOW}📋 Logs do ambiente de desenvolvimento:${NC}"
            docker-compose logs -f
            ;;
        "status")
            echo -e "${YELLOW}📊 Status do ambiente de desenvolvimento:${NC}"
            docker-compose ps
            ;;
        *)
            echo -e "${RED}❌ Opção inválida para desenvolvimento: $OPTION${NC}"
            exit 1
            ;;
    esac
}

# Função para deploy de produção
deploy_prod() {
    echo -e "${BLUE}🚀 Deploy de Produção${NC}"
    echo "======================"
    
    case $OPTION in
        "build")
            echo -e "${YELLOW}📦 Fazendo build para produção...${NC}"
            ./mvnw clean package -DskipTests
            docker-compose -f docker-compose.prod.yml build
            ;;
        "start")
            echo -e "${YELLOW}🚀 Iniciando ambiente de produção...${NC}"
            docker-compose -f docker-compose.prod.yml up -d
            echo -e "${GREEN}✅ Produção iniciada!${NC}"
            echo "   🚀 App: http://localhost:8080"
            echo "   📊 Health: http://localhost:8080/actuator/health"
            ;;
        "stop")
            echo -e "${YELLOW}🛑 Parando ambiente de produção...${NC}"
            docker-compose -f docker-compose.prod.yml down
            echo -e "${GREEN}✅ Produção parada!${NC}"
            ;;
        "restart")
            echo -e "${YELLOW}🔄 Reiniciando ambiente de produção...${NC}"
            docker-compose -f docker-compose.prod.yml down
            docker-compose -f docker-compose.prod.yml up -d
            echo -e "${GREEN}✅ Produção reiniciada!${NC}"
            ;;
        "logs")
            echo -e "${YELLOW}📋 Logs do ambiente de produção:${NC}"
            docker-compose -f docker-compose.prod.yml logs -f
            ;;
        "status")
            echo -e "${YELLOW}📊 Status do ambiente de produção:${NC}"
            docker-compose -f docker-compose.prod.yml ps
            ;;
        *)
            echo -e "${RED}❌ Opção inválida para produção: $OPTION${NC}"
            exit 1
            ;;
    esac
}

# Função para rollback
rollback() {
    echo -e "${RED}🚨 ROLLBACK - Voltando para versão anterior${NC}"
    echo "============================================="
    
    echo -e "${YELLOW}⚠️  ATENÇÃO: Esta operação irá reverter para a versão anterior!${NC}"
    read -p "Tem certeza? (y/N): " confirm
    
    if [[ "$confirm" =~ ^[Yy]$ ]]; then
        echo -e "${YELLOW}🔄 Fazendo rollback...${NC}"
        
        # Parar serviços atuais
        docker-compose down 2>/dev/null || true
        docker-compose -f docker-compose.prod.yml down 2>/dev/null || true
        
        # Voltar para commit anterior
        git log --oneline -5
        echo ""
        read -p "Digite o hash do commit para voltar: " commit_hash
        
        if [ -n "$commit_hash" ]; then
            git checkout $commit_hash
            echo -e "${GREEN}✅ Rollback realizado para commit: $commit_hash${NC}"
        else
            echo -e "${RED}❌ Hash de commit inválido!${NC}"
            exit 1
        fi
    else
        echo -e "${YELLOW}❌ Rollback cancelado!${NC}"
    fi
}

# Função para backup
backup() {
    echo -e "${BLUE}💾 Backup do Banco de Dados${NC}"
    echo "============================="
    
    BACKUP_FILE="backup_$(date +%Y%m%d_%H%M%S).sql"
    
    echo -e "${YELLOW}📦 Criando backup: $BACKUP_FILE${NC}"
    docker-compose exec postgres pg_dump -U gabriel taskdaydb > $BACKUP_FILE
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Backup criado com sucesso: $BACKUP_FILE${NC}"
    else
        echo -e "${RED}❌ Erro ao criar backup!${NC}"
        exit 1
    fi
}

# Função para status geral
status() {
    echo -e "${BLUE}📊 Status Geral do TaskDay${NC}"
    echo "============================="
    
    echo -e "${YELLOW}🐳 Containers Docker:${NC}"
    docker ps --filter "name=taskday" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
    
    echo ""
    echo -e "${YELLOW}🌿 Branches Git:${NC}"
    git branch -vv
    
    echo ""
    echo -e "${YELLOW}📋 Últimos Commits:${NC}"
    git log --oneline -5
    
    echo ""
    echo -e "${YELLOW}🔍 Health Check:${NC}"
    curl -s http://localhost:8080/actuator/health 2>/dev/null || echo "❌ Aplicação não está rodando"
}

# Executar comando baseado no ambiente
case $ENVIRONMENT in
    "dev")
        deploy_dev
        ;;
    "prod")
        deploy_prod
        ;;
    "staging")
        echo -e "${YELLOW}🧪 Deploy de Staging (usando configurações de desenvolvimento)${NC}"
        deploy_dev
        ;;
    "rollback")
        rollback
        ;;
    "backup")
        backup
        ;;
    "status")
        status
        ;;
    *)
        echo -e "${RED}❌ Ambiente inválido: $ENVIRONMENT${NC}"
        echo "Ambientes válidos: dev, prod, staging, rollback, backup, status"
        exit 1
        ;;
esac

echo -e "${GREEN}🎉 Operação concluída!${NC}"