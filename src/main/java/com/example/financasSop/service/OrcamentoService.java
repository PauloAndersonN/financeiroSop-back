package com.example.financasSop.service;


import com.example.financasSop.dto.request.AtualizarOrcamentoRequest;
import com.example.financasSop.dto.request.CriarOrcamentoRequest;
import com.example.financasSop.dto.response.CriarOrcamentoResponse;
import com.example.financasSop.dto.response.ListOrcamentoResponse;
import com.example.financasSop.model.Orcamento;
import com.example.financasSop.model.StatusOrcamento;
import com.example.financasSop.model.TipoOrcamento;
import com.example.financasSop.repository.OrcamentoRepository;
import com.example.financasSop.repository.TipoOrcamentoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final TipoOrcamentoRepository tipoOrcamentoRepository;

    public OrcamentoService(OrcamentoRepository orcamentoRepository,
                            TipoOrcamentoRepository tipoOrcamentoRepository) {
        this.orcamentoRepository = orcamentoRepository;
        this.tipoOrcamentoRepository = tipoOrcamentoRepository;
    }

    public Page<ListOrcamentoResponse> list(int page, int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        if (size > 100) size = 100;

        Pageable pageable = PageRequest.of(page, size, Sort.by("dataCriacao").descending());

        return orcamentoRepository.findAll(pageable)
                .map(ListOrcamentoResponse::from);
    }

    public CriarOrcamentoResponse buscarPorId(Long id) {
        Orcamento orcamento = orcamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado"));

        return CriarOrcamentoResponse.from(orcamento);
    }

    public CriarOrcamentoResponse criar(CriarOrcamentoRequest request) {

        // 1) Regra: NUP não pode repetir
        if (orcamentoRepository.findByNup(request.nup()).isPresent()) {
            throw new IllegalArgumentException("Já existe orçamento com este NUP");
        }

        // 2) Regra: tipo precisa existir
        TipoOrcamento tipo = tipoOrcamentoRepository.findById(request.tipoOrcamentoId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de orçamento não encontrado"));

        // 3) Criar entidade
        Orcamento orcamento = new Orcamento();
        orcamento.setNup(request.nup());
        orcamento.setTipo(tipo);
        orcamento.setStatus(StatusOrcamento.ABERTO);
        orcamento.setValorTotal(BigDecimal.ZERO);


        // 4) Salvar
        Orcamento salvo = orcamentoRepository.save(orcamento);

        // 5) Retornar DTO
        return CriarOrcamentoResponse.from(salvo);
    }

    public CriarOrcamentoResponse atualizar(Long id, AtualizarOrcamentoRequest request) {
        Orcamento orcamento = orcamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado"));

        // Regra: não pode editar se estiver FINALIZADO
        if (orcamento.getStatus() == StatusOrcamento.FINALIZADO) {
            throw new IllegalStateException("Não é permitido editar orçamento FINALIZADO");
        }

        // Regra: tipo precisa existir
        TipoOrcamento tipo = tipoOrcamentoRepository.findById(request.tipoOrcamentoId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de orçamento não encontrado"));

        // Atualiza apenas o que é permitido
        orcamento.setTipo(tipo);

        Orcamento salvo = orcamentoRepository.save(orcamento);
        return CriarOrcamentoResponse.from(salvo);
    }
}
