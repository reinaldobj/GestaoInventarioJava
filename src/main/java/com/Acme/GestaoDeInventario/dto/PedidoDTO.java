package com.Acme.GestaoDeInventario.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PedidoDTO {
    private long id;
    private ClienteDTO cliente;
    private List<PedidoProdutoDTO> itens;
    private String status;
    private double valorTotal;

    public PedidoDTO(ClienteDTO cliente, List<PedidoProdutoDTO> itens, String status, double valorTotal) {
        this.cliente = cliente;
        this.itens = itens;
        this.status = status;
        this.valorTotal = valorTotal;
    }
}
