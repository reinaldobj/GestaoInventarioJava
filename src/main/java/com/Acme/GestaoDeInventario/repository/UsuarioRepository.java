package com.Acme.GestaoDeInventario.repository;

import com.Acme.GestaoDeInventario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
