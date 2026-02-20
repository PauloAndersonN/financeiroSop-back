package com.example.financasSop.repository;

import com.example.financasSop.model.TipoOrcamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoOrcamentoRepository extends JpaRepository<TipoOrcamento, Long> {
    Optional<TipoOrcamento> findByNome(String nome);
}

