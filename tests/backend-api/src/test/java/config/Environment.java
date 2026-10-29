package config;

/**
 * Resolve a configuracao de ambiente para a suite de API.
 *
 * A URL base vem da propriedade de sistema "baseUrl". Se nao for informada,
 * usa o mock do Postman como padrao, mantendo o comportamento do projeto de
 * estudo doasync-api-tests. Isso permite rodar a mesma suite contra mock,
 * homologacao ou producao sem alterar codigo (ver ADR-005).
 */
public final class Environment {

    private static final String DEFAULT_BASE_URL =
            "https://44b6b107-644e-4eea-b0a6-572f9f6f7045.mock.pstmn.io";

    private Environment() {
    }

    /** URL base da API sob teste. */
    public static String baseUrl() {
        return System.getProperty("baseUrl", DEFAULT_BASE_URL);
    }

    /** Prefixo de versao da API, conforme padronizado na ADR-001. */
    public static String apiPrefix() {
        return "/api/v1";
    }

    /**
     * Token Bearer opcional. Quando a API real exigir autenticacao, passe
     * -Dtoken=xxx na execucao. Enquanto o mock for publico, fica vazio.
     */
    public static String token() {
        return System.getProperty("token", "");
    }

    public static boolean hasToken() {
        return !token().isBlank();
    }
}
