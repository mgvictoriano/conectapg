#!/bin/bash

echo "🧪 Testes Unitários - Frontend ConectaPG"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# Cores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Verifica se está no diretório correto
if [ ! -d "frontend" ]; then
    echo "❌ Erro: Execute este script na raiz do projeto"
    exit 1
fi

cd frontend

# Verifica se node_modules existe
if [ ! -d "node_modules" ]; then
    echo -e "${YELLOW}⚠️  Dependências não instaladas. Instalando...${NC}"
    npm install
    echo ""
fi

# Menu de opções
echo "Escolha uma opção:"
echo ""
echo "1) Executar todos os testes"
echo "2) Executar testes em modo watch"
echo "3) Executar testes com interface gráfica"
echo "4) Gerar relatório de cobertura"
echo "5) Executar testes de um arquivo específico"
echo ""
read -p "Opção [1-5]: " opcao

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

case $opcao in
    1)
        echo -e "${BLUE}🧪 Executando todos os testes...${NC}"
        echo ""
        npm test
        ;;
    2)
        echo -e "${BLUE}👀 Modo watch ativado (Ctrl+C para sair)${NC}"
        echo ""
        npm test -- --watch
        ;;
    3)
        echo -e "${BLUE}🎨 Abrindo interface gráfica...${NC}"
        echo ""
        npm run test:ui
        ;;
    4)
        echo -e "${BLUE}📊 Gerando relatório de cobertura...${NC}"
        echo ""
        npm run test:coverage
        echo ""
        echo -e "${GREEN}✅ Relatório gerado em: frontend/coverage/index.html${NC}"
        ;;
    5)
        echo "Arquivos de teste disponíveis:"
        echo ""
        find src/test -name "*.test.js*" -o -name "*.test.jsx" | nl
        echo ""
        read -p "Digite o número do arquivo: " numero
        arquivo=$(find src/test -name "*.test.js*" -o -name "*.test.jsx" | sed -n "${numero}p")
        
        if [ -n "$arquivo" ]; then
            echo ""
            echo -e "${BLUE}🧪 Executando: $arquivo${NC}"
            echo ""
            npm test -- "$arquivo"
        else
            echo -e "${YELLOW}❌ Arquivo não encontrado${NC}"
        fi
        ;;
    *)
        echo -e "${YELLOW}❌ Opção inválida${NC}"
        exit 1
        ;;
esac

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo -e "${GREEN}✅ Concluído!${NC}"
echo ""
