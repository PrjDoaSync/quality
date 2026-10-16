# Cenários de autenticação e permissões - DoaSync

Relacionadas #S10 e #S11.

Este documento reúne os cenários de teste de API dos requisitos de cadastro, login, tipos de usuário e autorização. Cada cenário descreve a chamada, a situação testada e o resultado esperado com o código HTTP. Serve de base para os testes de API das issues acima.

Os critérios de aceitação vêm do `requisitos.md`. Rotas, campos, códigos e mensagens vêm do `rotas-api.md`.

## Escopo

| Requisito | Descrição | Cenários |
| --- | --- | --- |
| RF-001 | Cadastro de usuário | CT-AUT-01 a CT-AUT-16 |
| RF-002 | Autenticação | CT-AUT-17 a CT-AUT-26 |
| RF-005 | Tipos de usuário e permissões | CT-PER-01 a CT-PER-06 |
| RNF-003 | Autorização no servidor | CT-PER-07 a CT-PER-10 |

## Convenções

| Código | Uso nestes cenários |
| --- | --- |
| 200 | Consulta ou operação realizada com sucesso |
| 201 | Recurso criado |
| 401 | Requisição sem identidade válida, por falta de token ou credenciais inválidas |
| 403 | Usuário autenticado, mas sem permissão para a operação |
| 409 | Dado único já cadastrado |
| 422 | Dado enviado inválido ou ausente |

- Tipo indica se o cenário é de Sucesso ou de Erro.
- As mensagens entre aspas são as definidas no `rotas-api.md`.
- A marcação "a confirmar" indica informação que o `rotas-api.md` não define. Cada uma tem um item na seção Dúvidas para o Backend.

## Massa de teste necessária

| Perfil | Usado em |
| --- | --- |
| Doador pessoa física e pessoa jurídica | CT-AUT-01 a CT-AUT-05, CT-AUT-18, CT-PER-01 a CT-PER-06 |
| Gestor com entidade vinculada | CT-AUT-19, CT-PER-10 |
| Dois gestores, cada um com a sua entidade | CT-PER-09 |
| Administrador | CT-AUT-20 |
| Usuário inativo | CT-AUT-26 |

---

## RF-001 Cadastro de usuário

Critério de aceitação. Após preencher os campos obrigatórios com dados válidos, a conta é criada e o usuário consegue se autenticar. E-mail já cadastrado é recusado com mensagem informativa.

Rota principal. POST /api/v1/doadores (acesso público).

| ID | Requisito de origem | Endpoint previsto | Tipo | Resultado esperado com código HTTP |
| --- | --- | --- | --- | --- |
| CT-AUT-01 | RF-001 | POST /api/v1/doadores | Sucesso | Cadastro de pessoa física com todos os dados válidos. Retorna 201 com a mensagem "Cadastro realizado com sucesso." e os dados do doador com status ATIVO, sem a senha |
| CT-AUT-02 | RF-001 | POST /api/v1/doadores | Sucesso | Cadastro de pessoa jurídica com todos os dados válidos. Retorna 201 com a mensagem "Cadastro realizado com sucesso." e os dados do doador com CNPJ e razão social, sem a senha |
| CT-AUT-03 | RF-001 | POST /api/v1/doadores | Sucesso | Cadastro com e-mail em maiúsculas e com espaços. Retorna 201 e o e-mail volta em minúsculas e sem espaços |
| CT-AUT-04 | RF-001 | POST /api/v1/doadores | Sucesso | Cadastro com CPF enviado sem máscara. Retorna 201 e o CPF volta com máscara |
| CT-AUT-05 | RF-001 | POST /api/v1/doadores e depois POST /api/v1/auth/login | Sucesso | Cadastro válido seguido de login com o mesmo e-mail e a mesma senha. O cadastro retorna 201 e o login retorna 200 com token e dados do usuário |
| CT-AUT-06 | RF-001 | POST /api/v1/doadores | Erro | Cadastro com e-mail que já existe. Retorna 409 com a mensagem "E-mail já cadastrado." |
| CT-AUT-07 | RF-001 | POST /api/v1/doadores | Erro | Cadastro com e-mail que já existe, escrito com letras maiúsculas. Retorna 409 com a mensagem "E-mail já cadastrado." |
| CT-AUT-08 | RF-001 | POST /api/v1/doadores | Erro | Cadastro de pessoa física com CPF que já existe. Retorna 409 com a mensagem "CPF já cadastrado." |
| CT-AUT-09 | RF-001 | POST /api/v1/doadores | Erro | Cadastro de pessoa jurídica com CNPJ que já existe. Retorna 409 (a confirmar, o documento mostra só e-mail e CPF) |
| CT-AUT-10 | RF-001 | POST /api/v1/doadores | Erro | Cadastro com tipo diferente de PESSOA_FISICA ou PESSOA_JURIDICA. Retorna 422 com a mensagem "Tipo deve ser PESSOA_FISICA ou PESSOA_JURIDICA." |
| CT-AUT-11 | RF-001 | POST /api/v1/doadores | Erro | Cadastro com nome vazio. Retorna 422 com a mensagem "Nome é obrigatório." |
| CT-AUT-12 | RF-001 | POST /api/v1/doadores | Erro | Cadastro com e-mail em formato inválido, por exemplo sem arroba. Retorna 422 com a mensagem "E-mail inválido." |
| CT-AUT-13 | RF-001 | POST /api/v1/doadores | Erro | Cadastro com senha de menos de 6 caracteres. Retorna 422 com a mensagem "Senha deve ter ao menos 6 caracteres." |
| CT-AUT-14 | RF-001 | POST /api/v1/doadores | Erro | Cadastro de pessoa física com CPF inválido. Retorna 422 com a mensagem "CPF inválido." |
| CT-AUT-15 | RF-001 | POST /api/v1/doadores | Erro | Cadastro de pessoa jurídica sem CNPJ. Retorna 422 com a mensagem "CNPJ é obrigatório." |
| CT-AUT-16 | RF-001 | POST /api/v1/doadores | Erro | Cadastro de pessoa jurídica sem razão social. Retorna 422 com a mensagem "Razão Social é obrigatória." |

