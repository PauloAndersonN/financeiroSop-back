package com.example.financasSop.dto.response;

import com.example.financasSop.model.Item;

public record ItemResponse(
        Long id,
        String nome,
        String unidade
) {
    public static ItemResponse from(Item item) {
        return new ItemResponse(item.getId(), item.getNome(), item.getUnidade());
    }
}

