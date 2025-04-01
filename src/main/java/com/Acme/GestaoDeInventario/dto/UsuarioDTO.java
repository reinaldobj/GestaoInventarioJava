package com.Acme.GestaoDeInventario.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private String endereco;
    private String telefone;
    private String tipoUsuario;

    public UsuarioDTO(String nome, String email, String endereco, String telefone, String tipoUsuario) {
        this.nome = nome;
        this.email = email;
        this.endereco = endereco;
        this.telefone = telefone;
        this.tipoUsuario = tipoUsuario;
    }
}
