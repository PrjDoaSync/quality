# Cenários de teste de API - DoaSync

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
| RF-010 | Criação de campanha | CT-CAM-01 a CT-CAM-19 |
| RF-011 | Edição de campanha | CT-CAM-20 a CT-CAM-34 |
| RF-012 | Publicação de campanha | CT-CAM-35 a CT-CAM-42 |
| RF-013 | Encerramento de campanha | CT-CAM-43 a CT-CAM-63 |
| RF-014 | Listagem de campanhas disponíveis | CT-CAM-64 a CT-CAM-74 |
| RF-015 | Detalhes da campanha | CT-CAM-75 a CT-CAM-87 |
| RF-016 | Busca de campanhas | CT-CAM-88 a CT-CAM-95 |
| RF-017 | Filtros de campanhas | CT-CAM-96 a CT-CAM-109 |

## Convenções

| Código | Uso nestes cenários |
| --- | --- |
| 200 | Consulta ou operação realizada com sucesso |
| 201 | Recurso criado |
| 401 | Requisição sem identidade válida, por falta de token ou credenciais inválidas |
| 403 | Usuário autenticado, mas sem permissão para a operação |
| 404 | Recurso inexistente ou não visível para o usuário |
| 409 | Dado único já cadastrado ou estado que não permite a operação |
| 422 | Dado enviado inválido ou ausente |

- Tipo indica se o cenário é de Sucesso ou de Erro.
- As mensagens entre aspas são as definidas no `rotas-api.md`.
- A marcação "a confirmar" indica informação que o `rotas-api.md` não define. Cada uma tem um item na seção Dúvidas para o Backend.

## Massa de teste necessária

| Perfil | Usado em |
| --- | --- |
| Doador pessoa física e pessoa jurídica | CT-AUT-01 a CT-AUT-05, CT-AUT-18, CT-PER-01 a CT-PER-06, CT-CAM-07, CT-CAM-24, CT-CAM-45, CT-CAM-51, CT-CAM-66, CT-CAM-77, CT-CAM-105 |
| Gestor com entidade vinculada | CT-AUT-19, CT-PER-10 |
| Gestor de entidade APROVADA, dono de campanhas | CT-CAM-01 a CT-CAM-05, CT-CAM-10 a CT-CAM-22, CT-CAM-27 a CT-CAM-34, CT-CAM-43 a CT-CAM-45, CT-CAM-53 a CT-CAM-56, CT-CAM-82, CT-CAM-98 a CT-CAM-100, CT-CAM-107 |
| Dois gestores, cada um com a sua entidade | CT-PER-09, CT-CAM-25, CT-CAM-52, CT-CAM-86, CT-CAM-106 |
| Gestor de entidade que não está APROVADA, por exemplo PENDENTE | CT-CAM-09, CT-CAM-41, CT-CAM-72, CT-CAM-84 |
| Administrador | CT-AUT-20, CT-CAM-08, CT-CAM-26, CT-CAM-46 a CT-CAM-49, CT-CAM-57 a CT-CAM-63, CT-CAM-83, CT-CAM-84, CT-CAM-101, CT-CAM-102, CT-CAM-108 |
| Visitante sem conta e sem token | CT-CAM-06, CT-CAM-23, CT-CAM-35 a CT-CAM-42, CT-CAM-50, CT-CAM-64, CT-CAM-65, CT-CAM-67 a CT-CAM-76, CT-CAM-78 a CT-CAM-81, CT-CAM-85, CT-CAM-87 a CT-CAM-97, CT-CAM-103, CT-CAM-104, CT-CAM-109 |
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
| CT-PER-02 | RF-005 | POST /api/v1/campanhas | Erro | Doador autenticado tenta criar uma campanha, que é uma operação de gestão. Retorna 403 com a mensagem "Somente entidades podem criar campanhas." |
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

## RF-010 Criação de campanha

Critério de aceitação. A campanha é salva com status "rascunho" e aparece no painel da entidade.

Rotas cobertas. POST /api/v1/campanhas (acesso ENTIDADE com status APROVADA), marcada como RF11 no `rotas-api.md`. O cenário que verifica a campanha na lista da entidade usa GET /api/v1/campanhas apenas para consultar. A cobertura funcional da listagem fica nos cenários do RF-014 e do RF-017.

