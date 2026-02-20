package com.example.financasSop.repository;

import com.example.financasSop.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    boolean existsByNomeIgnoreCase(String nome);
}

