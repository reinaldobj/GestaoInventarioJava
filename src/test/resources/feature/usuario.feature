Feature: Gerenciamento de Usuários
  Como um visitante ou usuário do sistema,
  Quero gerenciar meus dados (ou os dados de outros usuários, no caso de administradores),
  Para que eu possa realizar operações de cadastro, consulta, atualização e exclusão de usuários.

  Scenario: Cadastrar um novo usuário com sucesso
    Given que o usuário deseja se cadastrar com os seguintes dados:
      | nome          | email            | senha      |
      | João da Silva | joao@exemplo.com | Teste@2025 |
    When eu envio uma requisição POST com o usuário para "/usuarios"
    Then o sistema deve retornar um status 201
    And a resposta deve conter um id válido

  Scenario: Tentar cadastrar um usuário sem nome
    Given que o usuário deseja se cadastrar com os seguintes dados:
      | nome | email             | senha      |
      |      | maria@exemplo.com | Teste@2025 |
    When eu envio uma requisição POST com o usuário para "/usuarios"
    Then o sistema deve retornar um status 400
    And o sistema me retorna uma mensagem "O usuário deve ter um nome."

#  Scenario: Tentar cadastrar um usuário sem email
#    Given que o usuário deseja se cadastrar com os seguintes dados:
#      | nome         | email | endereco   | telefone  | tipoUsuario |
#      | Carlos Souza |       | Rua C, 789 | 777777777 | CLIENTE     |
#    When eu envio uma requisição POST com o usuário para "/usuarios"
#    Then o sistema deve retornar um status 400
#    And o sistema me retorna uma mensagem "O usuário deve ter um email."

  Scenario: Consultar os detalhes de um usuário existente
    Given que o usuário deseja se cadastrar com os seguintes dados:
      | nome          | email           | senha      |
      | Ana Oliveira  | ana@exemplo.com | Teste@2025 |
    When eu envio uma requisição POST com o usuário para "/usuarios"
    When eu envio uma requisição GET com o usuário para "/usuarios"
    Then o sistema deve retornar os detalhes do usuário
    And a resposta deve conter o nome "Ana Oliveira" e o email "ana@exemplo.com"

  Scenario: Atualizar os dados de um usuário existente
    Given que o usuário deseja se cadastrar com os seguintes dados:
      | nome        | email             | senha      |
      | Pedro Lima  | pedro@exemplo.com | Teste@2025 |
    When eu envio uma requisição POST com o usuário para "/usuarios"
    When eu envio uma requisição PUT para "/usuarios/" com os novos dados:
      | nome            | email                   | senha       |
      | Pedro L. Silva  | pedro.silva@exemplo.com | Teste@2025  |
    Then o sistema deve atualizar o usuário
    And a resposta deve conter o nome "Pedro L. Silva" e o email "pedro.silva@exemplo.com"

  Scenario: Excluir um usuário existente
    Given que o usuário deseja se cadastrar com os seguintes dados:
      | nome        | email             | senha      |
      | Maria Fernandes  | maria@exemplo.com | Teste@2025 |
    When eu envio uma requisição POST com o usuário para "/usuarios"
    When eu envio uma requisição DELETE com usuário para "/usuarios/"
    Then o sistema deve retornar um status 204
