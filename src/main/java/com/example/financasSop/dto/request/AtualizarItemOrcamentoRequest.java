package com.example.financasSop.dto.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record AtualizarItemOrcamentoRequest(
        Long itemId,
        @NotNull
        BigDecimal quantidade,
        @NotNull
        BigDecimal valorUnitario
) {}

