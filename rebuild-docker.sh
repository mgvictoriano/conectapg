#!/bin/bash

# Script para rebuild completo do projeto ConectaPG

echo "🔨 Rebuild completo do ConectaPG..."
echo ""

# Para e remove containers
echo "🛑 Parando containers..."
docker stop conectapg-backend conectapg-postgres 2>/dev/null || true
docker rm conectapg-backend conectapg-postgres 2>/dev/null || true

# Remove a imagem antiga
echo "🗑️  Removendo imagem antiga..."
docker rmi conectapg-backend:latest 2>/dev/null || true

# Rebuild da imagem
echo "🏗️  Reconstruindo imagem..."
cd backend
docker build --no-cache -t conectapg-backend:latest .
cd ..

# Inicia os containers
echo "🚀 Iniciando containers..."
./start-docker.sh
