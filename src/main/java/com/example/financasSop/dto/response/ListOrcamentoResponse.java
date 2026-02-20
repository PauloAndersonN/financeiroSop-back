package com.example.financasSop.dto.response;


import com.example.financasSop.model.Orcamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ListOrcamentoResponse(
        Long id,
        String nup,
        String tipoOrcamento,
        BigDecimal valorTotal,
        String status,
        LocalDateTime dataCriacao
) {
    public static ListOrcamentoResponse from(Orcamento o) {
        return new ListOrcamentoResponse(
                o.getId(),
                o.getNup(),
                o.getTipo().getNome(),
                o.getValorTotal(),
                o.getStatus().name(),
                o.getDataCriacao()
        );
    }
}
