package com.conectapg.api.dto;

import com.conectapg.domain.model.Usuario.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {

    private Long id;
    private String nome;
    private String email;
    private TipoUsuario tipo;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private Integer totalOcorrencias;
}
