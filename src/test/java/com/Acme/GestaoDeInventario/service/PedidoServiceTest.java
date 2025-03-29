package com.Acme.GestaoDeInventario.service;

import ch.qos.logback.core.net.server.Client;
import com.Acme.GestaoDeInventario.model.*;
import com.Acme.GestaoDeInventario.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private UsuarioService usuarioService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void deveSalvarPedido() {
        Produto produto = new Produto();
        produto.setId(1);
        produto.setQuantidade(50);
        produto.setPreco(100.0);
        produto.setNome("Cadeira");

        Produto produto2 = new Produto();
        produto2.setId(2);
        produto2.setQuantidade(100);
        produto2.setPreco(500.0);
        produto2.setNome("Mesa");

        when(produtoService.buscarProdutoPorId(1L)).thenReturn(produto);
        when(produtoService.buscarProdutoPorId(2L)).thenReturn(produto2);

        PedidoProduto itemPedido = new PedidoProduto();
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(2);

        PedidoProduto itemPedido2 = new PedidoProduto();
        itemPedido2.setProduto(produto2);
        itemPedido2.setQuantidade(5);

        List<PedidoProduto> itens = List.of(itemPedido, itemPedido2);

        Pedido pedido = new Pedido();
        Usuario cliente = new Usuario();
        cliente.setId(1L);

        int quantidadeProduto1 = produto.getQuantidade();
        int quantidadeProduto2 = produto2.getQuantidade();

        pedido.setCliente(cliente);
        pedido.setItens(itens);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Pedido resultado = pedidoService.criarPedido(pedido);
        assertNotNull(resultado);
        assertEquals(StatusPedido.PENDENTE, resultado.getStatus());
        assertTrue(resultado.getValorTotal() > 0);

        int estoqueEsperadoProduto1 = quantidadeProduto1 - itemPedido.getQuantidade();
        int estoqueEsperadoProduto2 = quantidadeProduto2 - itemPedido2.getQuantidade();

        assertEquals(pedido.getItens().get(0).getProduto().getQuantidade(), estoqueEsperadoProduto1);
        assertEquals(pedido.getItens().get(1).getProduto().getQuantidade(), estoqueEsperadoProduto2);

        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    public void deveLancarExceptionAoTentarCadasrtarPedidoSemItens() {
        Pedido pedido = new Pedido();
        Usuario cliente = new Usuario();
        cliente.setId(1L);

        pedido.setCliente(cliente);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.criarPedido(pedido);
        });

        assertEquals("O pedido deve ter itens", exception.getMessage());
    }

    @Test
    public void deveLancarExceptionAoTentarCadasrtarPedidoComProdutoInexistente() {
        Produto produto = criarMockProduto(1, 50, 100.0, "Cadeira", "Cadeira gamer");
        Produto produto2 = criarMockProduto(2, 100, 500.0, "Mesa", "Mesa de escritório");
        Usuario cliente = criarMockUsuario(1L, "João Silva", "teste@gmail.com", "Rua Teste, 123", "1234-5678");

        when(produtoService.buscarProdutoPorId(1L)).thenReturn(null);
        when(produtoService.buscarProdutoPorId(2L)).thenReturn(produto2);
        when(usuarioService.buscarUsuarioPorId(1L)).thenReturn(cliente);

        PedidoProduto itemPedido = new PedidoProduto();
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(2);

        PedidoProduto itemPedido2 = new PedidoProduto();
        itemPedido2.setProduto(produto2);
        itemPedido2.setQuantidade(5);

        List<PedidoProduto> itens = List.of(itemPedido, itemPedido2);

        Pedido pedido = new Pedido();
        Usuario clientePedido = new Usuario();
        clientePedido.setId(1L);

        int quantidadeProduto1 = produto.getQuantidade();
        int quantidadeProduto2 = produto2.getQuantidade();

        pedido.setCliente(clientePedido);
        pedido.setItens(itens);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.criarPedido(pedido);
        });

        assertEquals("Produto não encontrado", exception.getMessage());
    }

    private static Produto criarMockProduto(long id, int quantidade, double preco, String nome, String descricao) {
        Produto produto = new Produto();
        produto.setId(id);
        produto.setQuantidade(quantidade);
        produto.setPreco(preco);
        produto.setNome(nome);
        produto.setDescricao(descricao);

        return produto;
    }

    private static Usuario criarMockUsuario(long id, String nome, String email, String endereco, String telefone) {
        Usuario cliente = new Usuario();
        cliente.setId(id);
        cliente.setNome(nome);
        cliente.setEmail(email);
        cliente.setEndereco(endereco);
        cliente.setTelefone(telefone);
        cliente.setTipoUsuario(TipoUsuario.CLIENTE);
        return cliente;
    }

    @Test
    public void deveLancarExceptionAoTentarCadasrtarPedidoSemEstoque() {
        Produto produto = new Produto();
        produto.setId(1);
        produto.setQuantidade(0);
        produto.setPreco(100.0);
        produto.setNome("Cadeira");

        Produto produto2 = new Produto();
        produto2.setId(2);
        produto2.setQuantidade(100);
        produto2.setPreco(500.0);
        produto2.setNome("Mesa");

        when(produtoService.buscarProdutoPorId(1L)).thenReturn(produto);
        when(produtoService.buscarProdutoPorId(2L)).thenReturn(produto2);

        PedidoProduto itemPedido = new PedidoProduto();
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(2);

        PedidoProduto itemPedido2 = new PedidoProduto();
        itemPedido2.setProduto(produto2);
        itemPedido2.setQuantidade(5);

        List<PedidoProduto> itens = List.of(itemPedido, itemPedido2);

        Pedido pedido = new Pedido();
        Usuario cliente = new Usuario();
        cliente.setId(1L);

        int quantidadeProduto1 = produto.getQuantidade();
        int quantidadeProduto2 = produto2.getQuantidade();

        pedido.setCliente(cliente);
        pedido.setItens(itens);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.criarPedido(pedido);
        });

        assertEquals("Quantidade do produto não disponível", exception.getMessage());
    }

    @Test
    public void deveRetornarOPedido() {
        Produto produto = new Produto();
        produto.setId(1);
        produto.setQuantidade(50);
        produto.setPreco(100.0);
        produto.setNome("Cadeira");

        Produto produto2 = new Produto();
        produto2.setId(2);
        produto2.setQuantidade(100);
        produto2.setPreco(500.0);
        produto2.setNome("Mesa");

        when(produtoService.buscarProdutoPorId(1L)).thenReturn(produto);
        when(produtoService.buscarProdutoPorId(2L)).thenReturn(produto2);

        PedidoProduto itemPedido = new PedidoProduto();
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(2);

        PedidoProduto itemPedido2 = new PedidoProduto();
        itemPedido2.setProduto(produto2);
        itemPedido2.setQuantidade(5);

        List<PedidoProduto> itens = List.of(itemPedido, itemPedido2);

        Usuario cliente = new Usuario();
        cliente.setId(1L);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setItens(itens);

        when (pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        Pedido resultado = pedidoService.buscarPedidoPorId(1L);

        assertNotNull(resultado);
        assertEquals(cliente.getId(), resultado.getCliente().getId());
        assertEquals(itens.size(), resultado.getItens().size());
    }

    @Test
    public void deveRetornarExcecaoAoBuscarPedidoInexistente() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.buscarPedidoPorId(1L);
        });

        assertEquals("Pedido não encontrado", exception.getMessage());
    }
}
