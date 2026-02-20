package com.example.financasSop.model;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "medicao",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_medicao_numero", columnNames = "numero")
        }
)
@Data
public class Medicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false, length = 50)
    private String numero; // único

    @Column(name = "data_medicao", nullable = false)
    private LocalDate dataMedicao;

    @Column(name = "valor_medicao", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorMedicao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusMedicao status;

    @Column(name = "observacao", length = 500)
    private String observacao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "orcamento_id", nullable = false)
    private Orcamento orcamento;

    public Medicao() {}

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = StatusMedicao.ABERTA;
        }
    }
}
