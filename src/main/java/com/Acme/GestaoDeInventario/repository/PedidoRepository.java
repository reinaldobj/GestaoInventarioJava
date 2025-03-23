package com.Acme.GestaoDeInventario.repository;

import com.Acme.GestaoDeInventario.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
        List<Pedido> findByClienteId(Long idUsuario);
    // Aqui você pode adicionar métodos personalizados, se necessário
    // Exemplo: List<Pedido> findByCliente(Usuario cliente);
}
