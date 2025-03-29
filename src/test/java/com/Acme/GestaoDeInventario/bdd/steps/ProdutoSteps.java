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
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class ProdutoSteps {
    private final RestTemplate restTemplate = new RestTemplate();
    private ResponseEntity<String> response;
    private final String BASE_URL = "http://localhost:8080";

    private Produto produto;
    private Long produtoId;

    private SharedSteps sharedSteps = new SharedSteps();

    public ProdutoSteps(SharedSteps sharedSteps){
        this.sharedSteps = sharedSteps;
    }

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
        sharedSteps.enviarRequisicaoPost(endpoint, produto);

        response = sharedSteps.getResponse();

       if (response.getStatusCode() == HttpStatus.CREATED) {
            produtoId = Long.parseLong(JsonPath.read(response.getBody(), "$.id").toString());
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
        sharedSteps.enviarRequisicaoGet(endpoint, produtoId);

        response = sharedSteps.getResponse();
    }

    @Then("recebo o produto correspondente")
    public void validarProdutoCorrespondente() {
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().contains(produto.getNome());
    }

    @When("eu envio uma requisição DELETE para {string}")
    public void enviarRequisicaoDelete(String endpoint) {
            assert produtoId != null : "Erro: produtoId está nulo! O produto precisa ser criado antes de ser excluído.";
            sharedSteps.enviarRequisicaoDelete(endpoint, produtoId);
            response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @When("eu coloco um codigo inexistente")
    public void euColocoUmCodigoInexistente() {
        produtoId = 999999L; // Um ID que provavelmente não existe
    }
}
