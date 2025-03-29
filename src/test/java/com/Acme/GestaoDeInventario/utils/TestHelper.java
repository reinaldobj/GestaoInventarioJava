package com.Acme.GestaoDeInventario.utils;

import com.Acme.GestaoDeInventario.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestHelper {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public TestHelper(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public Produto criarProduto(String nome, String descricao, double preco, int quantidade) throws Exception {
        Produto produto = new Produto(nome, descricao, preco, quantidade);
        MvcResult resultado = mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produto)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readValue(resultado.getResponse().getContentAsString(), Produto.class);
    }

    public Usuario criarUsuario(String nome, String email) throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setEndereco("Endereço Teste");
        usuario.setTelefone("1234-5678");
        usuario.setTipoUsuario(TipoUsuario.CLIENTE);

        MvcResult resultado = mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readValue(resultado.getResponse().getContentAsString(), Usuario.class);
    }

    public PedidoProduto criarPedidoProduto(long idProduto, int quantidade) {
        Produto produto = new Produto();
        produto.setId(idProduto);

        PedidoProduto itemPedido = new PedidoProduto();
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(quantidade);
        return itemPedido;
    }

    public String gerarJson(Object objeto) throws Exception {
        return objectMapper.writeValueAsString(objeto);
    }
}
