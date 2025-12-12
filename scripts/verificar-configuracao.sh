#!/bin/bash

# Script para verificar se as configurações estão corretas

echo "╔════════════════════════════════════════════════════════════╗"
echo "║  Verificação de Configuração - ConectaPG                  ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

cd /home/michellevictoriano/Documentos/conectapg/backend

# Cores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Função para verificar
check() {
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓${NC} $1"
    else
        echo -e "${RED}✗${NC} $1"
    fi
}

# 1. Verificar estrutura de arquivos
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "1. Verificando estrutura de arquivos..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

if [ -f "src/main/java/com/conectapg/ConectaPgApplication.java" ]; then
    echo -e "${GREEN}✓${NC} ConectaPgApplication.java existe"
else
    echo -e "${RED}✗${NC} ConectaPgApplication.java NÃO encontrado"
fi

if [ -f "pom.xml" ]; then
    echo -e "${GREEN}✓${NC} pom.xml existe"
else
    echo -e "${RED}✗${NC} pom.xml NÃO encontrado"
fi

echo ""

# 2. Verificar anotações na classe principal
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "2. Verificando anotações em ConectaPgApplication..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

APP_FILE="src/main/java/com/conectapg/ConectaPgApplication.java"

if grep -q "@SpringBootApplication" "$APP_FILE"; then
    echo -e "${GREEN}✓${NC} @SpringBootApplication presente"
else
    echo -e "${RED}✗${NC} @SpringBootApplication AUSENTE"
fi

if grep -q "@ComponentScan" "$APP_FILE"; then
    echo -e "${GREEN}✓${NC} @ComponentScan presente"
else
    echo -e "${RED}✗${NC} @ComponentScan AUSENTE"
fi

if grep -q "@EnableCaching" "$APP_FILE"; then
    echo -e "${GREEN}✓${NC} @EnableCaching presente"
else
    echo -e "${RED}✗${NC} @EnableCaching AUSENTE"
fi

if grep -q "@PostConstruct" "$APP_FILE"; then
    echo -e "${GREEN}✓${NC} @PostConstruct presente"
else
    echo -e "${RED}✗${NC} @PostConstruct AUSENTE"
fi

if grep -q "TimeZone.setDefault" "$APP_FILE"; then
    echo -e "${GREEN}✓${NC} Configuração de timezone presente"
else
    echo -e "${RED}✗${NC} Configuração de timezone AUSENTE"
fi

echo ""

# 3. Verificar dependências no POM
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "3. Verificando dependências no pom.xml..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

if grep -q "spring-boot-starter-cache" "pom.xml"; then
    echo -e "${GREEN}✓${NC} spring-boot-starter-cache presente"
else
    echo -e "${RED}✗${NC} spring-boot-starter-cache AUSENTE"
fi

if grep -q "spring-boot-starter-test" "pom.xml"; then
    echo -e "${GREEN}✓${NC} spring-boot-starter-test presente"
else
    echo -e "${RED}✗${NC} spring-boot-starter-test AUSENTE"
fi

if grep -q "junit-jupiter" "pom.xml"; then
    echo -e "${GREEN}✓${NC} junit-jupiter presente"
else
    echo -e "${RED}✗${NC} junit-jupiter AUSENTE"
fi

if grep -q "mockito-core" "pom.xml"; then
    echo -e "${GREEN}✓${NC} mockito-core presente"
else
    echo -e "${RED}✗${NC} mockito-core AUSENTE"
fi

echo ""

# 4. Verificar configuração do Maven Surefire
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "4. Verificando Maven Surefire Plugin..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

if grep -q "maven-surefire-plugin" "pom.xml"; then
    echo -e "${GREEN}✓${NC} maven-surefire-plugin presente"
    
    if grep -A5 "maven-surefire-plugin" "pom.xml" | grep -q "2.22.2"; then
        echo -e "${GREEN}✓${NC} Versão 2.22.2 (recomendada)"
    else
        echo -e "${YELLOW}⚠${NC} Versão diferente de 2.22.2"
    fi
    
    if grep -q "useSystemClassLoader" "pom.xml"; then
        echo -e "${GREEN}✓${NC} useSystemClassLoader configurado"
    else
        echo -e "${YELLOW}⚠${NC} useSystemClassLoader não configurado"
    fi
