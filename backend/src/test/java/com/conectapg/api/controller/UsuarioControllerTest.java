package com.conectapg.api.controller;

import com.conectapg.api.dto.UsuarioRequest;
import com.conectapg.api.dto.UsuarioResponse;
import com.conectapg.domain.model.Usuario;
import com.conectapg.domain.service.UsuarioService;
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

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    private UsuarioRequest usuarioRequest;
    private UsuarioResponse usuarioResponse;

    @BeforeEach
    void setUp() {
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

    @Test
    @WithMockUser
    void deveListarTodosUsuarios() throws Exception {
        List<UsuarioResponse> usuarios = Arrays.asList(usuarioResponse);
        when(usuarioService.listarTodos()).thenReturn(usuarios);

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                .andExpect(jsonPath("$[0].email").value("joao@example.com"));
    }

    @Test
    @WithMockUser
    void deveBuscarUsuarioPorId() throws Exception {
        when(usuarioService.buscarPorId(1L)).thenReturn(usuarioResponse);

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao@example.com"));
    }

    @Test
    @WithMockUser
    void deveBuscarUsuarioPorEmail() throws Exception {
        when(usuarioService.buscarPorEmail("joao@example.com")).thenReturn(usuarioResponse);

        mockMvc.perform(get("/usuarios/email/joao@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    @WithMockUser
    void deveCriarUsuario() throws Exception {
        when(usuarioService.criar(any(UsuarioRequest.class))).thenReturn(usuarioResponse);

        mockMvc.perform(post("/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao@example.com"));
    }

    @Test
    @WithMockUser
    void deveAtualizarUsuario() throws Exception {
        when(usuarioService.atualizar(eq(1L), any(UsuarioRequest.class))).thenReturn(usuarioResponse);

        mockMvc.perform(put("/usuarios/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    @WithMockUser
    void deveAtivarDesativarUsuario() throws Exception {
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

        mockMvc.perform(patch("/usuarios/1/ativo")
                        .with(csrf())
                        .param("ativo", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ativo").value(false));
    }

    @Test
    @WithMockUser
    void deveDeletarUsuario() throws Exception {
        mockMvc.perform(delete("/usuarios/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void deveRejeitarUsuarioComEmailInvalido() throws Exception {
        usuarioRequest.setEmail("email-invalido");

        mockMvc.perform(post("/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void deveRejeitarUsuarioComSenhaCurta() throws Exception {
        usuarioRequest.setSenha("123");

        mockMvc.perform(post("/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void deveRejeitarUsuarioSemNome() throws Exception {
        usuarioRequest.setNome("");

        mockMvc.perform(post("/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isBadRequest());
    }
}
