package com.example.financasSop.controller;

import com.example.financasSop.dto.request.CriarItemOrcamentoRequest;
import com.example.financasSop.dto.response.ItemOrcamentoResponse;
import com.example.financasSop.dto.request.AtualizarItemOrcamentoRequest;
import com.example.financasSop.service.ItemOrcamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orcamentos/{orcamentoId}/itens")
@Tag(name = "Itens de Orçamento", description = "Itens vinculados a um orçamento")
public class ItemOrcamentoController {

    private final ItemOrcamentoService service;

    public ItemOrcamentoController(ItemOrcamentoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Adiciona item ao orçamento", description = "Cria um item de orçamento vinculado ao catálogo de itens.")
    public ResponseEntity<ItemOrcamentoResponse> criar(@PathVariable Long orcamentoId,
                                                       @Valid @RequestBody CriarItemOrcamentoRequest req) {
        ItemOrcamentoResponse resp = service.criar(orcamentoId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PutMapping("/{itemOrcamentoId}")
    @Operation(summary = "Edita item do orçamento", description = "Atualiza quantidade, valor unitário e, opcionalmente, o item do catálogo.")
    public ItemOrcamentoResponse atualizar(@PathVariable Long orcamentoId,
                                           @PathVariable Long itemOrcamentoId,
                                           @Valid @RequestBody AtualizarItemOrcamentoRequest req) {
        return service.atualizar(orcamentoId, itemOrcamentoId, req);
    }

    @GetMapping
    @Operation(summary = "Lista itens do orçamento", description = "Retorna todos os itens vinculados ao orçamento informado.")
    public List<ItemOrcamentoResponse> listar(@PathVariable Long orcamentoId) {
        return service.listar(orcamentoId);
    }

    @DeleteMapping("/{itemOrcamentoId}")
    @Operation(summary = "Remove item do orçamento", description = "Exclui um item do orçamento e recalcula o total.")
    public ResponseEntity<Void> deletar(@PathVariable Long orcamentoId,
                                        @PathVariable Long itemOrcamentoId) {
        service.deletar(orcamentoId, itemOrcamentoId);
        return ResponseEntity.noContent().build();
    }
}