| ID | Requisito de origem | Endpoint previsto | Tipo | Resultado esperado com código HTTP |
| --- | --- | --- | --- | --- |
| CT-CAM-01 | RF-010 | POST /api/v1/campanhas | Sucesso | Entidade aprovada cria campanha com título, descrição, meta e datas válidos. Retorna 201 com a mensagem "Campanha criada com sucesso." e os dados da campanha com valorArrecadado 0, percentualAtingido 0.00 e status ATIVA (a confirmar, o `requisitos.md` pede status "rascunho") |
| CT-CAM-02 | RF-010 | POST /api/v1/campanhas | Sucesso | Entidade aprovada cria campanha sem enviar o identificador da entidade. Retorna 201 e a campanha vem vinculada à entidade do usuário autenticado |
| CT-CAM-03 | RF-010 | POST /api/v1/campanhas e depois GET /api/v1/campanhas | Sucesso | Campanha criada aparece na lista da própria entidade. A criação retorna 201 e a consulta com o entidadeId da entidade dona retorna 200 com a campanha na lista (a confirmar, o documento não define uma rota de painel da entidade) |
| CT-CAM-04 | RF-010 | POST /api/v1/campanhas | Sucesso | Criação com data de encerramento igual à data de início. Retorna 201, pois a regra aceita data igual ou posterior |
| CT-CAM-05 | RF-010 | POST /api/v1/campanhas | Sucesso | Criação com data de início no passado e data de encerramento no futuro. Retorna 201 (a confirmar, o documento não define regra para a data de início no passado) |
| CT-CAM-06 | RF-010 | POST /api/v1/campanhas | Erro | Criação sem enviar token. Retorna 401 com a mensagem "Não autenticado." |
| CT-CAM-07 | RF-010 | POST /api/v1/campanhas | Erro | Doador autenticado tenta criar campanha. Retorna 403 com a mensagem "Somente entidades podem criar campanhas." |
| CT-CAM-08 | RF-010 | POST /api/v1/campanhas | Erro | Administrador autenticado tenta criar campanha. Retorna 403 com a mensagem "Somente entidades podem criar campanhas." (a confirmar) |
| CT-CAM-09 | RF-010 | POST /api/v1/campanhas | Erro | Entidade que ainda não foi aprovada, por exemplo com status PENDENTE, tenta criar campanha. Retorna 403 com a mensagem "Somente entidades aprovadas podem criar campanhas." |
| CT-CAM-10 | RF-010 | POST /api/v1/campanhas | Erro | Criação sem título. Retorna 422 com a mensagem "Título é obrigatório." |
| CT-CAM-11 | RF-010 | POST /api/v1/campanhas | Erro | Criação sem descrição. Retorna 422 com a mensagem "Descrição é obrigatória." |
| CT-CAM-12 | RF-010 | POST /api/v1/campanhas | Erro | Criação com meta igual a zero. Retorna 422 com a mensagem "Meta deve ser maior que zero." |
| CT-CAM-13 | RF-010 | POST /api/v1/campanhas | Erro | Criação com meta negativa. Retorna 422 com a mensagem "Meta deve ser maior que zero." |
| CT-CAM-14 | RF-010 | POST /api/v1/campanhas | Erro | Criação sem data de início. Retorna 422 com a mensagem "Data de início é obrigatória." |
| CT-CAM-15 | RF-010 | POST /api/v1/campanhas | Erro | Criação sem data de encerramento. Retorna 422 com a mensagem "Data de encerramento é obrigatória." |
| CT-CAM-16 | RF-010 | POST /api/v1/campanhas | Erro | Criação com data de encerramento anterior à data de início. Retorna 422 com a mensagem "Data de encerramento deve ser igual ou posterior à data de início." |
| CT-CAM-17 | RF-010 | POST /api/v1/campanhas | Erro | Criação com data de encerramento no passado. Retorna 422 com a mensagem "Data de encerramento não pode estar no passado." |
| CT-CAM-18 | RF-010 | POST /api/v1/campanhas | Erro | Criação com corpo vazio, sem nenhum campo. Retorna 422 com as mensagens de título, descrição, meta, data de início e data de encerramento (a confirmar se vêm juntas) |
| CT-CAM-19 | RF-010 | POST /api/v1/campanhas | Erro | Criação com JSON malformado. Retorna 400 com a mensagem "Corpo da requisição inválido." |

## RF-011 Edição de campanha

Critério de aceitação. As alterações salvas são refletidas na página de detalhes da campanha.

Rota principal. PATCH /api/v1/campanhas/{id} (acesso entidade dona), marcada como Grupo 2 no `rotas-api.md`. O cenário que verifica o detalhe usa GET /api/v1/campanhas/{id} apenas para consultar. A cobertura funcional do detalhe fica nos cenários do RF-015.

| ID | Requisito de origem | Endpoint previsto | Tipo | Resultado esperado com código HTTP |
| --- | --- | --- | --- | --- |
| CT-CAM-20 | RF-011 | PATCH /api/v1/campanhas/{id} | Sucesso | Entidade dona edita título, descrição, meta e datas de uma campanha ATIVA com dados válidos. Retorna 200 com a mensagem "Campanha atualizada com sucesso." e os dados da campanha com os novos valores |
| CT-CAM-21 | RF-011 | PATCH /api/v1/campanhas/{id} | Sucesso | Entidade dona edita apenas o título. Retorna 200 e os demais campos continuam com o valor anterior, pois os campos são opcionais |
| CT-CAM-22 | RF-011 | PATCH /api/v1/campanhas/{id} e depois GET /api/v1/campanhas/{id} | Sucesso | Campanha editada e consultada em seguida. A edição retorna 200 e o detalhe retorna 200 com os valores alterados |
| CT-CAM-23 | RF-011 | PATCH /api/v1/campanhas/{id} | Erro | Edição sem enviar token. Retorna 401 com a mensagem "Não autenticado." (a confirmar) |
| CT-CAM-24 | RF-011 | PATCH /api/v1/campanhas/{id} | Erro | Doador autenticado tenta editar uma campanha. Retorna 403 com a mensagem "Sem permissão." |
| CT-CAM-25 | RF-011 | PATCH /api/v1/campanhas/{id} | Erro | Entidade tenta editar campanha de outra entidade. Retorna 403 com a mensagem "Sem permissão." (a confirmar se é 403 ou 404) |
| CT-CAM-26 | RF-011 | PATCH /api/v1/campanhas/{id} | Erro | Administrador tenta editar uma campanha, pois o acesso é só da entidade dona. Retorna 403 com a mensagem "Sem permissão." (a confirmar) |
| CT-CAM-27 | RF-011 | PATCH /api/v1/campanhas/{id} | Erro | Edição de campanha que não existe. Retorna 404 com a mensagem "Campanha não encontrada." (a confirmar) |
| CT-CAM-28 | RF-011 | PATCH /api/v1/campanhas/{id} | Erro | Entidade dona edita campanha ENCERRADA. Retorna 409 com a mensagem "Somente campanhas ativas podem ser editadas." |
| CT-CAM-29 | RF-011 | PATCH /api/v1/campanhas/{id} | Erro | Entidade dona edita campanha DESATIVADA. Retorna 409 com a mensagem "Somente campanhas ativas podem ser editadas." |
| CT-CAM-30 | RF-011 | PATCH /api/v1/campanhas/{id} | Erro | Edição com o título vazio. Retorna 422 com a mensagem "Título não pode ser vazio." |
| CT-CAM-31 | RF-011 | PATCH /api/v1/campanhas/{id} | Erro | Edição com a descrição vazia. Retorna 422 com a mensagem "Descrição não pode ser vazia." (a confirmar) |
| CT-CAM-32 | RF-011 | PATCH /api/v1/campanhas/{id} | Erro | Edição com meta igual a zero. Retorna 422 com a mensagem "Meta deve ser maior que zero." (a confirmar) |
| CT-CAM-33 | RF-011 | PATCH /api/v1/campanhas/{id} | Erro | Edição com data de encerramento anterior à data de início. Retorna 422 com a mensagem "Data de encerramento deve ser igual ou posterior à data de início." (a confirmar) |
| CT-CAM-34 | RF-011 | PATCH /api/v1/campanhas/{id} | Erro | Edição com JSON malformado. Retorna 400 com a mensagem "Corpo da requisição inválido." |

