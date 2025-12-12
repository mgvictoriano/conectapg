# 🔧 Solução: JUnit not found in module 'conectapg'

## ❌ Erro
```
JUnit not found in module 'conectapg'
```

## ✅ Solução Passo a Passo

### 1️⃣ Recarregar Projeto Maven

**Opção A: Atalho (Mais Rápido)**
```
Ctrl + Shift + O
```

**Opção B: Menu Maven**
```
1. Clique direito no arquivo pom.xml
2. Maven → Reload Project
```

**Opção C: Aba Maven**
```
1. Abra a aba "Maven" (lateral direita)
2. Clique no ícone 🔄 (Reload All Maven Projects)
```

### 2️⃣ Limpar e Reinstalar

No terminal do IntelliJ (`Alt + F12`):

```bash
cd backend
./mvnw clean install -U
```

O `-U` força o download de dependências atualizadas.

### 3️⃣ Invalidar Cache do IntelliJ

```
1. File → Invalidate Caches / Restart...
2. Marque todas as opções:
   ☑ Invalidate and Restart
   ☑ Clear file system cache and Local History
   ☑ Clear downloaded shared indexes
3. Clique em "Invalidate and Restart"
4. Aguarde o IntelliJ reiniciar e reindexar
```

### 4️⃣ Verificar Estrutura do Projeto

```
1. File → Project Structure (Ctrl + Alt + Shift + S)
2. Project:
   - SDK: 17
   - Language level: 17
3. Modules:
   - conectapg-backend deve estar listado
   - Sources: src/main/java
   - Test Sources: src/test/java
4. Apply → OK
```

### 5️⃣ Verificar se JUnit foi Baixado

**No terminal:**
```bash
cd backend
./mvnw dependency:tree | grep junit
```

**Deve aparecer:**
```
[INFO] +- org.junit.jupiter:junit-jupiter:jar:5.10.1:test
[INFO] |  +- org.junit.jupiter:junit-jupiter-api:jar:5.10.1:test
[INFO] |  +- org.junit.jupiter:junit-jupiter-params:jar:5.10.1:test
[INFO] |  \- org.junit.jupiter:junit-jupiter-engine:jar:5.10.1:test
```

### 6️⃣ Reimportar Módulo Maven

Se ainda não funcionar:

```
1. Feche o projeto: File → Close Project
2. Delete a pasta .idea (se existir)
3. Reabra o projeto: File → Open
4. Selecione a pasta do projeto
5. Aguarde o IntelliJ importar o Maven
```

### 7️⃣ Verificar Configuração de Teste

**Certifique-se que o diretório de teste está marcado:**

```
1. Clique direito em backend/src/test/java
2. Mark Directory as → Test Sources Root
```

### 8️⃣ Atualizar Run Configuration

```
1. Run → Edit Configurations...
2. Selecione a configuração de teste
3. Verifique:
   - Module: conectapg-backend
   - Working directory: $MODULE_DIR$
   - Use classpath of module: conectapg-backend
4. Apply → OK
```

## 🎯 Solução Rápida (Tente Primeiro)

Execute estes 3 comandos em sequência:

```bash
# 1. Limpar e instalar
cd backend
./mvnw clean install -U

# 2. No IntelliJ: Ctrl + Shift + O (Reload Maven)

# 3. File → Invalidate Caches / Restart
```

## 🔍 Verificar se Funcionou

### Teste 1: Ver Dependências
```
1. Abra aba "Maven" (lateral direita)
2. Expanda "Dependencies"
3. Procure "junit-jupiter"
4. Deve estar lá! ✅
```

### Teste 2: Executar Teste
```
1. Abra UsuarioServiceTest.java
2. Veja se aparece o ícone verde ▶ ao lado da classe
3. Clique no ▶
4. Selecione "Run 'UsuarioServiceTest'"
```

Se executar: ✅ **Funcionou!**

## 🐛 Problemas Específicos

### "Cannot resolve symbol 'Test'"

**Solução:**
```java
// Verifique o import
import org.junit.jupiter.api.Test; // ✅ Correto
import org.junit.Test; // ❌ Errado (JUnit 4)
```

### "No tests found"

**Solução:**
```
1. Verifique que a classe é pública:
   public class UsuarioServiceTest extends ServerTest { // ✅

2. Verifique que os métodos são públicos ou package-private:
   @Test
   void Entao_deve_fazer_algo() { // ✅
```

### "Module not specified"

**Solução:**
```
1. Run → Edit Configurations
2. Module: conectapg-backend
3. Apply → OK
```

## 📊 Checklist de Verificação

Antes de executar os testes, verifique:

- [ ] Maven recarregado (`Ctrl + Shift + O`)
- [ ] `./mvnw clean install -U` executado com sucesso
- [ ] Cache invalidado (File → Invalidate Caches)
- [ ] JDK 17 configurado
- [ ] `src/test/java` marcado como Test Sources Root
- [ ] Dependências visíveis na aba Maven
- [ ] Ícone verde ▶ aparece ao lado das classes de teste

## 🚀 Comando Definitivo

Se nada funcionar, execute esta sequência completa:

```bash
# 1. Limpar completamente
cd backend
./mvnw clean
rm -rf target/
rm -rf ~/.m2/repository/org/junit

# 2. Reinstalar tudo
./mvnw clean install -U

# 3. Verificar
./mvnw dependency:tree | grep junit
```

Depois no IntelliJ:
```
1. File → Invalidate Caches / Restart
2. Aguarde reiniciar
3. Ctrl + Shift + O (Reload Maven)
4. Aguarde indexação terminar
```

## 💡 Dica Pro

**Verifique se o Maven está usando o JDK correto:**

```
1. Settings (Ctrl + Alt + S)
2. Build, Execution, Deployment → Build Tools → Maven
3. Maven home directory: (deve apontar para Maven válido)
4. JRE: Use Project JDK (17)
5. Apply → OK
```

## 🎯 Última Tentativa

Se NADA funcionar, tente criar um teste simples para verificar:

```java
package com.conectapg;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleTest {
    @Test
    void test() {
        assertEquals(1, 1);
    }
}
```

Se este teste simples funcionar, o problema é na configuração dos testes complexos, não no JUnit.

## 📞 Informações Úteis

**Versões no pom.xml:**
- Spring Boot: 3.2.0
- JUnit: 5.10.1 (gerenciado pelo Spring Boot)
- Java: 17

**Estrutura esperada:**
```
backend/
├── pom.xml
├── src/
│   ├── main/java/
│   └── test/
│       ├── java/com/conectapg/
│       │   ├── ServerTest.java
│       │   └── domain/service/
│       │       └── UsuarioServiceTest.java
│       └── resources/
│           └── application-test.yml
```

---

**Execute os passos 1, 2 e 3 primeiro! Geralmente resolve! 🚀**
