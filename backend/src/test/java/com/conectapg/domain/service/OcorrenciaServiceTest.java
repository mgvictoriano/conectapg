package com.conectapg.domain.service;

import com.conectapg.api.dto.OcorrenciaRequest;
import com.conectapg.api.dto.OcorrenciaResponse;
import com.conectapg.api.mapper.OcorrenciaMapper;
import com.conectapg.domain.model.Ocorrencia;
import com.conectapg.domain.model.Ocorrencia.StatusOcorrencia;
import com.conectapg.domain.model.Ocorrencia.TipoOcorrencia;
import com.conectapg.domain.model.Usuario;
import com.conectapg.domain.repository.OcorrenciaRepository;
import com.conectapg.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OcorrenciaServiceTest {

    @Mock
    private OcorrenciaRepository ocorrenciaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private OcorrenciaMapper mapper;

    @InjectMocks
    private OcorrenciaService ocorrenciaService;

    private Ocorrencia ocorrencia;
    private Usuario usuario;
    private OcorrenciaRequest ocorrenciaRequest;
    private OcorrenciaResponse ocorrenciaResponse;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@example.com");

        ocorrencia = new Ocorrencia();
        ocorrencia.setId(1L);
        ocorrencia.setTitulo("Poste queimado");
        ocorrencia.setDescricao("Poste da rua está sem iluminação");
        ocorrencia.setLocalizacao("Rua das Flores, 123");
        ocorrencia.setTipo(TipoOcorrencia.ILUMINACAO);
        ocorrencia.setStatus(StatusOcorrencia.ABERTA);
        ocorrencia.setUsuario(usuario);
        ocorrencia.setDataCriacao(LocalDateTime.now());

        ocorrenciaRequest = new OcorrenciaRequest();
        ocorrenciaRequest.setTitulo("Poste queimado");
        ocorrenciaRequest.setDescricao("Poste da rua está sem iluminação");
        ocorrenciaRequest.setLocalizacao("Rua das Flores, 123");
        ocorrenciaRequest.setTipo(TipoOcorrencia.ILUMINACAO);
        ocorrenciaRequest.setStatus(StatusOcorrencia.ABERTA);
        ocorrenciaRequest.setUsuarioId(1L);

        OcorrenciaResponse.UsuarioResumo usuarioResumo = OcorrenciaResponse.UsuarioResumo.builder()
                .id(1L)
                .nome("João Silva")
                .email("joao@example.com")
                .build();

        ocorrenciaResponse = OcorrenciaResponse.builder()
                .id(1L)
                .titulo("Poste queimado")
                .descricao("Poste da rua está sem iluminação")
                .localizacao("Rua das Flores, 123")
                .tipo(TipoOcorrencia.ILUMINACAO)
                .status(StatusOcorrencia.ABERTA)
                .usuario(usuarioResumo)
                .dataCriacao(LocalDateTime.now())
                .dataAtualizacao(LocalDateTime.now())
                .build();
    }

    @Test
    void deveListarTodasOcorrencias() {
        // Dado
        List<Ocorrencia> ocorrencias = Arrays.asList(ocorrencia);
        when(ocorrenciaRepository.findAllOrderByDataCriacaoDesc()).thenReturn(ocorrencias);
        when(mapper.toResponse(any(Ocorrencia.class))).thenReturn(ocorrenciaResponse);

        // Quando
        List<OcorrenciaResponse> resultado = ocorrenciaService.listarTodas();

        // Então
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTitulo()).isEqualTo("Poste queimado");
        verify(ocorrenciaRepository).findAllOrderByDataCriacaoDesc();
        verify(mapper).toResponse(ocorrencia);
    }

    @Test
    void deveBuscarOcorrenciaPorId() {
        // Dado
        when(ocorrenciaRepository.findById(1L)).thenReturn(Optional.of(ocorrencia));
        when(mapper.toResponse(ocorrencia)).thenReturn(ocorrenciaResponse);

        // Quando
        OcorrenciaResponse resultado = ocorrenciaService.buscarPorId(1L);

        // Então
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getTitulo()).isEqualTo("Poste queimado");
        verify(ocorrenciaRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoOcorrenciaNaoEncontradaPorId() {
        // Dado
        when(ocorrenciaRepository.findById(999L)).thenReturn(Optional.empty());

        // Quando/Então
        assertThatThrownBy(() -> ocorrenciaService.buscarPorId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ocorrência não encontrada com id: 999");
    }

    @Test
    void deveBuscarOcorrenciasPorStatus() {
        // Dado
        List<Ocorrencia> ocorrencias = Arrays.asList(ocorrencia);
        when(ocorrenciaRepository.findByStatus(StatusOcorrencia.ABERTA)).thenReturn(ocorrencias);
        when(mapper.toResponse(ocorrencia)).thenReturn(ocorrenciaResponse);

        // Quando
        List<OcorrenciaResponse> resultado = ocorrenciaService.buscarPorStatus(StatusOcorrencia.ABERTA);

        // Então
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getStatus()).isEqualTo(StatusOcorrencia.ABERTA);
        verify(ocorrenciaRepository).findByStatus(StatusOcorrencia.ABERTA);
    }

    @Test
    void deveBuscarOcorrenciasPorUsuario() {
        // Dado
        List<Ocorrencia> ocorrencias = Arrays.asList(ocorrencia);
        when(ocorrenciaRepository.findByUsuarioId(1L)).thenReturn(ocorrencias);
        when(mapper.toResponse(ocorrencia)).thenReturn(ocorrenciaResponse);

        // Quando
        List<OcorrenciaResponse> resultado = ocorrenciaService.buscarPorUsuario(1L);

        // Então
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getUsuario().getId()).isEqualTo(1L);
        verify(ocorrenciaRepository).findByUsuarioId(1L);
    }

    @Test
    void deveBuscarOcorrenciasPorLocalizacao() {
        // Dado
        List<Ocorrencia> ocorrencias = Arrays.asList(ocorrencia);
        when(ocorrenciaRepository.findByLocalizacaoContaining("Rua das Flores")).thenReturn(ocorrencias);
        when(mapper.toResponse(ocorrencia)).thenReturn(ocorrenciaResponse);

        // Quando
        List<OcorrenciaResponse> resultado = ocorrenciaService.buscarPorLocalizacao("Rua das Flores");

        // Então
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getLocalizacao()).contains("Rua das Flores");
        verify(ocorrenciaRepository).findByLocalizacaoContaining("Rua das Flores");
    }

    @Test
    void deveCriarNovaOcorrencia() {
        // Dado
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(mapper.toEntity(ocorrenciaRequest, usuario)).thenReturn(ocorrencia);
        when(ocorrenciaRepository.save(ocorrencia)).thenReturn(ocorrencia);
        when(mapper.toResponse(ocorrencia)).thenReturn(ocorrenciaResponse);

        // Quando
        OcorrenciaResponse resultado = ocorrenciaService.criar(ocorrenciaRequest);

        // Então
        assertThat(resultado).isNotNull();
        assertThat(resultado.getTitulo()).isEqualTo("Poste queimado");
        verify(usuarioRepository).findById(1L);
        verify(ocorrenciaRepository).save(ocorrencia);
    }

    @Test
    void deveLancarExcecaoAoCriarOcorrenciaComUsuarioInexistente() {
        // Dado
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());
        ocorrenciaRequest.setUsuarioId(999L);

        // Quando/Então
        assertThatThrownBy(() -> ocorrenciaService.criar(ocorrenciaRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuário não encontrado com id: 999");
        
        verify(ocorrenciaRepository, never()).save(any());
    }

    @Test
    void deveAtualizarOcorrencia() {
        // Dado
        when(ocorrenciaRepository.findById(1L)).thenReturn(Optional.of(ocorrencia));
        when(ocorrenciaRepository.save(ocorrencia)).thenReturn(ocorrencia);
        when(mapper.toResponse(ocorrencia)).thenReturn(ocorrenciaResponse);

        // Quando
        OcorrenciaResponse resultado = ocorrenciaService.atualizar(1L, ocorrenciaRequest);

        // Então
        assertThat(resultado).isNotNull();
        verify(mapper).updateEntity(ocorrencia, ocorrenciaRequest);
        verify(ocorrenciaRepository).save(ocorrencia);
    }

    @Test
    void deveLancarExcecaoAoAtualizarOcorrenciaInexistente() {
        // Dado
        when(ocorrenciaRepository.findById(999L)).thenReturn(Optional.empty());

        // Quando/Então
        assertThatThrownBy(() -> ocorrenciaService.atualizar(999L, ocorrenciaRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ocorrência não encontrada");
        
        verify(ocorrenciaRepository, never()).save(any());
    }

    @Test
    void deveAtualizarStatusDaOcorrencia() {
        // Dado
        when(ocorrenciaRepository.findById(1L)).thenReturn(Optional.of(ocorrencia));
        when(ocorrenciaRepository.save(ocorrencia)).thenReturn(ocorrencia);
        
        OcorrenciaResponse ocorrenciaAtualizada = OcorrenciaResponse.builder()
                .id(1L)
                .status(StatusOcorrencia.EM_ANDAMENTO)
                .build();
        when(mapper.toResponse(ocorrencia)).thenReturn(ocorrenciaAtualizada);

        // Quando
        OcorrenciaResponse resultado = ocorrenciaService.atualizarStatus(1L, StatusOcorrencia.EM_ANDAMENTO);

        // Então
        assertThat(resultado.getStatus()).isEqualTo(StatusOcorrencia.EM_ANDAMENTO);
        verify(ocorrenciaRepository).save(ocorrencia);
    }

    @Test
    void deveLancarExcecaoAoAtualizarStatusDeOcorrenciaInexistente() {
        // Dado
        when(ocorrenciaRepository.findById(999L)).thenReturn(Optional.empty());

        // Quando/Então
        assertThatThrownBy(() -> ocorrenciaService.atualizarStatus(999L, StatusOcorrencia.EM_ANDAMENTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ocorrência não encontrada");
    }

    @Test
    void deveDeletarOcorrencia() {
        // Dado
        when(ocorrenciaRepository.findById(1L)).thenReturn(Optional.of(ocorrencia));

        // Quando
        ocorrenciaService.deletar(1L);

        // Então
        verify(ocorrenciaRepository).findById(1L);
        verify(ocorrenciaRepository).delete(ocorrencia);
    }

    @Test
    void deveLancarExcecaoAoDeletarOcorrenciaInexistente() {
        // Dado
        when(ocorrenciaRepository.findById(999L)).thenReturn(Optional.empty());

        // Quando/Então
        assertThatThrownBy(() -> ocorrenciaService.deletar(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ocorrência não encontrada");
        
        verify(ocorrenciaRepository, never()).delete(any());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverOcorrencias() {
        // Dado
        when(ocorrenciaRepository.findAllOrderByDataCriacaoDesc()).thenReturn(Arrays.asList());

        // Quando
        List<OcorrenciaResponse> resultado = ocorrenciaService.listarTodas();

        // Então
        assertThat(resultado).isEmpty();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverOcorrenciasComStatus() {
        // Dado
        when(ocorrenciaRepository.findByStatus(StatusOcorrencia.RESOLVIDA)).thenReturn(Arrays.asList());

        // Quando
        List<OcorrenciaResponse> resultado = ocorrenciaService.buscarPorStatus(StatusOcorrencia.RESOLVIDA);

        // Então
        assertThat(resultado).isEmpty();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverOcorrenciasDoUsuario() {
        // Dado
        when(ocorrenciaRepository.findByUsuarioId(999L)).thenReturn(Arrays.asList());

        // Quando
        List<OcorrenciaResponse> resultado = ocorrenciaService.buscarPorUsuario(999L);

        // Então
        assertThat(resultado).isEmpty();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverOcorrenciasNaLocalizacao() {
        // Dado
        when(ocorrenciaRepository.findByLocalizacaoContaining("Local Inexistente")).thenReturn(Arrays.asList());

        // Quando
        List<OcorrenciaResponse> resultado = ocorrenciaService.buscarPorLocalizacao("Local Inexistente");

        // Então
        assertThat(resultado).isEmpty();
    }
}
