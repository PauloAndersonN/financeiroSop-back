package com.example.financasSop.dto.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CriarItemMedicaoRequest(
        @NotNull Long itemOrcamentoId,
        @NotNull BigDecimal quantidadeMedida
) {}

