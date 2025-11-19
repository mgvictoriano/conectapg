package com.conectapg.api.controller;

import com.conectapg.api.dto.OcorrenciaRequest;
import com.conectapg.api.dto.OcorrenciaResponse;
import com.conectapg.domain.model.Ocorrencia.StatusOcorrencia;
import com.conectapg.domain.service.OcorrenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ocorrencias")
@RequiredArgsConstructor
@Tag(name = "Ocorrências", description = "Endpoints para gerenciamento de ocorrências")
public class OcorrenciaController {

    private final OcorrenciaService ocorrenciaService;

    @GetMapping
    @Operation(summary = "Listar todas as ocorrências")
    public ResponseEntity<List<OcorrenciaResponse>> listarTodas() {
        return ResponseEntity.ok(ocorrenciaService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar ocorrência por ID")
    public ResponseEntity<OcorrenciaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ocorrenciaService.buscarPorId(id));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Buscar ocorrências por status")
    public ResponseEntity<List<OcorrenciaResponse>> buscarPorStatus(@PathVariable StatusOcorrencia status) {
        return ResponseEntity.ok(ocorrenciaService.buscarPorStatus(status));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Buscar ocorrências por usuário")
    public ResponseEntity<List<OcorrenciaResponse>> buscarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(ocorrenciaService.buscarPorUsuario(usuarioId));
    }

    @GetMapping("/localizacao")
    @Operation(summary = "Buscar ocorrências por localização")
    public ResponseEntity<List<OcorrenciaResponse>> buscarPorLocalizacao(@RequestParam String localizacao) {
        return ResponseEntity.ok(ocorrenciaService.buscarPorLocalizacao(localizacao));
    }

    @PostMapping
    @Operation(summary = "Criar nova ocorrência")
    public ResponseEntity<OcorrenciaResponse> criar(@Valid @RequestBody OcorrenciaRequest request) {
        OcorrenciaResponse novaOcorrencia = ocorrenciaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaOcorrencia);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar ocorrência")
    public ResponseEntity<OcorrenciaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody OcorrenciaRequest request) {
        return ResponseEntity.ok(ocorrenciaService.atualizar(id, request));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status da ocorrência")
    public ResponseEntity<OcorrenciaResponse> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusOcorrencia status) {
        return ResponseEntity.ok(ocorrenciaService.atualizarStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar ocorrência")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        ocorrenciaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
