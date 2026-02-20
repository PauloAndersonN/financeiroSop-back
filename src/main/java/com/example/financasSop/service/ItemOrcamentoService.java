package com.example.financasSop.service;

import com.example.financasSop.dto.request.CriarItemOrcamentoRequest;
import com.example.financasSop.dto.response.ItemOrcamentoResponse;
import com.example.financasSop.dto.request.AtualizarItemOrcamentoRequest;
import com.example.financasSop.model.ItemOrcamento;
import com.example.financasSop.model.Item;
import com.example.financasSop.model.Orcamento;
import com.example.financasSop.model.StatusOrcamento;
import com.example.financasSop.repository.ItemOrcamentoRepository;
import com.example.financasSop.repository.OrcamentoRepository;
import com.example.financasSop.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ItemOrcamentoService {

    private final ItemOrcamentoRepository itemRepo;
    private final OrcamentoRepository orcamentoRepo;
    private final ItemRepository itemRepository;

    public ItemOrcamentoService(ItemOrcamentoRepository itemRepo, OrcamentoRepository orcamentoRepo,
                                ItemRepository itemRepository) {
        this.itemRepo = itemRepo;
        this.orcamentoRepo = orcamentoRepo;
        this.itemRepository = itemRepository;
    }

    public ItemOrcamentoResponse criar(Long orcamentoId, CriarItemOrcamentoRequest req) {

        Orcamento orcamento = orcamentoRepo.findById(orcamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado"));

        if (orcamento.getStatus() == StatusOrcamento.FINALIZADO) {
            throw new IllegalStateException("Não é permitido alterar orçamento FINALIZADO");
        }

        Item itemCatalogo = itemRepository.findById(req.itemId())
                .orElseThrow(() -> new IllegalArgumentException("Item não encontrado"));

        ItemOrcamento item = new ItemOrcamento();
        item.setItem(itemCatalogo);
        item.setQuantidade(req.quantidade());
        item.setValorUnitario(req.valorUnitario());
        item.setOrcamento(orcamento);
        // valorTotal do item é calculado no @PrePersist/@PreUpdate

        ItemOrcamento salvo = itemRepo.save(item);

        // Recalcula total do orçamento
        BigDecimal total = itemRepo.findByOrcamentoId(orcamentoId).stream()
                .map(ItemOrcamento::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        orcamento.setValorTotal(total);
        orcamentoRepo.save(orcamento);

        return ItemOrcamentoResponse.from(salvo);
    }

    public ItemOrcamentoResponse atualizar(Long orcamentoId, Long itemOrcamentoId, AtualizarItemOrcamentoRequest req) {
        Orcamento orcamento = orcamentoRepo.findById(orcamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado"));

        if (orcamento.getStatus() == StatusOrcamento.FINALIZADO) {
            throw new IllegalStateException("Não é permitido alterar orçamento FINALIZADO");
        }

        ItemOrcamento item = itemRepo.findByIdAndOrcamentoId(itemOrcamentoId, orcamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Item do orçamento não encontrado"));

        if (req.itemId() != null) {
            Item itemCatalogo = itemRepository.findById(req.itemId())
                    .orElseThrow(() -> new IllegalArgumentException("Item não encontrado"));
            item.setItem(itemCatalogo);
        }

        item.setQuantidade(req.quantidade());
        item.setValorUnitario(req.valorUnitario());

        ItemOrcamento salvo = itemRepo.save(item);

        // Recalcula total do orçamento
        java.math.BigDecimal total = itemRepo.findByOrcamentoId(orcamentoId).stream()
                .map(ItemOrcamento::getValorTotal)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        orcamento.setValorTotal(total);
        orcamentoRepo.save(orcamento);

        return ItemOrcamentoResponse.from(salvo);
    }

    public java.util.List<ItemOrcamentoResponse> listar(Long orcamentoId) {
        orcamentoRepo.findById(orcamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado"));
        return itemRepo.findByOrcamentoId(orcamentoId).stream()
                .map(ItemOrcamentoResponse::from)
                .toList();
    }

    public void deletar(Long orcamentoId, Long itemOrcamentoId) {
        Orcamento orcamento = orcamentoRepo.findById(orcamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado"));

        if (orcamento.getStatus() == StatusOrcamento.FINALIZADO) {
            throw new IllegalStateException("Não é permitido alterar orçamento FINALIZADO");
        }

        ItemOrcamento item = itemRepo.findByIdAndOrcamentoId(itemOrcamentoId, orcamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Item do orçamento não encontrado"));

        itemRepo.delete(item);

        // Recalcula total do orçamento após a remoção
        java.math.BigDecimal total = itemRepo.findByOrcamentoId(orcamentoId).stream()
                .map(ItemOrcamento::getValorTotal)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        orcamento.setValorTotal(total);
        orcamentoRepo.save(orcamento);
    }
}

