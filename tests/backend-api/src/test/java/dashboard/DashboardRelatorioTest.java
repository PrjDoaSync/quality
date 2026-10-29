package dashboard;

import config.BaseTest;
import config.Endpoints;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

/**
 * POST /api/v1/relatorios/exportar
 *
 * O endpoint devolve arquivo (CSV), entao a conferência é feita no header
 * Content-Type, nao em campos de JSON. O mock recusa POST sem corpo com 400,
 * por isso o caso positivo envia um corpo JSON vazio ({}).
 */
@DisplayName("Relatorios - Exportar")
class DashboardRelatorioTest extends BaseTest {

    @Test
    @DisplayName("Positivo: retorna 200 e CSV")
    void deveRetornar200ComCsv() {
        given()
            .spec(spec())
            .body("{}")
        .when()
            .post(Endpoints.RELATORIOS_EXPORTAR)
        .then()
            .statusCode(200)
            .contentType(containsString("text/csv"));
    }

    @Test
    @DisplayName("Negativo: caminho inexistente retorna 404")
    void deveRetornar404EmCaminhoInexistente() {
        given()
            .spec(spec())
            .body("{}")
        .when()
            .post(Endpoints.RELATORIOS_EXPORTAR + "-inexistente")
        .then()
            .statusCode(404);
    }
}
