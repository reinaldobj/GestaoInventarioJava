package com.Acme.GestaoDeInventario.controller;

import com.Acme.GestaoDeInventario.model.*;
import com.Acme.GestaoDeInventario.repository.PedidoRepository;
import com.Acme.GestaoDeInventario.utils.TestHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PedidoControllerTest {

    private static final String URL_PEDIDOS = "/pedidos";
    private static final String URL_USUARIOS = "/usuarios";
    private static final String URL_PRODUTOS = "/produtos";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PedidoRepository pedidoRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private TestHelper testHelper;

    @BeforeEach
    void setup() {
        testHelper = new TestHelper(mockMvc, objectMapper);
    }

    @Test
    void deveCriarPedido() throws Exception {
        Produto produto1 = testHelper.criarProduto("Cadeira", "Cadeira Teste", 100.00, 100);
        Produto produto2 = testHelper.criarProduto("Mesa", "Mesa Teste", 1000.00, 100);

        Usuario usuario = testHelper.criarUsuario("Usuario Teste", "teste@teste.com");

        PedidoProduto itemPedido1 = testHelper.criarPedidoProduto(produto1.getId(), 2);
        PedidoProduto itemPedido2 = testHelper.criarPedidoProduto(produto2.getId(), 1);

        Pedido pedido = new Pedido();
        pedido.setCliente(usuario);
        pedido.setItens(List.of(itemPedido1, itemPedido2));

        int estoqueEsperadoProduto1 = produto1.getQuantidade() - itemPedido1.getQuantidade();
        int estoqueEsperadoProduto2 = produto2.getQuantidade() - itemPedido2.getQuantidade();

        MvcResult resultado = mockMvc.perform(post(URL_PEDIDOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(StatusPedido.PENDENTE.toString()))
                .andReturn();

        Pedido pedidoCriado = objectMapper.readValue(resultado.getResponse().getContentAsString(), Pedido.class);

        assertTrue(pedidoCriado.getId() > 0);
        assertEquals(estoqueEsperadoProduto1, pedidoCriado.getItens().get(0).getProduto().getQuantidade());
        assertEquals(estoqueEsperadoProduto2, pedidoCriado.getItens().get(1).getProduto().getQuantidade());
    }

    @Test
    void deveRetornarExceptionAoCadastrarPedidoComItemQueNaoExiste() throws Exception {
        Usuario usuario = testHelper.criarUsuario("Cliente Teste", "cliente@teste.com");

        PedidoProduto itemPedidoInvalido = testHelper.criarPedidoProduto(9999L, 2);

        Pedido pedido = new Pedido();
        pedido.setCliente(usuario);
        pedido.setItens(List.of(itemPedidoInvalido));

        mockMvc.perform(post(URL_PEDIDOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Produto não encontrado"));
    }
}