## RF-002 Autenticação

Critério de aceitação. Credenciais válidas dão acesso à área correspondente ao perfil do usuário. Credenciais inválidas exibem mensagem de erro sem revelar qual campo está incorreto.

Rota principal. POST /api/v1/auth/login (acesso público).

| ID | Requisito de origem | Endpoint previsto | Tipo | Resultado esperado com código HTTP |
| --- | --- | --- | --- | --- |
| CT-AUT-17 | RF-002 | POST /api/v1/auth/login | Sucesso | Login com credenciais válidas não expõe a senha do usuário. Retorna 200 com token e dados do usuário, sem o campo de senha |
| CT-AUT-18 | RF-002 | POST /api/v1/auth/login | Sucesso | Login de doador com credenciais válidas. Retorna 200 com token e o usuário com o tipo do doador |
| CT-AUT-19 | RF-002 | POST /api/v1/auth/login | Sucesso | Login de gestor de entidade com credenciais válidas. Retorna 200 com token e o usuário com entidadeId e statusEntidade preenchidos |
| CT-AUT-20 | RF-002 | POST /api/v1/auth/login | Sucesso | Login de administrador com credenciais válidas. Retorna 200 com token e o usuário com o tipo de administrador (valor do tipo a confirmar) |
| CT-AUT-21 | RF-002 | POST /api/v1/auth/login | Erro | E-mail inexistente, não autentica. Retorna 401 com a mensagem "E-mail ou senha inválidos." |
| CT-AUT-22 | RF-002 | POST /api/v1/auth/login | Erro | Senha errada, não autentica. Retorna 401 com a mensagem "E-mail ou senha inválidos." |
| CT-AUT-23 | RF-002 | POST /api/v1/auth/login | Erro | Login sem e-mail. Retorna 422 com a mensagem "E-mail é obrigatório." |
| CT-AUT-24 | RF-002 | POST /api/v1/auth/login | Erro | Login sem senha. Retorna 422 com a mensagem "Senha é obrigatória." |
| CT-AUT-25 | RF-002 | POST /api/v1/auth/login | Erro | Login sem e-mail e sem senha. Retorna 422 com as mensagens "E-mail é obrigatório." e "Senha é obrigatória." (a confirmar se vêm juntas) |
| CT-AUT-26 | RF-002 | POST /api/v1/auth/login | Erro | Login de usuário inativo. Retorna 401 com a mensagem "E-mail ou senha inválidos." |

## RF-005 Tipos de usuário e permissões

Critério de aceitação. Um doador não consegue acessar telas ou operações de gestão. Um gestor só gerencia dados da própria entidade.

Rotas cobertas. GET e PATCH /api/v1/usuarios/me, marcadas como RF05 no `rotas-api.md`. O cenário de bloqueio usa uma rota de outro requisito apenas como alvo do teste. A cobertura funcional dessa rota fica nos cenários do requisito dela.

