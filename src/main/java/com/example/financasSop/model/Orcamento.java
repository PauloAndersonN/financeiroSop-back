package com.example.financasSop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(
        name = "orcamento",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_orcamento_nup", columnNames = "nup")
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nup", length = 17, nullable = false, updatable = false)
    private String nup;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_orcamento_id", nullable = false)
    private TipoOrcamento tipo;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusOrcamento status;

    @Column(name = "valor_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorTotal;


    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = StatusOrcamento.ABERTO;
        }
        if (this.dataCriacao == null) {
            this.dataCriacao = LocalDateTime.now();
        }
    }

}
