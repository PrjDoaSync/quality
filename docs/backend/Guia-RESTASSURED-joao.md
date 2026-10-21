# Guia Rápido — REST Assured

## 1. Objetivo

Este guia apresenta os conceitos básicos do **REST Assured** para automação de testes de APIs REST utilizando Java.

Ao final, a equipe deverá ser capaz de:

- Configurar o REST Assured em um projeto Maven;
- Estruturar testes utilizando `given / when / then`;
- Validar status HTTP;
- Validar headers da resposta;
- Validar informações do body;
- Executar um teste automatizado de API.

> **Versão utilizada neste guia:** REST Assured `6.0.1`.

---

## 2. O que é REST Assured?

O **REST Assured** é uma biblioteca Java utilizada para testar APIs REST de forma automatizada.

Ela permite enviar requisições HTTP e validar suas respostas de maneira simples e legível.

Um teste pode ser estruturado seguindo o padrão:

```text
given → when → then
```

Onde:

- **given**: configura as condições da requisição;
- **when**: executa a ação/requisição;
- **then**: valida o resultado esperado.

Exemplo:

```java
given()
    .when()
        .get("/get")
    .then()
        .statusCode(200);
```

---

## 3. Instalação e configuração

### 3.1. Pré-requisitos

O projeto deve possuir:

- Java instalado;
- Maven configurado;
- Um projeto Java com `pom.xml`;
- JUnit 5 para execução dos testes.

### 3.2. Dependências Maven

No arquivo `pom.xml`, adicione o REST Assured e o JUnit 5:

```xml
<dependencies>

    <!-- REST Assured -->
    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>rest-assured</artifactId>
        <version>6.0.1</version>
        <scope>test</scope>
    </dependency>

    <!-- JUnit 5 -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.13.4</version>
        <scope>test</scope>
    </dependency>

</dependencies>
```

Depois de salvar o `pom.xml`, atualize as dependências do Maven.

Pelo terminal:

```bash
mvn test
```

No Windows, utilizando o Maven Wrapper:

```powershell
.\mvnw.cmd test
```

---

## 4. Estrutura de um teste

A estrutura básica do REST Assured é:

```java
given()
    // configuração da requisição
.when()
    // execução da requisição
.then()
    // validações
;
```

### Given

É utilizado para preparar a requisição.

Exemplo:

```java
given()
    .header("Accept", "application/json")
```

Também pode ser utilizado para:

- Headers;
- Query parameters;
- Path parameters;
- Body;
- Autenticação;
- Cookies.

### When

É onde a requisição HTTP é executada.

Exemplo:

```java
.when()
    .get("/get")
```

Outros métodos HTTP:

```java
.get()
.post()
.put()
.patch()
.delete()
```

### Then

É utilizado para validar a resposta da API.

Exemplo:

```java
.then()
    .statusCode(200);
```

Também é possível validar:

- Body;
- Headers;
- Tempo de resposta;
- Campos JSON;
- Valores específicos.

---

# 5. Teste de exemplo

Para o exemplo deste guia será utilizado o endpoint público:

```text
https://httpbin.org/get
```

Esse endpoint responde a uma requisição GET retornando informações da própria requisição.

## 5.1. Criando o teste

Crie o arquivo:

```text
src/test/java/com/codemind/fieldops/RestAssuredExampleTest.java
```

Conteúdo:

```java
package com.codemind.fieldops;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;

class RestAssuredExampleTest {

    @Test
    void deveValidarEndpointGet() {

        given()
            .header("Accept", "application/json")

        .when()
            .get("https://httpbin.org/get")

        .then()
            // Validação do status HTTP
            .statusCode(200)

            // Validação do header
            .header("Content-Type", containsString("application/json"))

            // Validação do body
            .body("url", equalTo("https://httpbin.org/get"));
    }
}
```

---

# 6. Entendendo o teste

O teste acima possui três etapas principais.

### 6.1. Given

```java
given()
    .header("Accept", "application/json")
```

Aqui configuramos o header `Accept`, indicando que esperamos uma resposta em JSON.

### 6.2. When

```java
.when()
    .get("https://httpbin.org/get")
```

Aqui executamos uma requisição HTTP `GET`.

### 6.3. Then