## RF-012 Publicação de campanha

Critério de aceitação. Campanha publicada passa a constar na listagem pública. Campanha em rascunho não aparece.

Rotas cobertas. O `rotas-api.md` não tem rota de publicar nem status "rascunho". A campanha nasce ATIVA na criação (POST /api/v1/campanhas) e a visibilidade pública é consultada em GET /api/v1/campanhas e GET /api/v1/campanhas/{id}, acesso público. Os cenários abaixo cobrem essa visibilidade e deixam marcado "a confirmar" o que depende da rota de publicação. A cobertura funcional da listagem fica nos cenários do RF-014.

| ID | Requisito de origem | Endpoint previsto | Tipo | Resultado esperado com código HTTP |
| --- | --- | --- | --- | --- |
| CT-CAM-35 | RF-012 | GET /api/v1/campanhas | Sucesso | Visitante sem token lista campanhas e a campanha ATIVA, dentro do período e de entidade aprovada, consta na lista. Retorna 200 com a campanha (a confirmar, o documento não tem rota de publicar e a criação já deixa a campanha ATIVA) |
| CT-CAM-36 | RF-012 | GET /api/v1/campanhas/{id} | Sucesso | Visitante sem token consulta o detalhe de campanha ATIVA de entidade aprovada. Retorna 200 com os dados da campanha |
| CT-CAM-37 | RF-012 | (a confirmar) | Sucesso | Gestor publica uma campanha em rascunho. Retorna (a confirmar), pois o documento não define rota de publicação nem status rascunho |
| CT-CAM-38 | RF-012 | GET /api/v1/campanhas | Erro | Visitante lista campanhas e a campanha DESATIVADA não consta na lista. Retorna 200 sem a campanha |
| CT-CAM-39 | RF-012 | GET /api/v1/campanhas | Erro | Visitante lista campanhas e a campanha ENCERRADA não consta na lista. Retorna 200 sem a campanha |
| CT-CAM-40 | RF-012 | GET /api/v1/campanhas/{id} | Erro | Visitante consulta o detalhe de campanha DESATIVADA. Retorna 404 com a mensagem "Campanha não encontrada." |
| CT-CAM-41 | RF-012 | GET /api/v1/campanhas/{id} | Erro | Visitante consulta o detalhe de campanha de entidade não aprovada. Retorna 404 com a mensagem "Campanha não encontrada." |
| CT-CAM-42 | RF-012 | GET /api/v1/campanhas | Erro | Campanha em rascunho não consta na lista pública. Retorna 200 sem a campanha (a confirmar, o status rascunho não existe no documento) |

## RF-013 Encerramento de campanha

Critério de aceitação. Após o encerramento, a campanha exibe status "encerrada" e a opção de doar fica indisponível.

Rota principal. PATCH /api/v1/campanhas/{id}/status (acesso entidade dona, somente para ENCERRADA, ou ADMIN), marcada como RF13 e RF15 no `rotas-api.md`. Esta rota também permite ao administrador desativar e reativar campanha, e os dois casos estão cobertos aqui. Os cenários que verificam o detalhe e a doação usam GET /api/v1/campanhas/{id} e POST /api/v1/doacoes apenas como consulta. A cobertura funcional deles fica nos cenários do RF-015 e do módulo de doações.

