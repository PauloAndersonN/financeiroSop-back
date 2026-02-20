package com.example.financasSop.service;

import com.example.financasSop.dto.request.CriarMedicaoRequest;
import com.example.financasSop.dto.response.MedicaoListResponse;
import com.example.financasSop.dto.response.MedicaoResponse;
import com.example.financasSop.model.*;
import com.example.financasSop.repository.ItemMedicaoRepository;
import com.example.financasSop.repository.ItemOrcamentoRepository;
import com.example.financasSop.repository.MedicaoRepository;
import com.example.financasSop.repository.OrcamentoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class MedicaoService {

    private final MedicaoRepository medicaoRepository;
    private final ItemMedicaoRepository itemMedicaoRepository;
    private final OrcamentoRepository orcamentoRepository;
    private final ItemOrcamentoRepository itemOrcamentoRepository;

    public MedicaoService(MedicaoRepository medicaoRepository,
                          ItemMedicaoRepository itemMedicaoRepository,
                          OrcamentoRepository orcamentoRepository,
                          ItemOrcamentoRepository itemOrcamentoRepository) {
        this.medicaoRepository = medicaoRepository;
        this.itemMedicaoRepository = itemMedicaoRepository;
        this.orcamentoRepository = orcamentoRepository;
        this.itemOrcamentoRepository = itemOrcamentoRepository;
    }

    public MedicaoResponse criar(Long orcamentoId, CriarMedicaoRequest req) {
        Orcamento orcamento = orcamentoRepository.findById(orcamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado"));

        if (medicaoRepository.existsByOrcamentoIdAndStatus(orcamentoId, StatusMedicao.ABERTA)) {
            throw new IllegalStateException("Já existe uma medição ABERTA para este orçamento");
        }

        Medicao m = new Medicao();
        m.setNumero(req.numero());
        m.setDataMedicao(req.dataMedicao());
        m.setObservacao(req.observacao());
        m.setStatus(StatusMedicao.ABERTA);
        m.setValorMedicao(BigDecimal.ZERO);
        m.setOrcamento(orcamento);

        Medicao salvo = medicaoRepository.save(m);
        return MedicaoResponse.from(salvo);
    }

    public List<MedicaoResponse> listar(Long orcamentoId) {
        orcamentoRepository.findById(orcamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado"));
        return medicaoRepository.findByOrcamentoIdOrderByDataMedicaoDesc(orcamentoId).stream()
                .map(MedicaoResponse::from)
                .toList();
    }

    public Page<MedicaoListResponse> listarGlobal(int page,
                                                  int size,
                                                  StatusMedicao status,
                                                  Long orcamentoId,
                                                  String nup,
                                                  LocalDate dataDe,
                                                  LocalDate dataAte) {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        if (size > 100) size = 100;

        Pageable pageable = PageRequest.of(page, size, Sort.by("dataMedicao").descending().and(Sort.by("id").descending()));
        Specification<Medicao> spec = (root, query, cb) -> cb.conjunction();

        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (orcamentoId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("orcamento").get("id"), orcamentoId));
        }
        if (nup != null && !nup.isBlank()) {
            String like = "%" + nup.toLowerCase().trim() + "%";
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("orcamento").get("nup")), like));
        }
        if (dataDe != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dataMedicao"), dataDe));
        }
        if (dataAte != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("dataMedicao"), dataAte));
        }

        return medicaoRepository.findAll(spec, pageable)
                .map(MedicaoListResponse::from);
    }

    public MedicaoResponse validar(Long orcamentoId, Long medicaoId) {
        Medicao medicao = medicaoRepository.findById(medicaoId)
                .orElseThrow(() -> new IllegalArgumentException("Medição não encontrada"));

        if (!medicao.getOrcamento().getId().equals(orcamentoId)) {
            throw new IllegalArgumentException("Medição não pertence ao orçamento informado");
        }

        if (medicao.getStatus() != StatusMedicao.ABERTA) {
            throw new IllegalStateException("Somente medições ABERTAS podem ser validadas");
        }

        // Valida limites e atualiza acumulados
        var itens = itemMedicaoRepository.findByMedicaoId(medicaoId);
        for (ItemMedicao im : itens) {
            ItemOrcamento io = im.getItemOrcamento();
            if (io == null) continue;
            BigDecimal acumulada = io.getQuantidadeAcumulada() == null ? BigDecimal.ZERO : io.getQuantidadeAcumulada();
            BigDecimal novaAcumulada = acumulada.add(im.getQuantidadeMedida());
            if (io.getQuantidade() != null && novaAcumulada.compareTo(io.getQuantidade()) > 0) {
                throw new IllegalStateException("Quantidade medida excede a quantidade do item do orçamento");
            }
        }

        // Se válido, aplica atualização dos acumulados
        for (ItemMedicao im : itens) {
            ItemOrcamento io = im.getItemOrcamento();
            if (io == null) continue;
            BigDecimal acumulada = io.getQuantidadeAcumulada() == null ? BigDecimal.ZERO : io.getQuantidadeAcumulada();
            io.setQuantidadeAcumulada(acumulada.add(im.getQuantidadeMedida()));
            itemOrcamentoRepository.save(io);
        }

        // Marca como VALIDADA e recalcula valor da medição
        medicao.setStatus(StatusMedicao.VALIDADA);
        BigDecimal total = itens.stream()
                .map(ItemMedicao::getValorTotalMedido)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        medicao.setValorMedicao(total);

        Medicao salvo = medicaoRepository.save(medicao);
        return MedicaoResponse.from(salvo);
    }
}

