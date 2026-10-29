package config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

/**
 * Configuracao base de todos os testes de API.
 *
 * Define a URL base uma unica vez e expoe uma RequestSpecification pronta,
 * com Content-Type JSON e, quando houver, o header Authorization. Toda classe
 * de teste deve estender BaseTest e usar {@link #spec()} no given().
 */
public abstract class BaseTest {

    protected static RequestSpecification spec;

    @BeforeAll
    static void configurar() {
        RestAssured.baseURI = Environment.baseUrl();

        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON);

        if (Environment.hasToken()) {
            builder.addHeader("Authorization", "Bearer " + Environment.token());
        }

        spec = builder.build();
    }

    /** Spec reaproveitavel para o given() de cada teste. */
    protected static RequestSpecification spec() {
        return spec;
    }
}