| ID | Requisito de origem | Endpoint previsto | Tipo | Resultado esperado com código HTTP |
| --- | --- | --- | --- | --- |
| CT-CAM-43 | RF-013 | PATCH /api/v1/campanhas/{id}/status | Sucesso | Entidade dona encerra campanha ATIVA. Retorna 200 com a mensagem "Campanha encerrada." e a campanha com status ENCERRADA |
| CT-CAM-44 | RF-013 | PATCH /api/v1/campanhas/{id}/status e depois GET /api/v1/campanhas/{id} | Sucesso | Campanha encerrada pela entidade dona e consultada em seguida. O encerramento retorna 200 e o detalhe retorna 200 com status ENCERRADA e recebendoDoacoes false |
| CT-CAM-45 | RF-013 | PATCH /api/v1/campanhas/{id}/status e depois POST /api/v1/doacoes | Sucesso | Campanha encerrada não recebe doação. O encerramento retorna 200 e a doação para a campanha retorna 409 com a mensagem "Esta campanha não está recebendo doações." |
| CT-CAM-46 | RF-013 | PATCH /api/v1/campanhas/{id}/status | Sucesso | Administrador encerra campanha ATIVA. Retorna 200 com a mensagem "Campanha encerrada." e a campanha com status ENCERRADA |
| CT-CAM-47 | RF-013 | PATCH /api/v1/campanhas/{id}/status | Sucesso | Administrador encerra campanha DESATIVADA. Retorna 200 com a mensagem "Campanha encerrada." e a campanha com status ENCERRADA |
| CT-CAM-48 | RF-013 | PATCH /api/v1/campanhas/{id}/status | Sucesso | Administrador desativa campanha ATIVA informando o motivo. Retorna 200 com a mensagem "Campanha desativada." e a campanha com status DESATIVADA e o motivoDesativacao informado |
| CT-CAM-49 | RF-013 | PATCH /api/v1/campanhas/{id}/status | Sucesso | Administrador reativa campanha DESATIVADA. Retorna 200 com a mensagem "Campanha reativada." e a campanha com status ATIVA |
| CT-CAM-50 | RF-013 | PATCH /api/v1/campanhas/{id}/status | Erro | Alteração de status sem enviar token. Retorna 401 com a mensagem "Não autenticado." (a confirmar) |
| CT-CAM-51 | RF-013 | PATCH /api/v1/campanhas/{id}/status | Erro | Doador autenticado tenta encerrar uma campanha. Retorna 403 com a mensagem "Sem permissão." |
| CT-CAM-52 | RF-013 | PATCH /api/v1/campanhas/{id}/status | Erro | Entidade tenta encerrar campanha de outra entidade. Retorna 403 com a mensagem "Sem permissão." |
| CT-CAM-53 | RF-013 | PATCH /api/v1/campanhas/{id}/status | Erro | Entidade dona pede o status DESATIVADA, operação restrita ao administrador. Retorna 403 com a mensagem "Sem permissão." |
| CT-CAM-54 | RF-013 | PATCH /api/v1/campanhas/{id}/status | Erro | Entidade dona pede o status ATIVA para uma campanha DESATIVADA, operação restrita ao administrador. Retorna 403 com a mensagem "Sem permissão." (a confirmar se não retorna o 409 "Campanha desativada pelo administrador.") |
| CT-CAM-55 | RF-013 | PATCH /api/v1/campanhas/{id}/status | Erro | Entidade dona tenta encerrar campanha DESATIVADA. Retorna 409 com a mensagem "Campanha desativada pelo administrador." (a confirmar, o documento não diz em que caso essa mensagem é usada) |
| CT-CAM-56 | RF-013 | PATCH /api/v1/campanhas/{id}/status | Erro | Entidade dona tenta encerrar campanha que já está ENCERRADA. Retorna 409 com a mensagem "A campanha já está com este status." |
| CT-CAM-57 | RF-013 | PATCH /api/v1/campanhas/{id}/status | Erro | Administrador tenta reativar campanha ENCERRADA. Retorna 409 com a mensagem "Campanha encerrada não pode ser alterada." |
| CT-CAM-58 | RF-013 | PATCH /api/v1/campanhas/{id}/status | Erro | Administrador desativa campanha que já está DESATIVADA. Retorna 409 com a mensagem "A campanha já está com este status." |
| CT-CAM-59 | RF-013 | PATCH /api/v1/campanhas/{id}/status | Erro | Administrador desativa campanha sem informar o motivo. Retorna 422 com a mensagem "Motivo da desativação é obrigatório." |
| CT-CAM-60 | RF-013 | PATCH /api/v1/campanhas/{id}/status | Erro | Alteração com status fora dos valores permitidos. Retorna 422 com a mensagem "Status inválido. Use: ATIVA, ENCERRADA, DESATIVADA" |
| CT-CAM-61 | RF-013 | PATCH /api/v1/campanhas/{id}/status | Erro | Alteração sem enviar o status. Retorna 422 com a mensagem "Status inválido. Use: ATIVA, ENCERRADA, DESATIVADA" |
| CT-CAM-62 | RF-013 | PATCH /api/v1/campanhas/{id}/status | Erro | Alteração de status de campanha que não existe. Retorna 404 com a mensagem "Campanha não encontrada." (a confirmar) |
| CT-CAM-63 | RF-013 | PATCH /api/v1/campanhas/{id}/status | Erro | Alteração de status com JSON malformado. Retorna 400 com a mensagem "Corpo da requisição inválido." |

## RF-014 Listagem de campanhas disponíveis

Critério de aceitação. A listagem mostra apenas campanhas publicadas e não encerradas, com título, entidade e imagem.

Rota principal. GET /api/v1/campanhas (acesso público, token opcional), marcada como RF12 e RF14 no `rotas-api.md`. Busca e filtros ficam nos cenários do RF-016 e do RF-017.

