# 🚀 Guia de Run Configurations - ConectaPG

## ✅ Configurações Criadas

Criei **6 configurações de execução** prontas para usar no IntelliJ IDEA:

### 📁 Localização
```
.run/
├── Todos os Testes.run.xml                    ✨ Executa todos os testes
├── Testes Unitários.run.xml                   ✨ Apenas testes unitários
├── Testes de Integração.run.xml               ✨ Apenas testes de integração
├── UsuarioServiceTest.run.xml                 ✨ Teste específico (unitário)
├── UsuarioServiceIntegrationTest.run.xml      ✨ Teste específico (integração)
└── Backend Spring Boot.run.xml                ✨ Roda a aplicação
```

## 🎯 Como Usar no IntelliJ IDEA

### 1️⃣ Acessar as Configurações

As configurações já aparecem automaticamente no IntelliJ! Você verá no canto superior direito:

```
┌─────────────────────────────────────┐
│ [▶] Todos os Testes          ▼     │
└─────────────────────────────────────┘
```

### 2️⃣ Selecionar e Executar

**Opção 1: Dropdown**
1. Clique no dropdown (▼)
2. Selecione a configuração desejada
3. Clique no botão Play (▶) ou Debug (🐛)

**Opção 2: Menu Run**
- `Run` → `Run...` → Escolha a configuração
- Atalho: `Alt + Shift + F10` (Linux/Windows) ou `Ctrl + Alt + R` (Mac)

### 3️⃣ Rodar Teste Direto na Classe

**Método 1: Botão Verde ao Lado da Classe**
```java
▶ class UsuarioServiceTest {  // ← Clique aqui
    @Nested
    ▶ class Dado_um_usuario_valido {  // ← Ou aqui
        @Test
        ▶ void deve_criar_usuario() {  // ← Ou aqui
```

**Método 2: Clique Direito**
1. Clique direito na classe/método
2. `Run 'UsuarioServiceTest'` ou `Debug 'UsuarioServiceTest'`

**Método 3: Atalho de Teclado**
- `Ctrl + Shift + F10` - Executa o teste no cursor
- `Shift + F10` - Reexecuta último teste
- `Ctrl + Shift + F9` - Debug do teste no cursor

## 📊 Configurações Disponíveis

### 1. **Todos os Testes** 🎯
Executa todos os testes do projeto (unitários + integração).

**Quando usar:**
- ✅ Antes de fazer commit
- ✅ Validar tudo está funcionando
- ✅ CI/CD local

**Atalho:** Selecione e pressione `Shift + F10`

---

### 2. **Testes Unitários** ⚡
Executa apenas testes unitários (rápidos, com mocks).

**Padrão:** `*ServiceTest`, `*ControllerTest`

**Quando usar:**
- ✅ Desenvolvimento rápido
- ✅ TDD (Test-Driven Development)
- ✅ Validar lógica isolada

**Vantagem:** Muito rápido! (~2-5 segundos)

---

### 3. **Testes de Integração** 🔗
Executa apenas testes de integração (com Spring Context).

**Padrão:** `*IntegrationTest`

**Quando usar:**
- ✅ Validar integração com banco
- ✅ Testar comportamento real
- ✅ Antes de deploy

**Nota:** Mais lento (~10-30 segundos) por causa do Spring Context

---

### 4. **UsuarioServiceTest** 📝
Executa apenas os testes unitários do UsuarioService.

**Quando usar:**
- ✅ Desenvolvendo UsuarioService
- ✅ Debug específico
- ✅ TDD focado

---

### 5. **UsuarioServiceIntegrationTest** 🧪
Executa apenas os testes de integração do UsuarioService.

**Quando usar:**
- ✅ Validar persistência
- ✅ Testar transações
- ✅ Verificar constraints do banco

---

### 6. **Backend Spring Boot** 🚀
Inicia a aplicação Spring Boot.

**Quando usar:**
- ✅ Testar API manualmente
- ✅ Usar com frontend
- ✅ Testar no Swagger

**Acesso:**
- API: http://localhost:8081/api
- Swagger: http://localhost:8081/api/swagger-ui.html

## 🎨 Atalhos Úteis no IntelliJ

### Executar Testes
| Ação | Atalho |
|------|--------|
| Executar teste no cursor | `Ctrl + Shift + F10` |
| Reexecutar último teste | `Shift + F10` |
| Debug teste no cursor | `Ctrl + Shift + F9` |
| Executar com cobertura | `Ctrl + Shift + F10` + `with Coverage` |

