package com.example.financasSop.controller;

import com.example.financasSop.dto.request.CriarItemMedicaoRequest;
import com.example.financasSop.dto.response.ItemMedicaoResponse;
import com.example.financasSop.service.ItemMedicaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orcamentos/{orcamentoId}/medicoes/{medicaoId}/itens")
@Tag(name = "Itens da Medição", description = "Itens medidos por medição")
public class ItemMedicaoController {

    private final ItemMedicaoService service;

    public ItemMedicaoController(ItemMedicaoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Adiciona item na medição", description = "Adiciona um item medido na medição ABERTA, respeitando limites do item do orçamento.")
    public ResponseEntity<ItemMedicaoResponse> adicionar(@PathVariable Long orcamentoId,
                                                         @PathVariable Long medicaoId,
                                                         @Valid @RequestBody CriarItemMedicaoRequest req) {
        ItemMedicaoResponse resp = service.adicionar(orcamentoId, medicaoId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping
    @Operation(summary = "Lista itens da medição", description = "Lista os itens já adicionados na medição.")
    public List<ItemMedicaoResponse> listar(@PathVariable Long orcamentoId,
                                            @PathVariable Long medicaoId) {
        return service.listar(orcamentoId, medicaoId);
    }

    @DeleteMapping("/{itemMedicaoId}")
    @Operation(summary = "Remove item da medição", description = "Remove um item da medição ABERTA e recalcula o valor da medição.")
    public ResponseEntity<Void> deletar(@PathVariable Long orcamentoId,
                                        @PathVariable Long medicaoId,
                                        @PathVariable Long itemMedicaoId) {
        service.deletar(orcamentoId, medicaoId, itemMedicaoId);
        return ResponseEntity.noContent().build();
    }
}

