# doasync-api-tests

Testes automatizados de API do DoaSync com RestAssured e JUnit 5.

Hoje os testes rodam contra um mock do Postman, porque a API real ainda não existe. Para apontar para outra API, basta passar a URL na execução. Nenhum teste precisa mudar.

## Requisitos

- Java 21 ou superior.
- Maven 3.9 ou superior. Se não tiver o Maven instalado, use o do IntelliJ (veja abaixo).

## Como rodar

Todos os testes, contra o mock:

```bash
mvn test
```

Contra outra API:

```bash
mvn test -DbaseUrl=https://endereco-da-api
```

No PowerShell, coloque aspas, senão a URL é cortada no primeiro ponto:

```powershell
mvn test "-DbaseUrl=https://endereco-da-api"
```

Uma classe só:

```bash
mvn test -Dtest=DashboardKpisTest
```

### Sem Maven instalado

- **Pelo IntelliJ.** Abra a classe de teste e clique no botão de executar ao lado dela. Para trocar a URL, edite a configuração de execução e coloque `-DbaseUrl=...` em VM options.
- **Pelo Maven do IntelliJ.** Use o `mvn.cmd` que vem com ele, em `plugins\maven\lib\maven3\bin` dentro da pasta de instalação do IntelliJ.

## URL base

A classe `config/BaseTest` define a URL uma única vez. Ela lê a propriedade `baseUrl` e, se não vier nenhuma, usa o mock. O endereço do mock está no próprio `BaseTest`.

## Mock

O mock é o **DoaSync Mock**, criado a partir da coleção **DoaSync API** no Postman (workspace Projeto de Extensão - Doasync). Ele roda na nuvem do Postman, é público e responde com os exemplos salvos na coleção. Os dados são fictícios.

Se o mock sumir, crie outro a partir da coleção (Mock collection, sem marcar "Make mock server private") e passe a nova URL em `-DbaseUrl`. Antes, salve todos os exemplos.

## Estrutura

```
src/test/java
  config/      BaseTest, com a URL base
  dashboard/   uma classe de teste por endpoint
```

## Testes atuais

| Classe | Endpoint | O que valida |
| --- | --- | --- |
| `DashboardKpisTest` | GET `/api/v1/dashboard/kpis` | Status 200 e content-type JSON |
| `DashboardFinanceiroTest` | GET `/api/v1/dashboard/financeiro` | Status 200 e content-type JSON |
| `DashboardAuditoriaTest` | GET `/api/v1/dashboard/auditoria` | Status 200 e content-type JSON |
| `DashboardRelatorioTest` | POST `/api/v1/relatorios/exportar` | Status 200 e content-type `text/csv` |

## Como adicionar um teste

1. Crie uma classe em `src/test/java` que estenda `BaseTest`.
2. Use o caminho sem o domínio, por exemplo `/api/v1/campanhas`.
3. Sempre confira o status junto com o resto. Um teste sem conferência passa mesmo com 404.

```java
@Test
void deveRetornar200() {
    given()
    .when()
        .get("/api/v1/dashboard/kpis")
    .then()
        .statusCode(200)
        .contentType(ContentType.JSON);
}
```

## Limitações

- Os exemplos do mock são suposições. Um teste verde contra o mock prova que o mock funciona, e não que a API real responderá assim.
- Os testes conferem só status e content-type. Os campos do body entram quando o contrato for confirmado com o Backend.
- O mock recusa POST sem corpo com 400. Por isso o teste do exportar envia `{}` como JSON.

## Documentos relacionados

- Relatório do trabalho, em `docs/backend/relatorio-testes-api-doasync.md`.
- Cenários de teste de API, em `docs/backend/cenarios-testes-api.md`.
