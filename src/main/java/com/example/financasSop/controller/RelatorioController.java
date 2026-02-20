package com.example.financasSop.controller;

import com.example.financasSop.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relatorios")
@Tag(name = "Relatórios", description = "Relatórios em Jasper")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping(value = "/orcamentos/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    @Operation(
            summary = "Gera relatório simples de orçamentos em PDF",
            description = "Retorna um PDF gerado com JasperReports contendo os orçamentos.")
    public ResponseEntity<byte[]> relatorioOrcamentosPdf() {
        byte[] pdf = relatorioService.gerarRelatorioOrcamentosPdf();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=relatorio-orcamentos.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
