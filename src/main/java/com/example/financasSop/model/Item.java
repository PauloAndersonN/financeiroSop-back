package com.example.financasSop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "item", uniqueConstraints = @UniqueConstraint(name = "uk_item_nome", columnNames = "nome"))
@AllArgsConstructor
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 150, unique = true)
    private String nome;

    @Column(name = "unidade", length = 50)
    private String unidade;

    public Item() {
    }
}