| ID | Requisito de origem | Endpoint previsto | Tipo | Resultado esperado com código HTTP |
| --- | --- | --- | --- | --- |
| CT-PER-01 | RF-005 | GET /api/v1/usuarios/me | Sucesso | Perfil do usuário autenticado. Retorna 200 com o objeto data do cadastro do doador |
| CT-PER-02 | RF-005 | POST /api/v1/campanhas | Erro | Doador autenticado tenta criar uma campanha, que é uma operação de gestão. Retorna 403 (corpo da resposta a confirmar) |
| CT-PER-03 | RF-005 | PATCH /api/v1/usuarios/me | Sucesso | Perfil do usuário é atualizado. Retorna 200 com a mensagem "Perfil atualizado com sucesso." e o objeto data com os dados atualizados |
| CT-PER-04 | RF-005 | PATCH /api/v1/usuarios/me | Erro | Atualização do perfil com o nome vazio. Retorna 422 com a mensagem "Nome não pode ser vazio." |
| CT-PER-05 | RF-005 | PATCH /api/v1/usuarios/me | Erro | Atualização da senha com menos de 6 caracteres. Retorna 422 com a mensagem "Senha deve ter ao menos 6 caracteres." |
| CT-PER-06 | RF-005 | PATCH /api/v1/usuarios/me | Erro | Atualização da senha informando a senha atual errada. Retorna 422 com a mensagem "Senha atual incorreta." |

## RNF-003 Autorização no servidor

Critério de aceitação. Chamadas diretas à API sem permissão retornam erro de autorização (HTTP 401 ou 403) em testes de API.

Regra de leitura dos códigos. 401 quando não há identidade válida. 403 quando há identidade válida, mas o perfil não tem permissão.

| ID | Requisito de origem | Endpoint previsto | Tipo | Resultado esperado com código HTTP |
| --- | --- | --- | --- | --- |
| CT-PER-07 | RNF-003 | GET /api/v1/usuarios/me | Erro | Consulta do próprio perfil sem estar autenticado, sem enviar token. Retorna 401 (corpo da resposta a confirmar, o documento só define o código) |
| CT-PER-08 | RNF-003 | GET /api/v1/usuarios/me | Erro | Consulta do próprio perfil com token inválido ou adulterado. Retorna 401 (a confirmar, o documento só define 401 para a falta de token) |
| CT-PER-09 | RF-005, RNF-003 | PATCH /api/v1/entidades/{id} | Erro | Gestor autenticado tenta editar os dados de uma entidade que não é a dele. Retorna 403 (a confirmar se é 403 ou 404) |
| CT-PER-10 | RF-005, RNF-003 | PATCH /api/v1/entidades/{id}/status | Erro | Gestor autenticado tenta alterar o status da própria entidade, operação restrita ao administrador. Retorna 403 (corpo da resposta a confirmar) |

---

## Dúvidas para o Backend

| # | Requisito | Cenário | Dúvida |
| --- | --- | --- | --- |
| 1 | RF-001 | CT-AUT-09 | Confirmar se CNPJ repetido também retorna 409, pois o documento mostra só e-mail e CPF |
| 2 | RF-001 | CT-AUT-14 | Confirmar se CPF é obrigatório para pessoa física, pois o documento mostra só a mensagem de CPF inválido |
| 3 | RF-002 | CT-AUT-19 | Confirmar se gestor com entidade PENDENTE, REPROVADA ou INATIVA consegue fazer login |
| 4 | RF-002 | CT-AUT-20 | Confirmar qual é o valor do campo tipo para o administrador |
| 5 | RF-002 | CT-AUT-25 | Confirmar se, com e-mail e senha vazios, as duas mensagens vêm juntas na mesma resposta |
| 6 | RNF-003 | CT-PER-07 e CT-PER-08 | Confirmar qual é o corpo da resposta 401 sem token e com token inválido, pois o documento só cita o código |
| 7 | RF-005 | CT-PER-02, CT-PER-09 e CT-PER-10 | Confirmar qual é o corpo da resposta 403, pois o documento não define |
| 8 | RF-005 | CT-PER-09 | Confirmar se o gestor que edita a entidade de outro recebe 403 ou 404 |
| 9 | Geral | Todos | Confirmar a numeração dos requisitos, pois o `rotas-api.md` cita o login como RF03 e RF06 e o `requisitos.md` cita RF-002 |