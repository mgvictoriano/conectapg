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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Testes de integração para UsuarioService.
 * 
 * Usa contexto Spring completo com banco H2 em memória.
 * Valida comportamento real incluindo persistência e transações.
 */
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
                Optional<Usuario> salvo = usuarioRepository.findById(resultado.getId());
                assertThat(salvo).isPresent();
                assertThat(salvo.get().getEmail()).isEqualTo("joao@example.com");
            }
            
            @Test
            void deve_gerar_id_automaticamente() {
                assertThat(resultado.getId()).isNotNull();
                assertThat(resultado.getId()).isGreaterThan(0);
            }
            
            @Test
            void deve_retornar_dados_corretos() {
                assertThat(resultado.getNome()).isEqualTo("João Silva");
                assertThat(resultado.getEmail()).isEqualTo("joao@example.com");
                assertThat(resultado.getTipo()).isEqualTo(TipoUsuario.CIDADAO);
                assertThat(resultado.getAtivo()).isTrue();
            }
            
            @Test
            void deve_criptografar_senha_no_banco() {
                Usuario salvo = usuarioRepository.findById(resultado.getId()).orElseThrow();
                assertThat(salvo.getSenha()).isNotEqualTo("senha123");
                assertThat(salvo.getSenha()).startsWith("$2a$"); // BCrypt
            }
            
            @Test
            void deve_definir_data_de_criacao() {
                Usuario salvo = usuarioRepository.findById(resultado.getId()).orElseThrow();
                assertThat(salvo.getDataCriacao()).isNotNull();
            }
        }
        
        @Nested
        class Quando_buscar_usuario_criado {
            
            Long usuarioId;
            
            @BeforeEach
            void setup() {
                UsuarioResponse criado = usuarioService.criar(usuarioRequest);
                usuarioId = criado.getId();
            }
            
            @Test
            void deve_encontrar_por_id() {
                UsuarioResponse encontrado = usuarioService.buscarPorId(usuarioId);
                assertThat(encontrado.getId()).isEqualTo(usuarioId);
                assertThat(encontrado.getEmail()).isEqualTo("joao@example.com");
            }
            
            @Test
            void deve_encontrar_por_email() {
                UsuarioResponse encontrado = usuarioService.buscarPorEmail("joao@example.com");
                assertThat(encontrado.getId()).isEqualTo(usuarioId);
            }
            
            @Test
            void deve_aparecer_na_listagem() {
                List<UsuarioResponse> todos = usuarioService.listarTodos();
                assertThat(todos).hasSize(1);
                assertThat(todos.get(0).getId()).isEqualTo(usuarioId);
            }
        }
        
        @Nested
        class Quando_atualizar_usuario {
            
            Long usuarioId;
            
            @BeforeEach
            void setup() {
                UsuarioResponse criado = usuarioService.criar(usuarioRequest);
                usuarioId = criado.getId();
                
                usuarioRequest.setNome("João Silva Atualizado");
                usuarioService.atualizar(usuarioId, usuarioRequest);
            }
            
            @Test
            void deve_persistir_alteracoes_no_banco() {
                Usuario atualizado = usuarioRepository.findById(usuarioId).orElseThrow();
                assertThat(atualizado.getNome()).isEqualTo("João Silva Atualizado");
            }
            
            @Test
            void deve_manter_mesmo_id() {
                UsuarioResponse atualizado = usuarioService.buscarPorId(usuarioId);
                assertThat(atualizado.getId()).isEqualTo(usuarioId);
            }
        }
        
        @Nested
        class Quando_deletar_usuario {
            
            Long usuarioId;
            
            @BeforeEach
            void setup() {
                UsuarioResponse criado = usuarioService.criar(usuarioRequest);
                usuarioId = criado.getId();
                usuarioService.deletar(usuarioId);
            }
            
            @Test
            void deve_remover_do_banco_de_dados() {
                Optional<Usuario> deletado = usuarioRepository.findById(usuarioId);
                assertThat(deletado).isEmpty();
            }
            
            @Test
            void nao_deve_aparecer_na_listagem() {
                List<UsuarioResponse> todos = usuarioService.listarTodos();
                assertThat(todos).isEmpty();
            }
        }
    }
    
    @Nested
    class Dado_um_email_ja_cadastrado {
        
        UsuarioRequest primeiroUsuario;
        UsuarioRequest segundoUsuario;
        
        @BeforeEach
        void setup() {
            primeiroUsuario = new UsuarioRequest();
            primeiroUsuario.setNome("Primeiro Usuario");
            primeiroUsuario.setEmail("duplicado@example.com");
            primeiroUsuario.setSenha("senha123");
            primeiroUsuario.setTipo(TipoUsuario.CIDADAO);
            
            segundoUsuario = new UsuarioRequest();
            segundoUsuario.setNome("Segundo Usuario");
            segundoUsuario.setEmail("duplicado@example.com");
            segundoUsuario.setSenha("outrasenha");
            segundoUsuario.setTipo(TipoUsuario.CIDADAO);
            
            usuarioService.criar(primeiroUsuario);
        }
        
        @Nested
        class Quando_tentar_criar_usuario_com_mesmo_email {
            
            @Test
            void deve_lancar_excecao() {
                assertThatThrownBy(() -> usuarioService.criar(segundoUsuario))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("Email já cadastrado");
            }
            
            @Test
            void nao_deve_criar_segundo_usuario_no_banco() {
                try {
                    usuarioService.criar(segundoUsuario);
                } catch (Exception e) {
                    // Ignora exceção esperada
                }
                
                long total = usuarioRepository.count();
                assertThat(total).isEqualTo(1);
            }
        }
    }
    
    @Nested
    class Dado_multiplos_usuarios_cadastrados {
        
        @BeforeEach
        void setup() {
            UsuarioRequest cidadao = new UsuarioRequest();
            cidadao.setNome("Cidadão");
            cidadao.setEmail("cidadao@example.com");
            cidadao.setSenha("senha123");
            cidadao.setTipo(TipoUsuario.CIDADAO);
            cidadao.setAtivo(true);
            
            UsuarioRequest admin = new UsuarioRequest();
            admin.setNome("Admin");
            admin.setEmail("admin@example.com");
            admin.setSenha("senha123");
            admin.setTipo(TipoUsuario.ADMIN);
            admin.setAtivo(true);
            
            UsuarioRequest inativo = new UsuarioRequest();
            inativo.setNome("Inativo");
            inativo.setEmail("inativo@example.com");
            inativo.setSenha("senha123");
            inativo.setTipo(TipoUsuario.CIDADAO);
            inativo.setAtivo(false);
            
            usuarioService.criar(cidadao);
            usuarioService.criar(admin);
            usuarioService.criar(inativo);
        }
        
        @Nested
        class Quando_listar_todos {
            
            @Test
            void deve_retornar_todos_usuarios() {
                List<UsuarioResponse> todos = usuarioService.listarTodos();
                assertThat(todos).hasSize(3);
            }
        }
        
        @Nested
        class Quando_buscar_por_tipo {
            
            @Test
            void deve_retornar_apenas_cidadaos() {
                List<UsuarioResponse> cidadaos = usuarioService.buscarPorTipo(TipoUsuario.CIDADAO);
                assertThat(cidadaos).hasSize(2);
                assertThat(cidadaos).allMatch(u -> u.getTipo() == TipoUsuario.CIDADAO);
            }
            
            @Test
            void deve_retornar_apenas_admins() {
                List<UsuarioResponse> admins = usuarioService.buscarPorTipo(TipoUsuario.ADMIN);
                assertThat(admins).hasSize(1);
                assertThat(admins.get(0).getTipo()).isEqualTo(TipoUsuario.ADMIN);
            }
        }
        
        @Nested
        class Quando_buscar_ativos {
            
            @Test
            void deve_retornar_apenas_usuarios_ativos() {
                List<UsuarioResponse> ativos = usuarioService.buscarAtivos();
                assertThat(ativos).hasSize(2);
                assertThat(ativos).allMatch(UsuarioResponse::getAtivo);
            }
        }
    }
}