| ID | Requisito de origem | Endpoint previsto | Tipo | Resultado esperado com código HTTP |
| --- | --- | --- | --- | --- |
| CT-CAM-64 | RF-014 | GET /api/v1/campanhas | Sucesso | Visitante sem token lista as campanhas. Retorna 200 com a lista data e o objeto meta com total, page, limit e totalPages, contendo só campanhas recebendo doações |
| CT-CAM-65 | RF-014 | GET /api/v1/campanhas | Sucesso | Cada item da lista traz título, entidade com id, razão social e nome fantasia, e o progresso com percentualAtingido. Retorna 200 (a confirmar a imagem, o objeto Campanha não tem campo de imagem) |
| CT-CAM-66 | RF-014 | GET /api/v1/campanhas | Sucesso | Doador autenticado lista as campanhas. Retorna 200 com a mesma lista que o visitante vê |
| CT-CAM-67 | RF-014 | GET /api/v1/campanhas | Sucesso | Listagem sem informar page e limit. Retorna 200 com meta page 1 e limit 10 |
| CT-CAM-68 | RF-014 | GET /api/v1/campanhas | Sucesso | Listagem com page 2 e limit 5. Retorna 200 com no máximo 5 campanhas e meta com page 2 e limit 5 |
| CT-CAM-69 | RF-014 | GET /api/v1/campanhas | Sucesso | Listagem quando não há campanha recebendo doações. Retorna 200 com data vazio e meta total 0 |
| CT-CAM-70 | RF-014 | GET /api/v1/campanhas | Erro | Campanha com a data de encerramento já vencida não consta na lista. Retorna 200 sem a campanha (a confirmar se o status dela é ATIVA ou ENCERRADA) |
| CT-CAM-71 | RF-014 | GET /api/v1/campanhas | Erro | Campanha ATIVA com a data de início no futuro não consta na lista. Retorna 200 sem a campanha |
| CT-CAM-72 | RF-014 | GET /api/v1/campanhas | Erro | Campanha ATIVA de entidade que não está APROVADA não consta na lista. Retorna 200 sem a campanha |
| CT-CAM-73 | RF-014 | GET /api/v1/campanhas | Erro | Listagem com limit acima de 50. Retorna 200 com no máximo 50 campanhas por página |
| CT-CAM-74 | RF-014 | GET /api/v1/campanhas | Erro | Listagem com page ou limit que não é número positivo. Retorna 422 com a mensagem "Valor inválido." (a confirmar) |

## RF-015 Detalhes da campanha

Critério de aceitação. Ao selecionar uma campanha na listagem, a página de detalhes apresenta descrição, entidade, imagens, período e progresso da arrecadação.

Rota principal. GET /api/v1/campanhas/{id} (acesso público), marcada como RF14 no `rotas-api.md`. Campanha DESATIVADA ou de entidade não aprovada só é visível para o dono e o administrador.

| ID | Requisito de origem | Endpoint previsto | Tipo | Resultado esperado com código HTTP |
| --- | --- | --- | --- | --- |
| CT-CAM-75 | RF-015 | GET /api/v1/campanhas/{id} | Sucesso | Visitante sem token consulta campanha ATIVA de entidade aprovada. Retorna 200 com o objeto data da campanha |
| CT-CAM-76 | RF-015 | GET /api/v1/campanhas/{id} | Sucesso | O detalhe traz descrição, entidade com id, razão social e nome fantasia, período com dataInicio e dataFim, e progresso com metaValor, valorArrecadado e percentualAtingido. Retorna 200 (a confirmar as imagens, o objeto Campanha não tem campo de imagem) |
| CT-CAM-77 | RF-015 | GET /api/v1/campanhas/{id} | Sucesso | Doador autenticado consulta campanha ATIVA. Retorna 200 com os mesmos dados que o visitante vê |
| CT-CAM-78 | RF-015 | GET /api/v1/campanhas/{id} | Sucesso | Visitante consulta campanha ENCERRADA. Retorna 200 com status ENCERRADA e recebendoDoacoes false |
| CT-CAM-79 | RF-015 | GET /api/v1/campanhas/{id} | Sucesso | Campanha com meta de 1000.00 e doações confirmadas somando 250.00. Retorna 200 com valorArrecadado 250.00 e percentualAtingido 25.00 |
| CT-CAM-80 | RF-015 | GET /api/v1/campanhas/{id} | Sucesso | Campanha sem nenhuma doação confirmada. Retorna 200 com valorArrecadado 0 e percentualAtingido 0.00 |
| CT-CAM-81 | RF-015 | GET /api/v1/campanhas/{id} | Sucesso | Campanha com doações confirmadas acima da meta. Retorna 200 (a confirmar se o percentualAtingido passa de 100.00) |
| CT-CAM-82 | RF-015 | GET /api/v1/campanhas/{id} | Sucesso | Entidade dona consulta a própria campanha DESATIVADA. Retorna 200 com a campanha |
| CT-CAM-83 | RF-015 | GET /api/v1/campanhas/{id} | Sucesso | Administrador consulta campanha DESATIVADA. Retorna 200 com a campanha |
| CT-CAM-84 | RF-015 | GET /api/v1/campanhas/{id} | Sucesso | Administrador consulta campanha de entidade não aprovada. Retorna 200 com a campanha |
| CT-CAM-85 | RF-015 | GET /api/v1/campanhas/{id} | Erro | Consulta de campanha que não existe. Retorna 404 com a mensagem "Campanha não encontrada." |
| CT-CAM-86 | RF-015 | GET /api/v1/campanhas/{id} | Erro | Entidade tenta consultar campanha DESATIVADA de outra entidade. Retorna 404 com a mensagem "Campanha não encontrada." |
| CT-CAM-87 | RF-015 | GET /api/v1/campanhas/{id} | Erro | Consulta com id que não tem formato de UUID. Retorna 404 com a mensagem "Campanha não encontrada." (a confirmar) |

