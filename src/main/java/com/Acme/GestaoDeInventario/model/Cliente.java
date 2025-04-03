package com.Acme.GestaoDeInventario.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name="usuario_id", unique = true)
    private Usuario usuario;

    private String telefone;

    private String endereco;

    private int numero;

    public Cliente(Usuario usuario, String telefone, String endereco, int numero) {
        this.usuario = usuario;
        this.telefone = telefone;
        this.endereco = endereco;
        this.numero = numero;
    }
}
