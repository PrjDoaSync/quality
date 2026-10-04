package config;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {

    @BeforeAll
    static void configurar() {
        RestAssured.baseURI = System.getProperty("baseUrl", "http://localhost:3001");
    }
}