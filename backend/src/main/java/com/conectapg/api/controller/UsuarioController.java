package com.conectapg.api.controller;

import com.conectapg.api.dto.UsuarioRequest;
import com.conectapg.api.dto.UsuarioResponse;
import com.conectapg.domain.model.Usuario.TipoUsuario;
import com.conectapg.domain.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar todos os usuários")
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar usuário por email")
    public ResponseEntity<UsuarioResponse> buscarPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(usuarioService.buscarPorEmail(email));
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Buscar usuários por tipo")
    public ResponseEntity<List<UsuarioResponse>> buscarPorTipo(@PathVariable TipoUsuario tipo) {
        return ResponseEntity.ok(usuarioService.buscarPorTipo(tipo));
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar usuários ativos")
    public ResponseEntity<List<UsuarioResponse>> buscarAtivos() {
        return ResponseEntity.ok(usuarioService.buscarAtivos());
    }

    @PostMapping
    @Operation(summary = "Criar novo usuário")
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse novoUsuario = usuarioService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário")
    public ResponseEntity<UsuarioResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.atualizar(id, request));
    }

    @PatchMapping("/{id}/ativo")
    @Operation(summary = "Ativar ou desativar usuário")
    public ResponseEntity<UsuarioResponse> ativarDesativar(
            @PathVariable Long id,
            @RequestParam Boolean ativo) {
        return ResponseEntity.ok(usuarioService.ativarDesativar(id, ativo));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar usuário")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
