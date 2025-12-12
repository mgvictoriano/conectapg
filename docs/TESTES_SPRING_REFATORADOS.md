# 🎯 Testes Spring Boot - Padrão Refatorado

## ✅ Mudança Implementada

Refatorei os testes para seguir o **padrão de testes de integração Spring Boot** com beans reais (`@Autowired`), igual ao exemplo do projeto Distribuição.

## 🔄 Antes vs Depois

### ❌ Antes (Testes Unitários com Mocks)
```java
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    
    @Mock
    private UsuarioRepository usuarioRepository;
    
    @Mock
    private UsuarioMapper mapper;
    
    @InjectMocks
    private UsuarioService usuarioService;
    
    // Testes com mocks
}
```

### ✅ Depois (Testes de Integração Spring)
```java
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class UsuarioServiceTest extends ServerTest {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Nested
    @Transactional
    class Dado_um_usuario_valido extends ServerTest {
        // Testes com beans reais
    }
}
```

## 🎯 Características do Novo Padrão

### 1. **Extends ServerTest**
```java
public class UsuarioServiceTest extends ServerTest {
```
- ✅ Herda contexto Spring completo
- ✅ Banco H2 em memória
- ✅ Profile "test" ativo
- ✅ Configurações de teste

### 2. **@Autowired de Beans Reais**
```java
@Autowired
private UsuarioService usuarioService;

@Autowired
private UsuarioRepository usuarioRepository;
```
- ✅ Beans reais do Spring
- ✅ Sem mocks
- ✅ Comportamento real

### 3. **@Transactional em @Nested**
```java
@Nested
@Transactional
class Dado_um_usuario_valido extends ServerTest {
```
- ✅ Rollback automático após cada teste
- ✅ Isolamento entre testes
- ✅ Banco limpo a cada execução

### 4. **Extends ServerTest em Cada @Nested**
```java
@Nested
class Quando_criar_usuario extends ServerTest {
```
- ✅ Mantém contexto Spring
- ✅ Acesso aos beans
- ✅ Configurações herdadas

### 5. **Nomenclatura BDD**
```java
@Test
void Entao_deve_persistir_no_banco_de_dados() {
```
- ✅ Começa com "Entao_"
- ✅ Descreve comportamento esperado
- ✅ Legível e claro

## 📊 Estrutura Completa

```java
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class UsuarioServiceTest extends ServerTest {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Nested
    @Transactional
    class Dado_um_usuario_valido extends ServerTest {
        
        private UsuarioRequest usuarioRequest;
        
        @BeforeEach
        void setup() {
            usuarioRepository.deleteAll(); // Limpa banco
            
            usuarioRequest = new UsuarioRequest();
            usuarioRequest.setNome("João Silva");
            usuarioRequest.setEmail("joao@example.com");
            // ... configuração
        }
        
        @Nested
        class Quando_criar_usuario extends ServerTest {
            
            private UsuarioResponse usuarioResponse;
            
            @BeforeEach
            void setup() {
                usuarioResponse = usuarioService.criar(usuarioRequest);
            }
            
            @Test
            void Entao_deve_persistir_no_banco_de_dados() {
                Usuario salvo = usuarioRepository.findById(usuarioResponse.getId()).orElseThrow();
                assertEquals("joao@example.com", salvo.getEmail());
            }
            
            @Test
            void Entao_deve_gerar_id_automaticamente() {
                assertNotNull(usuarioResponse.getId());
                assertTrue(usuarioResponse.getId() > 0);
            }
        }
    }
}
```

## 🎨 Padrão Seguido

### Igual ao Exemplo (EquilibrioComponentTest)

✅ **Classe pública** extends ServerTest
```java
public class UsuarioServiceTest extends ServerTest {
```

✅ **@Autowired** de beans reais
```java
@Autowired
private UsuarioService usuarioService;
```

✅ **@Nested @Transactional** no contexto
```java
@Nested
@Transactional
class Dado_um_usuario_valido extends ServerTest {
```

✅ **@Nested** extends ServerTest em cada nível
```java
@Nested
class Quando_criar_usuario extends ServerTest {
```

✅ **Nomenclatura** Entao_deve_fazer_algo
```java
@Test
void Entao_deve_persistir_no_banco_de_dados() {
```

✅ **@BeforeEach** para setup
```java
@BeforeEach
void setup() {
    usuarioRepository.deleteAll();
}
```

✅ **JUnit Assertions**
```java
assertEquals(expected, actual);
assertTrue(condition);
assertNotNull(value);
```

## 🔧 Configuração Necessária

### 1. ServerTest
```java
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public abstract class ServerTest {
}
```

### 2. application-test.yml
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;MODE=PostgreSQL
  jpa:
    hibernate:
      ddl-auto: create-drop
  flyway:
    enabled: false
```

### 3. pom.xml
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

## 🚀 Vantagens

### Testes de Integração Reais

✅ **Comportamento Real**
- Testa com beans reais do Spring
- Valida persistência no banco
- Verifica transações

✅ **Maior Confiança**
- Testa integração completa
- Detecta problemas reais
- Valida constraints do banco

✅ **Manutenção**
- Menos mocks para manter
- Código mais simples
- Mais próximo da produção

## 📝 Exemplo Completo

```java
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class UsuarioServiceTest extends ServerTest {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Nested
    @Transactional
    class Dado_um_email_ja_cadastrado extends ServerTest {
        
        private UsuarioRequest primeiroUsuario;
        private UsuarioRequest segundoUsuario;
        
        @BeforeEach
        void setup() {
            usuarioRepository.deleteAll();
            
            primeiroUsuario = new UsuarioRequest();
            primeiroUsuario.setEmail("duplicado@example.com");
            // ... configuração
            
            segundoUsuario = new UsuarioRequest();
            segundoUsuario.setEmail("duplicado@example.com");
            // ... configuração
            
            usuarioService.criar(primeiroUsuario);
        }
        
        @Nested
        class Quando_tentar_criar_usuario_com_mesmo_email extends ServerTest {
            
            @Test
            void Entao_deve_lancar_excecao() {
                RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                    usuarioService.criar(segundoUsuario);
                });
                assertTrue(exception.getMessage().contains("Email já cadastrado"));
            }
            
            @Test
            void Entao_nao_deve_criar_segundo_usuario_no_banco() {
                try {
                    usuarioService.criar(segundoUsuario);
                } catch (Exception e) {
                    // Ignora exceção esperada
                }
                
                long total = usuarioRepository.count();
                assertEquals(1, total);
            }
        }
    }
}
```

## 🎯 Checklist de Refatoração

Para refatorar um teste:

- [ ] Remover `@ExtendWith(MockitoExtension.class)`
- [ ] Adicionar `extends ServerTest` na classe principal
- [ ] Trocar `@Mock` por `@Autowired`
- [ ] Remover `@InjectMocks`
- [ ] Adicionar `@Transactional` nos @Nested de contexto
- [ ] Adicionar `extends ServerTest` em cada @Nested
- [ ] Trocar `assertThat()` por `assertEquals()`, `assertTrue()`, etc
- [ ] Adicionar `usuarioRepository.deleteAll()` no @BeforeEach
- [ ] Renomear testes para `Entao_deve_fazer_algo()`
- [ ] Tornar classe `public`

## 🎉 Resultado

**Testes refatorados para padrão Spring Boot:**

```
✅ Extends ServerTest
✅ @Autowired de beans reais
✅ @Transactional para rollback
✅ Banco H2 em memória
✅ Padrão BDD mantido
✅ Nomenclatura correta
✅ Estrutura igual ao exemplo
```

---

**Testes prontos no padrão Spring Boot! 🚀**
