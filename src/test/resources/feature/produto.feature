Feature: Gerenciamento de Produtos

  Scenario: Criar um produto com sucesso
    Given que o usuário deseja cadastrar um novo produto com os dados:
      | nome          | descricao       | preco | quantidade |
      | Produto Teste | Descrição Teste | 99.99 | 10         |
    When eu envio uma requisição POST para "/produtos"
    Then o sistema deve retornar um status 201
    And o produto é criado com sucesso

  Scenario: Buscar um produto existente
    Given que o usuário deseja cadastrar um novo produto com os dados:
      | nome         | descricao       | preco | quantidade |
      | Teste Buscar | Descrição Teste | 99.99 | 10         |
    When eu envio uma requisição POST para "/produtos"
    Then o sistema deve retornar um status 201

    Given que o produto já foi cadastrado
    When eu envio uma requisição GET para "/produtos/"
    Then o sistema deve retornar um status 200
    And recebo o produto correspondente

  Scenario: Deletar um produto existente
    Given que o usuário deseja cadastrar um novo produto com os dados:
      | nome             | descricao       | preco | quantidade |
      | Teste  Cadastrar | Descrição Teste | 99.99 | 10         |
    When eu envio uma requisição POST para "/produtos"
    Then o sistema deve retornar um status 201

    Given que o produto já foi cadastrado
    When eu envio uma requisição DELETE para "/produtos"
    Then o sistema deve retornar um status 204

  Scenario: Criar um produto sem nome
    Given que o usuário deseja cadastrar um novo produto com os dados:
      | nome | descricao       | preco | quantidade |
      |      | Descrição Teste | 99.99 | 10         |
    When eu envio uma requisição POST para "/produtos"
    Then o sistema deve retornar um status 400
    And o sistema me retorna uma mensagem "O produto deve ter um nome"

  Scenario: Criar um produto sem descrição
    Given que o usuário deseja cadastrar um novo produto com os dados:
      | nome          | descricao | preco | quantidade |
      | Produto Teste |           | 99.99 | 10         |
    When eu envio uma requisição POST para "/produtos"
    Then o sistema deve retornar um status 400
    And o sistema me retorna uma mensagem "O produto deve ter uma descrição"

  Scenario: Criar um produto sem preço
    Given que o usuário deseja cadastrar um novo produto com os dados:
      | nome          | descricao       | preco | quantidade |
      | Produto Teste | Descrição Teste | -1.00 | 10         |
    When eu envio uma requisição POST para "/produtos"
    Then o sistema deve retornar um status 400
    And o sistema me retorna uma mensagem "O preço deve ser maior ou igual a zero"

  Scenario: Criar um produto com quantidade negativa
    Given que o usuário deseja cadastrar um novo produto com os dados:
      | nome          | descricao       | preco | quantidade |
      | Produto Teste | Descrição Teste | 99.99 | -10        |
    When eu envio uma requisição POST para "/produtos"
    Then o sistema deve retornar um status 400
    And o sistema me retorna uma mensagem "A quantidade deve ser maior ou igual a zero"

  Scenario: Buscar um produto inexistente
    When eu coloco um codigo inexistente
    When eu envio uma requisição GET para "/produtos/"
    Then o sistema deve retornar um status 404
    And o sistema me retorna uma mensagem "Produto não encontrado"

  Scenario: Deletar um produto inexistente
    When eu coloco um codigo inexistente
    When eu envio uma requisição DELETE para "/produtos/"
    Then o sistema deve retornar um status 404
    And o sistema me retorna uma mensagem "Produto não encontrado"