# Solução Final: Problema JUnit + Configuração Run Configuration

## 📋 Mudanças Realizadas no POM

### Alterações Críticas

Baseado no POM do projeto funcionando (Attornatus), foram feitas as seguintes mudanças:

1. **Versão do Maven Surefire Plugin**: `3.0.0` → `2.22.2`
   - A versão 2.22.2 é mais estável e compatível com Spring Boot 3.x
   - Mesma versão usada no projeto de referência

2. **Adicionado `pluginManagement`**:
   - Centraliza configurações de plugins
   - Padrão recomendado pelo Maven
   - Facilita manutenção

3. **Configuração `useSystemClassLoader=false`**:
   - Resolve problemas de classloader com JUnit 5
   - Evita conflitos de dependências

### Comparação: Antes vs Depois

**ANTES**:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0</version>
    <configuration>
        <includes>
            <include>**/*Test.java</include>
            <include>**/*Tests.java</include>
            <include>**/*IntegrationTest.java</include>
        </includes>
    </configuration>
</plugin>
```

**DEPOIS**:
```xml
<build>
    <pluginManagement>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.22.2</version>
                <configuration>
                    <useSystemClassLoader>false</useSystemClassLoader>
                    <includes>
                        <include>**/*Test.java</include>
                        <include>**/*Tests.java</include>
                    </includes>
                </configuration>
            </plugin>
        </plugins>
    </pluginManagement>
    
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

## 🔧 Passos para Resolver o Problema

### Passo 1: Executar Script de Limpeza

```bash
cd /home/michellevictoriano/Documentos/conectapg
./resolver-junit.sh
```

Este script irá:
- ✅ Limpar cache do Maven local (JUnit, Mockito, AssertJ)
- ✅ Limpar diretório `target`
- ✅ Verificar instalação do Maven
- ✅ Baixar dependências novamente (se Maven disponível)

### Passo 2: Invalidar Cache do IntelliJ

1. No IntelliJ IDEA:
   - `File` → `Invalidate Caches / Restart...`
   - Selecione: `Invalidate and Restart`
   - Aguarde o IntelliJ reiniciar

### Passo 3: Recarregar Projeto Maven

1. Abra a aba `Maven` (lateral direita do IntelliJ)
2. Clique no ícone de refresh (🔄) `Reload All Maven Projects`
3. Aguarde o download das dependências

### Passo 4: Compilar Projeto

Na aba Maven:
1. Expanda `conectapg-backend` → `Lifecycle`
2. Execute (duplo clique):
   - `clean`
   - `compile`
   - `test-compile`

### Passo 5: Verificar Dependências

1. Na aba Maven: `conectapg-backend` → `Dependencies`
2. Verifique se aparecem:
   - ✅ `junit-jupiter-5.x.x.jar`
   - ✅ `mockito-core-5.x.x.jar`
   - ✅ `assertj-core-3.x.x.jar`

## 🚀 Configurar Run Configuration

### Para Executar a Aplicação

Consulte o arquivo: `CONFIGURAR_RUN_CONFIGURATION.md`

**Resumo rápido**:
1. `Run` → `Edit Configurations...`
2. `+` → `Spring Boot`
3. Configure:
   - **Name**: ConectaPG Application
   - **Main class**: `com.conectapg.ConectaPgApplication`
   - **JRE**: 17
   - **Module**: `conectapg-backend`

### Para Executar Testes

**Método 1 - Automático** (Recomendado):
1. Abra `UsuarioServiceTest.java`
2. Clique com botão direito na classe
3. Selecione `Run 'UsuarioServiceTest'`
4. IntelliJ criará a configuração automaticamente

**Método 2 - Manual**:
1. `Run` → `Edit Configurations...`
2. `+` → `JUnit`
3. Configure:
   - **Name**: UsuarioServiceTest
   - **Test kind**: Class
   - **Class**: `com.conectapg.domain.service.UsuarioServiceTest`
   - **JRE**: 17
   - **Module**: `conectapg-backend.test`

## 🔍 Verificação Final

Execute os seguintes testes:

### 1. Verificar JARs no repositório local

```bash
# JUnit
ls -lh ~/.m2/repository/org/junit/jupiter/junit-jupiter/

# Mockito
ls -lh ~/.m2/repository/org/mockito/mockito-core/

# AssertJ
ls -lh ~/.m2/repository/org/assertj/assertj-core/
```

### 2. Executar teste via Maven (IntelliJ)

Na aba Maven:
- `conectapg-backend` → `Lifecycle` → `test`

### 3. Executar teste via Run Configuration

- Abra `UsuarioServiceTest.java`
- Clique no ícone ▶️ verde ao lado da classe
- Ou use: `Ctrl + Shift + F10`

## ❓ Solução de Problemas

### Problema: "Cannot resolve symbol 'Test'"

**Solução**:
1. Verifique imports no arquivo de teste:
   ```java
   import org.junit.jupiter.api.Test;
   import org.junit.jupiter.api.BeforeEach;
   ```
2. Se ainda não resolver:
   - `File` → `Project Structure` → `Modules`
   - Selecione `conectapg-backend`
   - Aba `Dependencies`
   - Verifique se `junit-jupiter` está presente

### Problema: "Class not found: UsuarioServiceTest"

**Solução**:
1. Verifique se o módulo está correto na Run Configuration:
   - Deve ser: `conectapg-backend.test` (não apenas `conectapg-backend`)
2. Recompile o projeto:
   - `Build` → `Rebuild Project`

### Problema: "Port 8080 already in use"

**Solução**:
1. Verifique processos rodando:
   ```bash
   lsof -i :8080
   ```
2. Mate o processo:
   ```bash
   kill -9 <PID>
   ```
3. Ou mude a porta em `application.properties`:
   ```properties
   server.port=8081
   ```

## 📚 Arquivos de Referência

- ✅ `CONFIGURAR_RUN_CONFIGURATION.md` - Guia completo de configuração
- ✅ `resolver-junit.sh` - Script de limpeza e resolução
- ✅ `pom.xml` - Atualizado com configurações corretas

## ✨ Próximos Passos

1. ✅ Executar `resolver-junit.sh`
2. ✅ Invalidar cache do IntelliJ
3. ✅ Recarregar projeto Maven
4. ✅ Configurar Run Configuration
5. ✅ Executar primeiro teste
6. 🎯 Desenvolver novos testes e funcionalidades

## 🎓 Diferenças Principais vs Projeto Attornatus

| Aspecto | Projeto Attornatus | ConectaPG |
|---------|-------------------|-----------|
| Spring Boot | 2.4.2 | 3.2.0 |
| Java | 17 | 17 |
| JPA Provider | EclipseLink | Hibernate |
| Database | Oracle | PostgreSQL |
| Surefire Plugin | 2.22.2 | 2.22.2 ✅ |
| JUnit | 5.x (implícito) | 5.x (explícito) |

**Nota**: O projeto Attornatus não declara JUnit explicitamente porque vem transitivamente do `spring-boot-starter-test`. No ConectaPG, declaramos explicitamente para maior controle.

## 📝 Observações Importantes

1. **Não precisa do `spring-instrument`**: Isso é específico para EclipseLink (usado no projeto Attornatus). Como usamos Hibernate, não precisamos.

2. **Versão do Surefire**: A versão 2.22.2 é a mais estável para JUnit 5. A versão 3.x pode ter problemas de compatibilidade.

3. **pluginManagement vs plugins**: 
   - `pluginManagement`: Define configurações padrão
   - `plugins`: Aplica os plugins ao projeto
   - Usar ambos é a melhor prática

4. **useSystemClassLoader=false**: Essencial para evitar problemas de classloader com JUnit 5 e Spring Boot 3.x.
