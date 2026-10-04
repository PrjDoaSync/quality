# Relatório de testes de API do DoaSync (S10 e S11)

04/10/2026

## Contexto e objetivo

Este relatório registra o trabalho feito para entregar as issues S10 e S11 da Sprint 2 (QA no Shift-Left) na terça-feira, 6 de outubro de 2026. O resultado é uma coleção no Postman com um mock local e um projeto RestAssured que já roda contra esse mock e só precisa trocar o endereço quando a API real existir.

**Issues atendidas**

- **S10 (#12), Colaborar no Mapeamento dos Contratos de API.** Mapear com o Backend os endpoints de cada tela do Figma, revisar campos obrigatórios, tipos e status HTTP, montar uma coleção no Postman ou Insomnia e registrar os contratos no repositório.
- **S11 (#13), Estruturar o Projeto de Automação de API (RESTAssured).** Organizar a estrutura e a configuração base, escrever os primeiros scripts com asserções de status code, headers e payload, manter a URL base parametrizada e documentar como executar a suíte.

## Origem dos contratos

Os endpoints usados vêm de uma issue real e recente do time de Backend, mas os dados de resposta dos exemplos são fictícios. A fonte é a [issue #53 do repositório doa-sync](https://github.com/PrjDoaSync/doa-sync/issues/53), a história de usuário US-01 (Visualizar Indicadores Globais, Evolução Financeira e Relatórios de Gestão), que lista as tarefas relacionadas com os endpoints abaixo.

| Tarefa na issue | Método | Caminho | O que a issue informa |
| --- | --- | --- | --- |
| TS-01-02 | GET | `/api/v1/dashboard/kpis` | Cards de KPI do dashboard (Total Arrecadado, Doações do Mês, Agendamentos Pendentes, Projetos Ativos) |
| TS-01-02 | GET | `/api/v1/dashboard/financeiro` | Evolução financeira por categorias e canais de entrada, com seletor de período |
| TS-01-03 | POST | `/api/v1/relatorios/exportar` | Exportação de relatórios customizados em PDF e CSV |
| TS-01-04 | GET | `/api/v1/dashboard/auditoria` | Histórico e logs de auditoria das ações administrativas |

**O que é real e o que é fictício**

- **Real, vindo da issue.** O método HTTP e o caminho de cada endpoint, o nome dos indicadores, os formatos de exportação (PDF e CSV) e a finalidade de cada rota.
- **Fictício, criado neste trabalho.** Os nomes dos campos do JSON, os tipos, os valores e a estrutura das respostas. A issue não informa body, tipos nem status codes, então esses exemplos foram inventados a partir dos critérios de aceite da US-01.

Todo exemplo salvo no Postman recebeu a descrição "campos a confirmar com o Backend". Por isso o que este trabalho entrega é um contrato previsto, e não um contrato fechado. A tarefa TS-01-05 da mesma issue prevê documentar os contratos em OpenAPI/Swagger, e quando essa documentação existir ela deve substituir os exemplos fictícios.

## Montagem no Postman

A coleção **DoaSync API** tem a pasta **US-01 Dashboard** com os quatro requests da issue #53, todos usando a variável de coleção `baseUrl` no início do endereço (`{{baseUrl}}/api/v1/...`). Trocar o ambiente passa a exigir mudar um valor só.

A coleção pode ser aberta pelo link [DoaSync API no Postman](https://amos-simoes123-7124459.postman.co/workspace/Projeto-de-Exten%25C3%25A7%25C3%25A3o---Doasync~938b90c7-0d98-476e-921c-1faf8da4ff9e/collection/57954665-c6f3afbb-221f-4147-bcf7-ccc2514d7dbc?action=share&source=copy-link&creator=57954665), que exige acesso ao workspace Projeto de Extensão - Doasync.

Cada request tem um exemplo salvo, e o mock responde com base nele. Um exemplo tem duas metades. Na de cima ficam o método e a URL da chamada. Na de baixo ficam o status, o header `Content-Type` e o body da resposta.

| Endpoint | Status | Content-Type da resposta | Conteúdo do exemplo (fictício) |
| --- | --- | --- | --- |
| GET kpis | 200 | application/json | Objeto com quatro indicadores |
| GET financeiro | 200 | application/json | Objeto com evolução mensal, totais por categoria e por canal |
| POST exportar | 200 | text/csv | Texto CSV com id, categoria e total |
| GET auditoria | 200 | application/json | Lista de registros de log com usuário, ação e data |

**Exemplo de GET kpis**

```json
{
  "totalArrecadado": 125430.50,
  "doacoesMes": 87,
  "agendamentosPendentes": 12,
  "projetosAtivos": 6
}
```

**Exemplo de GET financeiro**

```json
{
  "periodo": "2026-10",
  "evolucao": [
    { "mes": "2026-08", "total": 38200.00 },
    { "mes": "2026-09", "total": 41850.75 },
    { "mes": "2026-10", "total": 45379.75 }
  ],
  "porCategoria": [
    { "categoria": "Alimentos", "total": 21000.00 },
    { "categoria": "Educação", "total": 14379.75 },
    { "categoria": "Saúde", "total": 10000.00 }
  ],
  "porCanal": [
    { "canal": "Pix", "total": 28000.00 },
    { "canal": "Cartão", "total": 17379.75 }
  ]
}
```

**Exemplo de GET auditoria**

```json
[
  {
    "id": 1,
    "usuario": "admin@doasync.com",
    "acao": "EXPORTOU_RELATORIO",
    "descricao": "Exportou relatório financeiro em PDF",
    "dataHora": "2026-10-01T14:32:10"
  },
  {
    "id": 2,
    "usuario": "gestor@doasync.com",
    "acao": "ATUALIZOU_PROJETO",
    "descricao": "Alterou o status do projeto para ativo",
    "dataHora": "2026-10-02T09:15:44"
  }
]
```

**Exemplo de POST exportar**

O endpoint devolve um arquivo e não JSON. Como um PDF real não cabe em uma caixa de texto, o exemplo usa CSV.

```csv
id,categoria,total
1,Alimentos,21000.00
2,Educação,14379.75
```

## Mock server local

Como nenhuma API existe ainda, os testes rodam contra o mock nativo do Postman, o que a própria issue S11 prevê. O mock **DoaSync Mock** foi criado a partir da coleção e roda no computador do autor em `http://localhost:3001`, e é esse o valor da variável `baseUrl`.

A página do mock no Postman abre pelo link [DoaSync Mock no Postman](https://amos-simoes123-7124459.postman.co/workspace/Projeto-de-Exten%25C3%25A7%25C3%25A3o---Doasync~938b90c7-0d98-476e-921c-1faf8da4ff9e/mock/01a1087b-ec34-7559-a928-2c4c2b7ec6d8), com o mesmo requisito de acesso. O endereço que os testes chamam continua sendo o local, e o link só abre a página do mock dentro do Postman.

- O código gerado pelo Postman mostra a porta 4500 como padrão, mas o servidor sobe na 3001, que é a porta exibida no topo da aba do mock.
- O mock só responde enquanto o Postman está aberto e o mock está ligado (botão Start). Os testes precisam dele ativo.
- Rota desconhecida devolve 404 com a mensagem "Endpoint not defined". Endpoint sem exemplo salvo devolve uma resposta genérica ("Hi from postman mock").
- A aba **Logs** do mock lista cada chamada recebida com o caminho exato, e foi a principal ferramenta de diagnóstico do trabalho.
- A opção de atualizar o mock automaticamente foi marcada, mas ao salvar exemplos novos o mock continuou com a versão antiga. Foi preciso recriar o mock uma vez e reiniciá-lo com Stop e Start outra vez. A causa não foi confirmada.

## Projeto RestAssured

O projeto **doasync-api-tests** é um projeto Maven criado no IntelliJ, na pasta `quality\docs\backend`. Ele usa Java 21 e duas dependências de teste.

| Item | Valor |
| --- | --- |
| JDK | Microsoft OpenJDK 21.0.12 (ms-21) |
| Language level e compilador | 21 (`maven.compiler.source` e `maven.compiler.target`) |
| rest-assured | 6.0.1, escopo `test` |
| junit-jupiter | 6.1.2, escopo `test` |
| Hamcrest | 2.2, trazido pelo RestAssured (fornece `equalTo` e `notNullValue`) |

O IntelliJ criou o projeto em Java 26. Como o RestAssured 6 roda sobre Groovy 5, optou-se pelo Java 21 (versão LTS) por cautela. Não foi confirmado que o Java 26 causaria erro.

**Estrutura de pastas em `src/test/java`**

- `config`, com a classe `BaseTest`, que muda pouco.
- `dashboard`, com uma classe de teste por endpoint, que tende a crescer.

**URL base parametrizada**

A classe `BaseTest` define o endereço uma única vez antes de todos os testes. O valor vem da propriedade de sistema `baseUrl` e, se ela não for informada, usa o mock. Quando a API real existir, basta rodar a suíte passando outro valor e nenhum teste precisa mudar.

```java
package config;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {

    @BeforeAll
    static void configurar() {
        RestAssured.baseURI = System.getProperty("baseUrl", "http://localhost:3001");
    }
}
```

## Testes escritos

Há um teste por endpoint no pacote `dashboard`, e os três de leitura já passaram contra o mock. Eles validam apenas o que é seguro afirmar sem um contrato fechado, que é o status da resposta e o tipo do conteúdo. Nenhum teste confere campos do body ainda.

| Classe | Endpoint | O que valida | Situação |
| --- | --- | --- | --- |
| `DashboardKpisTest` | GET `/api/v1/dashboard/kpis` | Status 200 e content-type JSON | Passou |
| `DashboardFinanceiroTest` | GET `/api/v1/dashboard/financeiro` | Status 200 e content-type JSON | Passou |
| `DashboardAuditoriaTest` | GET `/api/v1/dashboard/auditoria` | Status 200 e content-type JSON | Passou |
| `DashboardRelatorioTest` | POST `/api/v1/relatorios/exportar` | Status 200 e content-type `text/csv` | Passou |

**Como o teste é lido**

O RestAssured segue a frase "dado que, quando, então". O bloco `given` prepara a chamada, o `when` dispara o método no caminho, e o `then` confere a resposta. Se qualquer conferência falhar, o teste fica vermelho.

```java
@Test
void deveRetornar200ComJson() {
    given()
    .when()
        .get("/api/v1/dashboard/kpis")
    .then()
        .statusCode(200)
        .contentType(ContentType.JSON);
}
```

O teste do relatório devolve arquivo, então a conferência é feita no header e não em campos de JSON. A versão correta usa `post`, confere `statusCode(200)` e compara o content-type com a string `"text/csv"`.

## Problemas encontrados e lições

Seis problemas apareceram no caminho, e a maioria veio de detalhes de configuração e não de lógica dos testes.

| Problema | Causa | Como foi resolvido |
| --- | --- | --- |
| O mock não devolvia o JSON do exemplo | JSON e Content-Type estavam na metade de cima (requisição) e a URL do exemplo estava vazia | Mover o JSON para a metade de baixo (resposta), preencher a URL e criar o header Content-Type na resposta |
| O Exportar aparecia como GET no mock | A troca do método para POST não tinha sido salva | Salvar todas as abas (Ctrl+S) antes de criar o mock |
| O mock respondia "Hi from postman mock" | O mock foi criado antes dos exemplos existirem | Recriar o mock e depois reiniciar com Stop e Start |
| O teste do KPIs recebeu 404 | O caminho foi digitado como `/api.;v1/...` em vez de `/api/v1/...` | A aba Logs do mock mostrou o caminho exato que chegou, e a barra foi corrigida |
| O teste do Relatório passou com JSON, mas o endpoint devolve texto | O teste usava GET em uma rota POST, o 404 do mock vem em JSON e o teste não conferia o status | Trocar para `post`, conferir `statusCode(200)` e usar `"text/csv"`, já que `ContentType.TEXT` significa text/plain |
| O projeto nasceu em Java 26 | O IntelliJ escolheu o JDK mais novo disponível | Baixar o JDK 21 e alinhar SDK, language level e `pom.xml` |

**O que fica de aprendizado**

- Um teste sem conferência passa sempre. Todo teste deve conferir o status junto com o resto, senão um 404 pode parecer sucesso.
- Quando uma rota não é encontrada, a aba Logs do mock mostra o caminho exato recebido e permite comparar com o esperado.
- O mock só é tão confiável quanto os exemplos, que aqui são suposições. Um teste verde contra o mock prova que o mock funciona, e não que a API real responderá assim.

## Situação das tarefas

A parte técnica das duas issues está montada, mas o critério de aceite da S10 depende do Backend e ainda não pode ser cumprido por completo.

| Issue | Tarefa | Situação | Observação |
| --- | --- | --- | --- |
| S10 | Mapear os endpoints que alimentam cada tela | Parcial | Só os endpoints da US-01, tirados da issue #53 |
| S10 | Revisar campos obrigatórios, tipos e status HTTP | Pendente | Depende do Backend, hoje há só exemplos fictícios |
| S10 | Montar a coleção no Postman | Feita | Coleção DoaSync API com mock local |
| S10 | Registrar os contratos revisados no repositório | Pendente | Este relatório e a coleção exportada podem ser anexados |
| S11 | Estruturar pastas e configuração base | Feita | Projeto Maven com pacotes `config` e `dashboard` |
| S11 | Escrever os primeiros scripts de teste | Parcial | Conferem status e content-type, ainda sem payload |
| S11 | Manter a URL base parametrizada | Feita | Propriedade `baseUrl` com o mock como padrão |
| S11 | Documentar como executar a suíte | Pendente | Falta o README |

Critérios de aceite. O da S10 pede contratos revisados em conjunto com o Backend, o que não aconteceu porque o time está travado. O da S11 pede o projeto estruturado e os primeiros scripts prontos para rodar contra a API quando ela existir, o que está atendido para os endpoints da US-01.

## Pendências e próximos passos

O que falta para fechar a entrega de terça-feira é pequeno e está todo sob controle do autor. O restante depende do Backend sair do bloqueio.

**Para a entrega**

- [ ] Escrever um README curto com como ligar o mock, como rodar a suíte e como apontar para outra URL.
- [ ] Validar a execução pela linha de comando com `mvn test -DbaseUrl=<endereço>`. Até aqui os testes foram rodados apenas pelo IntelliJ, e essa forma ainda não foi testada.
- [ ] Registrar no repositório os contratos previstos, marcando o que é suposição como "a confirmar com o Backend".

**Depende do Backend**

- [ ] Confirmar com o Backend os campos do body, os tipos, os status codes e se há autenticação.
- [ ] Comparar os exemplos com a documentação OpenAPI/Swagger prevista na tarefa TS-01-05 da issue #53.
- [ ] Adicionar asserções de campos do body (por exemplo `projetosAtivos`) depois que o contrato for confirmado.
- [ ] Se existir autenticação, incluir um teste sem token esperando 401.
- [ ] Trocar o valor de `baseUrl` para a API real assim que ela for entregue.
