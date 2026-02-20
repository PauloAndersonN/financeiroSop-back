package com.example.financasSop.service;

import com.example.financasSop.dto.request.CriarItemMedicaoRequest;
import com.example.financasSop.dto.response.ItemMedicaoResponse;
import com.example.financasSop.model.*;
import com.example.financasSop.repository.ItemMedicaoRepository;
import com.example.financasSop.repository.ItemOrcamentoRepository;
import com.example.financasSop.repository.MedicaoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ItemMedicaoService {

    private final ItemMedicaoRepository itemMedicaoRepository;
    private final MedicaoRepository medicaoRepository;
    private final ItemOrcamentoRepository itemOrcamentoRepository;

    public ItemMedicaoService(ItemMedicaoRepository itemMedicaoRepository,
                              MedicaoRepository medicaoRepository,
                              ItemOrcamentoRepository itemOrcamentoRepository) {
        this.itemMedicaoRepository = itemMedicaoRepository;
        this.medicaoRepository = medicaoRepository;
        this.itemOrcamentoRepository = itemOrcamentoRepository;
    }

    public ItemMedicaoResponse adicionar(Long orcamentoId, Long medicaoId, CriarItemMedicaoRequest req) {
        Medicao medicao = medicaoRepository.findById(medicaoId)
                .orElseThrow(() -> new IllegalArgumentException("Medição não encontrada"));

        if (!medicao.getOrcamento().getId().equals(orcamentoId)) {
            throw new IllegalArgumentException("Medição não pertence ao orçamento informado");
        }
        if (medicao.getStatus() != StatusMedicao.ABERTA) {
            throw new IllegalStateException("Não é permitido alterar itens de medição VALIDADA");
        }

        ItemOrcamento itemOrcamento = itemOrcamentoRepository.findById(req.itemOrcamentoId())
                .orElseThrow(() -> new IllegalArgumentException("Item do orçamento não encontrado"));

        if (!itemOrcamento.getOrcamento().getId().equals(orcamentoId)) {
            throw new IllegalArgumentException("Item do orçamento não pertence ao orçamento da medição");
        }

        // Verifica se (acumulada + soma desta medição incluindo novo item) <= quantidade total do item
        BigDecimal acumulada = itemOrcamento.getQuantidadeAcumulada() == null ? BigDecimal.ZERO : itemOrcamento.getQuantidadeAcumulada();
        BigDecimal jaMedidaNesta = itemMedicaoRepository.findByMedicaoIdAndItemOrcamentoId(medicaoId, itemOrcamento.getId()).stream()
                .map(ItemMedicao::getQuantidadeMedida)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal novaSoma = acumulada.add(jaMedidaNesta).add(req.quantidadeMedida());
        if (itemOrcamento.getQuantidade() != null && novaSoma.compareTo(itemOrcamento.getQuantidade()) > 0) {
            throw new IllegalStateException("Quantidade medida excede a quantidade do item do orçamento");
        }

        ItemMedicao im = new ItemMedicao();
        im.setMedicao(medicao);
        im.setItemOrcamento(itemOrcamento);
        im.setQuantidadeMedida(req.quantidadeMedida());

        ItemMedicao salvo = itemMedicaoRepository.save(im);

        // Recalcula valor da medição (soma dos itens)
        java.math.BigDecimal total = itemMedicaoRepository.findByMedicaoId(medicaoId).stream()
                .map(ItemMedicao::getValorTotalMedido)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        medicao.setValorMedicao(total);
        medicaoRepository.save(medicao);

        return ItemMedicaoResponse.from(salvo);
    }

    public List<ItemMedicaoResponse> listar(Long orcamentoId, Long medicaoId) {
        Medicao medicao = medicaoRepository.findById(medicaoId)
                .orElseThrow(() -> new IllegalArgumentException("Medição não encontrada"));
        if (!medicao.getOrcamento().getId().equals(orcamentoId)) {
            throw new IllegalArgumentException("Medição não pertence ao orçamento informado");
        }
        return itemMedicaoRepository.findByMedicaoId(medicaoId).stream()
                .map(ItemMedicaoResponse::from)
                .toList();
    }

    public void deletar(Long orcamentoId, Long medicaoId, Long itemMedicaoId) {
        Medicao medicao = medicaoRepository.findById(medicaoId)
                .orElseThrow(() -> new IllegalArgumentException("Medição não encontrada"));
        if (!medicao.getOrcamento().getId().equals(orcamentoId)) {
            throw new IllegalArgumentException("Medição não pertence ao orçamento informado");
        }
        if (medicao.getStatus() != StatusMedicao.ABERTA) {
            throw new IllegalStateException("Não é permitido alterar itens de medição VALIDADA");
        }

        ItemMedicao im = itemMedicaoRepository.findById(itemMedicaoId)
                .orElseThrow(() -> new IllegalArgumentException("Item de medição não encontrado"));
        if (!im.getMedicao().getId().equals(medicaoId)) {
            throw new IllegalArgumentException("Item de medição não pertence à medição informada");
        }

        itemMedicaoRepository.delete(im);

        // Recalcula valor da medição após remoção
        java.math.BigDecimal total = itemMedicaoRepository.findByMedicaoId(medicaoId).stream()
                .map(ItemMedicao::getValorTotalMedido)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        medicao.setValorMedicao(total);
        medicaoRepository.save(medicao);
    }
}

