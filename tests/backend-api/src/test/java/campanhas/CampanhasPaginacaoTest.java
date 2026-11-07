package campanhas;

import config.BaseTest;
import config.Endpoints;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

/**
 * GET /api/v1/campanhas - paginação da listagem pública.
 *
 * Segue a ADR-002: estende BaseTest, usa spec() e Endpoints.CAMPANHAS, e toda
 * asserção confere o status HTTP junto com o resto. As asserções são por
 * relação (ex.: "no máximo limit itens"), nunca por contagem fixa, porque os
 * dados da API local são fixtures em memória e podem mudar.
 *
 * Roda contra a API local do backend: mvn test -DbaseUrl=http://localhost:8080
 */
@DisplayName("Campanhas - paginação da listagem")
class CampanhasPaginacaoTest extends BaseTest {

    /** Limite máximo que a API aceita por página. */
    private static final int LIMITE_MAXIMO = 50;

    @Test
    @DisplayName("Positivo: sem parâmetros usa page=1 e limit=10 (padrões)")
    void semParametrosDeveUsarPaginaUmELimiteDez() {
        given()
            .spec(spec())
        .when()
            .get(Endpoints.CAMPANHAS)
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("meta.page", equalTo(1))
            .body("meta.limit", equalTo(10));
    }

    @Test
    @DisplayName("Positivo: page=2&limit=2 devolve no máximo 2 itens e meta.page=2")
    void paginaDoisComLimiteDoisDeveRespeitarOLimite() {
        given()
            .spec(spec())
            .queryParam("page", 2)
            .queryParam("limit", 2)
        .when()
            .get(Endpoints.CAMPANHAS)
        .then()
            .statusCode(200)
            .body("data", hasSize(lessThanOrEqualTo(2)))
            .body("meta.page", equalTo(2))
            .body("meta.limit", equalTo(2));
    }

    @Test
    @DisplayName("Negativo: limit=100 é limitado ao máximo da API (meta.limit=50)")
    void limiteAcimaDoMaximoDeveSerLimitadoACinquenta() {
        given()
            .spec(spec())
            .queryParam("limit", 100)
        .when()
            .get(Endpoints.CAMPANHAS)
        .then()
            .statusCode(200)
            .body("meta.limit", equalTo(LIMITE_MAXIMO))
            .body("data", hasSize(lessThanOrEqualTo(LIMITE_MAXIMO)));
    }

    @Test
    @DisplayName("Negativo: page=999 (além do fim) devolve 200 com data vazio")
    void paginaInexistenteDeveDevolverListaVazia() {
        given()
            .spec(spec())
            .queryParam("page", 999)
        .when()
            .get(Endpoints.CAMPANHAS)
        .then()
            .statusCode(200)
            .body("data", empty());
    }
}
