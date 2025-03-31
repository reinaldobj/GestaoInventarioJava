package com.Acme.GestaoDeInventario.bdd.steps;

import com.Acme.GestaoDeInventario.model.TipoUsuario;
import com.Acme.GestaoDeInventario.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public class UsuarioStep {
    private final SharedSteps sharedSteps;
    private ResponseEntity<String> response;

    private Usuario usuario;

    @Autowired
    private ObjectMapper objectMapper;

    public UsuarioStep(SharedSteps sharedSteps) {
        this.sharedSteps = sharedSteps;
    }

    @Given("que o usuário deseja se cadastrar com os seguintes dados:")
    public void cadastrarUsuario(io.cucumber.datatable.DataTable table) {
        List<Map<String, String>> data = table.asMaps();
        Map<String, String> row = data.getFirst(); // Pega a primeira linha da tabela

        usuario = new Usuario();
        usuario.setNome(row.get("nome"));
        usuario.setEmail(row.get("email"));
        usuario.setEndereco(row.get("endereco"));
        usuario.setTelefone(row.get("telefone"));
        usuario.setTipoUsuario(TipoUsuario.CLIENTE);
    }

    @When("eu envio uma requisição POST com o usuário para {string}")
    public void enviarRequisicaoPost(String endpoint) throws Exception {
        sharedSteps.enviarRequisicaoPost(endpoint, usuario);
        response = sharedSteps.getResponse();

        usuario = objectMapper.readValue(response.getBody(), Usuario.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Usuário cadastrado com sucesso!");
        } else {
            System.out.println("Erro ao cadastrar usuário: " + response.getStatusCode());
        }
    }

    @And("a resposta deve conter um id válido")
    public void validarId() {
        String body = response.getBody();
        assert body != null : "Erro: O corpo da resposta está vazio!";

        // Verifica se o corpo da resposta contém o campo "id"
        assert body.contains("id") : "Erro: O ID não foi encontrado na resposta!";
        // Extrai o ID do corpo da resposta
        Long id = Long.parseLong(body.substring(body.indexOf("id") + 4, body.indexOf(",")));

        // Verifica se o ID é maior que zero
        assert id > 0 : "Erro: O ID do usuário deve ser maior que zero!";
    }

    @When("eu envio uma requisição GET com o usuário para {string}")
    public void enviarRequisicaoGet(String endpoint) {
        sharedSteps.enviarRequisicaoGet(endpoint, usuario.getId());
        response = sharedSteps.getResponse();

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Usuário encontrado com sucesso!");
        } else {
            System.out.println("Erro ao encontrar usuário: " + response.getStatusCode());
        }
    }

    @Then("o sistema deve retornar os detalhes do usuário")
    public void validarDetalhesUsuario() {
        assert response.getStatusCode().is2xxSuccessful() : "Erro: O status da resposta não é 200 OK!";
        assert response.getBody() != null : "Erro: O corpo da resposta está vazio!";
        assert response.getBody().contains(usuario.getNome()) : "Erro: O nome do usuário não foi encontrado na resposta!";
        assert response.getBody().contains(usuario.getEmail()) : "Erro: O email do usuário não foi encontrado na resposta!";
    }

    @And("a resposta deve conter o nome {string} e o email {string}")
    public void validarNomeEEmail(String nome, String email) {
        assert response.getBody() != null : "Erro: O corpo da resposta está vazio!";
        assert response.getBody().contains(nome) : "Erro: O nome do usuário não foi encontrado na resposta!";
        assert response.getBody().contains(email) : "Erro: O email do usuário não foi encontrado na resposta!";
    }

    @When("eu envio uma requisição PUT para {string} com os novos dados:")
    public void enviarRequisicaoPut(String endpoint, io.cucumber.datatable.DataTable table) {
        List<Map<String, String>> data = table.asMaps();
        Map<String, String> row = data.getFirst(); // Pega a primeira linha da tabela

        usuario.setNome(row.get("nome"));
        usuario.setEmail(row.get("email"));
        usuario.setEndereco(row.get("endereco"));
        usuario.setTelefone(row.get("telefone"));

        sharedSteps.enviarRequisicaoPut(endpoint, usuario.getId(), usuario);
        response = sharedSteps.getResponse();

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Usuário atualizado com sucesso!");
        } else {
            System.out.println("Erro ao atualizar usuário: " + response.getStatusCode());
        }
    }

    @Then("o sistema deve atualizar o usuário")
    public void validarAtualizacaoUsuario() {
        assert response.getStatusCode().is2xxSuccessful() : "Erro: O status da resposta não é 200 OK!";
        assert response.getBody() != null : "Erro: O corpo da resposta está vazio!";
        assert response.getBody().contains(usuario.getNome()) : "Erro: O nome do usuário não foi encontrado na resposta!";
        assert response.getBody().contains(usuario.getEmail()) : "Erro: O email do usuário não foi encontrado na resposta!";
    }

    @When("eu envio uma requisição DELETE com usuário para {string}")
    public void enviarRequisicaoDelete(String endpoint) {
        sharedSteps.enviarRequisicaoDelete(endpoint, usuario.getId());
        response = sharedSteps.getResponse();

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Usuário excluído com sucesso!");
        } else {
            System.out.println("Erro ao excluir usuário: " + response.getStatusCode());
        }
    }

}
