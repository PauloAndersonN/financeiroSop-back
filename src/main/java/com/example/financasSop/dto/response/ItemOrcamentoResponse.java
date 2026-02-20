package com.example.financasSop.dto.response;

import com.example.financasSop.model.ItemOrcamento;

import java.math.BigDecimal;

public record ItemOrcamentoResponse(
        Long id,
        Long itemId,
        String itemNome,
        BigDecimal quantidade,
        BigDecimal valorUnitario,
        BigDecimal valorTotal
) {
    public static ItemOrcamentoResponse from(ItemOrcamento i) {
        return new ItemOrcamentoResponse(
                i.getId(),
                i.getItem() != null ? i.getItem().getId() : null,
                i.getItem() != null ? i.getItem().getNome() : null,
                i.getQuantidade(),
                i.getValorUnitario(),
                i.getValorTotal()
        );
    }
}

