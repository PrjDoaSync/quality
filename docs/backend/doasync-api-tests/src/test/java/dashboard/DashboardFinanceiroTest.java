package dashboard;

import config.BaseTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;


public class DashboardFinanceiroTest extends BaseTest{
    @Test
    void deveRetornar200ComArray(){
        given()
            .when()
                .get("/api/v1/dashboard/financeiro")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }
}
