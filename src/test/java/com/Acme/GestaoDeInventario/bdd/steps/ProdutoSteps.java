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
        }
        catch (HttpClientErrorException ex){
            response = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getStatusCode());
        }
    }

    @And("o produto é criado com sucesso")
    public void validarCriacaoProduto(String titulo ) {
        assert response.getStatusCode() == HttpStatus.CREATED;
        assert response.getBody() != null;
        assert response.getBody().contains(produto.getNome());

        int id = JsonPath.read(response.getBody(), "$.id");
        assert id > 0;
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

//    @Then("recebo o produto correspondente")
//    public void validarProdutoCorrespondente() {
//        assert response.getStatusCode() == HttpStatus.OK;
//        assert response.getBody() != null;
//        assert response.getBody().contains("Produto Teste");
//    }
//
//    @And("o corpo da resposta deve conter uma mensagem de erro")
//    public void validarCorpoErro() {
//        assert response.getBody() != null;
//        assert response.getBody().contains("O produto deve ter  um nome") ||
//                response.getBody().contains("O produto deve ter uma descrição") ||
//                response.getBody().contains("A quantidade deve ser maior ou igual a zero") ||
//                response.getBody().contains("O preço deve ser maior ou igual a zero");
//    }
}
