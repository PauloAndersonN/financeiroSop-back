package com.example.financasSop.repository;

import com.example.financasSop.model.Medicao;
import com.example.financasSop.model.StatusMedicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MedicaoRepository extends JpaRepository<Medicao, Long>, JpaSpecificationExecutor<Medicao> {
    boolean existsByOrcamentoIdAndStatus(Long orcamentoId, StatusMedicao status);
    List<Medicao> findByOrcamentoIdOrderByDataMedicaoDesc(Long orcamentoId);
}
