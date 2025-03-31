package com.Acme.GestaoDeInventario.model;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Entity
@Table(name = "produtos")
public class Produto {
    public Produto(String nome, String descricao, double preco, int quantidade) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Nome do produto é obrigatório")
    @Column(nullable = false)
    private String nome;

    private String descricao;

    @NotBlank(message = "O preço é obrigatório")
    @Min(value = 0, message = "O preço deve ser maior ou igual a zero")
    @Column(nullable = false)
    private double preco;

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 0, message = "A quantidade deve ser maior ou igual a zero")
    @Column(nullable = false)
    private int quantidade;
}
