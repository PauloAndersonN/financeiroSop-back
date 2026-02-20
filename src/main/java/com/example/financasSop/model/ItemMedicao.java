package com.example.financasSop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "item_medicao")
@AllArgsConstructor
@Data
public class ItemMedicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantidade_medida", nullable = false, precision = 12, scale = 2)
    private BigDecimal quantidadeMedida;

    @Column(name = "valor_total_medido", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorTotalMedido;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_orcamento_id", nullable = false)
    private ItemOrcamento itemOrcamento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medicao_id", nullable = false)
    private Medicao medicao;

    public ItemMedicao() {}

    @PrePersist
    @PreUpdate
    public void calcularTotalMedido() {
        if (quantidadeMedida != null && itemOrcamento != null && itemOrcamento.getValorUnitario() != null) {
            this.valorTotalMedido = itemOrcamento.getValorUnitario().multiply(quantidadeMedida);
        }
    }

}
