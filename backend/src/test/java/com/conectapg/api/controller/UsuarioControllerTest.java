package com.conectapg.api.controller;

import com.conectapg.api.dto.UsuarioRequest;
import com.conectapg.api.dto.UsuarioResponse;
import com.conectapg.domain.model.Usuario;
import com.conectapg.domain.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @Nested
    class Dado_um_usuario_valido {

        UsuarioRequest usuarioRequest;
        UsuarioResponse usuarioResponse;

        @BeforeEach
        void setup() {
            usuarioRequest = new UsuarioRequest();
            usuarioRequest.setNome("João Silva");
            usuarioRequest.setEmail("joao@example.com");
            usuarioRequest.setSenha("senha123");
            usuarioRequest.setTipo(Usuario.TipoUsuario.CIDADAO);
            usuarioRequest.setAtivo(true);

            usuarioResponse = UsuarioResponse.builder()
                    .id(1L)
                    .nome("João Silva")
                    .email("joao@example.com")
                    .tipo(Usuario.TipoUsuario.CIDADAO)
                    .ativo(true)
                    .dataCriacao(LocalDateTime.now())
                    .totalOcorrencias(0)
                    .build();
        }

        @Nested
        @WithMockUser
        class Quando_listar_todos_usuarios {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                List<UsuarioResponse> usuarios = Arrays.asList(usuarioResponse);
                when(usuarioService.listarTodos()).thenReturn(usuarios);
                
                resultado = mockMvc.perform(get("/usuarios"));
            }

            @Test
            void deve_retornar_status_ok() throws Exception {
                resultado.andExpect(status().isOk());
            }

            @Test
            void deve_retornar_lista_com_nome_correto() throws Exception {
                resultado.andExpect(jsonPath("$[0].nome").value("João Silva"));
            }

            @Test
            void deve_retornar_lista_com_email_correto() throws Exception {
                resultado.andExpect(jsonPath("$[0].email").value("joao@example.com"));
            }
        }

        @Nested
        @WithMockUser
        class Quando_buscar_por_id {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                when(usuarioService.buscarPorId(1L)).thenReturn(usuarioResponse);
                resultado = mockMvc.perform(get("/usuarios/1"));
            }

            @Test
            void deve_retornar_status_ok() throws Exception {
                resultado.andExpect(status().isOk());
            }

            @Test
            void deve_retornar_usuario_com_nome_correto() throws Exception {
                resultado.andExpect(jsonPath("$.nome").value("João Silva"));
            }

            @Test
            void deve_retornar_usuario_com_email_correto() throws Exception {
                resultado.andExpect(jsonPath("$.email").value("joao@example.com"));
            }
        }

        @Nested
        @WithMockUser
        class Quando_buscar_por_email {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                when(usuarioService.buscarPorEmail("joao@example.com")).thenReturn(usuarioResponse);
                resultado = mockMvc.perform(get("/usuarios/email/joao@example.com"));
            }

            @Test
            void deve_retornar_status_ok() throws Exception {
                resultado.andExpect(status().isOk());
            }

            @Test
            void deve_retornar_usuario_com_nome_correto() throws Exception {
                resultado.andExpect(jsonPath("$.nome").value("João Silva"));
            }
        }

        @Nested
        @WithMockUser
        class Quando_buscar_por_tipo {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                UsuarioResponse usuarioAdmin = UsuarioResponse.builder()
                        .id(2L)
                        .nome("Admin Sistema")
                        .email("admin@conectapg.com")
                        .tipo(Usuario.TipoUsuario.ADMIN)
                        .ativo(true)
                        .dataCriacao(LocalDateTime.now())
                        .totalOcorrencias(0)
                        .build();

                List<UsuarioResponse> usuarios = Arrays.asList(usuarioAdmin);
                when(usuarioService.buscarPorTipo(Usuario.TipoUsuario.ADMIN)).thenReturn(usuarios);
                
                resultado = mockMvc.perform(get("/usuarios/tipo/ADMIN"));
            }

            @Test
            void deve_retornar_status_ok() throws Exception {
                resultado.andExpect(status().isOk());
            }

            @Test
            void deve_retornar_usuarios_do_tipo_especificado() throws Exception {
                resultado.andExpect(jsonPath("$[0].tipo").value("ADMIN"));
            }

            @Test
            void deve_retornar_nome_do_usuario() throws Exception {
                resultado.andExpect(jsonPath("$[0].nome").value("Admin Sistema"));
            }
        }

        @Nested
        @WithMockUser
        class Quando_buscar_usuarios_ativos {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                List<UsuarioResponse> usuarios = Arrays.asList(usuarioResponse);
                when(usuarioService.buscarAtivos()).thenReturn(usuarios);
                
                resultado = mockMvc.perform(get("/usuarios/ativos"));
            }

            @Test
            void deve_retornar_status_ok() throws Exception {
                resultado.andExpect(status().isOk());
            }

            @Test
            void deve_retornar_apenas_usuarios_ativos() throws Exception {
                resultado.andExpect(jsonPath("$[0].ativo").value(true));
            }

            @Test
            void deve_retornar_nome_do_usuario() throws Exception {
                resultado.andExpect(jsonPath("$[0].nome").value("João Silva"));
            }
        }

        @Nested
        @WithMockUser
        class Quando_criar_usuario {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                when(usuarioService.criar(any(UsuarioRequest.class))).thenReturn(usuarioResponse);
                
                resultado = mockMvc.perform(post("/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)));
            }

            @Test
            void deve_retornar_status_created() throws Exception {
                resultado.andExpect(status().isCreated());
            }

            @Test
            void deve_retornar_usuario_com_nome_correto() throws Exception {
                resultado.andExpect(jsonPath("$.nome").value("João Silva"));
            }

            @Test
            void deve_retornar_usuario_com_email_correto() throws Exception {
                resultado.andExpect(jsonPath("$.email").value("joao@example.com"));
            }
        }

        @Nested
        @WithMockUser
        class Quando_atualizar_usuario {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                when(usuarioService.atualizar(eq(1L), any(UsuarioRequest.class))).thenReturn(usuarioResponse);
                
                resultado = mockMvc.perform(put("/usuarios/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)));
            }

            @Test
            void deve_retornar_status_ok() throws Exception {
                resultado.andExpect(status().isOk());
            }

            @Test
            void deve_retornar_usuario_atualizado() throws Exception {
                resultado.andExpect(jsonPath("$.nome").value("João Silva"));
            }
        }

        @Nested
        @WithMockUser
        class Quando_ativar_desativar_usuario {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                UsuarioResponse usuarioInativo = UsuarioResponse.builder()
                        .id(1L)
                        .nome("João Silva")
                        .email("joao@example.com")
                        .tipo(Usuario.TipoUsuario.CIDADAO)
                        .ativo(false)
                        .dataCriacao(LocalDateTime.now())
                        .totalOcorrencias(0)
                        .build();

                when(usuarioService.ativarDesativar(1L, false)).thenReturn(usuarioInativo);
                
                resultado = mockMvc.perform(patch("/usuarios/1/ativo")
                        .with(csrf())
                        .param("ativo", "false"));
            }

            @Test
            void deve_retornar_status_ok() throws Exception {
                resultado.andExpect(status().isOk());
            }

            @Test
            void deve_retornar_usuario_com_status_atualizado() throws Exception {
                resultado.andExpect(jsonPath("$.ativo").value(false));
            }
        }

        @Nested
        @WithMockUser
        class Quando_deletar_usuario {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                resultado = mockMvc.perform(delete("/usuarios/1").with(csrf()));
            }

            @Test
            void deve_retornar_status_no_content() throws Exception {
                resultado.andExpect(status().isNoContent());
            }
        }
    }

    @Nested
    @WithMockUser
    class Dado_um_usuario_com_dados_invalidos {

        UsuarioRequest usuarioRequest;

        @BeforeEach
        void setup() {
            usuarioRequest = new UsuarioRequest();
            usuarioRequest.setNome("João Silva");
            usuarioRequest.setEmail("joao@example.com");
            usuarioRequest.setSenha("senha123");
            usuarioRequest.setTipo(Usuario.TipoUsuario.CIDADAO);
        }

        @Nested
        class Quando_criar_com_email_invalido {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                usuarioRequest.setEmail("email-invalido");
                
                resultado = mockMvc.perform(post("/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)));
            }

            @Test
            void deve_retornar_status_bad_request() throws Exception {
                resultado.andExpect(status().isBadRequest());
            }
        }

        @Nested
        class Quando_criar_com_senha_curta {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                usuarioRequest.setSenha("123");
                
                resultado = mockMvc.perform(post("/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)));
            }

            @Test
            void deve_retornar_status_bad_request() throws Exception {
                resultado.andExpect(status().isBadRequest());
            }
        }

        @Nested
        class Quando_criar_sem_nome {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                usuarioRequest.setNome("");
                
                resultado = mockMvc.perform(post("/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)));
            }

            @Test
            void deve_retornar_status_bad_request() throws Exception {
                resultado.andExpect(status().isBadRequest());
            }
        }
    }
}
