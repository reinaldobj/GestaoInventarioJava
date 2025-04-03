package com.Acme.GestaoDeInventario.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "pedidos")
@NoArgsConstructor
public class Pedido {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Setter
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<PedidoProduto> itens;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status;

    @Column(nullable = false)
    private double valorTotal;

    public Pedido(Cliente cliente, List<PedidoProduto> itens, StatusPedido status) {
        this.cliente = cliente;
        this.itens = itens;
        this.status = status;
    }

    public void calcularValorTotal() {
        this.valorTotal = itens.stream()
                .mapToDouble(item -> item.getProduto().getPreco() * item.getQuantidade())
                .sum();
    }
}
