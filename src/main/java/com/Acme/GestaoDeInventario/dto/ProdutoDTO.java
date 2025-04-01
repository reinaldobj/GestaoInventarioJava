package com.Acme.GestaoDeInventario.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProdutoDTO {
    private long id;
    private String nome;
    private String descricao;
    private double preco;
    private int quantidade;

    public ProdutoDTO(String nome, String descricao, double preco, int quantidade) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
    }
}
