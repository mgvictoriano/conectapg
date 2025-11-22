#!/bin/bash

# Script para resolver problemas com JUnit no IntelliJ IDEA

echo "=== Resolvendo problemas com JUnit ==="
echo ""

# 1. Limpar cache do Maven local
echo "1. Limpando cache do Maven local..."
rm -rf ~/.m2/repository/org/junit
rm -rf ~/.m2/repository/org/mockito
rm -rf ~/.m2/repository/org/assertj
echo "✓ Cache limpo"
echo ""

# 2. Limpar target do projeto
echo "2. Limpando diretório target..."
cd /home/michellevictoriano/Documentos/conectapg/backend
rm -rf target
echo "✓ Target limpo"
echo ""

# 3. Verificar se Maven está instalado
echo "3. Verificando Maven..."
if command -v mvn &> /dev/null; then
    echo "✓ Maven encontrado: $(mvn -version | head -n 1)"
    MVN_CMD="mvn"
elif [ -f "mvnw" ]; then
    echo "✓ Maven Wrapper encontrado"
    MVN_CMD="./mvnw"
else
    echo "⚠ Maven não encontrado. Use o IntelliJ para executar os comandos Maven."
    MVN_CMD=""
fi
echo ""

# 4. Baixar dependências (se Maven disponível)
if [ -n "$MVN_CMD" ]; then
    echo "4. Baixando dependências..."
    $MVN_CMD dependency:purge-local-repository -DactTransitively=false -DreResolve=false
    $MVN_CMD dependency:resolve
    echo "✓ Dependências baixadas"
else
    echo "4. Pule esta etapa - use o IntelliJ:"
    echo "   - Abra a aba Maven (lateral direita)"
    echo "   - Clique em 'Reload All Maven Projects' (ícone 🔄)"
fi
echo ""

echo "=== Próximos passos no IntelliJ IDEA ==="
echo ""
echo "1. File → Invalidate Caches / Restart → Invalidate and Restart"
echo ""
echo "2. Após reiniciar, abra a aba Maven (lateral direita)"
echo "   - Clique no ícone de refresh (🔄)"
echo "   - Execute: conectapg-backend → Lifecycle → clean"
echo "   - Execute: conectapg-backend → Lifecycle → compile"
echo ""
echo "3. Verifique as dependências:"
echo "   - Maven → conectapg-backend → Dependencies"
echo "   - Procure por: junit-jupiter, mockito-core, assertj-core"
echo ""
echo "4. Configure o Run Configuration:"
echo "   - Run → Edit Configurations..."
echo "   - Crie uma configuração JUnit para seus testes"
echo ""
echo "5. Teste executando um teste unitário:"
echo "   - Abra: UsuarioServiceTest.java"
echo "   - Clique com botão direito → Run 'UsuarioServiceTest'"
echo ""
echo "=== Verificação das dependências baixadas ==="
echo ""

# Verificar se os JARs foram baixados
JUNIT_JAR=$(find ~/.m2/repository/org/junit/jupiter/junit-jupiter -name "*.jar" 2>/dev/null | head -n 1)
MOCKITO_JAR=$(find ~/.m2/repository/org/mockito/mockito-core -name "*.jar" 2>/dev/null | head -n 1)

if [ -n "$JUNIT_JAR" ]; then
    echo "✓ JUnit encontrado: $JUNIT_JAR"
else
    echo "✗ JUnit NÃO encontrado"
fi

if [ -n "$MOCKITO_JAR" ]; then
    echo "✓ Mockito encontrado: $MOCKITO_JAR"
else
    echo "✗ Mockito NÃO encontrado"
fi

echo ""
echo "=== Script concluído ==="
