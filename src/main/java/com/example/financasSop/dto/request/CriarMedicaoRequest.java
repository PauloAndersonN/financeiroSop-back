package com.example.financasSop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CriarMedicaoRequest(
        @NotBlank String numero,
        @NotNull LocalDate dataMedicao,
        String observacao
) {}

