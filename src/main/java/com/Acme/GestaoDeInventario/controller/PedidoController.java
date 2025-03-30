package com.Acme.GestaoDeInventario.controller;

import com.Acme.GestaoDeInventario.model.Pedido;
import com.Acme.GestaoDeInventario.model.StatusPedido;
import com.Acme.GestaoDeInventario.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@Valid @RequestBody Pedido pedido) {
        Pedido pedidoCriado = pedidoService.criarPedido(pedido);
        return ResponseEntity
                .created(URI.create("/pedidos/" + pedidoCriado.getId()))
                .body(pedidoCriado);
    }

    @GetMapping("/obter/{id}")
    public ResponseEntity<Pedido> buscarPedidoPorId(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscarPedidoPorId(id);
        if (pedido != null) {
            return ResponseEntity.ok(pedido);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/obterporcliente/{idUsuario}")
    public ResponseEntity<List<Pedido>> buscarPedidosPorCliente(@PathVariable Long idUsuario) {
        List<Pedido> pedido = pedidoService.listarPedidosPorCliente(idUsuario);
        if (pedido != null) {
            return ResponseEntity.ok(pedido);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> atualizarStatusPedido(@PathVariable Long id, @RequestBody String statusPedido) {
        try {
            StatusPedido novoStatus = StatusPedido.valueOf(statusPedido.toUpperCase());
            Pedido pedido = pedidoService.buscarPedidoPorId(id);

            if (pedido != null) {
                pedido.setStatus(novoStatus);
                pedidoService.salvarPedido(pedido);
                return ResponseEntity.ok(pedido);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Status inválido: " + statusPedido);
        }
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<Pedido> cancelarPedido(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscarPedidoPorId(id);
        if (pedido != null) {
            pedidoService.cancelarPedido(id);
            return ResponseEntity.ok(pedido);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
