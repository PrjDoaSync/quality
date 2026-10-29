package dashboard;

import config.BaseTest;
import config.Endpoints;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

/**
 * GET /api/v1/dashboard/kpis
 *
 * Segue a ADR-002: pelo menos 1 caso positivo e 1 negativo por endpoint.
 * Toda asserção confere o status HTTP junto com o resto.
 */
@DisplayName("Dashboard - KPIs")
class DashboardKpisTest extends BaseTest {

    @Test
    @DisplayName("Positivo: retorna 200 e JSON")
    void deveRetornar200ComJson() {
        given()
            .spec(spec())
        .when()
            .get(Endpoints.DASHBOARD_KPIS)
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
            .get(Endpoints.DASHBOARD_KPIS + "-inexistente")
        .then()
            .statusCode(404);
    }
}
