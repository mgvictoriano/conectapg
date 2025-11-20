#!/bin/bash

# Script para parar o projeto ConectaPG

echo "🛑 Parando ConectaPG..."
docker compose down

echo ""
echo "✅ Containers parados!"
echo ""
echo "💡 Para remover os volumes (dados do banco), use:"
echo "   docker compose down -v"
