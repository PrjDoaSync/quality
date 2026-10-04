package dashboard;

import config.BaseTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class DashboardRelatorioTest extends BaseTest{
    @Test
    void deveRetornar200ComTexto(){
        given()
                .when()
                    .post("/api/v1/relatorios/exportar")
                .then()
                .statusCode(200)
                .contentType("text/csv");
    }
}
