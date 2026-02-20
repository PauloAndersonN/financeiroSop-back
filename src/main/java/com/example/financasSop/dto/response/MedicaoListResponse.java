package com.example.financasSop.dto.response;

import com.example.financasSop.model.Medicao;
import com.example.financasSop.model.StatusMedicao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MedicaoListResponse(
        Long id,
        String numero,
        LocalDate dataMedicao,
        BigDecimal valorMedicao,
        StatusMedicao status,
        String observacao,
        Long orcamentoId,
        String nup
) {
    public static MedicaoListResponse from(Medicao m) {
        return new MedicaoListResponse(
                m.getId(),
                m.getNumero(),
                m.getDataMedicao(),
                m.getValorMedicao(),
                m.getStatus(),
                m.getObservacao(),
                m.getOrcamento().getId(),
                m.getOrcamento().getNup()
        );
    }
}
