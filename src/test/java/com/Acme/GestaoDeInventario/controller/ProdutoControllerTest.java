package com.Acme.GestaoDeInventario.controller;

import com.Acme.GestaoDeInventario.model.Produto;
import com.Acme.GestaoDeInventario.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Test
    void deveCriarProduto() throws Exception {
        String json = "{ \"nome\": \"Cadeira Gamer\", \"marca\": \"Marca Teste\", \"descricao\": \"Descrição Teste\", \"preco\": 100.0, \"quantidade\": 10 }";

        mockMvc.perform(post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Cadeira Gamer"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deveRetornarTodosOsProdutos() throws Exception {
        produtoRepository.save(new Produto("Mesa", "Mesa Ajustavel", 2000, 5));

        mockMvc.perform(get("/produtos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    void deveRetornar404QuandoProdutoNaoForEncontrado() throws Exception {
        mockMvc.perform(get("/produtos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Produto não encontrado"));
    }

    @Test
    void deveRetornarErroAoCadastrarProdutoSemNome() throws Exception {
        String json = "{ \"marca\": \"Marca Teste\", \"preco\": 100.0, \"quantidade\": 10 }";

        mockMvc.perform(post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("O produto deve ter  um nome"));
    }
}
