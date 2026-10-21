package config;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {

    @BeforeAll
    static void configurar() {
        RestAssured.baseURI = System.getProperty("baseUrl", "https://44b6b107-644e-4eea-b0a6-572f9f6f7045.mock.pstmn.io");
    }
}