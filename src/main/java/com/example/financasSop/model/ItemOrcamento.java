package com.example.financasSop.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "item_orcamento")
@AllArgsConstructor
@Data
public class ItemOrcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "quantidade", nullable = false, precision = 12, scale = 2)
    private BigDecimal quantidade;

    @Column(name = "valor_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorUnitario;

    @Column(name = "valor_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "quantidade_acumulada", nullable = false, precision = 12, scale = 2)
    private BigDecimal quantidadeAcumulada = BigDecimal.ZERO;

    @ManyToOne(optional = false)
    @JoinColumn(name = "orcamento_id", nullable = false)
    private Orcamento orcamento;

    public ItemOrcamento() {
    }

    @PrePersist
    @PreUpdate
    public void calcularTotal() {
        if (quantidade != null && valorUnitario != null) {
            this.valorTotal = valorUnitario.multiply(quantidade);
        }
        if (quantidadeAcumulada == null) {
            this.quantidadeAcumulada = BigDecimal.ZERO;
        }
    }

}
