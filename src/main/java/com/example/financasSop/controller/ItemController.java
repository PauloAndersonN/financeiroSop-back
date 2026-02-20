package com.example.financasSop.controller;

import com.example.financasSop.dto.request.CriarItemRequest;
import com.example.financasSop.dto.response.ItemResponse;
import com.example.financasSop.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itens")
@Tag(name = "Itens", description = "Catálogo de itens")
public class ItemController {

    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cria um novo item", description = "Cria um item no catálogo de itens.")
    public ResponseEntity<ItemResponse> criar(@Valid @RequestBody CriarItemRequest req) {
        ItemResponse resp = service.criar(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping
    @Operation(summary = "Lista itens", description = "Lista todos os itens do catálogo.")
    public List<ItemResponse> listar() {
        return service.listar();
    }


}

