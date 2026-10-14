# Cenários de autenticação e permissões - DoaSync

Relacionadas #S10 e #S11. Cobre RF-001, RF-002, RF-005 e RNF-003.

## Como preencher

- Uma linha por cenário. Um cenário é um teste que alguém vai fazer na API.
- Tipo é Sucesso ou Erro.
- Endpoint previsto, quando for deduzido, marque como "previsto". Só POST /api/v1/doacoes e GET /api/v1/usuarios aparecem na #S10.
- Resultado esperado sempre com o código HTTP.
- Se não souber o código ou o campo que a API vai usar, escreva "a confirmar" e anote a dúvida no fim do arquivo.

---

## RF-002 Autenticação

Critério de aceitação do documento. Credenciais válidas dão acesso à área correspondente ao perfil do usuário. Credenciais inválidas exibem mensagem de erro sem revelar qual campo está incorreto.

| ID | Requisito de origem | Endpoint previsto | Tipo | Resultado esperado com código HTTP |
| --- | --- | --- | --- | --- |
| CT-AUT-01 | RF-002 |  |  |  |
| CT-AUT-02 | RF-002 |  |  |  |
| CT-AUT-03 | RF-002 |  |  |  |
| CT-AUT-04 | RF-002 |  |  |  |

## RF-001 Cadastro de usuário

Critério de aceitação do documento. Após preencher os campos obrigatórios com dados válidos, a conta é criada e o usuário consegue se autenticar. E-mail já cadastrado é recusado com mensagem informativa.

| ID | Requisito de origem | Endpoint previsto | Tipo | Resultado esperado com código HTTP |
| --- | --- | --- | --- | --- |
| CT-AUT-05 | RF-001 | POST /api/v1/doadores | Sucesso | Cadastro de pessoa física com todos os dados válidos. Retorna 201 com a mensagem "Cadastro realizado com sucesso." e os dados do doador com status ATIVO, sem a senha |
| CT-AUT-06 | RF-001 | POST /api/v1/doadores | Sucesso | Cadastro de pessoa jurídica com todos os dados válidos. Retorna 201 com a mensagem "Cadastro realizado com sucesso." e os dados do doador com CNPJ e razão social, sem a senha |
| CT-AUT-07 | RF-001 | POST /api/v1/doadores | Sucesso | Cadastro com e-mail em maiúsculas e com espaços. Retorna 201 e o e-mail volta em minúsculas e sem espaços |
| CT-AUT-08 | RF-001 | POST /api/v1/doadores | Sucesso | Cadastro com CPF enviado sem máscara. Retorna 201 e o CPF volta com máscara |
| CT-AUT-09 | RF-001 | POST /api/v1/doadores e depois POST /api/v1/auth/login | Sucesso | Cadastro válido seguido de login com o mesmo e-mail e a mesma senha. O cadastro retorna 201 e o login retorna 200 com token e dados do usuário |
| CT-AUT-10 | RF-001 | POST /api/v1/doadores | Erro | Cadastro com e-mail que já existe. Retorna 409 com a mensagem "E-mail já cadastrado." |
| CT-AUT-11 | RF-001 | POST /api/v1/doadores | Erro | Cadastro com e-mail que já existe, escrito com letras maiúsculas. Retorna 409 com a mensagem "E-mail já cadastrado." |
| CT-AUT-12 | RF-001 | POST /api/v1/doadores | Erro | Cadastro de pessoa física com CPF que já existe. Retorna 409 com a mensagem "CPF já cadastrado." |
| CT-AUT-13 | RF-001 | POST /api/v1/doadores | Erro | Cadastro de pessoa jurídica com CNPJ que já existe. Retorna 409 (a confirmar, o documento mostra só e-mail e CPF) |
| CT-AUT-14 | RF-001 | POST /api/v1/doadores | Erro | Cadastro com tipo diferente de PESSOA_FISICA ou PESSOA_JURIDICA. Retorna 422 com a mensagem "Tipo deve ser PESSOA_FISICA ou PESSOA_JURIDICA." |
| CT-AUT-15 | RF-001 | POST /api/v1/doadores | Erro | Cadastro com nome vazio. Retorna 422 com a mensagem "Nome é obrigatório." |
| CT-AUT-16 | RF-001 | POST /api/v1/doadores | Erro | Cadastro com e-mail em formato inválido, por exemplo sem arroba. Retorna 422 com a mensagem "E-mail inválido." |
| CT-AUT-17 | RF-001 | POST /api/v1/doadores | Erro | Cadastro com senha de menos de 6 caracteres. Retorna 422 com a mensagem "Senha deve ter ao menos 6 caracteres." |
| CT-AUT-18 | RF-001 | POST /api/v1/doadores | Erro | Cadastro de pessoa física com CPF inválido. Retorna 422 com a mensagem "CPF inválido." |
| CT-AUT-19 | RF-001 | POST /api/v1/doadores | Erro | Cadastro de pessoa jurídica sem CNPJ. Retorna 422 com a mensagem "CNPJ é obrigatório." |
| CT-AUT-20 | RF-001 | POST /api/v1/doadores | Erro | Cadastro de pessoa jurídica sem razão social. Retorna 422 com a mensagem "Razão Social é obrigatória." |


## RF-005 Tipos de usuário e permissões

Critério de aceitação do documento. Um doador não consegue acessar telas ou operações de gestão. Um gestor só gerencia dados da própria entidade.

| ID | Requisito de origem | Endpoint previsto | Tipo | Resultado esperado com código HTTP |
| --- | --- | --- | --- | --- |
| CT-PER-01 | RF-005 |  |  |  |
| CT-PER-02 | RF-005 |  |  |  |
| CT-PER-03 | RF-005 |  |  |  |
| CT-PER-04 | RF-005 |  |  |  |

## RNF-003 Autorização no servidor

Critério de aceitação do documento. Chamadas diretas à API sem permissão retornam erro de autorização (HTTP 401 ou 403) em testes de API.

| ID | Requisito de origem | Endpoint previsto | Tipo | Resultado esperado com código HTTP |
| --- | --- | --- | --- | --- |
| CT-PER-05 | RNF-003 |  |  |  |
| CT-PER-06 | RNF-003 |  |  |  |
| CT-PER-07 | RNF-003 |  |  |  |
| CT-PER-08 | RNF-003 |  |  |  |

---

## Dúvidas para o Backend deste bloco

1. RF001 -> CT-AUT-07 | Confirmar se é obrigatório que o cliente informe o documento para poder criar a conta
2. 
3.