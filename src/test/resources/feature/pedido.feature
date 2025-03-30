Feature: GerenciamentoDePedidos

  Scenario: Criar um pedido com produtos disponíveis
  Given que um cliente selecionou um ou mais produtos
  When eu envio uma requisição POST com o Pedido para "/pedidos"
  Then o sistema deve registrar o pedido com status "PENDENTE"
  And calcular o valor total do pedido

  Scenario: Criar um pedido sem produtos
  Given que um cliente tenta criar um pedido sem selecionar produtos
  When eu envio uma requisição POST com o Pedido para "/pedidos"
  Then o sistema me retorna uma mensagem "O pedido deve ter itens"

  Scenario: Criar um pedido com produto sem estoque suficiente
  Given que um cliente selecionou um ou mais produtos
  And pelo menos um dos produtos não possui estoque suficiente
  When eu envio uma requisição POST com o Pedido para "/pedidos"
  Then o sistema me retorna uma mensagem "Quantidade do produto não disponível"

  Scenario: Cliente visualiza um pedido
  Given que um cliente possui pedidos registrados no sistema
  When ele acessar a lista de pedidos
  Then o sistema deve exibir os pedidos

  Scenario: Cliente consulta um pedido inexistente
  Given que um cliente tenta acessar um pedido que não existe
  When ele acessar a lista de pedidos
  Then o sistema deve retornar um status 404

  Scenario: Cancelamento de um pedido pendente
  Given que um cliente possui pedidos registrados no sistema
  When ele solicitar o cancelamento do pedido
  Then o sistema deve retornar um status 200