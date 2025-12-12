#!/bin/bash

echo "🔧 Resolvendo dependências Maven..."

cd backend

# Verifica se Maven está instalado
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven não está instalado no sistema"
    echo ""
    echo "📝 Soluções alternativas:"
    echo "1. Instalar Maven: sudo apt install maven"
    echo "2. Usar IntelliJ IDEA para recarregar o projeto Maven"
    echo "3. Usar Docker: docker run -it --rm -v \"\$PWD\":/app -w /app maven:3.9-eclipse-temurin-17 mvn dependency:resolve"
    exit 1
fi

echo "✅ Maven encontrado"
echo ""

# Limpa e baixa dependências
echo "🧹 Limpando projeto..."
mvn clean

echo ""
echo "📦 Baixando dependências..."
mvn dependency:resolve dependency:resolve-plugins

echo ""
echo "🔨 Compilando projeto..."
mvn compile test-compile

echo ""
echo "✅ Dependências resolvidas!"
echo ""
echo "📝 Próximos passos no IntelliJ:"
echo "1. File → Invalidate Caches / Restart"
echo "2. Aguarde o IntelliJ reiniciar"
echo "3. O erro deve desaparecer"
