package com.example.financasSop.dto.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;

public record CriarItemOrcamentoRequest(
        @NotNull
        Long itemId,
        @NotNull
        BigDecimal quantidade,
        @NotNull
        BigDecimal valorUnitario
) {}