### Navegação
| Ação | Atalho |
|------|--------|
| Ir para teste | `Ctrl + Shift + T` |
| Alternar entre teste e código | `Ctrl + Shift + T` |
| Executar configuração | `Alt + Shift + F10` |

### Debug
| Ação | Atalho |
|------|--------|
| Toggle breakpoint | `Ctrl + F8` |
| Debug último teste | `Shift + F9` |
| Step Over | `F8` |
| Step Into | `F7` |
| Resume | `F9` |

## 🔧 Personalizar Configurações

### Editar Configuração Existente

1. Clique no dropdown das configurações
2. `Edit Configurations...`
3. Selecione a configuração
4. Modifique conforme necessário
5. `Apply` → `OK`

### Criar Nova Configuração

**Para Teste Específico:**
1. Abra a classe de teste
2. Clique direito na classe
3. `Run 'NomeDoTeste'`
4. IntelliJ cria automaticamente!

**Manualmente:**
1. `Run` → `Edit Configurations...`
2. `+` → `JUnit`
3. Configure:
   - **Name:** Nome da configuração
   - **Test kind:** Class/Method/Pattern
   - **Class:** Classe de teste
   - **VM options:** `-ea -Dspring.profiles.active=test`
4. `Apply` → `OK`

## 📝 Configurações Recomendadas

### Para Desenvolvimento Diário
```
1. Testes Unitários (rápido)
2. Teste específico da classe atual
3. Backend Spring Boot (para testar API)
```

### Antes de Commit
```
1. Todos os Testes
2. Verificar cobertura
```

### Para Debug
```
1. Teste específico com breakpoints
2. Debug mode (🐛)
3. Evaluate expressions (Alt + F8)
```

## 🎯 Dicas Pro

### 1. **Executar Teste Rapidamente**
- Posicione o cursor no teste
- `Ctrl + Shift + F10`
- Pronto! ✅

### 2. **Reexecutar Teste Falhado**
- `Shift + F10` reexecuta o último
- Muito útil durante debug

### 3. **Executar com Cobertura**
- Clique direito na classe
- `Run 'Test' with Coverage`
- Veja quais linhas foram testadas

### 4. **Debug Eficiente**
- Coloque breakpoint (`Ctrl + F8`)
- `Ctrl + Shift + F9` para debug
- Use `F8` (step over) e `F7` (step into)

### 5. **Filtrar Testes**
- Na janela de testes, use o filtro
- Filtre por: Passed, Failed, Ignored
- Reexecute apenas os falhados

## 🐛 Troubleshooting

### Configurações Não Aparecem?

**Solução 1:** Reabrir projeto
```
File → Close Project
File → Open → Selecione o projeto
```

**Solução 2:** Invalidar cache
```
File → Invalidate Caches / Restart
```

**Solução 3:** Reimportar Maven
```
Clique direito no pom.xml
Maven → Reload Project
```

### Testes Não Executam?

**Verifique:**
1. ✅ Maven sincronizado
2. ✅ JDK 17 configurado
3. ✅ Dependências baixadas
4. ✅ Módulo correto selecionado

**Solução:**
```bash
# No terminal
cd backend
./mvnw clean install
```

### Spring Context Não Inicia?

**Verifique:**
1. ✅ H2 dependency no pom.xml
2. ✅ application-test.yml existe
3. ✅ Profile "test" ativo
4. ✅ @SpringBootTest na classe

## 📊 Comparação de Performance

| Tipo | Tempo Médio | Quando Usar |
|------|-------------|-------------|
| Teste Unitário | 0.1s - 2s | Desenvolvimento |
| Teste Integração | 5s - 15s | Validação completa |
| Todos os Testes | 30s - 60s | Antes de commit |

## 🎉 Resumo

**Configurações Prontas:**
```
✅ 6 Run Configurations criadas
✅ Testes unitários e integração
✅ Configuração do Spring Boot
✅ Atalhos documentados
✅ Pronto para usar!
```

**Como Usar:**
1. Abra IntelliJ IDEA
2. Veja as configurações no dropdown superior
3. Selecione e execute (▶)
4. Ou use `Ctrl + Shift + F10` direto na classe

**Atalho Favorito:**
```
Ctrl + Shift + F10 = Executa teste no cursor
Shift + F10 = Reexecuta último teste
```

---

**Testes prontos para executar com um clique! 🚀**