## RF-016 Busca de campanhas

Critério de aceitação. A busca retorna somente campanhas publicadas que contenham o termo informado.

Rota principal. GET /api/v1/campanhas com o parâmetro busca (acesso público, token opcional), marcada como RF12 e RF14 no `rotas-api.md`. Nessa rota o parâmetro busca procura por trecho do título.

| ID | Requisito de origem | Endpoint previsto | Tipo | Resultado esperado com código HTTP |
| --- | --- | --- | --- | --- |
| CT-CAM-88 | RF-016 | GET /api/v1/campanhas?busca={trecho} | Sucesso | Busca por um trecho do título. Retorna 200 só com as campanhas cujo título contém o trecho |
| CT-CAM-89 | RF-016 | GET /api/v1/campanhas?busca={trecho} | Sucesso | Busca com o trecho em letras diferentes do título, por exemplo em maiúsculas. Retorna 200 com a campanha encontrada (a confirmar) |
| CT-CAM-90 | RF-016 | GET /api/v1/campanhas?busca={trecho} | Sucesso | Busca por um trecho que não existe em nenhum título. Retorna 200 com data vazio e meta total 0 |
| CT-CAM-91 | RF-016 | GET /api/v1/campanhas?busca={termo} | Sucesso | Busca por um termo que aparece só na descrição da campanha. Retorna 200 com a campanha (a confirmar, o `rotas-api.md` busca só no título) |
| CT-CAM-92 | RF-016 | GET /api/v1/campanhas?busca={trecho}&page=1&limit=1 | Sucesso | Busca com paginação. Retorna 200 com uma campanha e meta com o total de campanhas encontradas |
| CT-CAM-93 | RF-016 | GET /api/v1/campanhas?busca= | Sucesso | Busca com o parâmetro vazio. Retorna 200 com a lista completa de campanhas recebendo doações (a confirmar) |
| CT-CAM-94 | RF-016 | GET /api/v1/campanhas?busca={trecho} | Erro | Campanha DESATIVADA ou ENCERRADA com o trecho no título não consta no resultado da busca do visitante. Retorna 200 sem a campanha |
| CT-CAM-95 | RF-016 | GET /api/v1/campanhas?busca={termo} | Erro | Busca com caracteres especiais e texto de injeção, por exemplo uma aspa seguida de comando SQL. Retorna 200 sem erro e sem alterar dados (a confirmar) |

## RF-017 Filtros de campanhas

Critério de aceitação. Ao aplicar um filtro, apenas as campanhas que atendem ao critério são exibidas.

Rota principal. GET /api/v1/campanhas com os parâmetros entidadeId e status (acesso público, token opcional), marcada como RF12 e RF14 no `rotas-api.md`. O filtro de status só vale para o administrador ou para a entidade dona, com o próprio entidadeId. Para os demais o filtro é ignorado e só aparecem campanhas recebendo doações.

| ID | Requisito de origem | Endpoint previsto | Tipo | Resultado esperado com código HTTP |
| --- | --- | --- | --- | --- |
| CT-CAM-96 | RF-017 | GET /api/v1/campanhas?entidadeId={id} | Sucesso | Visitante filtra por entidade. Retorna 200 só com campanhas dessa entidade que estão recebendo doações |
| CT-CAM-97 | RF-017 | GET /api/v1/campanhas?entidadeId={id} | Sucesso | Filtro por entidade que não tem campanhas. Retorna 200 com data vazio e meta total 0 |
| CT-CAM-98 | RF-017 | GET /api/v1/campanhas?entidadeId={id}&status=ENCERRADA | Sucesso | Entidade dona filtra as próprias campanhas por status ENCERRADA. Retorna 200 só com as campanhas ENCERRADAS dela |
| CT-CAM-99 | RF-017 | GET /api/v1/campanhas?entidadeId={id}&status=DESATIVADA | Sucesso | Entidade dona filtra as próprias campanhas por status DESATIVADA. Retorna 200 só com as campanhas DESATIVADAS dela |
| CT-CAM-100 | RF-017 | GET /api/v1/campanhas?entidadeId={id}&status=ATIVA | Sucesso | Entidade dona filtra as próprias campanhas por status ATIVA. Retorna 200 só com as campanhas ATIVAS dela (a confirmar se inclui as que estão fora do período) |
| CT-CAM-101 | RF-017 | GET /api/v1/campanhas?status=ENCERRADA | Sucesso | Administrador filtra por status ENCERRADA sem informar a entidade. Retorna 200 com as campanhas ENCERRADAS de todas as entidades |
| CT-CAM-102 | RF-017 | GET /api/v1/campanhas?entidadeId={id}&status=DESATIVADA | Sucesso | Administrador filtra por entidade e por status DESATIVADA. Retorna 200 só com as campanhas DESATIVADAS dessa entidade |
| CT-CAM-103 | RF-017 | GET /api/v1/campanhas?entidadeId={id}&busca={trecho} | Sucesso | Filtro por entidade combinado com busca. Retorna 200 só com campanhas da entidade cujo título contém o trecho |
| CT-CAM-104 | RF-017 | GET /api/v1/campanhas?status=ENCERRADA | Erro | Visitante envia o filtro de status. O filtro é ignorado e retorna 200 só com campanhas recebendo doações |
| CT-CAM-105 | RF-017 | GET /api/v1/campanhas?status=DESATIVADA | Erro | Doador autenticado envia o filtro de status. O filtro é ignorado e retorna 200 só com campanhas recebendo doações |
| CT-CAM-106 | RF-017 | GET /api/v1/campanhas?entidadeId={id de outra entidade}&status=ENCERRADA | Erro | Entidade envia o filtro de status com o entidadeId de outra entidade. O filtro é ignorado e retorna 200 só com campanhas recebendo doações dessa outra entidade |
| CT-CAM-107 | RF-017 | GET /api/v1/campanhas?status=ENCERRADA | Erro | Entidade envia o filtro de status sem informar o entidadeId. Retorna 200 (a confirmar se o Backend assume a entidade do token e aplica o filtro ou se ignora o filtro) |
| CT-CAM-108 | RF-017 | GET /api/v1/campanhas?status={valor inválido} | Erro | Administrador envia status fora de ATIVA, ENCERRADA e DESATIVADA. Retorna 422 com a mensagem "Status inválido. Use: ATIVA, ENCERRADA, DESATIVADA" (a confirmar) |
| CT-CAM-109 | RF-017 | GET /api/v1/campanhas?entidadeId={valor fora do formato UUID} | Erro | Filtro por entidadeId fora do formato UUID. Retorna 422 (a confirmar, o documento não define) |

