package com.Acme.GestaoDeInventario.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClienteDTO {
    private long id;

    private UsuarioDTO usuario;

    private String telefone;

    private String endereco;

    private int numero;

    public ClienteDTO(UsuarioDTO usuario, String telefone, String endereco, int numero) {
        this.usuario = usuario;
        this.telefone = telefone;
        this.endereco = endereco;
        this.numero = numero;
    }
}
