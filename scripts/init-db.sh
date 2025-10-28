#!/bin/bash

# Jooby - Script de Inicialização do Banco de Dados
# Uso: ./scripts/init-db.sh [opção]
# Opções: reset, status, logs, connect

# Cores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# Função para mostrar ajuda
show_help() {
    echo -e "${BLUE}🗄️  Jooby - Gerenciamento do Banco de Dados${NC}"
    echo "============================================="
    echo ""
    echo -e "${YELLOW}Uso:${NC}"
    echo "  ./scripts/init-db.sh [opção]"
    echo ""
    echo -e "${YELLOW}Opções:${NC}"
    echo "  reset     - Resetar banco (apaga todos os dados!)"
    echo "  status    - Verificar status do banco"
    echo "  logs      - Ver logs do PostgreSQL"
    echo "  connect   - Conectar ao banco via psql"
    echo "  help      - Mostrar esta ajuda"
    echo ""
    echo -e "${YELLOW}Exemplos:${NC}"
    echo "  ./scripts/init-db.sh status"
    echo "  ./scripts/init-db.sh reset"
    echo "  ./scripts/init-db.sh connect"
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

# Função para resetar banco
reset_database() {
    echo -e "${BLUE}🗄️  Resetando Banco de Dados${NC}"
    echo "==============================="
    
    echo -e "${YELLOW}⚠️  ATENÇÃO: Esta operação irá apagar TODOS os dados!${NC}"
    read -p "Tem certeza? Digite 'sim' para confirmar: " confirm
    
    if [ "$confirm" != "sim" ]; then
        echo -e "${YELLOW}❌ Operação cancelada.${NC}"
        exit 0
    fi
    
    echo -e "${YELLOW}🛑 Parando containers...${NC}"
    docker-compose down
    
    echo -e "${YELLOW}🗑️  Removendo volumes...${NC}"
    docker-compose down -v
    
    echo -e "${YELLOW}🚀 Iniciando banco limpo...${NC}"
    docker-compose up postgres -d
    
    echo -e "${YELLOW}⏳ Aguardando banco inicializar...${NC}"
    sleep 15
    
    echo -e "${GREEN}✅ Banco resetado com sucesso!${NC}"
    echo ""
    echo -e "${YELLOW}📋 Próximos passos:${NC}"
    echo "1. Execute: ./scripts/start-all.sh"
    echo "2. Ou apenas: docker-compose up -d"
}

# Função para verificar status
check_status() {
    echo -e "${BLUE}📊 Status do Banco de Dados${NC}"
    echo "============================="
    
    # Verificar se container está rodando
    if docker-compose ps postgres | grep -q "Up"; then
        echo -e "${GREEN}✅ Container PostgreSQL está rodando${NC}"
    else
        echo -e "${RED}❌ Container PostgreSQL não está rodando${NC}"
        echo "Execute: docker-compose up postgres -d"
        exit 1
    fi
    
    # Verificar se banco está acessível
    if docker exec jooby-postgres pg_isready -U gabriel -d joobydb &> /dev/null; then
        echo -e "${GREEN}✅ Banco de dados está acessível${NC}"
    else
        echo -e "${RED}❌ Banco de dados não está acessível${NC}"
        exit 1
    fi
    
    # Verificar tabelas
    echo -e "${YELLOW}📋 Verificando tabelas...${NC}"
    docker exec jooby-postgres psql -U gabriel -d joobydb -c "\dt" 2>/dev/null | grep -E "(contractor|client|client_jobs)" > /dev/null
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Tabelas principais existem${NC}"
    else
        echo -e "${YELLOW}⚠️  Tabelas principais não encontradas${NC}"
        echo "Execute: ./scripts/init-db.sh reset"
    fi
    
    echo ""
    echo -e "${YELLOW}📊 Informações do banco:${NC}"
    echo "   🗄️  Nome: joobydb"
    echo "   👤 Usuário: gabriel"
    echo "   🐳 Container: jooby-postgres"
    echo "   🌐 Porta: 5432"
}

# Função para ver logs
show_logs() {
    echo -e "${BLUE}📋 Logs do PostgreSQL${NC}"
    echo "======================="
    
    docker-compose logs postgres
}

# Função para conectar ao banco
connect_database() {
    echo -e "${BLUE}🔌 Conectando ao Banco de Dados${NC}"
    echo "================================="
    
    echo -e "${YELLOW}📋 Comandos úteis:${NC}"
    echo "   \\dt          - Listar tabelas"
    echo "   \\d+ table    - Descrever tabela"
    echo "   \\q           - Sair"
    echo ""
    
    docker exec -it jooby-postgres psql -U gabriel -d joobydb
}

# Executar comando baseado na opção
case $OPTION in
    "reset")
        reset_database
        ;;
    "status")
        check_status
        ;;
    "logs")
        show_logs
        ;;
    "connect")
        connect_database
        ;;
    *)
        echo -e "${RED}❌ Opção inválida: $OPTION${NC}"
        show_help
        exit 1
        ;;
esac

echo -e "${GREEN}🎉 Operação concluída!${NC}"
