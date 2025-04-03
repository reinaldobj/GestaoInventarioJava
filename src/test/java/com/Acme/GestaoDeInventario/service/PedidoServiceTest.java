package com.Acme.GestaoDeInventario.service;

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

    @Mock
    private ClienteService clienteService;

    private static Produto criarMockProduto(long id, int quantidade, double preco, String nome, String descricao) {
        Produto produto = new Produto();
        produto.setId(id);
        produto.setQuantidade(quantidade);
        produto.setPreco(preco);
        produto.setNome(nome);
        produto.setDescricao(descricao);

        return produto;
    }

    private static Usuario criarMockUsuario(long id, String nome, String email, String senha) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);

        return usuario;
    }

    private static Cliente criarMockCliente(long id, String endereco, String telefone, int numero) {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNumero(numero);
        cliente.setEndereco(endereco);
        cliente.setTelefone(telefone);

        return cliente;
    }

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
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        int quantidadeProduto1 = produto.getQuantidade();
        int quantidadeProduto2 = produto2.getQuantidade();

        pedido.setCliente(cliente);
        pedido.setItens(itens);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Pedido resultado = pedidoService.criar(pedido);
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
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        pedido.setCliente(cliente);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Exception exception = assertThrows(RuntimeException.class, () -> pedidoService.criar(pedido));

        assertEquals("O pedido deve ter itens", exception.getMessage());
    }

    @Test
    public void deveLancarExceptionAoTentarCadasrtarPedidoComProdutoInexistente() {
        Produto produto = criarMockProduto(1, 50, 100.0, "Cadeira", "Cadeira gamer");
        Produto produto2 = criarMockProduto(2, 100, 500.0, "Mesa", "Mesa de escritório");
        Usuario usuario = criarMockUsuario(1L, "João Silva", "teste@gmail.com", "Teste@2025");
        Cliente cliente = criarMockCliente(1L, "Rua Teste", "1234-5678", 123);

        when(produtoService.buscarProdutoPorId(1L)).thenReturn(produto);

        when(produtoService.buscarProdutoPorId(1L)).thenReturn(null);
        when(produtoService.buscarProdutoPorId(2L)).thenReturn(produto2);
        when(usuarioService.buscarUsuarioPorId(1L)).thenReturn(usuario);
        when(clienteService.buscarPorId(1L)).thenReturn(cliente);

        PedidoProduto itemPedido = new PedidoProduto();
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(2);

        PedidoProduto itemPedido2 = new PedidoProduto();
        itemPedido2.setProduto(produto2);
        itemPedido2.setQuantidade(5);

        List<PedidoProduto> itens = List.of(itemPedido, itemPedido2);

        Pedido pedido = new Pedido();
        Cliente clientePedido = new Cliente();
        clientePedido.setId(1L);

        pedido.setCliente(clientePedido);
        pedido.setItens(itens);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Exception exception = assertThrows(RuntimeException.class, () -> pedidoService.criar(pedido));

        assertEquals("Produto não encontrado", exception.getMessage());
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
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        pedido.setCliente(cliente);
        pedido.setItens(itens);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Exception exception = assertThrows(RuntimeException.class, () -> pedidoService.criar(pedido));

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

        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setItens(itens);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        Pedido resultado = pedidoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(cliente.getId(), resultado.getCliente().getId());
        assertEquals(itens.size(), resultado.getItens().size());
    }

    @Test
    public void deveRetornarExcecaoAoBuscarPedidoInexistente() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> pedidoService.buscarPorId(1L));

        assertEquals("Pedido não encontrado", exception.getMessage());
    }
}
