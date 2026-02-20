package com.example.financasSop.repository;

import com.example.financasSop.model.ItemMedicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemMedicaoRepository extends JpaRepository<ItemMedicao, Long> {
    List<ItemMedicao> findByMedicaoId(Long medicaoId);
    List<ItemMedicao> findByMedicaoIdAndItemOrcamentoId(Long medicaoId, Long itemOrcamentoId);
}

