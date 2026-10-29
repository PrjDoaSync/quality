# backend-api — Testes de API (RestAssured + JUnit 5)

Ambiente de testes de integração/API do DoaSync. Evolui o projeto de estudo `docs/backend/doasync-api-tests` com configuração por ambiente, catálogo de endpoints, spec reaproveitável e casos positivos e negativos por endpoint (ADR-002).

Enquanto a API real não existe, a suíte roda contra o **mock do Postman** (público, dados fictícios). Para apontar para outra API, basta passar a URL na execução — nenhum teste precisa mudar.

## Pré-requisitos

- Java 21 ou superior.
- Maven 3.9 ou superior (ou o Maven embutido do IntelliJ).

## Como rodar

Contra o mock (padrão):

```bash
mvn test
```

Contra outra API (ex.: homologação):

```bash
mvn test -DbaseUrl=https://api-homolog.doasync
```

No PowerShell, use aspas para a URL não ser cortada no primeiro ponto:

```powershell
mvn test "-DbaseUrl=https://api-homolog.doasync"
```

Com token Bearer (quando a API exigir autenticação):

```powershell
mvn test "-DbaseUrl=https://api-homolog.doasync" "-Dtoken=SEU_TOKEN"
```

Uma classe só:

```bash
mvn test -Dtest=DashboardKpisTest
```

Relatórios ficam em `target/surefire-reports`.

## Estrutura

```
src/test/java
  config/
    Environment.java   Resolve baseUrl, prefixo /api/v1 e token
    BaseTest.java      Define a URL base e a RequestSpecification (JSON + Authorization)
    Endpoints.java     Catálogo central de caminhos da API
  dashboard/           Uma classe de teste por endpoint (US-01)
```

## Convenções (ADR-002)

- Toda classe de teste estende `BaseTest` e usa `spec()` no `given()`.
- No mínimo **1 caso positivo e 1 negativo** por endpoint.
- Toda asserção confere o **status HTTP** junto com o resto. Um teste sem conferência de status passa mesmo com 404.
- Caminhos ficam em `Endpoints`, não como strings soltas.

## Testes atuais

| Classe | Endpoint | Positivo | Negativo |
| --- | --- | --- | --- |
| `DashboardKpisTest` | GET `/api/v1/dashboard/kpis` | 200 + JSON | 404 em caminho inexistente |
| `DashboardFinanceiroTest` | GET `/api/v1/dashboard/financeiro` | 200 + JSON | 404 em caminho inexistente |
| `DashboardAuditoriaTest` | GET `/api/v1/dashboard/auditoria` | 200 + JSON | 404 em caminho inexistente |
| `DashboardRelatorioTest` | POST `/api/v1/relatorios/exportar` | 200 + `text/csv` | 404 em caminho inexistente |

## Como adicionar um teste

1. Crie uma classe em `src/test/java` que estenda `BaseTest`.
2. Adicione o caminho em `Endpoints` (sem o domínio, ex.: `/api/v1/campanhas`).
3. Escreva um caso positivo e um negativo; sempre confira o status.

```java
@Test
void deveRetornar200() {
    given().spec(spec())
    .when().get(Endpoints.DASHBOARD_KPIS)
    .then().statusCode(200).contentType(ContentType.JSON);
}
```

## Limitações

- Os exemplos do mock são suposições. Um teste verde contra o mock prova que o mock funciona, não que a API real responderá assim.
- Os testes conferem status e content-type. Asserções de campos do body entram quando o contrato OpenAPI for confirmado com o Backend (ADR-001).
