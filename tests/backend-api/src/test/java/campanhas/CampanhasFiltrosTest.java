package campanhas;

import config.BaseTest;
import config.Endpoints;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("Campanhas - filtros e busca")
public class CampanhasFiltrosTest extends BaseTest {

    @Test
    @DisplayName("status=ATIVA: todos os itens com status ATIVA")
    void filtroAtivaDeveRetornarSoAtivas() {
        given().spec(spec()).queryParam("status", "ATIVA")
                .when()
                    .get(Endpoints.CAMPANHAS)
                .then().statusCode(200)
                    .body("data", not(empty()))
                    .body("data.status", everyItem(equalTo("ATIVA")));
    }

    @Test
    @DisplayName("status=ENCERRADA: todos os itens com status ENCERRADA")
    void filtroEncerradaDeveRetornarSoEncerradas() {
        given().spec(spec()).queryParam("status", "ENCERRADA")
                .when()
                    .get(Endpoints.CAMPANHAS)
                .then().statusCode(200)
                    .body("data", not(empty()))
                    .body("data.status", everyItem(equalTo("ENCERRADA")));
    }
}
