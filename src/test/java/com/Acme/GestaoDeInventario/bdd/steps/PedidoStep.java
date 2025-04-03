package com.Acme.GestaoDeInventario.bdd.steps;

import com.Acme.GestaoDeInventario.dto.*;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

public class PedidoStep {
    private final SharedSteps sharedSteps;
    private ResponseEntity<String> response;

    private PedidoDTO pedido;
    private Long pedidoId;
    private double valorTotal;
    private String nomeCliente;

    @Autowired
    public PedidoStep(SharedSteps sharedSteps) {
        this.sharedSteps = sharedSteps;
    }

    @Given("que um cliente selecionou um ou mais produtos")
    public void criarPedidoValido() throws Exception {
        double valorProduto1 = 150.0;
        double valorProduto2 = 300.0;

        long idProduto1 = criarProduto("Cadeira Gamer", "Cadeira Gamer - Alta Performance", valorProduto1, 10);
        long idProduto2 = criarProduto("Mesa Gamer", "Mesa Gamer - Design Moderno", valorProduto2, 5);

        nomeCliente = "Cliente Teste";
        long idUsuario = criarUsuario(nomeCliente, "cliente@teste.com", "TESTE@2025");

        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setId(idUsuario);

        long idCliente = criarCliente(usuario, "Rua Teste, 123", "123456789", 123);

        ProdutoDTO produtoPedido1 = new ProdutoDTO();
        produtoPedido1.setId(idProduto1);

        ProdutoDTO produtoPedido2 = new ProdutoDTO();
        produtoPedido2.setId(idProduto2);

        ClienteDTO clientePedido = new ClienteDTO();
        clientePedido.setId(idCliente);

        PedidoProdutoDTO itemPedido1 = new PedidoProdutoDTO();
        itemPedido1.setProduto(produtoPedido1);
        itemPedido1.setQuantidade(2);

        PedidoProdutoDTO itemPedido2 = new PedidoProdutoDTO();
        itemPedido2.setProduto(produtoPedido2);
        itemPedido2.setQuantidade(1);

        double valorTotalItem1 = valorProduto1 * itemPedido1.getQuantidade();
        double valorTotalItem2 = valorProduto2 * itemPedido2.getQuantidade();

        valorTotal = valorTotalItem1 + valorTotalItem2;

        pedido = new PedidoDTO();
        pedido.setItens(List.of(itemPedido1, itemPedido2));
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
    public void validarStatus(String status) {
        String statusPedido = JsonPath.read(response.getBody(), "$.status").toString();

        assert statusPedido.equals(status);
    }

    @And("calcular o valor total do pedido")
    public void calcularValorTotal() {
        double valorTotal = Double.parseDouble(JsonPath.read(response.getBody(), "$.valorTotal").toString());

        assert valorTotal == this.valorTotal;
    }

    @Given("que um cliente tenta criar um pedido sem selecionar produtos")
    public void clienteTentaCriarPedidoSemSelecionarProdutos() throws Exception {
        long idUsuario = criarUsuario("Cliente Teste", "cliente@teste.com", "TESTE@2025");
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setId(idUsuario);

        long idCliente = criarCliente(usuario, "Rua Teste, 123", "123456789", 123);

        ClienteDTO clientePedido = new ClienteDTO();
        clientePedido.setId(idCliente);

        pedido = new PedidoDTO();
        pedido.setCliente(clientePedido);
    }

    @And("pelo menos um dos produtos não possui estoque suficiente")
    public void alterarQuantidadeDeItemDoPedido() {
        for (PedidoProdutoDTO item : pedido.getItens()) {
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
    public void acessarListaDePedidos() {
        sharedSteps.enviarRequisicaoGet("/pedidos/obter", pedidoId);
        response = sharedSteps.getResponse();
    }

    @Then("o sistema deve exibir os pedidos")
    public void validarListaDePedidos() {
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().contains(nomeCliente);
    }

    @Given("que um cliente tenta acessar um pedido que não existe")
    public void clienteTentaAcessarPedidoInexistente() {
        pedidoId = 999999L; // ID de pedido inexistente
    }

    @When("ele solicitar o cancelamento do pedido")
    public void solicitarCancelamentoPedido() {
        try {
            Object payload = null;

            sharedSteps.enviarRequisicaoPut("/pedidos/cancelar/", pedidoId, payload);
            response = sharedSteps.getResponse();
        } catch (HttpClientErrorException ex) {
            response = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getStatusCode());
        }
    }

    private long criarProduto(String nome, String descricao, double preco, int quantidade) {
        ProdutoDTO produto = new ProdutoDTO(nome, descricao, preco, quantidade);
        sharedSteps.enviarRequisicaoPost("/produtos", produto);
        response = sharedSteps.getResponse();

        if (response.getStatusCode() == HttpStatus.CREATED) {
            return Integer.parseInt(JsonPath.read(response.getBody(), "$.id").toString());
        }

        return -1;
    }

    private long criarUsuario(String nome, String email, String senha) throws Exception {
        UsuarioDTO usuario = new UsuarioDTO(nome, email, senha);
        sharedSteps.enviarRequisicaoPost("/usuarios", usuario);
        response = sharedSteps.getResponse();

        if (response.getStatusCode() == HttpStatus.CREATED) {
            return Integer.parseInt(JsonPath.read(response.getBody(), "$.id").toString());
        }

        return -1;
    }

    private long criarCliente(UsuarioDTO usuario, String endereco, String telefone, int numero) throws Exception {
        ClienteDTO cliente = new ClienteDTO(usuario, endereco, telefone, numero);
        sharedSteps.enviarRequisicaoPost("/clientes", usuario);
        response = sharedSteps.getResponse();

        if (response.getStatusCode() == HttpStatus.CREATED) {
            return Integer.parseInt(JsonPath.read(response.getBody(), "$.id").toString());
        }

        return -1;
    }
}
