package com.example.financasSop.repository;

import com.example.financasSop.model.ItemOrcamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface ItemOrcamentoRepository extends JpaRepository<ItemOrcamento, Long> {
    List<ItemOrcamento> findByOrcamentoId(Long orcamentoId);
    java.util.Optional<ItemOrcamento> findByIdAndOrcamentoId(Long id, Long orcamentoId);
}
