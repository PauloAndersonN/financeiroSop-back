package com.example.financasSop.dto.request;


import jakarta.validation.constraints.NotNull;

public record AtualizarOrcamentoRequest(
        @NotNull
        Long tipoOrcamentoId
) {}
