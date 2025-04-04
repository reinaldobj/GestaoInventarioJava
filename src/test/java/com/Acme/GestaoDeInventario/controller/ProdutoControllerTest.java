package com.Acme.GestaoDeInventario.controller;

import com.Acme.GestaoDeInventario.dto.ProdutoDTO;
import com.Acme.GestaoDeInventario.utils.TestHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProdutoControllerTest {

    private static final String URL_BASE = "/produtos";
    private static final String PRODUTO_NOME = "Cadeira Gamer";
    private static final String PRODUTO_DESCRICAO = "Descrição Teste";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String produtoJsonValido;
    private String produtoJsonSemNome;

    private TestHelper testHelper;

    @BeforeEach
    void setup() throws Exception {
        testHelper = new TestHelper(mockMvc, objectMapper);
        produtoJsonValido = testHelper.gerarJson(new ProdutoDTO(PRODUTO_NOME, PRODUTO_DESCRICAO, 100.0, 10));
        produtoJsonSemNome = testHelper.gerarJson(new ProdutoDTO(null, PRODUTO_DESCRICAO, 100.0, 10));
    }

    @Test
    void deveCriarProdutoComSucesso() throws Exception {
        mockMvc.perform(post(URL_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(produtoJsonValido))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(PRODUTO_NOME))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void deveRetornarTodosOsProdutos() throws Exception {
        mockMvc.perform(get(URL_BASE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    void deveRetornar404QuandoProdutoNaoForEncontrado() throws Exception {
        mockMvc.perform(get(URL_BASE + "/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Produto não encontrado"));
    }

    @Test
    void deveRetornarErroAoCadastrarProdutoSemNome() throws Exception {
        mockMvc.perform(post(URL_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(produtoJsonSemNome))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("O produto deve ter um nome"));
    }
}
