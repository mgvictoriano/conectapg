#!/bin/bash

# Script para iniciar o projeto ConectaPG usando Docker direto

echo "🚀 Iniciando ConectaPG..."
echo ""

# Verifica se o Docker está rodando
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker não está rodando. Por favor, inicie o Docker primeiro."
    exit 1
fi

# Cria a rede se não existir
echo "🌐 Criando rede Docker..."
docker network create conectapg-network 2>/dev/null || echo "   Rede já existe"

# Para containers existentes
echo "🛑 Parando containers existentes..."
docker stop conectapg-backend conectapg-postgres 2>/dev/null || true
docker rm conectapg-backend conectapg-postgres 2>/dev/null || true

# Inicia PostgreSQL
echo "🐘 Iniciando PostgreSQL..."
docker run -d \
  --name conectapg-postgres \
  --network conectapg-network \
  -e POSTGRES_DB=conectapg \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres123 \
  -p 5432:5432 \
  postgres:15-alpine

# Aguarda PostgreSQL inicializar
echo "⏳ Aguardando PostgreSQL inicializar..."
sleep 5

# Verifica se a imagem do backend existe, senão faz build
if ! docker image inspect conectapg-backend:latest > /dev/null 2>&1; then
    echo "🏗️  Fazendo build da aplicação (primeira vez pode demorar)..."
    cd backend
    docker build -t conectapg-backend:latest .
    cd ..
fi

# Inicia o backend
echo "☕ Iniciando aplicação Spring Boot..."
docker run -d \
  --name conectapg-backend \
  --network conectapg-network \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://conectapg-postgres:5432/conectapg \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=postgres123 \
  -e SPRING_PROFILES_ACTIVE=dev \
  -p 8081:8080 \
  conectapg-backend:latest

# Aguarda a aplicação inicializar
echo "⏳ Aguardando aplicação inicializar..."
sleep 10

# Mostra os logs
echo ""
echo "✅ Containers iniciados!"
echo ""
echo "📊 Status dos containers:"
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
echo ""
echo "📝 Para ver os logs em tempo real, use:"
echo "   docker logs -f conectapg-backend"
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
