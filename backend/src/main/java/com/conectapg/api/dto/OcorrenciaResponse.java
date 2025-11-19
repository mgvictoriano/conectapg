package com.conectapg.api.dto;

import com.conectapg.domain.model.Ocorrencia.StatusOcorrencia;
import com.conectapg.domain.model.Ocorrencia.TipoOcorrencia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OcorrenciaResponse {

    private Long id;
    private String titulo;
    private String descricao;
    private String localizacao;
    private StatusOcorrencia status;
    private TipoOcorrencia tipo;
    private UsuarioResumo usuario;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UsuarioResumo {
        private Long id;
        private String nome;
        private String email;
    }
}
