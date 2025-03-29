package com.Acme.GestaoDeInventario.bdd.steps;

import com.Acme.GestaoDeInventario.model.*;
import com.Acme.GestaoDeInventario.utils.TestHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class PedidoStep {
    private final RestTemplate restTemplate = new RestTemplate();
    private final SharedSteps sharedSteps = new SharedSteps();
    private final String BASE_URL = "http://localhost:8080";
    private ResponseEntity<String> response;

    private Pedido pedido;
    private Long pedidoId;
    private double valorTotal;
    private String nomeCliente;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    public PedidoStep() {
    }

    @PostConstruct
    public void init() {
    }

    @Given("que um cliente selecionou um ou mais produtos")
    public void criarPedidoValido() throws Exception {
        long idProduto1 = criarProduto("Cadeira Gamer", "Cadeira Gamer - Alta Performance", 150.0, 10);
        long idProduto2 = criarProduto("Mesa Gamer", "Mesa Gamer - Design Moderno", 300.0, 5);

        nomeCliente = "Cliente Teste";
        long idCliente = criarUsuario(nomeCliente, "cliente@teste.com", "Rua Teste, 123", "123456789");

        Produto produtoPedido1 = new Produto();
        produtoPedido1.setId(idProduto1);

        Produto produtoPedido2 = new Produto();
        produtoPedido2.setId(idProduto2);

        Usuario clientePedido = new Usuario();
        clientePedido.setId(idCliente);

        PedidoProduto itemPedido1 = new PedidoProduto();
        itemPedido1.setProduto(produtoPedido1);
        itemPedido1.setQuantidade(2);

        PedidoProduto itemPedido2 = new PedidoProduto();
        itemPedido2.setProduto(produtoPedido2);
        itemPedido2.setQuantidade(1);

        double valorTotalItem1 = itemPedido1.getProduto().getPreco() * itemPedido1.getQuantidade();
        double valorTotalItem2 = itemPedido2.getProduto().getPreco() * itemPedido2.getQuantidade();

        valorTotal = valorTotalItem1 + valorTotalItem2;

        pedido = new Pedido();
        pedido.setItens(List.of(itemPedido1,itemPedido2));
        pedido.setCliente(clientePedido);
    }

    @When("eu envio uma requisição POST com o Pedido para {string}")
    public void enviarRequisicaoPost(String endpoint) throws Exception {
        sharedSteps.enviarRequisicaoPost(endpoint, pedido);
        response = sharedSteps.getResponse();

        if (response.getStatusCode() == HttpStatus.CREATED) {
            pedidoId = Long.parseLong(JsonPath.read(response.getBody(), "$.id").toString());
        }
    }

    @Then("o sistema deve registrar o pedido com status {string}")
    public void validarStatus(String status) throws Exception {
        String statusPedido = JsonPath.read(response.getBody(), "$.status").toString();

        assert statusPedido.equals("PENDENTE");
    }

    @And("calcular o valor total do pedido")
    public void calcularValorTotal() throws Exception {
        double valorTotal = Double.parseDouble(JsonPath.read(response.getBody(), "$.valorTotal").toString());

        assert valorTotal == this.valorTotal;
    }

    @Given("que um cliente tenta criar um pedido sem selecionar produtos")
    public void clienteTentaCriarPedidoSemSelecionarProdutos() throws Exception {
        long idCliente = criarUsuario("Cliente Teste", "cliente@teste.com", "Rua Teste, 123", "123456789");

        Usuario clientePedido = new Usuario();
        clientePedido.setId(idCliente);

        pedido = new Pedido();
        pedido.setCliente(clientePedido);
    }

    @Then("o sistema deve exibir uma mensagem de erro {string}")
    public void validarMensagemErro(String mensagem) throws Exception {
        String mensagemErro = JsonPath.read(response.getBody(), "$.message").toString();

        assert mensagemErro.equals(mensagem);
    }

    @And("pelo menos um dos produtos não possui estoque suficiente")
    public void alterarQuantidadeDeItemDoPedido(){
        for (PedidoProduto item : pedido.getItens()) {
            if (item.getProduto().getQuantidade() < item.getQuantidade()) {
                item.setQuantidade(item.getProduto().getQuantidade() + 99999);
            }
        }
    }

    @Given("que um cliente possui pedidos registrados no sistema")
    public void criarPedido() throws Exception {
        criarPedidoValido();
        enviarRequisicaoPost("/pedidos");

        if (response.getStatusCode() == HttpStatus.CREATED) {
            pedidoId = Long.parseLong(JsonPath.read(response.getBody(), "$.id").toString());
        }
    }

    @When("ele acessar a lista de pedidos")
    public void acessarListaDePedidos() throws Exception {
        sharedSteps.enviarRequisicaoGet("/pedidos/obter", pedidoId);
        response = sharedSteps.getResponse();
    }

    @Then("o sistema deve exibir os pedidos")
    public void validarListaDePedidos() throws Exception {
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().contains(nomeCliente);
    }

    @Given("que um cliente tenta acessar um pedido que não existe")
    public void clienteTentaAcessarPedidoInexistente() throws Exception {
        pedidoId = 999999L; // ID de pedido inexistente
    }

    @Then("o sistema deve retornar um status {int}")
    public void validarErro(int statusCode) throws Exception {
        assert response.getStatusCode().value() == statusCode;
    }

    @When("ele solicitar o cancelamento do pedido")
    public void solicitarCancelamentoPedido() throws Exception {
        try {
            Object payload = null;

            String url = BASE_URL + "/pedidos/cancelar/" + pedidoId;
            response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(payload), String.class);
        }
        catch (HttpClientErrorException ex) {
            response = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getStatusCode());
        }
    }

    private long criarProduto(String nome, String descricao, double preco, int quantidade) throws Exception {
        Produto produto = new Produto(nome, descricao, preco, quantidade);
        sharedSteps.enviarRequisicaoPost("/produtos", produto);
        response = sharedSteps.getResponse();

        if (response.getStatusCode() == HttpStatus.CREATED) {
            return Integer.parseInt(JsonPath.read(response.getBody(), "$.id").toString());
        }

        return -1;
    }

    private long criarUsuario(String nome, String email, String endereco, String telefone) throws Exception {
        Usuario usuario = new Usuario(nome, email, endereco, telefone, TipoUsuario.CLIENTE);
        sharedSteps.enviarRequisicaoPost("/usuarios", usuario);
        response = sharedSteps.getResponse();

        if (response.getStatusCode() == HttpStatus.CREATED) {
            return Integer.parseInt(JsonPath.read(response.getBody(), "$.id").toString());
        }

        return -1;
    }
}
