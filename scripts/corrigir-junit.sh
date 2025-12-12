#!/bin/bash

echo "🔧 Corrigindo problema 'JUnit not found in module'"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# Cores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Verifica se está no diretório correto
if [ ! -d "backend" ]; then
    echo -e "${RED}❌ Erro: Execute este script na raiz do projeto${NC}"
    exit 1
fi

cd backend

echo -e "${BLUE}1️⃣  Limpando projeto...${NC}"
./mvnw clean
echo ""

echo -e "${BLUE}2️⃣  Removendo cache local do JUnit...${NC}"
rm -rf ~/.m2/repository/org/junit
echo ""

echo -e "${BLUE}3️⃣  Reinstalando dependências...${NC}"
./mvnw clean install -U
echo ""

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Build bem-sucedido!${NC}"
    echo ""
    
    echo -e "${BLUE}4️⃣  Verificando JUnit...${NC}"
    ./mvnw dependency:tree | grep junit
    echo ""
    
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo ""
    echo -e "${GREEN}✅ Correção concluída!${NC}"
    echo ""
    echo "📝 Próximos passos no IntelliJ:"
    echo ""
    echo "1. Recarregar Maven:"
    echo "   ${YELLOW}Ctrl + Shift + O${NC}"
    echo ""
    echo "2. Invalidar Cache:"
    echo "   ${YELLOW}File → Invalidate Caches / Restart${NC}"
    echo ""
    echo "3. Testar:"
    echo "   ${YELLOW}Abra UsuarioServiceTest.java${NC}"
    echo "   ${YELLOW}Clique no ▶ verde ao lado da classe${NC}"
    echo ""
else
    echo -e "${RED}❌ Erro no build!${NC}"
    echo ""
    echo "Verifique os erros acima e tente:"
    echo "1. Verificar conexão com internet"
    echo "2. Verificar se JDK 17 está instalado"
    echo "3. Executar: ${YELLOW}./mvnw clean install -X${NC} (modo debug)"
    exit 1
fi
