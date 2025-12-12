# 🧪 Testes Spring Boot - Estrutura Completa

## ✅ Implementação Realizada

Criei a infraestrutura completa para testes Spring Boot no projeto ConectaPG:

### 📁 Arquivos Criados

```
backend/
├── src/test/java/com/conectapg/
│   └── ServerTest.java                    ✨ Classe base para testes Spring
├── src/test/resources/
│   └── application-test.yml               ✨ Configuração de testes
└── pom.xml                                ✏️  Adicionada dependência H2
```

## 🎯 ServerTest - Classe Base

```java
package com.conectapg;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.flyway.enabled=false"
})
public abstract class ServerTest {
    // Classe base para testes de integração
    // Subclasses herdam o contexto Spring completo
}
```

### 🔧 O que ServerTest Fornece

- ✅ **@SpringBootTest** - Contexto Spring completo
- ✅ **@ActiveProfiles("test")** - Profile de teste
- ✅ **H2 Database** - Banco em memória para testes
- ✅ **DDL Auto Create-Drop** - Recria schema a cada teste
- ✅ **Flyway Desabilitado** - Usa JPA para criar tabelas

## 📝 Como Usar nos Testes

### Opção 1: Testes de Integração (Com Spring Context)

Use quando precisar de:
- Beans reais do Spring
- Transações
- Banco de dados
- Configurações reais

```java
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UsuarioServiceIntegrationTest extends ServerTest {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Nested
    class Dado_um_usuario_valido {
        
        Usuario usuario;
        
        @BeforeEach
        void setup() {
            // Limpa banco antes de cada teste
            usuarioRepository.deleteAll();
            
            usuario = Usuario.builder()
                    .nome("João Silva")
                    .email("joao@example.com")
                    .senha("senha123")
                    .tipo(TipoUsuario.CIDADAO)
                    .ativo(true)
                    .build();
        }
        
        @Nested
        class Quando_criar_usuario {
            
            UsuarioResponse resultado;
            
            @BeforeEach
            void setup() {
                UsuarioRequest request = new UsuarioRequest();
                request.setNome(usuario.getNome());
                request.setEmail(usuario.getEmail());
                request.setSenha(usuario.getSenha());
                request.setTipo(usuario.getTipo());
                
                resultado = usuarioService.criar(request);
            }
            
            @Test
            void deve_salvar_no_banco_de_dados() {
                Optional<Usuario> salvo = usuarioRepository.findById(resultado.getId());
                assertThat(salvo).isPresent();
                assertThat(salvo.get().getEmail()).isEqualTo("joao@example.com");
            }
            
            @Test
            void deve_retornar_usuario_com_id_gerado() {
                assertThat(resultado.getId()).isNotNull();
                assertThat(resultado.getId()).isGreaterThan(0);
            }
        }
    }
}
```

### Opção 2: Testes Unitários (Com Mocks)

Use quando quiser:
- Testes rápidos
- Isolamento total
- Sem dependências externas

```java
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UsuarioServiceTest {
    
    @Mock
    private UsuarioRepository usuarioRepository;
    
    @Mock
    private UsuarioMapper mapper;
    
    @InjectMocks
    private UsuarioService usuarioService;
    
    @Nested
    class Dado_um_usuario_valido {
        // Mesma estrutura BDD com mocks
    }
}
```

## 🎯 Quando Usar Cada Tipo

### Testes de Integração (extends ServerTest)

**Use para:**
- ✅ Services que interagem com banco
- ✅ Validar transações
- ✅ Testar queries complexas
- ✅ Verificar constraints do banco
- ✅ Testar comportamento real

**Exemplo:**
```java
class UsuarioServiceIntegrationTest extends ServerTest {
    @Autowired
    private UsuarioService service;
    
    @Test
    void deve_lancar_excecao_ao_criar_email_duplicado() {
        // Testa constraint UNIQUE do banco
    }
}
```

### Testes Unitários (com @Mock)

**Use para:**
- ✅ Lógica de negócio isolada
- ✅ Testes rápidos
- ✅ Controllers (com @WebMvcTest)
- ✅ Mappers
- ✅ Validações

**Exemplo:**
```java
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @Mock
    private UsuarioRepository repository;
    
    @Test
    void deve_chamar_repository_save() {
        // Testa apenas a lógica
    }
}
```

## 📊 Estrutura Recomendada

```
backend/src/test/java/com/conectapg/
├── ServerTest.java                          ← Classe base
│
├── domain/service/
│   ├── UsuarioServiceTest.java             ← Unitário (Mocks)
│   ├── UsuarioServiceIntegrationTest.java  ← Integração (Spring)
│   ├── OcorrenciaServiceTest.java          ← Unitário (Mocks)
│   └── OcorrenciaServiceIntegrationTest.java ← Integração (Spring)
│
└── api/controller/
    ├── UsuarioControllerTest.java          ← @WebMvcTest (Mock Service)
    └── OcorrenciaControllerTest.java       ← @WebMvcTest (Mock Service)
```