```java
.then()
    .statusCode(200)
```

Verifica se a API respondeu com HTTP `200 OK`.

Depois:

```java
.header("Content-Type", containsString("application/json"))
```

Verifica se o header `Content-Type` indica uma resposta JSON.

Por fim:

```java
.body("url", equalTo("https://httpbin.org/get"));
```

Valida o campo `url` presente no JSON retornado pela API.

---

# 7. Executando o teste

No terminal, execute:

```bash
mvn test
```

Ou, utilizando o Maven Wrapper no Windows:

```powershell
.\mvnw.cmd test
```

Se o teste passar, o Maven deverá indicar que não houve falhas ou erros.

Isso demonstra que o REST Assured está configurado e que pelo menos um teste de API está funcionando.

---

# 8. Exemplos de validações

## Validar somente o status

```java
given()
.when()
    .get("/users")
.then()
    .statusCode(200);
```

## Validar um header

```java
given()
.when()
    .get("/users")
.then()
    .header("Content-Type", containsString("application/json"));
```

## Validar um campo do JSON

Para uma resposta:

```json
{
    "id": 10,
    "name": "João"
}
```

Podemos fazer:

```java
.then()
    .body("id", equalTo(10))
    .body("name", equalTo("João"));
```

## Validar múltiplos campos

```java
.then()
    .statusCode(200)
    .header("Content-Type", containsString("application/json"))
    .body("id", equalTo(10))
    .body("name", equalTo("João"));
```

---

# 9. Exemplo de POST

Para requisições que enviam dados, podemos utilizar `body()`:

```java
given()
    .contentType("application/json")
    .body("""
        {
            "name": "João",
            "job": "Analista de Sistemas"
        }
        """)

.when()
    .post("https://httpbin.org/post")

.then()
    .statusCode(200)
    .header("Content-Type", containsString("application/json"));
```

A ideia continua sendo a mesma:

```text
given → configurar
when  → executar
then  → validar
```

---

# 10. Boas práticas

## Organizar os testes

Uma estrutura possível:

```text
src
└── test
    └── java
        └── com
            └── codemind
                └── fieldops
                    ├── EquipmentControllerTest.java
                    ├── InspectionControllerTest.java
                    ├── ReviewControllerTest.java
                    └── UserControllerTest.java
```

## Evitar valores repetidos

Para vários testes, é possível definir uma URL base:

```java
import io.restassured.RestAssured;

@BeforeAll
static void setup() {
    RestAssured.baseURI = "http://localhost:8080";
}
```

Assim:

```java
given()
.when()
    .get("/users")
.then()
    .statusCode(200);
```

Em vez de repetir:

```java
.get("http://localhost:8080/users")
```

## Validar o comportamento esperado

Evite testes que verificam apenas se a API respondeu.

Prefira:

```java
.then()
    .statusCode(200)
    .body("id", equalTo(1))
    .body("name", equalTo("João"));
```

Dessa forma o teste verifica tanto o status quanto o conteúdo retornado.

---

# 11. Resumo dos principais métodos

| Método | Finalidade |
|---|---|
| `given()` | Configura a requisição |
| `when()` | Executa a requisição |
| `then()` | Inicia as validações |
| `get()` | Requisição GET |
| `post()` | Requisição POST |
| `put()` | Requisição PUT |
| `patch()` | Requisição PATCH |
| `delete()` | Requisição DELETE |
| `header()` | Define ou valida headers |
| `body()` | Define ou valida o body |
| `queryParam()` | Adiciona query parameter |
| `pathParam()` | Adiciona path parameter |
| `statusCode()` | Valida o status HTTP |
| `contentType()` | Valida o tipo de conteúdo |

---

# 12. Checklist da tarefa

- [x] Estudar a instalação e configuração básica do REST Assured;
- [x] Aprender a estrutura `given / when / then`;
- [x] Praticar validação de status;
- [x] Praticar validação de body;
- [x] Praticar validação de headers;
- [x] Criar teste de exemplo;
- [x] Documentar o uso no repositório.

## Critério de aceite

**Guia introdutório do REST Assured com ao menos um teste de exemplo.**

O teste apresentado neste documento valida:

- HTTP status `200`;
- Header `Content-Type`;
- Campo `url` do JSON retornado pela API.
