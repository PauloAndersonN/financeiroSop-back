package com.example.financasSop.service;

import com.example.financasSop.model.Orcamento;
import com.example.financasSop.repository.OrcamentoRepository;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioService {

    private final OrcamentoRepository orcamentoRepository;

    public RelatorioService(OrcamentoRepository orcamentoRepository) {
        this.orcamentoRepository = orcamentoRepository;
    }

    public byte[] gerarRelatorioOrcamentosPdf() {
        List<RelatorioOrcamentoLinha> dados = orcamentoRepository
                .findAll(Sort.by("dataCriacao").descending())
                .stream()
                .map(RelatorioOrcamentoLinha::from)
                .toList();

        try (InputStream template = getClass().getResourceAsStream("/reports/orcamentos_simples.jrxml")) {
            if (template == null) {
                throw new IllegalStateException("Template Jasper nao encontrado: /reports/orcamentos_simples.jrxml");
            }

            var jasperReport = JasperCompileManager.compileReport(template);
            Map<String, Object> params = new HashMap<>();
            params.put("TITULO", "Relatorio Simples de Orcamentos");
            params.put("GERADO_EM", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

            JasperPrint print = JasperFillManager.fillReport(
                    jasperReport,
                    params,
                    new JRBeanCollectionDataSource(dados)
            );
            return JasperExportManager.exportReportToPdf(print);
        } catch (JRException e) {
            throw new IllegalStateException("Erro ao gerar relatorio Jasper", e);
        } catch (Exception e) {
            throw new IllegalStateException("Erro inesperado ao gerar relatorio", e);
        }
    }

    public static class RelatorioOrcamentoLinha {
        private Long id;
        private String nup;
        private String tipoOrcamento;
        private BigDecimal valorTotal;
        private String status;
        private String dataCriacao;

        public static RelatorioOrcamentoLinha from(Orcamento o) {
            RelatorioOrcamentoLinha l = new RelatorioOrcamentoLinha();
            l.id = o.getId();
            l.nup = o.getNup();
            l.tipoOrcamento = o.getTipo() != null ? o.getTipo().getNome() : "";
            l.valorTotal = o.getValorTotal();
            l.status = o.getStatus() != null ? o.getStatus().name() : "";
            l.dataCriacao = o.getDataCriacao() != null
                    ? o.getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    : "";
            return l;
        }

        public Long getId() {
            return id;
        }

        public String getNup() {
            return nup;
        }

        public String getTipoOrcamento() {
            return tipoOrcamento;
        }

        public BigDecimal getValorTotal() {
            return valorTotal;
        }

        public String getStatus() {
            return status;
        }

        public String getDataCriacao() {
            return dataCriacao;
        }
    }
}
