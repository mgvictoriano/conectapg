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

import static org.junit.jupiter.api.Assertions.*;

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
        private UsuarioResponse usuarioResponse;

        @BeforeEach
        void setup() {
            usuarioRepository.deleteAll();
            
            usuarioRequest = new UsuarioRequest();
            usuarioRequest.setNome("João Silva");
            usuarioRequest.setEmail("joao@example.com");
            usuarioRequest.setSenha("senha123");
            usuarioRequest.setTipo(TipoUsuario.CIDADAO);
            usuarioRequest.setAtivo(true);
        }

        @Nested
        class Quando_criar_usuario extends ServerTest {

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

            @Test
            void Entao_deve_retornar_dados_corretos() {
                assertEquals("João Silva", usuarioResponse.getNome());
                assertEquals("joao@example.com", usuarioResponse.getEmail());
                assertEquals(TipoUsuario.CIDADAO, usuarioResponse.getTipo());
                assertTrue(usuarioResponse.getAtivo());
            }

            @Test
            void Entao_deve_criptografar_senha_no_banco() {
                Usuario salvo = usuarioRepository.findById(usuarioResponse.getId()).orElseThrow();
                assertNotEquals("senha123", salvo.getSenha());
                assertTrue(salvo.getSenha().startsWith("$2a$")); // BCrypt
            }
        }

        @Nested
        class Quando_buscar_usuario_criado extends ServerTest {

            private Long usuarioId;

            @BeforeEach
            void setup() {
                UsuarioResponse criado = usuarioService.criar(usuarioRequest);
                usuarioId = criado.getId();
            }

            @Test
            void Entao_deve_encontrar_por_id() {
                UsuarioResponse encontrado = usuarioService.buscarPorId(usuarioId);
                assertEquals(usuarioId, encontrado.getId());
                assertEquals("joao@example.com", encontrado.getEmail());
            }

            @Test
            void Entao_deve_encontrar_por_email() {
                UsuarioResponse encontrado = usuarioService.buscarPorEmail("joao@example.com");
                assertEquals(usuarioId, encontrado.getId());
            }

            @Test
            void Entao_deve_aparecer_na_listagem() {
                List<UsuarioResponse> todos = usuarioService.listarTodos();
                assertEquals(1, todos.size());
                assertEquals(usuarioId, todos.get(0).getId());
            }
        }

        @Nested
        class Quando_atualizar_usuario extends ServerTest {

            private Long usuarioId;

            @BeforeEach
            void setup() {
                UsuarioResponse criado = usuarioService.criar(usuarioRequest);
                usuarioId = criado.getId();
                
                usuarioRequest.setNome("João Silva Atualizado");
                usuarioService.atualizar(usuarioId, usuarioRequest);
            }

            @Test
            void Entao_deve_persistir_alteracoes_no_banco() {
                Usuario atualizado = usuarioRepository.findById(usuarioId).orElseThrow();
                assertEquals("João Silva Atualizado", atualizado.getNome());
            }

            @Test
            void Entao_deve_manter_mesmo_id() {
                UsuarioResponse atualizado = usuarioService.buscarPorId(usuarioId);
                assertEquals(usuarioId, atualizado.getId());
            }
        }

        @Nested
        class Quando_deletar_usuario extends ServerTest {

            private Long usuarioId;

            @BeforeEach
            void setup() {
                UsuarioResponse criado = usuarioService.criar(usuarioRequest);
                usuarioId = criado.getId();
                usuarioService.deletar(usuarioId);
            }

            @Test
            void Entao_deve_remover_do_banco_de_dados() {
                assertTrue(usuarioRepository.findById(usuarioId).isEmpty());
            }

            @Test
            void Entao_nao_deve_aparecer_na_listagem() {
                List<UsuarioResponse> todos = usuarioService.listarTodos();
                assertTrue(todos.isEmpty());
            }
        }

        @Nested
        class Quando_ativar_desativar_usuario extends ServerTest {

            private Long usuarioId;

            @BeforeEach
            void setup() {
                UsuarioResponse criado = usuarioService.criar(usuarioRequest);
                usuarioId = criado.getId();
            }

            @Test
            void Entao_deve_desativar_usuario() {
                UsuarioResponse desativado = usuarioService.ativarDesativar(usuarioId, false);
                assertFalse(desativado.getAtivo());
                
                Usuario salvo = usuarioRepository.findById(usuarioId).orElseThrow();
                assertFalse(salvo.getAtivo());
            }

            @Test
            void Entao_deve_ativar_usuario_desativado() {
                usuarioService.ativarDesativar(usuarioId, false);
                UsuarioResponse ativado = usuarioService.ativarDesativar(usuarioId, true);
                
                assertTrue(ativado.getAtivo());
            }
        }
    }

    @Nested
    @Transactional
    class Dado_um_email_ja_cadastrado extends ServerTest {

        private UsuarioRequest primeiroUsuario;
        private UsuarioRequest segundoUsuario;

        @BeforeEach
        void setup() {
            usuarioRepository.deleteAll();
            
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

    @Nested
    @Transactional
    class Dado_multiplos_usuarios_cadastrados extends ServerTest {

        @BeforeEach
        void setup() {
            usuarioRepository.deleteAll();
            
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
        class Quando_listar_todos extends ServerTest {

            @Test
            void Entao_deve_retornar_todos_usuarios() {
                List<UsuarioResponse> todos = usuarioService.listarTodos();
                assertEquals(3, todos.size());
            }
        }

        @Nested
        class Quando_buscar_por_tipo extends ServerTest {

            @Test
            void Entao_deve_retornar_apenas_cidadaos() {
                List<UsuarioResponse> cidadaos = usuarioService.buscarPorTipo(TipoUsuario.CIDADAO);
                assertEquals(2, cidadaos.size());
                assertTrue(cidadaos.stream().allMatch(u -> u.getTipo() == TipoUsuario.CIDADAO));
            }

            @Test
            void Entao_deve_retornar_apenas_admins() {
                List<UsuarioResponse> admins = usuarioService.buscarPorTipo(TipoUsuario.ADMIN);
                assertEquals(1, admins.size());
                assertEquals(TipoUsuario.ADMIN, admins.get(0).getTipo());
            }
        }

        @Nested
        class Quando_buscar_ativos extends ServerTest {

            @Test
            void Entao_deve_retornar_apenas_usuarios_ativos() {
                List<UsuarioResponse> ativos = usuarioService.buscarAtivos();
                assertEquals(2, ativos.size());
                assertTrue(ativos.stream().allMatch(UsuarioResponse::getAtivo));
            }
        }
    }

    @Nested
    @Transactional
    class Dado_um_usuario_inexistente extends ServerTest {

        @BeforeEach
        void setup() {
            usuarioRepository.deleteAll();
        }

        @Nested
        class Quando_buscar_por_id extends ServerTest {

            @Test
            void Entao_deve_lancar_excecao() {
                RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                    usuarioService.buscarPorId(999L);
                });
                assertTrue(exception.getMessage().contains("Usuário não encontrado"));
            }
        }

        @Nested
        class Quando_buscar_por_email extends ServerTest {

            @Test
            void Entao_deve_lancar_excecao() {
                RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                    usuarioService.buscarPorEmail("inexistente@example.com");
                });
                assertTrue(exception.getMessage().contains("Usuário não encontrado"));
            }
        }

        @Nested
        class Quando_tentar_deletar extends ServerTest {

            @Test
            void Entao_deve_lancar_excecao() {
                RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                    usuarioService.deletar(999L);
                });
                assertTrue(exception.getMessage().contains("Usuário não encontrado"));
            }
        }
    }
}
