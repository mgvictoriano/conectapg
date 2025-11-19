package com.conectapg.domain.service;

import com.conectapg.api.dto.OcorrenciaRequest;
import com.conectapg.api.dto.OcorrenciaResponse;
import com.conectapg.api.mapper.OcorrenciaMapper;
import com.conectapg.domain.model.Ocorrencia;
import com.conectapg.domain.model.Ocorrencia.StatusOcorrencia;
import com.conectapg.domain.model.Usuario;
import com.conectapg.domain.repository.OcorrenciaRepository;
import com.conectapg.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OcorrenciaService {

    private final OcorrenciaRepository ocorrenciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final OcorrenciaMapper mapper;

    @Transactional(readOnly = true)
    public List<OcorrenciaResponse> listarTodas() {
        return ocorrenciaRepository.findAllOrderByDataCriacaoDesc()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OcorrenciaResponse buscarPorId(Long id) {
        Ocorrencia ocorrencia = ocorrenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ocorrência não encontrada com id: " + id));
        return mapper.toResponse(ocorrencia);
    }

    @Transactional(readOnly = true)
    public List<OcorrenciaResponse> buscarPorStatus(StatusOcorrencia status) {
        return ocorrenciaRepository.findByStatus(status)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OcorrenciaResponse> buscarPorUsuario(Long usuarioId) {
        return ocorrenciaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OcorrenciaResponse> buscarPorLocalizacao(String localizacao) {
        return ocorrenciaRepository.findByLocalizacaoContaining(localizacao)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OcorrenciaResponse criar(OcorrenciaRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + request.getUsuarioId()));
        
        Ocorrencia ocorrencia = mapper.toEntity(request, usuario);
        Ocorrencia salva = ocorrenciaRepository.save(ocorrencia);
        return mapper.toResponse(salva);
    }

    @Transactional
    public OcorrenciaResponse atualizar(Long id, OcorrenciaRequest request) {
        Ocorrencia ocorrencia = ocorrenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ocorrência não encontrada com id: " + id));
        
        mapper.updateEntity(ocorrencia, request);
        Ocorrencia atualizada = ocorrenciaRepository.save(ocorrencia);
        return mapper.toResponse(atualizada);
    }

    @Transactional
    public OcorrenciaResponse atualizarStatus(Long id, StatusOcorrencia novoStatus) {
        Ocorrencia ocorrencia = ocorrenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ocorrência não encontrada com id: " + id));
        ocorrencia.setStatus(novoStatus);
        Ocorrencia atualizada = ocorrenciaRepository.save(ocorrencia);
        return mapper.toResponse(atualizada);
    }

    @Transactional
    public void deletar(Long id) {
        Ocorrencia ocorrencia = ocorrenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ocorrência não encontrada com id: " + id));
        ocorrenciaRepository.delete(ocorrencia);
    }
}
