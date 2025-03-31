package com.Acme.GestaoDeInventario.controller;

import com.Acme.GestaoDeInventario.model.TipoUsuario;
import com.Acme.GestaoDeInventario.model.Usuario;
import com.Acme.GestaoDeInventario.utils.TestHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UsuarioControllerTest {
    private static final String URL_BASE = "/usuarios";
    private static final String USUARIO_NOME = "João Silva";
    private static final String USUARIO_ENDERECO = "Rua Teste, 123";
    private static final String USUARIO_EMAIL = "teste@gmail.com";
    private static final String USUARIO_TELEFONE = "1234-5678";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private String usuarioJsonValido;
    private String usuarioJsonSemNome;

    private TestHelper testHelper;

    @BeforeEach
    void setup() throws Exception {
        testHelper = new TestHelper(mockMvc, objectMapper);
        usuarioJsonValido = testHelper.gerarJson(new Usuario(USUARIO_NOME, USUARIO_ENDERECO, USUARIO_EMAIL, USUARIO_TELEFONE, TipoUsuario.CLIENTE));
        usuarioJsonSemNome = testHelper.gerarJson(new Usuario("", USUARIO_ENDERECO, USUARIO_EMAIL, USUARIO_TELEFONE, TipoUsuario.CLIENTE));
    }

    @Test
    void deveCriarUsuarioComSucesso() throws Exception {
        mockMvc.perform(post(URL_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJsonValido))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(USUARIO_NOME))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void deveRetornarErroAoCriarUsuarioSemNome() throws Exception {
        mockMvc.perform(post(URL_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJsonSemNome))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("O usuário deve ter um nome."));
    }

    @Test
    void deveRetornarErroAoCriarUsuarioSemEmail() throws Exception {
        var usuarioJsonInvalido = testHelper.gerarJson(new Usuario(USUARIO_NOME, "", USUARIO_ENDERECO, USUARIO_TELEFONE, TipoUsuario.CLIENTE));
        mockMvc.perform(post(URL_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJsonInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("O usuário deve ter um email."));
    }

    @Test
    void deveRetornarErroAoCriarUsuarioSemEndereco() throws Exception {
        var usuarioJsonInvalido = testHelper.gerarJson(new Usuario(USUARIO_NOME, USUARIO_EMAIL, "", USUARIO_TELEFONE, TipoUsuario.CLIENTE));
        mockMvc.perform(post(URL_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJsonInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("O usuário deve ter um endereço."));
    }

    @Test
    void deveRetornarErroAoCriarUsuarioSemTelefone() throws Exception {
        var usuarioJsonInvalido = testHelper.gerarJson(new Usuario(USUARIO_NOME, USUARIO_ENDERECO, USUARIO_EMAIL, "", TipoUsuario.CLIENTE));
        mockMvc.perform(post(URL_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJsonInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("O usuário deve ter um telefone."));
    }

    @Test
    void deveRetornarUmUsuarioPorIdComSucesso() throws Exception {
        Usuario usuario = testHelper.criarUsuario(USUARIO_NOME, USUARIO_EMAIL);
        mockMvc.perform(get(URL_BASE + "/" + usuario.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(USUARIO_NOME))
                .andExpect(jsonPath("$.id").value(usuario.getId()));
    }
}