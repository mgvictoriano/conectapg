# Guia: Configurar Run Configuration no IntelliJ IDEA

## 1. Configuração para Executar a Aplicação Spring Boot

### Opção A: Usando Spring Boot Run Configuration (Recomendado)

1. **Abrir configurações**:
   - Menu: `Run` → `Edit Configurations...`
   - Ou clique no dropdown ao lado do botão ▶️ e selecione `Edit Configurations...`

2. **Criar nova configuração**:
   - Clique no `+` (Add New Configuration)
   - Selecione `Spring Boot`

3. **Configurar os campos**:
   ```
   Name: ConectaPG Application
   Main class: com.conectapg.ConectaPgApplication
   Working directory: $MODULE_WORKING_DIR$
   Use classpath of module: conectapg-backend
   JRE: 17
   ```

4. **Variáveis de ambiente** (aba Environment):
   ```
   SPRING_PROFILES_ACTIVE=dev
   ```

5. **VM Options** (se necessário):
   ```
   -Xmx512m
   -Dspring.profiles.active=dev
   ```

6. **Aplicar e OK**

### Opção B: Usando Application Run Configuration

1. **Criar nova configuração**:
   - Clique no `+` → `Application`

2. **Configurar**:
   ```
   Name: ConectaPG App
   Main class: com.conectapg.ConectaPgApplication
   VM options: -Dspring.profiles.active=dev
   Working directory: /home/michellevictoriano/Documentos/conectapg/backend
   Use classpath of module: conectapg-backend.main
   JRE: 17
   ```

## 2. Configuração para Executar Testes

### Para Testes Unitários

1. **Criar configuração JUnit**:
   - `+` → `JUnit`

2. **Configurar**:
   ```
   Name: All Unit Tests
   Test kind: All in package
   Package: com.conectapg
   Search for tests: In whole project
   VM options: -ea
   Working directory: $MODULE_WORKING_DIR$
   Use classpath of module: conectapg-backend.test
   JRE: 17
   ```

### Para Teste Específico

1. **Método rápido**:
   - Abra a classe de teste (ex: `UsuarioServiceTest.java`)
   - Clique com botão direito na classe ou método
   - Selecione `Run 'UsuarioServiceTest'`
   - IntelliJ criará automaticamente a configuração

2. **Método manual**:
   - `+` → `JUnit`
   - Configure:
     ```
     Name: UsuarioServiceTest
     Test kind: Class
     Class: com.conectapg.domain.service.UsuarioServiceTest
     VM options: -ea
     Working directory: $MODULE_WORKING_DIR$
     Use classpath of module: conectapg-backend.test
     JRE: 17
     ```

## 3. Configuração para Maven

### Executar Maven Clean Install

1. **Criar configuração Maven**:
   - `+` → `Maven`

2. **Configurar**:
   ```
   Name: Maven Clean Install
   Working directory: /home/michellevictoriano/Documentos/conectapg/backend
   Command line: clean install
   ```

### Executar Apenas Testes

```
Name: Maven Test
Working directory: /home/michellevictoriano/Documentos/conectapg/backend
Command line: clean test
```

### Executar sem Testes

```
Name: Maven Install (Skip Tests)
Working directory: /home/michellevictoriano/Documentos/conectapg/backend
Command line: clean install -DskipTests
```

## 4. Atalhos Úteis

- **Executar última configuração**: `Shift + F10`
- **Debug última configuração**: `Shift + F9`
- **Editar configurações**: `Alt + Shift + F10` → `0`
- **Executar teste atual**: `Ctrl + Shift + F10` (cursor no teste)

## 5. Solução de Problemas Comuns

### Erro: "Class not found"
- Verifique se o módulo está correto: `conectapg-backend.main` ou `conectapg-backend.test`
- Execute `Maven` → `Reload Project`
- Execute `File` → `Invalidate Caches / Restart`

### Erro: "JUnit not found"
1. Verifique se as dependências foram baixadas:
   ```bash
   cd /home/michellevictoriano/Documentos/conectapg/backend
   mvn dependency:resolve
   ```

2. No IntelliJ:
   - Abra a aba `Maven` (lateral direita)
   - Clique no ícone de refresh (🔄)
   - Execute `Reimport All Maven Projects`

### Erro: "Port already in use"
- Verifique se já existe uma instância rodando
- Mude a porta no `application.properties`:
  ```properties
  server.port=8081
  ```

## 6. Configuração de Profiles

### Criar arquivo de configuração por ambiente

**application-dev.properties**:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/conectapg_dev
server.port=8080
logging.level.com.conectapg=DEBUG
```

**application-prod.properties**:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/conectapg_prod
server.port=8080
logging.level.com.conectapg=INFO
```

### Usar profile na Run Configuration

No campo `Environment variables`:
```
SPRING_PROFILES_ACTIVE=dev
```

Ou em `VM options`:
```
-Dspring.profiles.active=dev
```

## 7. Verificação Final

Após configurar, teste:

1. ✅ Executar aplicação: `Run 'ConectaPG Application'`
2. ✅ Executar teste unitário: `Run 'UsuarioServiceTest'`
3. ✅ Executar todos os testes: `Run 'All Unit Tests'`
4. ✅ Build Maven: `Run 'Maven Clean Install'`

## 8. Dicas Adicionais

- **Hot Reload**: Adicione `spring-boot-devtools` para reload automático
- **Debug**: Use breakpoints (`Ctrl + F8`) e execute em modo Debug (`Shift + F9`)
- **Logs**: Configure níveis de log no `application.properties`
- **Database**: Configure o H2 Console para testes locais
