package com.example.financasSop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarItemRequest(
        @NotBlank
        @Size(max = 150)
        String nome,
        @Size(max = 50)
        String unidade
) {}

