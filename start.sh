#!/bin/bash

# Script para iniciar o projeto ConectaPG

echo "🚀 Iniciando ConectaPG..."
echo ""

# Verifica se o Docker está rodando
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker não está rodando. Por favor, inicie o Docker primeiro."
    exit 1
fi

# Verifica se o arquivo .env existe, senão cria a partir do .env.example
if [ ! -f .env ]; then
    echo "📝 Criando arquivo .env a partir do .env.example..."
    cp .env.example .env
    echo "✅ Arquivo .env criado. Você pode editá-lo se necessário."
    echo ""
fi

# Para containers existentes
echo "🛑 Parando containers existentes..."
docker compose down

# Inicia os containers
echo "🐳 Iniciando containers..."
docker compose up -d

# Aguarda o PostgreSQL ficar pronto
echo "⏳ Aguardando PostgreSQL inicializar..."
sleep 5

# Mostra os logs
echo ""
echo "✅ Containers iniciados!"
echo ""
echo "📊 Status dos containers:"
docker compose ps
echo ""
echo "📝 Para ver os logs em tempo real, use:"
echo "   docker compose logs -f"
echo ""
echo "🌐 Acesse a aplicação em:"
echo "   - API: http://localhost:8081/api"
echo "   - Swagger: http://localhost:8081/api/swagger-ui.html"
echo ""
echo "🗄️  PostgreSQL disponível em:"
echo "   - Host: localhost"
echo "   - Port: 5432"
echo "   - Database: conectapg"
echo "   - User: postgres"
echo "   - Password: postgres123"
