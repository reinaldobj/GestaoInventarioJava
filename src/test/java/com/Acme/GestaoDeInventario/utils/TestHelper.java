package com.Acme.GestaoDeInventario.utils;

import com.Acme.GestaoDeInventario.dto.PedidoProdutoDTO;
import com.Acme.GestaoDeInventario.dto.ProdutoDTO;
import com.Acme.GestaoDeInventario.dto.UsuarioDTO;
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

    public ProdutoDTO criarProduto(String nome, String descricao, double preco, int quantidade) throws Exception {
        ProdutoDTO produto = new ProdutoDTO(nome, descricao, preco, quantidade);
        MvcResult resultado = mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produto)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readValue(resultado.getResponse().getContentAsString(), ProdutoDTO.class);
    }

    public UsuarioDTO criarUsuario(String nome, String email) throws Exception {
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setEndereco("Endereço Teste");
        usuario.setTelefone("1234-5678");
        usuario.setTipoUsuario("CLIENTE");

        MvcResult resultado = mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readValue(resultado.getResponse().getContentAsString(), UsuarioDTO.class);
    }

    public PedidoProdutoDTO criarPedidoProduto(long idProduto, int quantidade) {
        ProdutoDTO produto = new ProdutoDTO();
        produto.setId(idProduto);

        PedidoProdutoDTO itemPedido = new PedidoProdutoDTO();
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(quantidade);
        return itemPedido;
    }

    public String gerarJson(Object objeto) throws Exception {
        return objectMapper.writeValueAsString(objeto);
    }
}
