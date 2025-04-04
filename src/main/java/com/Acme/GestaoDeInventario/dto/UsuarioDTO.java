package com.Acme.GestaoDeInventario.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String TipoUsuario;

    public UsuarioDTO(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }
}