else
    echo -e "${RED}✗${NC} maven-surefire-plugin AUSENTE"
fi

echo ""

# 5. Verificar repositório Maven local
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "5. Verificando repositório Maven local..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

if [ -d "$HOME/.m2/repository/org/junit/jupiter" ]; then
    JUNIT_VERSION=$(ls -1 "$HOME/.m2/repository/org/junit/jupiter/junit-jupiter" 2>/dev/null | tail -n 1)
    if [ -n "$JUNIT_VERSION" ]; then
        echo -e "${GREEN}✓${NC} JUnit Jupiter encontrado (versão: $JUNIT_VERSION)"
    else
        echo -e "${YELLOW}⚠${NC} JUnit Jupiter: diretório existe mas sem versão"
    fi
else
    echo -e "${RED}✗${NC} JUnit Jupiter NÃO encontrado em ~/.m2/repository"
fi

if [ -d "$HOME/.m2/repository/org/mockito/mockito-core" ]; then
    MOCKITO_VERSION=$(ls -1 "$HOME/.m2/repository/org/mockito/mockito-core" 2>/dev/null | tail -n 1)
    if [ -n "$MOCKITO_VERSION" ]; then
        echo -e "${GREEN}✓${NC} Mockito encontrado (versão: $MOCKITO_VERSION)"
    else
        echo -e "${YELLOW}⚠${NC} Mockito: diretório existe mas sem versão"
    fi
else
    echo -e "${RED}✗${NC} Mockito NÃO encontrado em ~/.m2/repository"
fi

echo ""

# 6. Verificar testes
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "6. Verificando arquivos de teste..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

TEST_COUNT=$(find src/test/java -name "*Test.java" 2>/dev/null | wc -l)
echo -e "${GREEN}✓${NC} Encontrados $TEST_COUNT arquivos de teste"

if [ -f "src/test/java/com/conectapg/ServerTest.java" ]; then
    echo -e "${GREEN}✓${NC} ServerTest.java (classe base) encontrado"
else
    echo -e "${YELLOW}⚠${NC} ServerTest.java não encontrado"
fi

echo ""

# 7. Resumo e recomendações
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "7. Resumo e Próximos Passos"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# Contar problemas
PROBLEMS=0

if ! grep -q "@ComponentScan" "$APP_FILE"; then
    ((PROBLEMS++))
fi

if ! grep -q "@EnableCaching" "$APP_FILE"; then
    ((PROBLEMS++))
fi

if ! grep -q "spring-boot-starter-cache" "pom.xml"; then
    ((PROBLEMS++))
fi

if [ ! -d "$HOME/.m2/repository/org/junit/jupiter" ]; then
    ((PROBLEMS++))
fi

if [ $PROBLEMS -eq 0 ]; then
    echo -e "${GREEN}✓ Configuração completa!${NC}"
    echo ""
    echo "Próximos passos:"
    echo "1. No IntelliJ: Maven → Reload All Maven Projects (ícone 🔄)"
    echo "2. File → Invalidate Caches / Restart"
    echo "3. Executar um teste: Run → Run 'UsuarioServiceTest'"
    echo "4. Executar aplicação: Run → Run 'ConectaPgApplication'"
else
    echo -e "${YELLOW}⚠ Encontrados $PROBLEMS problemas${NC}"
    echo ""
    echo "Ações recomendadas:"
    
    if ! grep -q "@ComponentScan" "$APP_FILE" || ! grep -q "@EnableCaching" "$APP_FILE"; then
        echo "1. Verificar ConectaPgApplication.java"
    fi
    
    if ! grep -q "spring-boot-starter-cache" "pom.xml"; then
        echo "2. Adicionar spring-boot-starter-cache ao pom.xml"
    fi
    
    if [ ! -d "$HOME/.m2/repository/org/junit/jupiter" ]; then
        echo "3. Executar: ./resolver-junit.sh"
        echo "4. No IntelliJ: Maven → Reload All Maven Projects"
    fi
fi

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Verificação concluída!"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
