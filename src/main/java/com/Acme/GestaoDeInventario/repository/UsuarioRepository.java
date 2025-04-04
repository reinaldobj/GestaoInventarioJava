package com.Acme.GestaoDeInventario.repository;

import com.Acme.GestaoDeInventario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
