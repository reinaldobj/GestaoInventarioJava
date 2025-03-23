Feature: Gerenciamento de Produtos

  Scenario: Criar um produto com sucesso
    Given que o usuário deseja cadastrar um novo produto com os dados:
    | nome          | descricao          | preco | quantidade |
    | Produto Teste | Descrição Teste    | 99.99 | 10        |
    When eu envio uma requisição POST para "/produtos"
    Then a resposta deve ter o status 201
    And o produto é criado com sucesso


  Scenario: Criar um produto sem nome
    Given que o usuário deseja cadastrar um novo produto com os dados:
    | nome          | descricao          | preco | quantidade |
    |               | Descrição Teste    | 99.99 | 10        |
    When eu envio uma requisição POST para "/produtos"
    Then a resposta deve ter o status 400
    And recebo uma mensagem de erro "O produto deve ter  um nome"

  Scenario: Criar um produto sem descrição
    Given que o usuário deseja cadastrar um novo produto com os dados:
      | nome          | descricao          | preco | quantidade |
      | Produto Teste |                    | 99.99 | 10        |
    When eu envio uma requisição POST para "/produtos"
    Then a resposta deve ter o status 400
    And recebo uma mensagem de erro "O produto deve ter uma descrição"

  Scenario: Criar um produto sem preço
    Given que o usuário deseja cadastrar um novo produto com os dados:
      | nome          | descricao          | preco | quantidade |
      | Produto Teste | Descrição Teste    | -1.00 | 10        |
    When eu envio uma requisição POST para "/produtos"
    Then a resposta deve ter o status 400
    And recebo uma mensagem de erro "O preço deve ser maior ou igual a zero"

  Scenario: Criar um produto com quantidade negativa
    Given que o usuário deseja cadastrar um novo produto com os dados:
      | nome          | descricao          | preco | quantidade |
      | Produto Teste | Descrição Teste    | 99.99 | -10        |
    When eu envio uma requisição POST para "/produtos"
    Then a resposta deve ter o status 400
    And recebo uma mensagem de erro "A quantidade deve ser maior ou igual a zero"

  Scenario: Buscar um produto existente
    Given que existe um produto cadastrado com ID 1
    When eu envio uma requisição GET para "/produtos/1"
    Then a resposta deve ter o status 200
    And recebo o produto correspondente

  Scenario: Buscar um produto inexistente
    When eu envio uma requisição GET para "/produtos/999"
    Then a resposta deve ter o status 404
    And recebo uma mensagem de erro "Produto não encontrado"

  Scenario: Deletar um produto existente
    Given que existe um produto cadastrado com ID 1
    When eu envio uma requisição DELETE para "/produtos/1"
    Then a resposta deve ter o status 204

  Scenario: Deletar um produto inexistente
    When eu envio uma requisição DELETE para "/produtos/999"
    Then a resposta deve ter o status 404
    And recebo uma mensagem de erro "Produto não encontrado"
