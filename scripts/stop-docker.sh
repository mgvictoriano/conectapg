#!/bin/bash

# Script para parar o projeto ConectaPG

echo "🛑 Parando ConectaPG..."
docker stop conectapg-backend conectapg-postgres 2>/dev/null || true
docker rm conectapg-backend conectapg-postgres 2>/dev/null || true

echo ""
echo "✅ Containers parados!"
echo ""
echo "💡 Para remover os volumes (dados do banco), use:"
echo "   docker volume rm conectapg_postgres_data"
