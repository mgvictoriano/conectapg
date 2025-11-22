package com.conectapg.api.controller;

import com.conectapg.api.dto.OcorrenciaRequest;
import com.conectapg.api.dto.OcorrenciaResponse;
import com.conectapg.domain.model.Ocorrencia;
import com.conectapg.domain.service.OcorrenciaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

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
class OcorrenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OcorrenciaService ocorrenciaService;

    private OcorrenciaRequest ocorrenciaRequest;
    private OcorrenciaResponse ocorrenciaResponse;

    @BeforeEach
    void setUp() {
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

    @Test
    @WithMockUser
    void deveListarTodasOcorrencias() throws Exception {
        List<OcorrenciaResponse> ocorrencias = Arrays.asList(ocorrenciaResponse);
        when(ocorrenciaService.listarTodas()).thenReturn(ocorrencias);

        mockMvc.perform(get("/ocorrencias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Teste Ocorrência"));
    }

    @Test
    @WithMockUser
    void deveBuscarOcorrenciaPorId() throws Exception {
        when(ocorrenciaService.buscarPorId(1L)).thenReturn(ocorrenciaResponse);

        mockMvc.perform(get("/ocorrencias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Teste Ocorrência"));
    }

    @Test
    @WithMockUser
    void deveCriarOcorrencia() throws Exception {
        when(ocorrenciaService.criar(any(OcorrenciaRequest.class))).thenReturn(ocorrenciaResponse);

        mockMvc.perform(post("/ocorrencias")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ocorrenciaRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Teste Ocorrência"));
    }

    @Test
    @WithMockUser
    void deveAtualizarOcorrencia() throws Exception {
        when(ocorrenciaService.atualizar(eq(1L), any(OcorrenciaRequest.class))).thenReturn(ocorrenciaResponse);

        mockMvc.perform(put("/ocorrencias/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ocorrenciaRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Teste Ocorrência"));
    }

    @Test
    @WithMockUser
    void deveBuscarOcorrenciasPorStatus() throws Exception {
        List<OcorrenciaResponse> ocorrencias = Arrays.asList(ocorrenciaResponse);
        when(ocorrenciaService.buscarPorStatus(Ocorrencia.StatusOcorrencia.ABERTA)).thenReturn(ocorrencias);

        mockMvc.perform(get("/ocorrencias/status/ABERTA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("ABERTA"))
                .andExpect(jsonPath("$[0].titulo").value("Teste Ocorrência"));
    }

    @Test
    @WithMockUser
    void deveBuscarOcorrenciasPorUsuario() throws Exception {
        List<OcorrenciaResponse> ocorrencias = Arrays.asList(ocorrenciaResponse);
        when(ocorrenciaService.buscarPorUsuario(1L)).thenReturn(ocorrencias);

        mockMvc.perform(get("/ocorrencias/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuario.id").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Teste Ocorrência"));
    }

    @Test
    @WithMockUser
    void deveBuscarOcorrenciasPorLocalizacao() throws Exception {
        List<OcorrenciaResponse> ocorrencias = Arrays.asList(ocorrenciaResponse);
        when(ocorrenciaService.buscarPorLocalizacao("Local de teste")).thenReturn(ocorrencias);

        mockMvc.perform(get("/ocorrencias/localizacao")
                        .param("localizacao", "Local de teste"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].localizacao").value("Local de teste"));
    }

    @Test
    @WithMockUser
    void deveAtualizarStatusDaOcorrencia() throws Exception {
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

        mockMvc.perform(patch("/ocorrencias/1/status")
                        .with(csrf())
                        .param("status", "EM_ANDAMENTO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("EM_ANDAMENTO"));
    }

    @Test
    @WithMockUser
    void deveDeletarOcorrencia() throws Exception {
        mockMvc.perform(delete("/ocorrencias/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void deveRejeitarOcorrenciaSemTitulo() throws Exception {
        ocorrenciaRequest.setTitulo("");

        mockMvc.perform(post("/ocorrencias")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ocorrenciaRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void deveRejeitarOcorrenciaSemDescricao() throws Exception {
        ocorrenciaRequest.setDescricao("");

        mockMvc.perform(post("/ocorrencias")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ocorrenciaRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void deveRejeitarOcorrenciaSemLocalizacao() throws Exception {
        ocorrenciaRequest.setLocalizacao("");

        mockMvc.perform(post("/ocorrencias")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ocorrenciaRequest)))
                .andExpect(status().isBadRequest());
    }
}
