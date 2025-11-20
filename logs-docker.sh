#!/bin/bash

# Script para visualizar logs do projeto ConectaPG

if [ -z "$1" ]; then
    echo "📝 Mostrando logs do backend..."
    docker logs -f conectapg-backend
else
    echo "📝 Mostrando logs do container: $1"
    docker logs -f $1
fi
