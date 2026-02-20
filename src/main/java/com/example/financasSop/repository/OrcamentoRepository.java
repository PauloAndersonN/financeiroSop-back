package com.example.financasSop.repository;

import com.example.financasSop.model.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {
    Optional<Orcamento> findByNup(String nup);
}
