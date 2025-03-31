package com.Acme.GestaoDeInventario.bdd.steps;

import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


public class SharedSteps {

    protected static final String BASE_URL = "http://localhost:8080/";
    protected final RestTemplate restTemplate;
    protected ResponseEntity<String> response;

    public SharedSteps() {
        this.restTemplate = new RestTemplate();
    }

    @When("eu envio uma requisição POST para {string} com payload")
    public void enviarRequisicaoPost(String endpoint, Object payload) {
        try {
            String url = BASE_URL + endpoint;
            response = restTemplate.postForEntity(url, payload, String.class);
        } catch (HttpClientErrorException ex) {
            response = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getStatusCode());
        }
    }

    @When("eu envio uma requisição GET para {string} com id {long}")
    public void enviarRequisicaoGet(String endpoint, Long id) {
        try {
            String url = BASE_URL + endpoint + "/" + id;
            response = restTemplate.getForEntity(url, String.class);
        } catch (HttpClientErrorException ex) {
            response = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getStatusCode());
        }
    }

    @When("eu envio uma requisição DELETE para {string} com id {long}")
    public void enviarRequisicaoDelete(String endpoint, Long id) {
        try {
            String url = BASE_URL + endpoint + "/" + id;
            restTemplate.delete(url);
            response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (HttpClientErrorException ex) {
            response = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getStatusCode());
        }
    }

    @When("eu envio uma requisição PUT para {string} com id {long}")
    public void enviarRequisicaoPut(String endpoint, Long id, Object payload) {
        try {
            String url = BASE_URL + endpoint + "/" + id;
            response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(payload), String.class);
        } catch (HttpClientErrorException ex) {
            response = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getStatusCode());
        }
    }

    @And("o sistema me retorna uma mensagem {string}")
    public void validarMensagem(String mensagem) {
        assert response.getBody() != null;
        String mensagemErro = JsonPath.read(response.getBody(), "$.message").toString();

        assert mensagemErro.equals(mensagem) : "Erro: A mensagem de erro não foi encontrada!";
    }

    @Then("o sistema deve retornar um status {int}")
    public void validarStatus(int statusCode) {
        assert response.getStatusCode().value() == statusCode;
    }

    public ResponseEntity<String> getResponse() {
        return response;
    }
}