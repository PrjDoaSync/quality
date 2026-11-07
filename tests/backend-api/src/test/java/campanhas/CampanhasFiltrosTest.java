package campanhas;

import config.BaseTest;
import config.Endpoints;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    @DisplayName("busca=apae retorna itens, e busca=APAE retorna o mesmo total")
    void buscaDeveIgnorarMaiusculasEMinusculas() {
        int totalMinusculo = given().spec(spec()).queryParam("busca", "apae")
                .when()
                    .get(Endpoints.CAMPANHAS)
                .then().statusCode(200)
                    .body("data", not(empty()))
                    .extract().path("meta.total");

        int totalMaiusculo = given().spec(spec()).queryParam("busca", "APAE")
                .when()
                    .get(Endpoints.CAMPANHAS)
                .then().statusCode(200)
                    .extract().path("meta.total");

        assertEquals(totalMinusculo, totalMaiusculo);
    }

    @Test
    @DisplayName("busca=zzzzzz: data vazio e meta.total=0")
    void buscaSemResultadoDeveRetornarListaVazia() {
        given().spec(spec()).queryParam("busca", "zzzzzz")
                .when()
                    .get(Endpoints.CAMPANHAS)
                .then().statusCode(200)
                    .body("data", empty())
                    .body("meta.total", equalTo(0));
    }

    @Test
    @DisplayName("entidadeId válido: todos os itens são da mesma entidade")
    void filtroPorEntidadeValidaDeveRetornarSoDaquelaEntidade() {
        // Captura um entidadeId real da própria listagem, em vez de fixar um valor.
        String entidadeId = given().spec(spec())
                .when()
                    .get(Endpoints.CAMPANHAS)
                .then().statusCode(200)
                    .body("data", not(empty()))
                    .extract().path("data[0].entidade.id");

        given().spec(spec()).queryParam("entidadeId", entidadeId)
                .when()
                    .get(Endpoints.CAMPANHAS)
                .then().statusCode(200)
                    .body("data", not(empty()))
                    .body("data.entidade.id", everyItem(equalTo(entidadeId)));
    }

    @Test
    @DisplayName("entidadeId inexistente: data vazio")
    void filtroPorEntidadeInexistenteDeveRetornarListaVazia() {
        given().spec(spec()).queryParam("entidadeId", "00000000-0000-0000-0000-000000000000")
                .when()
                    .get(Endpoints.CAMPANHAS)
                .then().statusCode(200)
                    .body("data", empty());
    }
}
