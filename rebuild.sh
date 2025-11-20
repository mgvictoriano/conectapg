#!/bin/bash

# Script para rebuild completo do projeto ConectaPG

echo "🔨 Rebuild completo do ConectaPG..."
echo ""

# Para e remove containers
echo "🛑 Parando containers..."
docker compose down

# Rebuild das imagens
echo "🏗️  Reconstruindo imagens..."
docker compose build --no-cache

# Inicia os containers
echo "🚀 Iniciando containers..."
docker compose up -d

# Aguarda inicialização
echo "⏳ Aguardando inicialização..."
sleep 10

# Mostra status
echo ""
echo "✅ Rebuild concluído!"
echo ""
docker compose ps
