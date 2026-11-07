package campanhas;

import config.BaseTest;
import config.Endpoints;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * GET /api/v1/campanhas - contrato dos itens da listagem e campos calculados.
 *
 * Segue a ADR-002: estende BaseTest, usa spec() e Endpoints.CAMPANHAS, e toda
 * asserção confere o status HTTP junto com o resto. As asserções são por
 * relação (ex.: percentual x valores), nunca por contagem fixa, porque os
 * dados da API local são fixtures em memória e podem mudar.
 *
 * Roda contra a API local do backend: mvn test -DbaseUrl=http://localhost:8080
 */
@DisplayName("Campanhas - contrato do item e cálculos")
class CampanhasContratoTest extends BaseTest {

    /** Tolerância de arredondamento: a API arredonda percentualAtingido a 2 casas. */
    private static final double TOLERANCIA = 0.01;

    @Test
    @DisplayName("Positivo: cada item traz todos os campos do contrato")
    void cadaItemDeveTerTodosOsCamposDoContrato() {
        given()
            .spec(spec())
        .when()
            .get(Endpoints.CAMPANHAS)
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("data", not(empty()))
            .body("data.id", everyItem(notNullValue()))
            .body("data.titulo", everyItem(notNullValue()))
            .body("data.descricao", everyItem(notNullValue()))
            .body("data.metaValor", everyItem(notNullValue()))
            .body("data.valorArrecadado", everyItem(notNullValue()))
            .body("data.percentualAtingido", everyItem(notNullValue()))
            .body("data.recebendoDoacoes", everyItem(notNullValue()))
            .body("data.status", everyItem(notNullValue()))
            .body("data.entidade", everyItem(notNullValue()))
            .body("data.entidade.id", everyItem(notNullValue()))
            .body("data.entidade.nomeFantasia", everyItem(notNullValue()));
    }

    @Test
    @DisplayName("Positivo: percentualAtingido é coerente com valorArrecadado / metaValor")
    void percentualDeveSerCoerenteComOsValores() {
        JsonPath json = given()
            .spec(spec())
        .when()
            .get(Endpoints.CAMPANHAS)
        .then()
            .statusCode(200)
            .extract().jsonPath();

        List<Map<String, Object>> itens = json.getList("data");
        assertFalse(itens.isEmpty(), "a listagem deveria trazer ao menos 1 campanha");

        for (Map<String, Object> item : itens) {
            double meta = ((Number) item.get("metaValor")).doubleValue();
            double arrecadado = ((Number) item.get("valorArrecadado")).doubleValue();
            double percentual = ((Number) item.get("percentualAtingido")).doubleValue();

            double esperado = meta > 0 ? (arrecadado / meta) * 100 : 0;

            assertEquals(esperado, percentual, TOLERANCIA,
                    "percentualAtingido incoerente na campanha " + item.get("id"));
        }
    }

    @Test
    @DisplayName("Negativo: campanha ENCERRADA nunca recebe doações (recebendoDoacoes=false)")
    void campanhaEncerradaNaoDeveReceberDoacoes() {
        JsonPath json = given()
            .spec(spec())
        .when()
            .get(Endpoints.CAMPANHAS)
        .then()
            .statusCode(200)
            .extract().jsonPath();

        List<Boolean> recebendoEncerradas =
                json.getList("data.findAll { it.status == 'ENCERRADA' }.recebendoDoacoes");

        // Sem nenhuma ENCERRADA na massa de dados o teste não tem o que provar:
        // melhor abortar (skipped) do que passar vazio e dar falsa confiança.
        Assumptions.assumeFalse(recebendoEncerradas.isEmpty(),
                "a API não tem campanha ENCERRADA para validar");

        for (Boolean recebendo : recebendoEncerradas) {
            assertTrue(recebendo != null && !recebendo,
                    "campanha ENCERRADA não pode ter recebendoDoacoes=true");
        }
    }
}
