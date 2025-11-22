#!/bin/bash

echo "🧪 Testes Unitários - Backend ConectaPG"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# Cores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Verifica se está no diretório correto
if [ ! -d "backend" ]; then
    echo "❌ Erro: Execute este script na raiz do projeto"
    exit 1
fi

cd backend

# Menu de opções
echo "Escolha uma opção:"
echo ""
echo "1) Executar todos os testes"
echo "2) Executar testes dos Controllers"
echo "3) Executar testes dos Services"
echo "4) Executar teste específico"
echo "5) Gerar relatório de cobertura"
echo ""
read -p "Opção [1-5]: " opcao

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

case $opcao in
    1)
        echo -e "${BLUE}🧪 Executando todos os testes...${NC}"
        echo ""
        ./mvnw test
        ;;
    2)
        echo -e "${BLUE}🎮 Executando testes dos Controllers...${NC}"
        echo ""
        echo "Controllers disponíveis:"
        echo "  1) UsuarioControllerTest (13 testes)"
        echo "  2) OcorrenciaControllerTest (13 testes)"
        echo "  3) Ambos"
        echo ""
        read -p "Escolha [1-3]: " controller_opcao
        
        case $controller_opcao in
            1)
                ./mvnw test -Dtest=UsuarioControllerTest
                ;;
            2)
                ./mvnw test -Dtest=OcorrenciaControllerTest
                ;;
            3)
                ./mvnw test -Dtest=*ControllerTest
                ;;
            *)
                echo -e "${YELLOW}❌ Opção inválida${NC}"
                exit 1
                ;;
        esac
        ;;
    3)
        echo -e "${BLUE}⚙️  Executando testes dos Services...${NC}"
        echo ""
        echo "Services disponíveis:"
        echo "  1) UsuarioServiceTest (18 testes)"
        echo "  2) OcorrenciaServiceTest (18 testes)"
        echo "  3) Ambos"
        echo ""
        read -p "Escolha [1-3]: " service_opcao
        
        case $service_opcao in
            1)
                ./mvnw test -Dtest=UsuarioServiceTest
                ;;
            2)
                ./mvnw test -Dtest=OcorrenciaServiceTest
                ;;
            3)
                ./mvnw test -Dtest=*ServiceTest
                ;;
            *)
                echo -e "${YELLOW}❌ Opção inválida${NC}"
                exit 1
                ;;
        esac
        ;;
    4)
        echo "Testes disponíveis:"
        echo ""
        echo "Controllers:"
        echo "  1) UsuarioControllerTest"
        echo "  2) OcorrenciaControllerTest"
        echo ""
        echo "Services:"
        echo "  3) UsuarioServiceTest"
        echo "  4) OcorrenciaServiceTest"
        echo ""
        read -p "Digite o número do teste: " numero
        
        case $numero in
            1)
                ./mvnw test -Dtest=UsuarioControllerTest
                ;;
            2)
                ./mvnw test -Dtest=OcorrenciaControllerTest
                ;;
            3)
                ./mvnw test -Dtest=UsuarioServiceTest
                ;;
            4)
                ./mvnw test -Dtest=OcorrenciaServiceTest
                ;;
            *)
                echo -e "${YELLOW}❌ Opção inválida${NC}"
                exit 1
                ;;
        esac
        ;;
    5)
        echo -e "${BLUE}📊 Gerando relatório de cobertura...${NC}"
        echo ""
        ./mvnw test jacoco:report
        echo ""
        echo -e "${GREEN}✅ Relatório gerado em: backend/target/site/jacoco/index.html${NC}"
        echo ""
        echo "Para visualizar, abra o arquivo no navegador:"
        echo "  file://$(pwd)/target/site/jacoco/index.html"
        ;;
    *)
        echo -e "${YELLOW}❌ Opção inválida${NC}"
        exit 1
        ;;
esac

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# Verifica se os testes passaram
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Todos os testes passaram!${NC}"
else
    echo -e "${YELLOW}⚠️  Alguns testes falharam. Verifique os logs acima.${NC}"
fi

echo ""
