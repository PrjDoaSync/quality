package dashboard;

import config.BaseTest;
import config.Endpoints;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

/**
 * GET /api/v1/dashboard/financeiro
 */
@DisplayName("Dashboard - Financeiro")
class DashboardFinanceiroTest extends BaseTest {

    @Test
    @DisplayName("Positivo: retorna 200 e JSON")
    void deveRetornar200ComJson() {
        given()
            .spec(spec())
        .when()
            .get(Endpoints.DASHBOARD_FINANCEIRO)
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    @DisplayName("Negativo: caminho inexistente retorna 404")
    void deveRetornar404EmCaminhoInexistente() {
        given()
            .spec(spec())
        .when()
            .get(Endpoints.DASHBOARD_FINANCEIRO + "-inexistente")
        .then()
            .statusCode(404);
    }
}
