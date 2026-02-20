package com.example.financasSop.dto.response;

import com.example.financasSop.model.ItemMedicao;

import java.math.BigDecimal;

public record ItemMedicaoResponse(
        Long id,
        Long itemOrcamentoId,
        BigDecimal quantidadeMedida,
        BigDecimal valorTotalMedido
) {
    public static ItemMedicaoResponse from(ItemMedicao im) {
        return new ItemMedicaoResponse(
                im.getId(),
                im.getItemOrcamento() != null ? im.getItemOrcamento().getId() : null,
                im.getQuantidadeMedida(),
                im.getValorTotalMedido()
        );
    }
}

