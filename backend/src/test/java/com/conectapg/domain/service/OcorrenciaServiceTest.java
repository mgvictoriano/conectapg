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
import org.junit.jupiter.api.*;
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
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OcorrenciaServiceTest {

    @Mock
    private OcorrenciaRepository ocorrenciaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private OcorrenciaMapper mapper;

    @InjectMocks
    private OcorrenciaService ocorrenciaService;

    @Nested
    class Dado_uma_ocorrencia_valida {

        Ocorrencia ocorrencia;
        Usuario usuario;
        OcorrenciaRequest ocorrenciaRequest;
        OcorrenciaResponse ocorrenciaResponse;

        @BeforeEach
        void setup() {
            usuario = Usuario.builder()
                    .id(1L)
                    .nome("João Silva")
                    .email("joao@example.com")
                    .build();

            ocorrencia = Ocorrencia.builder()
                    .id(1L)
                    .titulo("Poste queimado")
                    .descricao("Poste da rua está sem iluminação")
                    .localizacao("Rua das Flores, 123")
                    .tipo(TipoOcorrencia.ILUMINACAO)
                    .status(StatusOcorrencia.ABERTA)
                    .usuario(usuario)
                    .dataCriacao(LocalDateTime.now())
                    .build();

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

        @Nested
        class Quando_listar_todas_ocorrencias {

            List<OcorrenciaResponse> resultado;

            @BeforeEach
            void setup() {
                when(ocorrenciaRepository.findAllOrderByDataCriacaoDesc()).thenReturn(Arrays.asList(ocorrencia));
                when(mapper.toResponse(any(Ocorrencia.class))).thenReturn(ocorrenciaResponse);
                resultado = ocorrenciaService.listarTodas();
            }

            @Test
            void deve_retornar_lista_com_ocorrencias() {
                assertThat(resultado).hasSize(1);
                assertThat(resultado.get(0).getTitulo()).isEqualTo("Poste queimado");
            }

            @Test
            void deve_chamar_repository_ordenado_por_data() {
                verify(ocorrenciaRepository).findAllOrderByDataCriacaoDesc();
            }

            @Test
            void deve_mapear_para_response() {
                verify(mapper).toResponse(ocorrencia);
            }
        }

        @Nested
        class Quando_buscar_por_id {

            @BeforeEach
            void setup() {
                when(ocorrenciaRepository.findById(1L)).thenReturn(Optional.of(ocorrencia));
                when(mapper.toResponse(ocorrencia)).thenReturn(ocorrenciaResponse);
            }

            @Test
            void deve_retornar_ocorrencia_encontrada() {
                OcorrenciaResponse resultado = ocorrenciaService.buscarPorId(1L);
                
                assertThat(resultado).isNotNull();
                assertThat(resultado.getId()).isEqualTo(1L);
                assertThat(resultado.getTitulo()).isEqualTo("Poste queimado");
            }

            @Test
            void deve_chamar_repository_com_id_correto() {
                ocorrenciaService.buscarPorId(1L);
                verify(ocorrenciaRepository).findById(1L);
            }
        }

        @Nested
        class Quando_buscar_por_status {

            @BeforeEach
            void setup() {
                when(ocorrenciaRepository.findByStatus(StatusOcorrencia.ABERTA)).thenReturn(Arrays.asList(ocorrencia));
                when(mapper.toResponse(ocorrencia)).thenReturn(ocorrenciaResponse);
            }

            @Test
            void deve_retornar_ocorrencias_com_status_especificado() {
                List<OcorrenciaResponse> resultado = ocorrenciaService.buscarPorStatus(StatusOcorrencia.ABERTA);
                
                assertThat(resultado).hasSize(1);
                assertThat(resultado.get(0).getStatus()).isEqualTo(StatusOcorrencia.ABERTA);
            }

            @Test
            void deve_chamar_repository_com_status_correto() {
                ocorrenciaService.buscarPorStatus(StatusOcorrencia.ABERTA);
                verify(ocorrenciaRepository).findByStatus(StatusOcorrencia.ABERTA);
            }
        }

        @Nested
        class Quando_buscar_por_usuario {

            @BeforeEach
            void setup() {
                when(ocorrenciaRepository.findByUsuarioId(1L)).thenReturn(Arrays.asList(ocorrencia));
                when(mapper.toResponse(ocorrencia)).thenReturn(ocorrenciaResponse);
            }

            @Test
            void deve_retornar_ocorrencias_do_usuario() {
                List<OcorrenciaResponse> resultado = ocorrenciaService.buscarPorUsuario(1L);
                
                assertThat(resultado).hasSize(1);
                assertThat(resultado.get(0).getUsuario().getId()).isEqualTo(1L);
            }

            @Test
            void deve_chamar_repository_com_usuario_id_correto() {
                ocorrenciaService.buscarPorUsuario(1L);
                verify(ocorrenciaRepository).findByUsuarioId(1L);
            }
        }

        @Nested
        class Quando_buscar_por_localizacao {

            @BeforeEach
            void setup() {
                when(ocorrenciaRepository.findByLocalizacaoContaining("Rua das Flores"))
                        .thenReturn(Arrays.asList(ocorrencia));
                when(mapper.toResponse(ocorrencia)).thenReturn(ocorrenciaResponse);
            }

            @Test
            void deve_retornar_ocorrencias_da_localizacao() {
                List<OcorrenciaResponse> resultado = ocorrenciaService.buscarPorLocalizacao("Rua das Flores");
                
                assertThat(resultado).hasSize(1);
                assertThat(resultado.get(0).getLocalizacao()).contains("Rua das Flores");
            }

            @Test
            void deve_chamar_repository_com_localizacao_correta() {
                ocorrenciaService.buscarPorLocalizacao("Rua das Flores");
                verify(ocorrenciaRepository).findByLocalizacaoContaining("Rua das Flores");
            }
        }

        @Nested
        class Quando_criar_ocorrencia {

            @BeforeEach
            void setup() {
                when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
                when(mapper.toEntity(ocorrenciaRequest, usuario)).thenReturn(ocorrencia);
                when(ocorrenciaRepository.save(ocorrencia)).thenReturn(ocorrencia);
                when(mapper.toResponse(ocorrencia)).thenReturn(ocorrenciaResponse);
            }

            @Test
            void deve_buscar_usuario_antes_de_criar() {
                ocorrenciaService.criar(ocorrenciaRequest);
                verify(usuarioRepository).findById(1L);
            }

            @Test
            void deve_salvar_ocorrencia_no_repositorio() {
                ocorrenciaService.criar(ocorrenciaRequest);
                verify(ocorrenciaRepository).save(ocorrencia);
            }

            @Test
            void deve_retornar_ocorrencia_criada() {
                OcorrenciaResponse resultado = ocorrenciaService.criar(ocorrenciaRequest);
                
                assertThat(resultado).isNotNull();
                assertThat(resultado.getTitulo()).isEqualTo("Poste queimado");
            }
        }

        @Nested
        class Quando_atualizar_ocorrencia {

            @BeforeEach
            void setup() {
                when(ocorrenciaRepository.findById(1L)).thenReturn(Optional.of(ocorrencia));
                when(ocorrenciaRepository.save(ocorrencia)).thenReturn(ocorrencia);
                when(mapper.toResponse(ocorrencia)).thenReturn(ocorrenciaResponse);
            }

            @Test
            void deve_atualizar_entidade_com_mapper() {
                ocorrenciaService.atualizar(1L, ocorrenciaRequest);
                verify(mapper).updateEntity(ocorrencia, ocorrenciaRequest);
            }

            @Test
            void deve_salvar_ocorrencia_atualizada() {
                ocorrenciaService.atualizar(1L, ocorrenciaRequest);
                verify(ocorrenciaRepository).save(ocorrencia);
            }

            @Test
            void deve_retornar_ocorrencia_atualizada() {
                OcorrenciaResponse resultado = ocorrenciaService.atualizar(1L, ocorrenciaRequest);
                assertThat(resultado).isNotNull();
            }
        }

        @Nested
        class Quando_atualizar_status {

            OcorrenciaResponse ocorrenciaAtualizada;

            @BeforeEach
            void setup() {
                when(ocorrenciaRepository.findById(1L)).thenReturn(Optional.of(ocorrencia));
                when(ocorrenciaRepository.save(ocorrencia)).thenReturn(ocorrencia);
                
                ocorrenciaAtualizada = OcorrenciaResponse.builder()
                        .id(1L)
                        .status(StatusOcorrencia.EM_ANDAMENTO)
                        .build();
                when(mapper.toResponse(ocorrencia)).thenReturn(ocorrenciaAtualizada);
            }

            @Test
            void deve_retornar_ocorrencia_com_novo_status() {
                OcorrenciaResponse resultado = ocorrenciaService.atualizarStatus(1L, StatusOcorrencia.EM_ANDAMENTO);
                assertThat(resultado.getStatus()).isEqualTo(StatusOcorrencia.EM_ANDAMENTO);
            }

            @Test
            void deve_salvar_ocorrencia_com_status_atualizado() {
                ocorrenciaService.atualizarStatus(1L, StatusOcorrencia.EM_ANDAMENTO);
                verify(ocorrenciaRepository).save(ocorrencia);
            }
        }

        @Nested
        class Quando_deletar_ocorrencia {

            @BeforeEach
            void setup() {
                when(ocorrenciaRepository.findById(1L)).thenReturn(Optional.of(ocorrencia));
            }

            @Test
            void deve_buscar_ocorrencia_antes_de_deletar() {
                ocorrenciaService.deletar(1L);
                verify(ocorrenciaRepository).findById(1L);
            }

            @Test
            void deve_deletar_ocorrencia_do_repositorio() {
                ocorrenciaService.deletar(1L);
                verify(ocorrenciaRepository).delete(ocorrencia);
            }
        }
    }

    @Nested
    class Dado_uma_ocorrencia_inexistente {

        @Nested
        class Quando_buscar_por_id {

            @BeforeEach
            void setup() {
                when(ocorrenciaRepository.findById(999L)).thenReturn(Optional.empty());
            }

            @Test
            void deve_lancar_excecao_com_mensagem_apropriada() {
                assertThatThrownBy(() -> ocorrenciaService.buscarPorId(999L))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("Ocorrência não encontrada com id: 999");
            }
        }

        @Nested
        class Quando_tentar_atualizar {

            @BeforeEach
            void setup() {
                when(ocorrenciaRepository.findById(999L)).thenReturn(Optional.empty());
            }

            @Test
            void deve_lancar_excecao_sem_tentar_salvar() {
                OcorrenciaRequest request = new OcorrenciaRequest();
                
                assertThatThrownBy(() -> ocorrenciaService.atualizar(999L, request))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("Ocorrência não encontrada");
                
                verify(ocorrenciaRepository, never()).save(any());
            }
        }

        @Nested
        class Quando_tentar_atualizar_status {

            @BeforeEach
            void setup() {
                when(ocorrenciaRepository.findById(999L)).thenReturn(Optional.empty());
            }

            @Test
            void deve_lancar_excecao_com_mensagem_apropriada() {
                assertThatThrownBy(() -> ocorrenciaService.atualizarStatus(999L, StatusOcorrencia.EM_ANDAMENTO))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("Ocorrência não encontrada");
            }
        }

        @Nested
        class Quando_tentar_deletar {

            @BeforeEach
            void setup() {
                when(ocorrenciaRepository.findById(999L)).thenReturn(Optional.empty());
            }

            @Test
            void deve_lancar_excecao_sem_tentar_deletar() {
                assertThatThrownBy(() -> ocorrenciaService.deletar(999L))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("Ocorrência não encontrada");
                
                verify(ocorrenciaRepository, never()).delete(any());
            }
        }
    }

    @Nested
    class Dado_um_usuario_inexistente {

        OcorrenciaRequest ocorrenciaRequest;

        @BeforeEach
        void setup() {
            ocorrenciaRequest = new OcorrenciaRequest();
            ocorrenciaRequest.setUsuarioId(999L);
        }

        @Nested
        class Quando_tentar_criar_ocorrencia {

            @BeforeEach
            void setup() {
                when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());
            }

            @Test
            void deve_lancar_excecao_sem_salvar_ocorrencia() {
                assertThatThrownBy(() -> ocorrenciaService.criar(ocorrenciaRequest))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("Usuário não encontrado com id: 999");
                
                verify(ocorrenciaRepository, never()).save(any());
            }
        }
    }

    @Nested
    class Dado_nenhuma_ocorrencia_cadastrada {

        @Nested
        class Quando_listar_todas {

            @BeforeEach
            void setup() {
                when(ocorrenciaRepository.findAllOrderByDataCriacaoDesc()).thenReturn(Arrays.asList());
            }

            @Test
            void deve_retornar_lista_vazia() {
                List<OcorrenciaResponse> resultado = ocorrenciaService.listarTodas();
                assertThat(resultado).isEmpty();
            }
        }

        @Nested
        class Quando_buscar_por_status {

            @BeforeEach
            void setup() {
                when(ocorrenciaRepository.findByStatus(StatusOcorrencia.RESOLVIDA)).thenReturn(Arrays.asList());
            }

            @Test
            void deve_retornar_lista_vazia() {
                List<OcorrenciaResponse> resultado = ocorrenciaService.buscarPorStatus(StatusOcorrencia.RESOLVIDA);
                assertThat(resultado).isEmpty();
            }
        }

        @Nested
        class Quando_buscar_por_usuario {

            @BeforeEach
            void setup() {
                when(ocorrenciaRepository.findByUsuarioId(999L)).thenReturn(Arrays.asList());
            }

            @Test
            void deve_retornar_lista_vazia() {
                List<OcorrenciaResponse> resultado = ocorrenciaService.buscarPorUsuario(999L);
                assertThat(resultado).isEmpty();
            }
        }

        @Nested
        class Quando_buscar_por_localizacao {

            @BeforeEach
            void setup() {
                when(ocorrenciaRepository.findByLocalizacaoContaining("Local Inexistente")).thenReturn(Arrays.asList());
            }

            @Test
            void deve_retornar_lista_vazia() {
                List<OcorrenciaResponse> resultado = ocorrenciaService.buscarPorLocalizacao("Local Inexistente");
                assertThat(resultado).isEmpty();
            }
        }
    }
}
