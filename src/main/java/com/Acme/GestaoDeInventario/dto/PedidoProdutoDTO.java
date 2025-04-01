package com.Acme.GestaoDeInventario.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PedidoProdutoDTO {
    private long id;

    @JsonBackReference
    private PedidoDTO pedido;
    private ProdutoDTO produto;
    private int quantidade;
    private double precoUnitario;

    public PedidoProdutoDTO(PedidoDTO pedido, ProdutoDTO produto, int quantidade, double precoUnitario) {
        this.pedido = pedido;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }
}
