package config;

/**
 * Catalogo central de endpoints da API do DoaSync.
 *
 * Mantem os caminhos em um unico lugar para evitar strings soltas pelos testes
 * e facilitar a atualizacao quando o contrato OpenAPI for confirmado (ADR-001).
 * Baseado em docs/backend/doasync-api-tests/MapeamentoAPI.md.
 */
public final class Endpoints {

    private static final String V1 = Environment.apiPrefix();

    private Endpoints() {
    }

    // Dashboard / relatorios (US-01, ja cobertos pelo mock)
    public static final String DASHBOARD_KPIS = V1 + "/dashboard/kpis";
    public static final String DASHBOARD_FINANCEIRO = V1 + "/dashboard/financeiro";
    public static final String DASHBOARD_AUDITORIA = V1 + "/dashboard/auditoria";
    public static final String RELATORIOS_EXPORTAR = V1 + "/relatorios/exportar";

    // Nucleo de negocio (contrato a confirmar com o Backend)
    public static final String AUTH_LOGIN = V1 + "/auth/login";
    public static final String CAMPANHAS = V1 + "/campanhas";
    public static final String DOACOES = V1 + "/doacoes";
    public static final String ENTIDADES = V1 + "/entidades";

    public static String campanhaPorId(String id) {
        return CAMPANHAS + "/" + id;
    }

    public static String doacaoPorId(String id) {
        return DOACOES + "/" + id;
    }
}
