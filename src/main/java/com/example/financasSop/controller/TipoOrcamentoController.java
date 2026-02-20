package com.example.financasSop.controller;

import com.example.financasSop.model.TipoOrcamento;
import com.example.financasSop.service.TipoOrcamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tipo-orcamento")
@Tag(name = "Tipos de Orçamento", description = "Consulta e manutenção dos tipos de orçamento")
public class TipoOrcamentoController {

    private final TipoOrcamentoService service;

    public TipoOrcamentoController(TipoOrcamentoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(
            summary = "Lista os tipos de orçamento",
            description = "Retorna todos os tipos de orçamento disponíveis para uso na criação de orçamentos.")
    public List<TipoOrcamento> listar() {
        return service.listar();
    }
}

