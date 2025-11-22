package com.conectapg.api.controller;

import com.conectapg.api.dto.OcorrenciaRequest;
import com.conectapg.api.dto.OcorrenciaResponse;
import com.conectapg.domain.model.Ocorrencia;
import com.conectapg.domain.service.OcorrenciaService;
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

@WebMvcTest(OcorrenciaController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OcorrenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OcorrenciaService ocorrenciaService;

    @Nested
    class Dado_uma_ocorrencia_valida {

        OcorrenciaRequest ocorrenciaRequest;
        OcorrenciaResponse ocorrenciaResponse;

        @BeforeEach
        void setup() {
            ocorrenciaRequest = new OcorrenciaRequest();
            ocorrenciaRequest.setTitulo("Teste Ocorrência");
            ocorrenciaRequest.setDescricao("Descrição de teste");
            ocorrenciaRequest.setLocalizacao("Local de teste");
            ocorrenciaRequest.setTipo(Ocorrencia.TipoOcorrencia.ILUMINACAO);
            ocorrenciaRequest.setStatus(Ocorrencia.StatusOcorrencia.ABERTA);
            ocorrenciaRequest.setUsuarioId(1L);

            OcorrenciaResponse.UsuarioResumo usuarioResumo = OcorrenciaResponse.UsuarioResumo.builder()
                    .id(1L)
                    .nome("Teste Usuario")
                    .email("teste@example.com")
                    .build();

            ocorrenciaResponse = OcorrenciaResponse.builder()
                    .id(1L)
                    .titulo("Teste Ocorrência")
                    .descricao("Descrição de teste")
                    .localizacao("Local de teste")
                    .tipo(Ocorrencia.TipoOcorrencia.ILUMINACAO)
                    .status(Ocorrencia.StatusOcorrencia.ABERTA)
                    .usuario(usuarioResumo)
                    .dataCriacao(LocalDateTime.now())
                    .dataAtualizacao(LocalDateTime.now())
                    .build();
        }

        @Nested
        @WithMockUser
        class Quando_listar_todas_ocorrencias {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                List<OcorrenciaResponse> ocorrencias = Arrays.asList(ocorrenciaResponse);
                when(ocorrenciaService.listarTodas()).thenReturn(ocorrencias);
                
                resultado = mockMvc.perform(get("/ocorrencias"));
            }

            @Test
            void deve_retornar_status_ok() throws Exception {
                resultado.andExpect(status().isOk());
            }

            @Test
            void deve_retornar_lista_com_titulo_correto() throws Exception {
                resultado.andExpect(jsonPath("$[0].titulo").value("Teste Ocorrência"));
            }
        }

        @Nested
        @WithMockUser
        class Quando_buscar_por_id {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                when(ocorrenciaService.buscarPorId(1L)).thenReturn(ocorrenciaResponse);
                resultado = mockMvc.perform(get("/ocorrencias/1"));
            }

            @Test
            void deve_retornar_status_ok() throws Exception {
                resultado.andExpect(status().isOk());
            }

            @Test
            void deve_retornar_ocorrencia_com_titulo_correto() throws Exception {
                resultado.andExpect(jsonPath("$.titulo").value("Teste Ocorrência"));
            }
        }

        @Nested
        @WithMockUser
        class Quando_buscar_por_status {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                List<OcorrenciaResponse> ocorrencias = Arrays.asList(ocorrenciaResponse);
                when(ocorrenciaService.buscarPorStatus(Ocorrencia.StatusOcorrencia.ABERTA)).thenReturn(ocorrencias);
                
                resultado = mockMvc.perform(get("/ocorrencias/status/ABERTA"));
            }

            @Test
            void deve_retornar_status_ok() throws Exception {
                resultado.andExpect(status().isOk());
            }

            @Test
            void deve_retornar_ocorrencias_com_status_correto() throws Exception {
                resultado.andExpect(jsonPath("$[0].status").value("ABERTA"));
            }

            @Test
            void deve_retornar_titulo_da_ocorrencia() throws Exception {
                resultado.andExpect(jsonPath("$[0].titulo").value("Teste Ocorrência"));
            }
        }

        @Nested
        @WithMockUser
        class Quando_buscar_por_usuario {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                List<OcorrenciaResponse> ocorrencias = Arrays.asList(ocorrenciaResponse);
                when(ocorrenciaService.buscarPorUsuario(1L)).thenReturn(ocorrencias);
                
                resultado = mockMvc.perform(get("/ocorrencias/usuario/1"));
            }

            @Test
            void deve_retornar_status_ok() throws Exception {
                resultado.andExpect(status().isOk());
            }

            @Test
            void deve_retornar_ocorrencias_do_usuario() throws Exception {
                resultado.andExpect(jsonPath("$[0].usuario.id").value(1));
            }

            @Test
            void deve_retornar_titulo_da_ocorrencia() throws Exception {
                resultado.andExpect(jsonPath("$[0].titulo").value("Teste Ocorrência"));
            }
        }

        @Nested
        @WithMockUser
        class Quando_buscar_por_localizacao {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                List<OcorrenciaResponse> ocorrencias = Arrays.asList(ocorrenciaResponse);
                when(ocorrenciaService.buscarPorLocalizacao("Local de teste")).thenReturn(ocorrencias);
                
                resultado = mockMvc.perform(get("/ocorrencias/localizacao")
                        .param("localizacao", "Local de teste"));
            }

            @Test
            void deve_retornar_status_ok() throws Exception {
                resultado.andExpect(status().isOk());
            }

            @Test
            void deve_retornar_ocorrencias_da_localizacao() throws Exception {
                resultado.andExpect(jsonPath("$[0].localizacao").value("Local de teste"));
            }
        }

        @Nested
        @WithMockUser
        class Quando_criar_ocorrencia {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                when(ocorrenciaService.criar(any(OcorrenciaRequest.class))).thenReturn(ocorrenciaResponse);
                
                resultado = mockMvc.perform(post("/ocorrencias")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ocorrenciaRequest)));
            }

            @Test
            void deve_retornar_status_created() throws Exception {
                resultado.andExpect(status().isCreated());
            }

            @Test
            void deve_retornar_ocorrencia_com_titulo_correto() throws Exception {
                resultado.andExpect(jsonPath("$.titulo").value("Teste Ocorrência"));
            }
        }

        @Nested
        @WithMockUser
        class Quando_atualizar_ocorrencia {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                when(ocorrenciaService.atualizar(eq(1L), any(OcorrenciaRequest.class))).thenReturn(ocorrenciaResponse);
                
                resultado = mockMvc.perform(put("/ocorrencias/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ocorrenciaRequest)));
            }

            @Test
            void deve_retornar_status_ok() throws Exception {
                resultado.andExpect(status().isOk());
            }

            @Test
            void deve_retornar_ocorrencia_atualizada() throws Exception {
                resultado.andExpect(jsonPath("$.titulo").value("Teste Ocorrência"));
            }
        }

        @Nested
        @WithMockUser
        class Quando_atualizar_status_da_ocorrencia {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                OcorrenciaResponse ocorrenciaAtualizada = OcorrenciaResponse.builder()
                        .id(1L)
                        .titulo("Teste Ocorrência")
                        .descricao("Descrição de teste")
                        .localizacao("Local de teste")
                        .tipo(Ocorrencia.TipoOcorrencia.ILUMINACAO)
                        .status(Ocorrencia.StatusOcorrencia.EM_ANDAMENTO)
                        .usuario(ocorrenciaResponse.getUsuario())
                        .dataCriacao(LocalDateTime.now())
                        .dataAtualizacao(LocalDateTime.now())
                        .build();

                when(ocorrenciaService.atualizarStatus(1L, Ocorrencia.StatusOcorrencia.EM_ANDAMENTO))
                        .thenReturn(ocorrenciaAtualizada);
                
                resultado = mockMvc.perform(patch("/ocorrencias/1/status")
                        .with(csrf())
                        .param("status", "EM_ANDAMENTO"));
            }

            @Test
            void deve_retornar_status_ok() throws Exception {
                resultado.andExpect(status().isOk());
            }

            @Test
            void deve_retornar_ocorrencia_com_status_atualizado() throws Exception {
                resultado.andExpect(jsonPath("$.status").value("EM_ANDAMENTO"));
            }
        }

        @Nested
        @WithMockUser
        class Quando_deletar_ocorrencia {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                resultado = mockMvc.perform(delete("/ocorrencias/1").with(csrf()));
            }

            @Test
            void deve_retornar_status_no_content() throws Exception {
                resultado.andExpect(status().isNoContent());
            }
        }
    }

    @Nested
    @WithMockUser
    class Dado_uma_ocorrencia_com_dados_invalidos {

        OcorrenciaRequest ocorrenciaRequest;

        @BeforeEach
        void setup() {
            ocorrenciaRequest = new OcorrenciaRequest();
            ocorrenciaRequest.setTitulo("Teste");
            ocorrenciaRequest.setDescricao("Descrição");
            ocorrenciaRequest.setLocalizacao("Local");
            ocorrenciaRequest.setTipo(Ocorrencia.TipoOcorrencia.ILUMINACAO);
        }

        @Nested
        class Quando_criar_sem_titulo {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                ocorrenciaRequest.setTitulo("");
                
                resultado = mockMvc.perform(post("/ocorrencias")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ocorrenciaRequest)));
            }

            @Test
            void deve_retornar_status_bad_request() throws Exception {
                resultado.andExpect(status().isBadRequest());
            }
        }

        @Nested
        class Quando_criar_sem_descricao {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                ocorrenciaRequest.setDescricao("");
                
                resultado = mockMvc.perform(post("/ocorrencias")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ocorrenciaRequest)));
            }

            @Test
            void deve_retornar_status_bad_request() throws Exception {
                resultado.andExpect(status().isBadRequest());
            }
        }

        @Nested
        class Quando_criar_sem_localizacao {

            ResultActions resultado;

            @BeforeEach
            void setup() throws Exception {
                ocorrenciaRequest.setLocalizacao("");
                
                resultado = mockMvc.perform(post("/ocorrencias")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ocorrenciaRequest)));
            }

            @Test
            void deve_retornar_status_bad_request() throws Exception {
                resultado.andExpect(status().isBadRequest());
            }
        }
    }
}
