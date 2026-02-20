package com.example.financasSop.controller;

import com.example.financasSop.dto.request.CriarMedicaoRequest;
import com.example.financasSop.dto.response.MedicaoResponse;
import com.example.financasSop.service.MedicaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orcamentos/{orcamentoId}/medicoes")
@Tag(name = "Medições", description = "Gestão de medições por orçamento")
public class MedicaoController {

    private final MedicaoService service;

    public MedicaoController(MedicaoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cria medição", description = "Cria uma medição ABERTA para o orçamento (apenas uma aberta por orçamento).")
    public ResponseEntity<MedicaoResponse> criar(@PathVariable Long orcamentoId,
                                                 @Valid @RequestBody CriarMedicaoRequest req) {
        MedicaoResponse resp = service.criar(orcamentoId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping
    @Operation(summary = "Lista medições", description = "Lista medições do orçamento.")
    public List<MedicaoResponse> listar(@PathVariable Long orcamentoId) {
        return service.listar(orcamentoId);
    }

    @PutMapping("/{medicaoId}/validar")
    @Operation(summary = "Valida medição", description = "Valida a medição (atualiza acumulados dos itens e define status VALIDADA).")
    public MedicaoResponse validar(@PathVariable Long orcamentoId,
                                   @PathVariable Long medicaoId) {
        return service.validar(orcamentoId, medicaoId);
    }
}

