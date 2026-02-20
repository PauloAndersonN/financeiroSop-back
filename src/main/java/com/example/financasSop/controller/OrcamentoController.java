package com.example.financasSop.controller;

import com.example.financasSop.dto.request.AtualizarOrcamentoRequest;
import com.example.financasSop.dto.request.CriarOrcamentoRequest;
import com.example.financasSop.dto.response.CriarOrcamentoResponse;
import com.example.financasSop.dto.response.ListOrcamentoResponse;
import com.example.financasSop.service.OrcamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orcamentos")
@Tag(name = "Orçamentos", description = "Operações relacionadas a orçamentos")
public class OrcamentoController {

    private final OrcamentoService orcamentoService;

    public OrcamentoController(OrcamentoService orcamentoService) {
        this.orcamentoService = orcamentoService;
    }

    // LISTAR (paginado)
    @GetMapping
    @Operation(
            summary = "Lista orçamentos com paginação",
            description = "Retorna uma lista paginada de orçamentos ordenada por data de criação (descendente).")
    public Page<ListOrcamentoResponse> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return orcamentoService.list(page, size);
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    @Operation(
            summary = "Busca um orçamento pelo ID",
            description = "Retorna os detalhes de um orçamento específico.")
    public CriarOrcamentoResponse buscarPorId(@PathVariable Long id) {
        return orcamentoService.buscarPorId(id);
    }

    // CRIAR
    @PostMapping
    @Operation(
            summary = "Cria um novo orçamento",
            description = "Cria um orçamento com status inicial ABERTO e valor total igual a zero.")
    public ResponseEntity<CriarOrcamentoResponse> criar(
            @Valid @RequestBody CriarOrcamentoRequest request) {

        CriarOrcamentoResponse response = orcamentoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ATUALIZAR
    @PutMapping("/{id}")
    @Operation(
            summary = "Atualiza um orçamento existente",
            description = "Atualiza os dados permitidos do orçamento. Não é permitido editar se o status estiver FINALIZADO.")
    public ResponseEntity<CriarOrcamentoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarOrcamentoRequest request) {

        CriarOrcamentoResponse response = orcamentoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

}
