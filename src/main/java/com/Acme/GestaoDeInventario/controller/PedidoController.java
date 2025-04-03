package com.Acme.GestaoDeInventario.controller;

import com.Acme.GestaoDeInventario.dto.PedidoDTO;
import com.Acme.GestaoDeInventario.mapper.PedidoMapper;
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
    public ResponseEntity<PedidoDTO> criarPedido(@Valid @RequestBody PedidoDTO pedidoDTO) {
        Pedido pedido = PedidoMapper.INSTANCE.pedidoDTOToPedido(pedidoDTO);

        Pedido pedidoCriado = pedidoService.criar(pedido);

        PedidoDTO pedidoCriadoDTO = PedidoMapper.INSTANCE.pedidoToPedidoDTO(pedidoCriado);

        return ResponseEntity
                .created(URI.create("/pedidos/" + pedidoCriadoDTO.getId()))
                .body(pedidoCriadoDTO);
    }

    @GetMapping("/obter/{id}")
    public ResponseEntity<PedidoDTO> buscarPedidoPorId(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscarPorId(id);

        PedidoDTO pedidoDTO = PedidoMapper.INSTANCE.pedidoToPedidoDTO(pedido);

        if (pedido != null) {
            return ResponseEntity.ok(pedidoDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/obterporcliente/{idUsuario}")
    public ResponseEntity<List<PedidoDTO>> buscarPedidosPorCliente(@PathVariable Long idCliente) {
        List<Pedido> pedido = pedidoService.listarPorCliente(idCliente);

        if (pedido.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<PedidoDTO> pedidosDTO = pedido.stream()
                .map(PedidoMapper.INSTANCE::pedidoToPedidoDTO)
                .toList();

        if (!pedido.isEmpty()) {
            return ResponseEntity.ok(pedidosDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> atualizarStatusPedido(@PathVariable Long id, @RequestBody String statusPedido) {
        try {
            StatusPedido novoStatus = StatusPedido.valueOf(statusPedido.toUpperCase());
            Pedido pedido = pedidoService.buscarPorId(id);

            if (pedido != null) {
                pedido.setStatus(novoStatus);
                pedidoService.salvar(pedido);

                PedidoDTO pedidoDTO = PedidoMapper.INSTANCE.pedidoToPedidoDTO(pedido);

                return ResponseEntity.ok(pedidoDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Status inválido: " + statusPedido);
        }
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<Pedido> cancelarPedido(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscarPorId(id);
        if (pedido != null) {
            pedidoService.cancelar(id);
            return ResponseEntity.ok(pedido);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
