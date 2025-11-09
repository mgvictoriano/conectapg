package com.conectapg.domain.service;

import com.conectapg.domain.model.Ocorrencia;
import com.conectapg.domain.model.Ocorrencia.StatusOcorrencia;
import com.conectapg.domain.repository.OcorrenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OcorrenciaService {

    private final OcorrenciaRepository ocorrenciaRepository;

    @Transactional(readOnly = true)
    public List<Ocorrencia> listarTodas() {
        return ocorrenciaRepository.findAllOrderByDataCriacaoDesc();
    }

    @Transactional(readOnly = true)
    public Ocorrencia buscarPorId(Long id) {
        return ocorrenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ocorrência não encontrada com id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Ocorrencia> buscarPorStatus(StatusOcorrencia status) {
        return ocorrenciaRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Ocorrencia> buscarPorUsuario(Long usuarioId) {
        return ocorrenciaRepository.findByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Ocorrencia> buscarPorLocalizacao(String localizacao) {
        return ocorrenciaRepository.findByLocalizacaoContaining(localizacao);
    }

    @Transactional
    public Ocorrencia criar(Ocorrencia ocorrencia) {
        return ocorrenciaRepository.save(ocorrencia);
    }

    @Transactional
    public Ocorrencia atualizar(Long id, Ocorrencia ocorrenciaAtualizada) {
        Ocorrencia ocorrencia = buscarPorId(id);
        
        ocorrencia.setTitulo(ocorrenciaAtualizada.getTitulo());
        ocorrencia.setDescricao(ocorrenciaAtualizada.getDescricao());
        ocorrencia.setLocalizacao(ocorrenciaAtualizada.getLocalizacao());
        ocorrencia.setTipo(ocorrenciaAtualizada.getTipo());
        ocorrencia.setStatus(ocorrenciaAtualizada.getStatus());
        
        return ocorrenciaRepository.save(ocorrencia);
    }

    @Transactional
    public Ocorrencia atualizarStatus(Long id, StatusOcorrencia novoStatus) {
        Ocorrencia ocorrencia = buscarPorId(id);
        ocorrencia.setStatus(novoStatus);
        return ocorrenciaRepository.save(ocorrencia);
    }

    @Transactional
    public void deletar(Long id) {
        Ocorrencia ocorrencia = buscarPorId(id);
        ocorrenciaRepository.delete(ocorrencia);
    }
}