---

## Dúvidas para o Backend

| # | Requisito | Cenário | Dúvida |
| --- | --- | --- | --- |
| 1 | RF-001 | CT-AUT-09 | Confirmar se CNPJ repetido também retorna 409, pois o documento mostra só e-mail e CPF |
| 2 | RF-001 | CT-AUT-14 | Confirmar se CPF é obrigatório para pessoa física, pois o documento mostra só a mensagem de CPF inválido |
| 3 | RF-002 | CT-AUT-19 | Confirmar se gestor com entidade PENDENTE, REPROVADA ou INATIVA consegue fazer login |
| 4 | RF-002 | CT-AUT-20 | Confirmar qual é o valor do campo tipo para o administrador |
| 5 | RF-002 | CT-AUT-25 | Confirmar se, com e-mail e senha vazios, as duas mensagens vêm juntas na mesma resposta. Ver também a dúvida 15 |
| 6 | RNF-003 | CT-PER-07 e CT-PER-08 | Confirmar qual é o corpo da resposta 401 sem token e com token inválido, pois o documento só cita o código. Ver também as dúvidas 17 e 25 |
| 7 | RF-005 | CT-PER-09 e CT-PER-10 | Confirmar qual é o corpo da resposta 403, pois o documento não define. Ver também a dúvida 14 |
| 8 | RF-005 | CT-PER-09 | Confirmar se o gestor que edita a entidade de outro recebe 403 ou 404. Ver também as dúvidas 18 e 32 |
| 9 | Geral | Todos | Confirmar a numeração dos requisitos, pois o `rotas-api.md` cita o login como RF03 e RF06 e o `requisitos.md` cita RF-002. Ver também as dúvidas 16 e 24 |
| 10 | RF-010 | CT-CAM-01 | Divergência entre os documentos. O `requisitos.md` pede status "rascunho" ao criar, mas o `rotas-api.md` cria a campanha como ATIVA. Confirmar qual vale. Ver também as dúvidas 16, 21 e 23 |
| 11 | RF-010 | CT-CAM-01 e CT-CAM-02 | Confirmar se o "objetivo" do `requisitos.md` corresponde ao campo metaValor e se a entidade beneficiada é sempre a do usuário autenticado, pois o `rotas-api.md` não recebe o identificador da entidade |
| 12 | RF-010 | CT-CAM-03 | Confirmar qual rota alimenta o painel da entidade, pois o documento só tem a listagem pública com filtro por entidadeId |
| 13 | RF-010 | CT-CAM-05 | Confirmar se a data de início no passado é aceita, pois o documento só define regra para a data de encerramento |
| 14 | RF-010 | CT-CAM-08 | Confirmar se a mensagem do 403 quando o administrador tenta criar campanha é a sugerida no cenário, pois o documento cita só entidades. Ver também as dúvidas 7 e 19 |
| 15 | RF-010 | CT-CAM-18 | Confirmar se, com o corpo vazio, todas as mensagens de campo vêm juntas na mesma resposta. Ver também a dúvida 5 |
| 16 | RF-011 | CT-CAM-20 | A rota de edição aparece como Grupo 2 no `rotas-api.md`, sem número de RF. Confirmar que ela cobre o RF-011 e que só campanha ATIVA pode ser editada, já que o `requisitos.md` não tem status rascunho nesse fluxo. Ver também as dúvidas 9 e 10 |
| 17 | RF-011 | CT-CAM-23 e CT-CAM-27 | Confirmar se o corpo do 401 sem token ("Não autenticado.") e do 404 de campanha inexistente ("Campanha não encontrada.") na edição são os sugeridos nos cenários, pois a rota 16 só cita o código. Ver também as dúvidas 6 e 25 |
| 18 | RF-011 | CT-CAM-25 | Confirmar se a entidade que edita a campanha de outra recebe 403 ou 404. Ver também a dúvida 8 |
| 19 | RF-011 | CT-CAM-26 | Confirmar se o administrador pode editar campanha, pois o acesso da rota é só da entidade dona. Ver também a dúvida 14 |
| 20 | RF-011 | CT-CAM-31 a CT-CAM-33 | Confirmar se as mensagens 422 de descrição vazia, meta inválida e datas inconsistentes na edição são as sugeridas nos cenários, pois a rota 16 só mostra a do título |
| 21 | RF-012 | CT-CAM-35 a CT-CAM-42 | Divergência entre os documentos. O `requisitos.md` pede publicar campanha e tem o status rascunho, mas o `rotas-api.md` não tem rota de publicar nem esse status, e a campanha nasce ATIVA e visível. Confirmar se criar equivale a publicar ou se a rota e o status serão criados. Ver também as dúvidas 10, 22 e 23 |
| 22 | RF-012 | CT-CAM-37 | Se a rota de publicação for criada, confirmar método, rota, perfis com acesso, mensagem de sucesso e erros, para fechar o cenário. Ver também a dúvida 21 |
| 23 | RF-012 | CT-CAM-42 | Confirmar como o rascunho deve aparecer ao gestor da entidade dona, pois a rota 13 só prevê os status ATIVA, ENCERRADA e DESATIVADA. Ver também as dúvidas 10 e 21 |
| 24 | RF-013 | CT-CAM-48 e CT-CAM-49 | Desativar e reativar campanha não têm requisito correspondente no `requisitos.md`, pois o RF-013 cobre só o encerramento. Confirmar a qual requisito esses dois casos pertencem, já que a rota 17 os cita como RF15 e a numeração difere entre os documentos. Ver também a dúvida 9 |
| 25 | RF-013 | CT-CAM-50 e CT-CAM-62 | Confirmar se a rota 17 também retorna 401 sem token e 404 para campanha inexistente, e com quais mensagens, pois o documento não lista esses dois códigos nessa rota. Ver também as dúvidas 6 e 17 |
| 26 | RF-013 | CT-CAM-54 e CT-CAM-55 | Confirmar a ordem das regras e as mensagens. A entidade dona que pede ATIVA para campanha DESATIVADA recebe 403 ou 409. A mensagem "Campanha desativada pelo administrador." vale para qual caso |
| 27 | RF-014 | CT-CAM-65 | Divergência entre os documentos. O `requisitos.md` pede imagem na listagem, mas o objeto Campanha do `rotas-api.md` não tem campo de imagem. Pedir ao Backend que confirme se o campo de imagem será incluído no objeto Campanha. Ver também a dúvida 30 |
| 28 | RF-014 | CT-CAM-70 | Confirmar se a campanha com a data de encerramento vencida passa a ENCERRADA automaticamente ou continua ATIVA, pois o `rotas-api.md` só define ENCERRADA pela ação da entidade e calcula recebendoDoacoes pela data. Ver também a dúvida 38 |
| 29 | RF-014 | CT-CAM-73 e CT-CAM-74 | Confirmar se a rota 13 limita o limit a 50 quando vem um valor maior e se retorna 422 com a mensagem "Valor inválido." quando page ou limit não é número positivo, pois o documento não define. Ver também a dúvida 39 |
| 30 | RF-015 | CT-CAM-76 | Divergência entre os documentos. O `requisitos.md` pede imagens na página de detalhes, mas o objeto Campanha do `rotas-api.md` não tem campo de imagem. Pedir ao Backend que confirme. Ver também a dúvida 27 |
| 31 | RF-015 | CT-CAM-81 | Confirmar como fica o percentualAtingido quando o valor arrecadado passa da meta, se limita a 100.00 ou mostra o valor real |
| 32 | RF-015 | CT-CAM-87 | Confirmar se id fora do formato UUID retorna 404 com "Campanha não encontrada." na rota 14, como a rota 8 faz para entidades, pois o documento não define isso para campanhas. Ver também as dúvidas 8 e 39 |
| 33 | RF-016 | CT-CAM-91 | Divergência entre os documentos. O `requisitos.md` pede busca por termo no título ou na descrição, mas a rota 13 busca só por trecho do título. Confirmar se a descrição entra na busca |
| 34 | RF-016 | CT-CAM-89 e CT-CAM-93 | Confirmar se a busca ignora maiúsculas e minúsculas e se o parâmetro busca vazio devolve a lista completa, pois o documento não define |
| 35 | RF-016 | CT-CAM-95 | Confirmar como a busca trata caracteres especiais e texto de injeção, pois o documento não define e o RNF-004 pede validação das entradas |
| 36 | RF-017 | CT-CAM-104 e CT-CAM-105 | Divergência entre os documentos. O `requisitos.md` lista visitante e doador como atores do filtro por entidade e por status, mas a rota 13 só aplica o filtro de status ao administrador e à entidade dona, e ignora para os demais. Confirmar se visitante e doador devem poder filtrar por status. Ver também a dúvida 37 |
| 37 | RF-017 | CT-CAM-107 | Confirmar se, quando a entidade dona envia o filtro de status sem o entidadeId, o Backend assume a entidade do token e aplica o filtro, pois a rota 13 cita "o próprio entidadeId" sem dizer se ele é obtido do token. Ver também a dúvida 36 |
| 38 | RF-017 | CT-CAM-100 | Confirmar se o filtro status ATIVA mostra também a campanha ATIVA fora do período, pois a lista pública mostra só as que estão recebendo doações. Ver também a dúvida 28 |
| 39 | RF-017 | CT-CAM-108 e CT-CAM-109 | Confirmar se status inválido retorna o 422 "Status inválido. Use: ATIVA, ENCERRADA, DESATIVADA", como na rota 17, e se entidadeId fora do formato UUID retorna 422 ou é ignorado na rota 13, pois o documento não define. Ver também as dúvidas 29 e 32 |