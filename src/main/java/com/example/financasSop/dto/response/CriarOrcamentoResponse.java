package com.example.financasSop.dto.response;

import com.example.financasSop.model.Orcamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CriarOrcamentoResponse(
        Long id,
        String nup,
        String tipoOrcamento,
        BigDecimal valorTotal,
        String status,
        LocalDateTime dataCriacao
) {

    public static CriarOrcamentoResponse from(Orcamento o) {
        return new CriarOrcamentoResponse(
                o.getId(),
                o.getNup(),
                o.getTipo().getNome(),
                o.getValorTotal(),
                o.getStatus().name(),
                o.getDataCriacao()
        );

}
}
