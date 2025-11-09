package com.conectapg.api.controller;

import com.conectapg.domain.model.Ocorrencia;
import com.conectapg.domain.model.Usuario;
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
import static org.mockito.Mockito.when;
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

    private Ocorrencia ocorrencia;

    @BeforeEach
    void setUp() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Teste Usuario");
        usuario.setEmail("teste@example.com");

        ocorrencia = new Ocorrencia();
        ocorrencia.setId(1L);
        ocorrencia.setTitulo("Teste Ocorrência");
        ocorrencia.setDescricao("Descrição de teste");
        ocorrencia.setLocalizacao("Local de teste");
        ocorrencia.setTipo(Ocorrencia.TipoOcorrencia.ILUMINACAO);
        ocorrencia.setStatus(Ocorrencia.StatusOcorrencia.ABERTA);
        ocorrencia.setUsuario(usuario);
        ocorrencia.setDataCriacao(LocalDateTime.now());
    }

    @Test
    @WithMockUser
    void deveListarTodasOcorrencias() throws Exception {
        List<Ocorrencia> ocorrencias = Arrays.asList(ocorrencia);
        when(ocorrenciaService.listarTodas()).thenReturn(ocorrencias);

        mockMvc.perform(get("/ocorrencias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Teste Ocorrência"));
    }

    @Test
    @WithMockUser
    void deveBuscarOcorrenciaPorId() throws Exception {
        when(ocorrenciaService.buscarPorId(1L)).thenReturn(ocorrencia);

        mockMvc.perform(get("/ocorrencias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Teste Ocorrência"));
    }
}
