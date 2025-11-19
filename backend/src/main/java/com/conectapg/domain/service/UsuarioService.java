package com.conectapg.domain.service;

import com.conectapg.api.dto.UsuarioRequest;
import com.conectapg.api.dto.UsuarioResponse;
import com.conectapg.api.mapper.UsuarioMapper;
import com.conectapg.domain.model.Usuario;
import com.conectapg.domain.model.Usuario.TipoUsuario;
import com.conectapg.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper mapper;

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
        return mapper.toResponse(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com email: " + email));
        return mapper.toResponse(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> buscarPorTipo(TipoUsuario tipo) {
        return usuarioRepository.findAll()
                .stream()
                .filter(u -> u.getTipo().equals(tipo))
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> buscarAtivos() {
        return usuarioRepository.findAll()
                .stream()
                .filter(Usuario::getAtivo)
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UsuarioResponse criar(UsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + request.getEmail());
        }

        Usuario usuario = mapper.toEntity(request);
        Usuario salvo = usuarioRepository.save(usuario);
        return mapper.toResponse(salvo);
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));

        // Verifica se o email já está em uso por outro usuário
        if (!usuario.getEmail().equals(request.getEmail()) && 
            usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + request.getEmail());
        }

        mapper.updateEntity(usuario, request);
        Usuario atualizado = usuarioRepository.save(usuario);
        return mapper.toResponse(atualizado);
    }

    @Transactional
    public void deletar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
        usuarioRepository.delete(usuario);
    }

    @Transactional
    public UsuarioResponse ativarDesativar(Long id, Boolean ativo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
        usuario.setAtivo(ativo);
        Usuario atualizado = usuarioRepository.save(usuario);
        return mapper.toResponse(atualizado);
    }
}
