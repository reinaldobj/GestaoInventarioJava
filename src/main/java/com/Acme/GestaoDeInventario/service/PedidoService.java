package com.Acme.GestaoDeInventario.service;

import com.Acme.GestaoDeInventario.exception.PedidoNaoEncontradoException;
import com.Acme.GestaoDeInventario.exception.ProdutoNaoEncontradoException;
import com.Acme.GestaoDeInventario.exception.ProdutoSemEstoqueException;
import com.Acme.GestaoDeInventario.model.Pedido;
import com.Acme.GestaoDeInventario.model.PedidoProduto;
import com.Acme.GestaoDeInventario.model.Produto;
import com.Acme.GestaoDeInventario.model.StatusPedido;
import com.Acme.GestaoDeInventario.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProdutoService produtoService;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoService produtoService) {
        this.pedidoRepository = pedidoRepository;
        this.produtoService = produtoService;
    }

    public Pedido criarPedido(Pedido pedido) {
        List<PedidoProduto> itensAtualizados = new ArrayList<>();

        for (PedidoProduto item : pedido.getItens()) {
            Produto produto = produtoService.buscarProdutoPorId(item.getProduto().getId());

            if (produto == null) {
                throw new ProdutoNaoEncontradoException("Produto não encontrado");
            }

            if (item.getQuantidade() > produto.getQuantidade()) {
                throw new ProdutoSemEstoqueException("Quantidade do produto não disponível");
            }

            produto.setQuantidade(produto.getQuantidade() - item.getProduto().getQuantidade());
            produtoService.salvarProduto(produto);

            item = new PedidoProduto(pedido, produto, item.getQuantidade(), produto.getPreco());
            itensAtualizados.add(item);
        }

        pedido.setItens(itensAtualizados);
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.calcularValorTotal();

        return pedidoRepository.save(pedido);
    }

    public Pedido salvarPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPedidoPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontradoException("Pedido não encontrado"));
    }

    public List<Pedido> listarPedidosPorCliente(Long idUsuario) {
        return pedidoRepository.findByClienteId(idUsuario);
    }

    public void cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontradoException("Pedido não encontrado"));

        for (PedidoProduto item : pedido.getItens()) {
            Produto produto = produtoService.buscarProdutoPorId(item.getProduto().getId());
            if (produto != null) {
                produto.setQuantidade(produto.getQuantidade() + item.getQuantidade());
                produtoService.salvarProduto(produto);
            }
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }
}
