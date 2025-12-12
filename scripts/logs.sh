#!/bin/bash

# Script para visualizar logs do projeto ConectaPG

if [ -z "$1" ]; then
    echo "📝 Mostrando logs de todos os containers..."
    docker compose logs -f
else
    echo "📝 Mostrando logs do container: $1"
    docker compose logs -f $1
fi