## 🔧 Configuração application-test.yml

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;MODE=PostgreSQL
    driver-class-name: org.h2.Driver
  
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: false
  
  flyway:
    enabled: false

server:
  port: 0  # Porta aleatória para testes paralelos
```

## 🎨 Exemplo Completo - Teste de Integração

```java
package com.conectapg.domain.service;

import com.conectapg.ServerTest;
import com.conectapg.api.dto.UsuarioRequest;
import com.conectapg.api.dto.UsuarioResponse;
import com.conectapg.domain.model.Usuario;
import com.conectapg.domain.model.Usuario.TipoUsuario;
import com.conectapg.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UsuarioServiceIntegrationTest extends ServerTest {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @BeforeEach
    void limparBanco() {
        usuarioRepository.deleteAll();
    }
    
    @Nested
    class Dado_um_usuario_valido {
        
        UsuarioRequest usuarioRequest;
        
        @BeforeEach
        void setup() {
            usuarioRequest = new UsuarioRequest();
            usuarioRequest.setNome("João Silva");
            usuarioRequest.setEmail("joao@example.com");
            usuarioRequest.setSenha("senha123");
            usuarioRequest.setTipo(TipoUsuario.CIDADAO);
            usuarioRequest.setAtivo(true);
        }
        
        @Nested
        class Quando_criar_usuario {
            
            UsuarioResponse resultado;
            
            @BeforeEach
            void setup() {
                resultado = usuarioService.criar(usuarioRequest);
            }
            
            @Test
            void deve_persistir_no_banco_de_dados() {
                Usuario salvo = usuarioRepository.findById(resultado.getId()).orElseThrow();
                assertThat(salvo.getEmail()).isEqualTo("joao@example.com");
            }
            
            @Test
            void deve_gerar_id_automaticamente() {
                assertThat(resultado.getId()).isNotNull();
                assertThat(resultado.getId()).isGreaterThan(0);
            }
            
            @Test
            void deve_criptografar_senha() {
                Usuario salvo = usuarioRepository.findById(resultado.getId()).orElseThrow();
                assertThat(salvo.getSenha()).isNotEqualTo("senha123");
                assertThat(salvo.getSenha()).startsWith("$2a$"); // BCrypt
            }
        }
        
        @Nested
        class Quando_tentar_criar_com_email_duplicado {
            
            @BeforeEach
            void setup() {
                usuarioService.criar(usuarioRequest);
            }
            
            @Test
            void deve_lancar_excecao() {
                assertThatThrownBy(() -> usuarioService.criar(usuarioRequest))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("Email já cadastrado");
            }
            
            @Test
            void nao_deve_criar_segundo_usuario() {
                try {
                    usuarioService.criar(usuarioRequest);
                } catch (Exception e) {
                    // Ignora exceção
                }
                
                long total = usuarioRepository.count();
                assertThat(total).isEqualTo(1);
            }
        }
    }
}
```

## 🚀 Como Executar

```bash
# Todos os testes (unitários + integração)
./mvnw test

# Apenas testes unitários (rápidos)
./mvnw test -Dtest=*Test

# Apenas testes de integração
./mvnw test -Dtest=*IntegrationTest

# Teste específico
./mvnw test -Dtest=UsuarioServiceIntegrationTest
```

## 📊 Vantagens da Estrutura

### Testes Unitários (Mocks)
- ⚡ **Rápidos** - Sem Spring Context
- 🎯 **Focados** - Testam lógica isolada
- 🔧 **Simples** - Fácil setup

### Testes de Integração (ServerTest)
- ✅ **Reais** - Testam comportamento real
- 🗄️ **Banco** - Validam persistência
- 🔐 **Transações** - Testam rollback
- 🎯 **Confiança** - Maior cobertura

## 🎉 Resultado

**Infraestrutura completa para testes Spring Boot:**

```
✅ ServerTest - Classe base criada
✅ H2 Database - Configurado para testes
✅ application-test.yml - Profile de teste
✅ Padrão BDD - Mantido
✅ Dois tipos de teste - Unitário e Integração
✅ Documentação completa
```

## 📝 Próximos Passos

1. **Manter testes unitários atuais** - São rápidos e úteis
2. **Criar testes de integração** - Para validar comportamento real
3. **Usar @Transactional** - Para rollback automático
4. **Limpar dados** - @BeforeEach com deleteAll()

---

**Estrutura Spring Boot completa para testes! 🚀**
