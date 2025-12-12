# 🔧 Resolver Erro: Cannot resolve symbol 'junit'

## 📋 Problema

```
Cannot resolve symbol 'junit'
Arquivo: OcorrenciaControllerTest.java
Linha: 8 (import org.junit.jupiter.api.*;)
```

## 🔍 Causa

O erro ocorre porque a IDE (IntelliJ IDEA) não conseguiu sincronizar as dependências do Maven. A dependência do JUnit **já está correta** no `pom.xml`, mas precisa ser carregada pela IDE.

### Verificação da Dependência

No arquivo `backend/pom.xml` (linhas 99-103):

```xml
<!-- JUnit 5 (Jupiter) - Explícito -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
```

✅ **A dependência está correta!** O problema é apenas de sincronização.

## ✅ Soluções

### Solução 1: Via IntelliJ IDEA (Mais Rápido) ⭐

Esta é a solução **recomendada** e mais simples:

#### Passo 1: Recarregar Maven
1. Abra a aba **Maven** (lateral direita do IntelliJ)
2. Clique no ícone 🔄 **Reload All Maven Projects**
3. Aguarde o download das dependências (pode levar alguns minutos)

#### Passo 2: Invalidar Cache
1. Menu: **File** → **Invalidate Caches / Restart...**
2. Selecione: **Invalidate and Restart**
3. Aguarde o IntelliJ reiniciar (1-2 minutos)

#### Passo 3: Verificar
1. Abra o arquivo `OcorrenciaControllerTest.java`
2. O erro na linha 8 deve ter desaparecido
3. O import `org.junit.jupiter.api.*` deve estar em verde

### Solução 2: Via Terminal com Maven

Se você tem Maven instalado no sistema:

```bash
cd /home/michellevictoriano/Documentos/conectapg
./resolver-dependencias.sh
```

Ou manualmente:

```bash
cd backend
mvn clean
mvn dependency:resolve
mvn compile test-compile
```

Depois, no IntelliJ:
- **File** → **Invalidate Caches / Restart**

### Solução 3: Via Docker (sem Maven instalado)

Se você não tem Maven mas tem Docker:

```bash
cd /home/michellevictoriano/Documentos/conectapg
./resolver-dependencias-docker.sh
```

Depois, no IntelliJ:
- **File** → **Invalidate Caches / Restart**

## 🎯 Por Que Isso Acontece?

### Causas Comuns

1. **Primeira vez abrindo o projeto**
   - IntelliJ ainda não baixou as dependências
   
2. **Mudanças no pom.xml**
   - Dependências foram adicionadas mas não sincronizadas
   
3. **Cache desatualizado**
   - IntelliJ está usando informações antigas

4. **Interrupção durante download**
   - Download das dependências foi interrompido

## 🔍 Como Verificar se Funcionou

### 1. Verificar Imports
Abra `OcorrenciaControllerTest.java` e verifique se estas linhas estão sem erro:

```java
import org.junit.jupiter.api.*;  // ✅ Deve estar em verde
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
```

### 2. Verificar External Libraries
No IntelliJ:
1. Expanda **External Libraries** (painel esquerdo)
2. Procure por **Maven: org.junit.jupiter:junit-jupiter**
3. Deve estar presente e expandível

### 3. Executar Teste
1. Abra `OcorrenciaControllerTest.java`
2. Clique com botão direito na classe
3. Selecione **Run 'OcorrenciaControllerTest'**
4. Os testes devem executar (mesmo que falhem por outros motivos)

## ⚠️ Problemas Persistentes?

Se o erro continuar após todas as soluções:

### 1. Verificar Estrutura do Projeto
```bash
cd /home/michellevictoriano/Documentos/conectapg
./verificar-configuracao.sh
```

### 2. Limpar Completamente
No IntelliJ:
1. **File** → **Close Project**
2. Deletar pasta `.idea` do projeto
3. Reabrir o projeto
4. Aguardar indexação completa

### 3. Verificar Configuração do Maven
No IntelliJ:
1. **File** → **Settings** → **Build, Execution, Deployment** → **Build Tools** → **Maven**
2. Verificar se **Maven home path** está configurado
3. Verificar se **User settings file** aponta para arquivo válido

### 4. Verificar JDK
No IntelliJ:
1. **File** → **Project Structure** → **Project**
2. Verificar se **SDK** é Java 17
3. Verificar se **Language level** é 17

## 📊 Checklist de Resolução

- [ ] Recarreguei Maven no IntelliJ (🔄 Reload All Maven Projects)
- [ ] Invalidei cache (File → Invalidate Caches / Restart)
- [ ] Aguardei IntelliJ reiniciar completamente
- [ ] Aguardei indexação terminar (barra de progresso inferior)
- [ ] Verifiquei que import está em verde
- [ ] Verifiquei External Libraries contém JUnit
- [ ] Consegui executar o teste

## 🎓 Entendendo o Problema

### O que é "Cannot resolve symbol"?

Este erro significa que a IDE não consegue encontrar a classe/pacote no classpath. Não é um erro de compilação do Maven, mas sim da IDE.

### Por que o pom.xml está correto mas o erro aparece?

O `pom.xml` define as dependências, mas a IDE precisa:
1. **Baixar** os JARs do repositório Maven
2. **Indexar** as classes dentro dos JARs
3. **Adicionar** ao classpath do projeto

Se qualquer etapa falhar, o erro aparece.

### Por que "Invalidate Caches" resolve?

O IntelliJ mantém um cache de todas as classes e símbolos do projeto. Se esse cache ficar desatualizado ou corrompido, a IDE não encontra as classes mesmo que elas existam. Invalidar o cache força a IDE a reconstruir todas as informações.

## 📚 Arquivos Relacionados

- `backend/pom.xml` - Configuração de dependências
- `backend/src/test/java/com/conectapg/api/controller/OcorrenciaControllerTest.java` - Arquivo com erro
- `verificar-configuracao.sh` - Script de verificação geral
- `resolver-dependencias.sh` - Script para resolver via Maven
- `resolver-dependencias-docker.sh` - Script para resolver via Docker

## 🚀 Próximos Passos

Após resolver o erro:

1. ✅ Executar todos os testes
2. ✅ Configurar Run Configuration para aplicação
3. ✅ Iniciar desenvolvimento

---

**Status**: 📝 Guia completo de resolução

**Solução Recomendada**: Usar IntelliJ IDEA (Solução 1) - é mais rápido e confiável.
