package com.Acme.GestaoDeInventario.repository;

import com.Acme.GestaoDeInventario.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    // Aqui você pode adicionar métodos personalizados, se necessário
    // Exemplo: List<Produto> findByNome(String nome);
}
