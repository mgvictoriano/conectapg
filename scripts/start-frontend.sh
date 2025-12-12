#!/bin/bash

echo "🎨 Iniciando Frontend ConectaPG..."
echo ""

# Verifica se Node.js está instalado
if ! command -v node &> /dev/null; then
    echo "❌ Node.js não está instalado!"
    echo ""
    echo "📦 Instalando Node.js 18..."
    echo "Execute os seguintes comandos:"
    echo ""
    echo "  curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -"
    echo "  sudo apt-get install -y nodejs"
    echo ""
    exit 1
fi

echo "✅ Node.js encontrado: $(node --version)"
echo "✅ npm encontrado: $(npm --version)"
echo ""

# Vai para o diretório do frontend
cd frontend

# Verifica se node_modules existe
if [ ! -d "node_modules" ]; then
    echo "📦 Instalando dependências (primeira vez pode demorar)..."
    npm install
    echo ""
fi

# Verifica se o backend está rodando
echo "🔍 Verificando backend..."
if curl -s http://localhost:8081/api/usuarios > /dev/null 2>&1; then
    echo "✅ Backend está rodando em http://localhost:8081"
else
    echo "⚠️  Backend não está respondendo em http://localhost:8081"
    echo ""
    echo "💡 Para iniciar o backend, execute em outro terminal:"
    echo "   ./start-docker.sh"
    echo ""
fi

echo ""
echo "🚀 Iniciando servidor de desenvolvimento..."
echo ""
echo "📱 Acesse a aplicação em:"
echo "   http://localhost:3000"
echo ""
echo "🔐 Usuários de teste:"
echo "   Admin: admin@conectapg.com / password"
echo "   Cidadão: joao@example.com / password"
echo ""
echo "⏹️  Para parar: Ctrl+C"
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# Inicia o Vite
npm run dev
