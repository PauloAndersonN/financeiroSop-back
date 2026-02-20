package com.example.financasSop.controller;

import com.example.financasSop.dto.response.MedicaoListResponse;
import com.example.financasSop.model.StatusMedicao;
import com.example.financasSop.service.MedicaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/medicoes")
@Tag(name = "Medições", description = "Consulta global de medições")
public class MedicaoGlobalController {

    private final MedicaoService medicaoService;

    public MedicaoGlobalController(MedicaoService medicaoService) {
        this.medicaoService = medicaoService;
    }

    @GetMapping
    @Operation(
            summary = "Lista medições com paginação e filtros",
            description = "Retorna medições de forma global, incluindo dados de orçamento (orcamentoId e nup).")
    public Page<MedicaoListResponse> listarGlobal(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) StatusMedicao status,
            @RequestParam(required = false) Long orcamentoId,
            @RequestParam(required = false) String nup,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataDe,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataAte) {

        return medicaoService.listarGlobal(page, size, status, orcamentoId, nup, dataDe, dataAte);
    }
}
