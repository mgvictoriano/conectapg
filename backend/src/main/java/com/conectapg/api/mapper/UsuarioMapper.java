package com.conectapg.api.mapper;

import com.conectapg.api.dto.UsuarioRequest;
import com.conectapg.api.dto.UsuarioResponse;
import com.conectapg.domain.model.Usuario;
import com.conectapg.domain.model.Usuario.TipoUsuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    private final PasswordEncoder passwordEncoder;

    public UsuarioMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario toEntity(UsuarioRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setTipo(request.getTipo() != null ? request.getTipo() : TipoUsuario.CIDADAO);
        usuario.setAtivo(request.getAtivo() != null ? request.getAtivo() : true);
        return usuario;
    }

    public void updateEntity(Usuario usuario, UsuarioRequest request) {
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        
        // Só atualiza a senha se foi fornecida
        if (request.getSenha() != null && !request.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        }
        
        if (request.getTipo() != null) {
            usuario.setTipo(request.getTipo());
        }
        
        if (request.getAtivo() != null) {
            usuario.setAtivo(request.getAtivo());
        }
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .tipo(usuario.getTipo())
                .ativo(usuario.getAtivo())
                .dataCriacao(usuario.getDataCriacao())
                .totalOcorrencias(usuario.getOcorrencias() != null ? usuario.getOcorrencias().size() : 0)
                .build();
    }
}
