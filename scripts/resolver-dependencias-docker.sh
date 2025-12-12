#!/bin/bash

echo "🐳 Resolvendo dependências Maven via Docker..."
echo ""

cd backend

# Verifica se Docker está instalado
if ! command -v docker &> /dev/null; then
    echo "❌ Docker não está instalado no sistema"
    echo ""
    echo "📝 Use a solução via IntelliJ IDEA:"
    echo "1. Abra a aba Maven (lateral direita)"
    echo "2. Clique em 🔄 Reload All Maven Projects"
    echo "3. File → Invalidate Caches / Restart"
    exit 1
fi

echo "✅ Docker encontrado"
echo ""

# Limpa e baixa dependências usando Docker
echo "🧹 Limpando projeto..."
docker run -it --rm \
    -v "$PWD":/app \
    -w /app \
    maven:3.9-eclipse-temurin-17 \
    mvn clean

echo ""
echo "📦 Baixando dependências..."
docker run -it --rm \
    -v "$PWD":/app \
    -w /app \
    maven:3.9-eclipse-temurin-17 \
    mvn dependency:resolve dependency:resolve-plugins

echo ""
echo "🔨 Compilando projeto..."
docker run -it --rm \
    -v "$PWD":/app \
    -w /app \
    maven:3.9-eclipse-temurin-17 \
    mvn compile test-compile

echo ""
echo "✅ Dependências resolvidas!"
echo ""
echo "📝 Próximos passos no IntelliJ:"
echo "1. File → Invalidate Caches / Restart"
echo "2. Aguarde o IntelliJ reiniciar"
echo "3. O erro deve desaparecer"
