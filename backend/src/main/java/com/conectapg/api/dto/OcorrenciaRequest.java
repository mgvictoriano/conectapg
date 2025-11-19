package com.conectapg.api.dto;

import com.conectapg.domain.model.Ocorrencia.StatusOcorrencia;
import com.conectapg.domain.model.Ocorrencia.TipoOcorrencia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcorrenciaRequest {

    @NotBlank(message = "Título é obrigatório")
    private String titulo;

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotBlank(message = "Localização é obrigatória")
    private String localizacao;

    @NotNull(message = "Tipo é obrigatório")
    private TipoOcorrencia tipo;

    private StatusOcorrencia status;

    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;
}
