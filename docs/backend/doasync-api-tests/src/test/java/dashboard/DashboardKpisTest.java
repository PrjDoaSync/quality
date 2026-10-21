package dashboard;

import config.BaseTest;
import  io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;


public class DashboardKpisTest extends  BaseTest{
    @Test
    void deveRetornar200ComJson(){
        given()
            .when()
                .get("/api/v1/dashboard/kpis")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }
}
