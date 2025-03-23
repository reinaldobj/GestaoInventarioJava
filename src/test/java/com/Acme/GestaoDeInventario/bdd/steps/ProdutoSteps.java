package com.Acme.GestaoDeInventario.bdd.steps;

import com.Acme.GestaoDeInventario.model.Produto;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class ProdutoSteps {
    private final RestTemplate restTemplate = new RestTemplate();
    private ResponseEntity<String> response;
    private final String BASE_URL = "http://localhost:8080";

    private Map<String, Object> produtoRequest;
    private Produto produto;
    private Long produtoId;

    @Given("que o usuário deseja cadastrar um novo produto com os dados:")
    public void criarProdutoValido(io.cucumber.datatable.DataTable table){
        List<Map<String, String>> data = table.asMaps();
        Map<String, String> row = data.get(0); // Pega a primeira linha da tabela

        produto = new Produto();
        produto.setNome(row.get("nome"));
        produto.setDescricao(row.get("descricao"));
        produto.setPreco(Double.parseDouble(row.get("preco")));
        produto.setQuantidade(Integer.parseInt(row.get("quantidade")));
    }

    @When("eu envio uma requisição POST para {string}")
    public void enviarRequisicaoPost(String endpoint) {
        try {
            String url = BASE_URL + endpoint;

            response = restTemplate.postForEntity(url, produto, String.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                produtoId = Long.parseLong(JsonPath.read(response.getBody(), "$.id").toString());
            }
        }
        catch (HttpClientErrorException ex){
            response = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getStatusCode());
        }
    }

    @And("o produto é criado com sucesso")
    public void validarCriacaoProduto() {
        assert response.getStatusCode() == HttpStatus.CREATED;
        assert response.getBody() != null;
        assert response.getBody().contains(produto.getNome());
        assert produtoId != null : "Erro: O ID do produto não foi armazenado!";
        assert produtoId > 0;
    }

    @Then("a resposta deve ter o status {int}")
    public void validarStatus(int status) {
        assert response.getStatusCode().value() == status;
    }

    @And("recebo uma mensagem de erro {string}")
    public void validarMensagemErro(String mensagem) {
        assert response.getBody() != null;
        assert response.getBody().contains(mensagem);
    }

    @Given("que o produto já foi cadastrado")
    public void produtoExistente() {

    }

    @When("eu envio uma requisição GET para {string}")
    public void enviarRequisicaoGet(String endpoint) {
        try {
            assert produtoId != null : "Erro: produtoId está nulo! O produto precisa ser criado antes de ser consultado.";
            String url = BASE_URL + endpoint + "/" + produtoId;

            response = restTemplate.getForEntity(url, String.class);
        }
        catch (HttpClientErrorException ex){
            response = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getStatusCode());
        }
    }


    @Then("recebo o produto correspondente")
    public void validarProdutoCorrespondente() {
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().contains(produto.getNome());
    }

    @When("eu envio uma requisição DELETE para {string}")
    public void enviarRequisicaoDelete(String endpoint) {
        try {
            assert produtoId != null : "Erro: produtoId está nulo! O produto precisa ser criado antes de ser excluído.";
            String url = BASE_URL + endpoint + "/" + produtoId;

            restTemplate.delete(url);
            response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (HttpClientErrorException ex){
            response = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getStatusCode());
        }
    }

    @When("eu coloco um codigo inexistente")
    public void euColocoUmCodigoInexistente() {
        produtoId = 999999L; // Um ID que provavelmente não existe
    }
}
