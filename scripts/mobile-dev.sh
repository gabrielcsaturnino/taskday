#!/bin/bash

# Jooby - Script para Desenvolvimento Mobile
# Uso: ./scripts/mobile-dev.sh [opção]
# Opções: setup, start, stop, build, test, clean

# Cores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# Função para mostrar ajuda
show_help() {
    echo -e "${BLUE}📱 Jooby - Desenvolvimento Mobile${NC}"
    echo "====================================="
    echo ""
    echo -e "${YELLOW}Uso:${NC}"
    echo "  ./scripts/mobile-dev.sh [opção]"
    echo ""
    echo -e "${YELLOW}Opções:${NC}"
    echo "  setup     - Setup inicial do ambiente mobile"
    echo "  start     - Iniciar ambiente de desenvolvimento"
    echo "  stop      - Parar ambiente"
    echo "  build     - Build do app mobile"
    echo "  test      - Executar testes"
    echo "  clean     - Limpar cache e dependências"
    echo "  android   - Executar no Android"
    echo "  ios       - Executar no iOS"
    echo "  help      - Mostrar esta ajuda"
    echo ""
    echo -e "${YELLOW}Exemplos:${NC}"
    echo "  ./scripts/mobile-dev.sh setup"
    echo "  ./scripts/mobile-dev.sh start"
    echo "  ./scripts/mobile-dev.sh android"
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
setup_mobile() {
    echo -e "${BLUE}📱 Setup do Ambiente Mobile${NC}"
    echo "============================="
    
    # 1. Verificar dependências
    echo -e "${YELLOW}🔍 Verificando dependências...${NC}"
    
    if ! command -v node &> /dev/null; then
        echo -e "${RED}❌ Node.js não está instalado!${NC}"
        echo "Instale com: https://nodejs.org/"
        exit 1
    fi
    
    if ! command -v npm &> /dev/null; then
        echo -e "${RED}❌ NPM não está instalado!${NC}"
        exit 1
    fi
    
    if ! command -v npx &> /dev/null; then
        echo -e "${RED}❌ NPX não está instalado!${NC}"
        exit 1
    fi
    
    echo -e "${GREEN}✅ Dependências verificadas!${NC}"
    
    # 2. Instalar dependências do mobile
    echo -e "${YELLOW}📦 Instalando dependências do mobile...${NC}"
    cd JoobyMobile
    npm install
    cd ..
    echo -e "${GREEN}✅ Dependências instaladas!${NC}"
    
    # 3. Configurar ambiente
    echo -e "${YELLOW}⚙️  Configurando ambiente...${NC}"
    
    # Criar arquivo de configuração
    cat > JoobyMobile/.env << EOF
# Jooby Mobile - Configurações de Ambiente
API_BASE_URL=http://10.0.2.2:8080/api/v1
API_TIMEOUT=10000
DEBUG_MODE=true
EOF
    
    echo -e "${GREEN}✅ Ambiente configurado!${NC}"
    
    # 4. Verificar Android SDK
    echo -e "${YELLOW}🤖 Verificando Android SDK...${NC}"
    if command -v adb &> /dev/null; then
        echo -e "${GREEN}✅ Android SDK encontrado!${NC}"
    else
        echo -e "${YELLOW}⚠️  Android SDK não encontrado. Instale o Android Studio.${NC}"
    fi
    
    echo -e "${GREEN}🎉 Setup mobile concluído!${NC}"
}

# Função para iniciar ambiente
start_mobile() {
    echo -e "${BLUE}🚀 Iniciando Ambiente Mobile${NC}"
    echo "=============================="
    
    # 1. Iniciar backend
    echo -e "${YELLOW}🔧 Iniciando backend...${NC}"
    docker-compose -f docker-compose.mobile.yml up -d backend postgres
    
    # Aguardar backend estar pronto
    echo -e "${YELLOW}⏳ Aguardando backend estar pronto...${NC}"
    sleep 10
    
    # 2. Verificar se backend está funcionando
    if curl -f http://localhost:8080/actuator/health &> /dev/null; then
        echo -e "${GREEN}✅ Backend funcionando!${NC}"
    else
        echo -e "${RED}❌ Backend não está funcionando!${NC}"
        exit 1
    fi
    
    # 3. Iniciar Metro bundler
    echo -e "${YELLOW}📱 Iniciando Metro bundler...${NC}"
    cd JoobyMobile
    npm start &
    cd ..
    
    echo -e "${GREEN}🎉 Ambiente mobile iniciado!${NC}"
    echo ""
    echo -e "${YELLOW}📋 URLs disponíveis:${NC}"
    echo "   🔧 Backend: http://localhost:8080"
    echo "   📊 pgAdmin: http://localhost:8081"
    echo "   📱 Metro: http://localhost:8081"
    echo ""
    echo -e "${YELLOW}📱 Para executar no dispositivo:${NC}"
    echo "   ./scripts/mobile-dev.sh android"
    echo "   ./scripts/mobile-dev.sh ios"
}

# Função para parar ambiente
stop_mobile() {
    echo -e "${BLUE}🛑 Parando Ambiente Mobile${NC}"
    echo "============================="
    
    # Parar containers
    docker-compose -f docker-compose.mobile.yml down
    
    # Parar Metro bundler
    pkill -f "react-native start" || true
    pkill -f "metro" || true
    
    echo -e "${GREEN}✅ Ambiente mobile parado!${NC}"
}

# Função para build
build_mobile() {
    echo -e "${BLUE}📦 Build do App Mobile${NC}"
    echo "========================"
    
    cd JoobyMobile
    
    # Android
    echo -e "${YELLOW}🤖 Fazendo build para Android...${NC}"
    npx react-native run-android --mode=release
    
    cd ..
    echo -e "${GREEN}✅ Build concluído!${NC}"
}

# Função para testes
test_mobile() {
    echo -e "${BLUE}🧪 Testes do Mobile${NC}"
    echo "===================="
    
    cd JoobyMobile
    
    # Testes unitários
    echo -e "${YELLOW}🧪 Executando testes unitários...${NC}"
    npm test
    
    # Testes de integração
    echo -e "${YELLOW}🔗 Executando testes de integração...${NC}"
    npm run test:integration
    
    cd ..
    echo -e "${GREEN}✅ Testes concluídos!${NC}"
}

# Função para limpeza
clean_mobile() {
    echo -e "${BLUE}🧹 Limpeza do Mobile${NC}"
    echo "===================="
    
    cd JoobyMobile
    
    # Limpar cache
    echo -e "${YELLOW}🗑️  Limpando cache...${NC}"
    npm run clean
    npx react-native clean
    
    # Limpar node_modules
    echo -e "${YELLOW}🗑️  Removendo node_modules...${NC}"
    rm -rf node_modules
    npm install
    
    cd ..
    echo -e "${GREEN}✅ Limpeza concluída!${NC}"
}

# Função para executar no Android
run_android() {
    echo -e "${BLUE}🤖 Executando no Android${NC}"
    echo "========================="
    
    cd JoobyMobile
    npx react-native run-android
    cd ..
}

# Função para executar no iOS
run_ios() {
    echo -e "${BLUE}🍎 Executando no iOS${NC}"
    echo "====================="
    
    cd JoobyMobile
    npx react-native run-ios
    cd ..
}

# Executar comando baseado na opção
case $OPTION in
    "setup")
        setup_mobile
        ;;
    "start")
        start_mobile
        ;;
    "stop")
        stop_mobile
        ;;
    "build")
        build_mobile
        ;;
    "test")
        test_mobile
        ;;
    "clean")
        clean_mobile
        ;;
    "android")
        run_android
        ;;
    "ios")
        run_ios
        ;;
    *)
        echo -e "${RED}❌ Opção inválida: $OPTION${NC}"
        show_help
        exit 1
        ;;
esac

echo -e "${GREEN}🎉 Operação concluída!${NC}"
