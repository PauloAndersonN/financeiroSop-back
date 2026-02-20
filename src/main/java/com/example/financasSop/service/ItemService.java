package com.example.financasSop.service;

import com.example.financasSop.dto.request.CriarItemRequest;
import com.example.financasSop.dto.response.ItemResponse;
import com.example.financasSop.model.Item;
import com.example.financasSop.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    public ItemResponse criar(CriarItemRequest req) {
        if (repository.existsByNomeIgnoreCase(req.nome())) {
            throw new IllegalArgumentException("Item com nome já existe");
        }

        Item item = new Item();
        item.setNome(req.nome());
        item.setUnidade(req.unidade());

        Item salvo = repository.save(item);
        return ItemResponse.from(salvo);
    }

    public List<ItemResponse> listar() {
        return repository.findAll().stream()
                .map(ItemResponse::from)
                .toList();
    }
}

