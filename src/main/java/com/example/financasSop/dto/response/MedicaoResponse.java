package com.example.financasSop.dto.response;

import com.example.financasSop.model.Medicao;
import com.example.financasSop.model.StatusMedicao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MedicaoResponse(
        Long id,
        String numero,
        LocalDate dataMedicao,
        BigDecimal valorMedicao,
        StatusMedicao status,
        String observacao
) {
    public static MedicaoResponse from(Medicao m) {
        return new MedicaoResponse(
                m.getId(),
                m.getNumero(),
                m.getDataMedicao(),
                m.getValorMedicao(),
                m.getStatus(),
                m.getObservacao()
        );
    }
}

