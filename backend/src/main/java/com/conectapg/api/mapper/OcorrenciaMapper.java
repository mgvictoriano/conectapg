package com.conectapg.api.mapper;

import com.conectapg.api.dto.OcorrenciaRequest;
import com.conectapg.api.dto.OcorrenciaResponse;
import com.conectapg.domain.model.Ocorrencia;
import com.conectapg.domain.model.Ocorrencia.StatusOcorrencia;
import com.conectapg.domain.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class OcorrenciaMapper {

    public Ocorrencia toEntity(OcorrenciaRequest request, Usuario usuario) {
        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setTitulo(request.getTitulo());
        ocorrencia.setDescricao(request.getDescricao());
        ocorrencia.setLocalizacao(request.getLocalizacao());
        ocorrencia.setTipo(request.getTipo());
        ocorrencia.setStatus(request.getStatus() != null ? request.getStatus() : StatusOcorrencia.ABERTA);
        ocorrencia.setUsuario(usuario);
        return ocorrencia;
    }

    public void updateEntity(Ocorrencia ocorrencia, OcorrenciaRequest request) {
        ocorrencia.setTitulo(request.getTitulo());
        ocorrencia.setDescricao(request.getDescricao());
        ocorrencia.setLocalizacao(request.getLocalizacao());
        ocorrencia.setTipo(request.getTipo());
        if (request.getStatus() != null) {
            ocorrencia.setStatus(request.getStatus());
        }
    }

    public OcorrenciaResponse toResponse(Ocorrencia ocorrencia) {
        return OcorrenciaResponse.builder()
                .id(ocorrencia.getId())
                .titulo(ocorrencia.getTitulo())
                .descricao(ocorrencia.getDescricao())
                .localizacao(ocorrencia.getLocalizacao())
                .status(ocorrencia.getStatus())
                .tipo(ocorrencia.getTipo())
                .usuario(toUsuarioResumo(ocorrencia.getUsuario()))
                .dataCriacao(ocorrencia.getDataCriacao())
                .dataAtualizacao(ocorrencia.getDataAtualizacao())
                .build();
    }

    private OcorrenciaResponse.UsuarioResumo toUsuarioResumo(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return OcorrenciaResponse.UsuarioResumo.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .build();
    }
}
