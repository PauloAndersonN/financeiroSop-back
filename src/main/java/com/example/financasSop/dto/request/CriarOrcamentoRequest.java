package com.example.financasSop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CriarOrcamentoRequest(
        @NotBlank
        @Size(max = 17)
        String nup,

        @NotNull
        Long tipoOrcamentoId
) {}
