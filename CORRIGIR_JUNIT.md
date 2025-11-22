# 🔧 Correção: Erro JUnit.jar

## ❌ Problema
```
Erro: não encontrou junit.jar
```

## ✅ Solução Implementada

Atualizei o `pom.xml` com:

### 1. **Dependências Explícitas do JUnit**
```xml
<!-- JUnit 5 (Jupiter) -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>

<!-- AssertJ -->
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <scope>test</scope>
</dependency>

<!-- Mockito -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
```

### 2. **Maven Surefire Plugin**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0</version>
</plugin>
```

### 3. **Maven Failsafe Plugin** (para testes de integração)
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-failsafe-plugin</artifactId>
    <version>3.0.0</version>
</plugin>
```

## 🚀 Passos para Corrigir

### 1️⃣ Recarregar Maven no IntelliJ

**Opção 1: Botão Reload**
```
1. Abra a aba "Maven" (lateral direita)
2. Clique no ícone de reload (🔄)
3. Aguarde download das dependências
```

**Opção 2: Menu**
```
1. Clique direito no pom.xml
2. Maven → Reload Project
```

**Opção 3: Atalho**
```
Ctrl + Shift + O (recarrega projeto Maven)
```

### 2️⃣ Limpar e Reinstalar

No terminal do IntelliJ (Alt + F12):
```bash
cd backend
./mvnw clean install
```

Ou se preferir Maven global:
```bash
cd backend
mvn clean install
```

### 3️⃣ Invalidar Cache (se necessário)

Se ainda não funcionar:
```
1. File → Invalidate Caches / Restart
2. Selecione "Invalidate and Restart"
3. Aguarde o IntelliJ reiniciar
```

### 4️⃣ Verificar JDK

Certifique-se que está usando JDK 17:
```
1. File → Project Structure (Ctrl + Alt + Shift + S)
2. Project → SDK: 17
3. Modules → conectapg-backend → Language Level: 17
```

## 🧪 Testar se Funcionou

### No Terminal
```bash
cd backend
./mvnw test
```

Se aparecer:
```
[INFO] Tests run: X, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

✅ **Funcionou!**

### No IntelliJ

1. Abra qualquer classe de teste
2. Clique no ícone verde (▶) ao lado da classe
3. Selecione "Run 'NomeDoTeste'"

Se os testes executarem: ✅ **Funcionou!**

## 🔍 Verificar Dependências

### Ver se JUnit foi baixado

**No IntelliJ:**
```
1. Abra aba "Maven" (lateral direita)
2. Expanda "Dependencies"
3. Procure por "junit-jupiter"
```

**No terminal:**
```bash
cd backend
./mvnw dependency:tree | grep junit
```

Deve aparecer:
```
[INFO] +- org.junit.jupiter:junit-jupiter:jar:5.10.1:test
```

## 🐛 Problemas Comuns

### 1. "Cannot resolve symbol 'Test'"

**Causa:** IDE não reconhece as anotações

**Solução:**
```
1. Recarregue Maven (Ctrl + Shift + O)
2. File → Invalidate Caches / Restart
```

### 2. "No tests found"

**Causa:** Maven Surefire não encontra os testes

**Solução:** Já configurado no pom.xml com:
```xml
<includes>
    <include>**/*Test.java</include>
    <include>**/*IntegrationTest.java</include>
</includes>
```

### 3. "Spring Boot context failed to load"

**Causa:** Configuração de teste incorreta

**Solução:** Verifique se existe:
- ✅ `src/test/resources/application-test.yml`
- ✅ H2 dependency no pom.xml
- ✅ `@SpringBootTest` na classe de teste

### 4. "Module not specified"

**Causa:** Run Configuration sem módulo

**Solução:**
```
1. Run → Edit Configurations
2. Selecione a configuração
3. Module: conectapg-backend
4. Apply → OK
```

## 📊 Estrutura Correta

Verifique se sua estrutura está assim:

```
backend/
├── pom.xml                              ✅ Atualizado
├── src/
│   ├── main/java/
│   │   └── com/conectapg/
│   │       └── ConectaPgApplication.java
│   └── test/
│       ├── java/com/conectapg/
│       │   ├── ServerTest.java          ✅ Classe base
│       │   ├── domain/service/
│       │   │   ├── UsuarioServiceTest.java
│       │   │   └── UsuarioServiceIntegrationTest.java
│       │   └── api/controller/
│       │       └── UsuarioControllerTest.java
│       └── resources/
│           └── application-test.yml     ✅ Config de teste
```

## ✅ Checklist Final

Antes de executar os testes, verifique:

- [ ] Maven recarregado (Ctrl + Shift + O)
- [ ] Dependências baixadas (veja aba Maven)
- [ ] JDK 17 configurado
- [ ] `./mvnw clean install` executado com sucesso
- [ ] Ícone verde (▶) aparece ao lado das classes de teste
- [ ] Run Configurations criadas

## 🎯 Teste Rápido

Execute este comando para verificar tudo:

```bash
cd backend
./mvnw clean test
```

**Resultado esperado:**
```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.conectapg.domain.service.UsuarioServiceTest
[INFO] Tests run: 30, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 104, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] BUILD SUCCESS
```

## 🎉 Pronto!

Se você viu "BUILD SUCCESS", está tudo funcionando! 🚀

Agora você pode:
- ✅ Executar testes no IntelliJ (▶)
- ✅ Usar atalhos (Ctrl + Shift + F10)
- ✅ Debug testes (🐛)
- ✅ Ver cobertura de código

---

**Problema resolvido! 🎊**
