package campanhas;

import config.BaseTest;
import config.Endpoints;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Campanhas - alias sem prefixo")
class CampanhasAliasTest extends BaseTest {

    @Test
    @DisplayName("GET /campanhas devolve o mesmo que GET /api/v1/campanhas")
    void aliasDeCampanhasDeveSerIgualAoPrefixado() {
        String c1 = given().spec(spec())
                .when().get(Endpoints.CAMPANHAS)
                .then().statusCode(200).extract().asString();

        String c2 = given().spec(spec())
                .when().get(Endpoints.CAMPANHAS_ALIAS)
                .then().statusCode(200).extract().asString();

        assertEquals(c1,c2);
    }

    @Test
    @DisplayName("GET /health devolve o mesmo que GET /api/v1/health")
    void aliasDeHealthDeveSerIgualAoPrefixado() {
        JsonPath h1 = given().spec(spec())
                .when().get(Endpoints.HEALTH)
                .then().statusCode(200).extract().jsonPath();

        JsonPath h2 = given().spec(spec())
                .when().get(Endpoints.HEALTH_ALIAS)
                .then().statusCode(200).extract().jsonPath();

        // O "timestamp" muda a cada chamada, então comparamos só os campos estáveis.
        assertEquals(h1.getString("status"), h2.getString("status"));
        assertEquals(h1.getString("modulo"), h2.getString("modulo"));
        assertEquals(h1.getString("versao"), h2.getString("versao"));
    }

}
