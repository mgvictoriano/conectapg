#!/bin/bash

echo "🔍 Testando Conexão Frontend ↔ Backend"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# Cores
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Teste 1: Verificar se o backend está rodando
echo "1️⃣  Verificando Backend (porta 8081)..."
if curl -s http://localhost:8081/api/usuarios > /dev/null 2>&1; then
    echo -e "${GREEN}✅ Backend está rodando e respondendo!${NC}"
    echo ""
    echo "   Testando endpoint /api/usuarios:"
    RESPONSE=$(curl -s http://localhost:8081/api/usuarios | jq -r '.[0].nome' 2>/dev/null || echo "erro")
    if [ "$RESPONSE" != "erro" ]; then
        echo -e "   ${GREEN}✅ Dados recebidos: Primeiro usuário = $RESPONSE${NC}"
    fi
else
    echo -e "${RED}❌ Backend NÃO está respondendo!${NC}"
    echo ""
    echo "   Para iniciar o backend, execute:"
    echo "   ${YELLOW}./start-docker.sh${NC}"
    echo ""
fi

echo ""

# Teste 2: Verificar containers Docker
echo "2️⃣  Verificando Containers Docker..."
if docker ps --format "table {{.Names}}\t{{.Status}}" | grep -q "conectapg"; then
    echo -e "${GREEN}✅ Containers encontrados:${NC}"
    docker ps --format "   {{.Names}}\t{{.Status}}" | grep "conectapg"
else
    echo -e "${RED}❌ Nenhum container do ConectaPG rodando${NC}"
fi

echo ""

# Teste 3: Verificar Node.js
echo "3️⃣  Verificando Node.js..."
if command -v node &> /dev/null; then
    NODE_VERSION=$(node --version)
    NPM_VERSION=$(npm --version)
    echo -e "${GREEN}✅ Node.js instalado: $NODE_VERSION${NC}"
    echo -e "${GREEN}✅ npm instalado: $NPM_VERSION${NC}"
else
    echo -e "${RED}❌ Node.js NÃO está instalado!${NC}"
    echo ""
    echo "   Para instalar, execute:"
    echo "   ${YELLOW}curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -${NC}"
    echo "   ${YELLOW}sudo apt-get install -y nodejs${NC}"
fi

echo ""

# Teste 4: Verificar dependências do frontend
echo "4️⃣  Verificando dependências do frontend..."
if [ -d "frontend/node_modules" ]; then
    echo -e "${GREEN}✅ Dependências instaladas${NC}"
else
    echo -e "${YELLOW}⚠️  Dependências NÃO instaladas${NC}"
    echo ""
    echo "   Para instalar, execute:"
    echo "   ${YELLOW}cd frontend && npm install${NC}"
fi

echo ""

# Teste 5: Verificar configuração do proxy
echo "5️⃣  Verificando configuração do Vite Proxy..."
if grep -q "http://localhost:8081" frontend/vite.config.js 2>/dev/null; then
    echo -e "${GREEN}✅ Proxy configurado corretamente${NC}"
    echo "   /api → http://localhost:8081"
else
    echo -e "${RED}❌ Configuração do proxy não encontrada${NC}"
fi

echo ""

# Teste 6: Verificar CORS no backend
echo "6️⃣  Verificando CORS no backend..."
if [ -f "backend/src/main/java/com/conectapg/config/SecurityConfig.java" ]; then
    if grep -q "localhost:3000" backend/src/main/java/com/conectapg/config/SecurityConfig.java; then
        echo -e "${GREEN}✅ CORS configurado para aceitar localhost:3000${NC}"
    else
        echo -e "${YELLOW}⚠️  CORS pode não estar configurado${NC}"
    fi
else
    echo -e "${YELLOW}⚠️  Arquivo SecurityConfig.java não encontrado${NC}"
fi

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# Resumo
echo "📊 RESUMO:"
echo ""

BACKEND_OK=false
FRONTEND_OK=false

if curl -s http://localhost:8081/api/usuarios > /dev/null 2>&1; then
    BACKEND_OK=true
fi

if command -v node &> /dev/null && [ -d "frontend/node_modules" ]; then
    FRONTEND_OK=true
fi

if [ "$BACKEND_OK" = true ] && [ "$FRONTEND_OK" = true ]; then
    echo -e "${GREEN}✅ TUDO PRONTO!${NC}"
    echo ""
    echo "Para iniciar o frontend, execute:"
    echo "   ${YELLOW}./start-frontend.sh${NC}"
    echo ""
    echo "Depois acesse: ${YELLOW}http://localhost:3000${NC}"
elif [ "$BACKEND_OK" = true ]; then
    echo -e "${YELLOW}⚠️  Backend OK, mas frontend precisa de configuração${NC}"
    echo ""
    echo "Execute:"
    echo "   ${YELLOW}cd frontend && npm install${NC}"
    echo "   ${YELLOW}./start-frontend.sh${NC}"
elif [ "$FRONTEND_OK" = true ]; then
    echo -e "${YELLOW}⚠️  Frontend OK, mas backend não está rodando${NC}"
    echo ""
    echo "Execute:"
    echo "   ${YELLOW}./start-docker.sh${NC}"
else
    echo -e "${RED}❌ Backend e Frontend precisam de configuração${NC}"
    echo ""
    echo "1. Inicie o backend: ${YELLOW}./start-docker.sh${NC}"
    echo "2. Instale Node.js (se necessário)"
    echo "3. Instale dependências: ${YELLOW}cd frontend && npm install${NC}"
    echo "4. Inicie o frontend: ${YELLOW}./start-frontend.sh${NC}"
fi

echo ""
