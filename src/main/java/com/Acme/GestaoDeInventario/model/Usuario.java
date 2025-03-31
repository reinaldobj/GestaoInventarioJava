package com.Acme.GestaoDeInventario.model;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    public Usuario(String nome, String email, String endereco, String telefone, TipoUsuario tipoUsuario) {
        this.nome = nome;
        this.email = email;
        this.endereco = endereco;
        this.telefone = telefone;
        this.tipoUsuario = tipoUsuario;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    @Size(max = 100, message = "O email deve ter no máximo 100 caracteres")
    private String email;

    @NotBlank(message = "O endereço é obrigatório")
    @Size(max = 200, message = "O endereço deve ter no máximo 200 caracteres")
    private String endereco;

    @NotBlank(message = "O telefone é obrigatório")
    private String telefone;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;
}
