import pytest
from selenium.webdriver.common.by import By
from pages.rotas_smoke_page import RotasSmokePage

ROTAS_SMOKE = [
    ("#116", "issue_116_home", "/", (By.XPATH, "//h1 | //*[@role='banner']"), "AC-01 · Home"),
    (
        "#117",
        "issue_117_explorar",
        "/explorar",
        (By.XPATH, "//*[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'campanha') or @type='search']"),
        "AC-02 · Explorar campanhas",
    ),
    ("#118", "issue_118_campanha_detalhe", "/campanhas/1", (By.XPATH, "//h1 | //main//*[self::h2 or self::h3]"), "AC-03 · Detalhe da campanha"),
    (
        "#119",
        "issue_119_doar",
        "/doar/1",
        (By.XPATH, "//button[normalize-space()] | //*[@role='button'][normalize-space()]"),
        "AC-04 · Fluxo de doação",
    ),
    (
        "#120",
        "issue_120_login",
        "/login",
        (By.XPATH, "//input[@type='email' or @type='password' or @name='email' or @name='password']"),
        "AC-05 · Login",
    ),
    (
        "#121",
        "issue_121_dashboard",
        "/dashboard",
        (By.XPATH, "//h1 | //main//*[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'kpi') or contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'visão geral')]"),
        "AC-06 · Dashboard",
    ),
    (
        "#122",
        "issue_122_dashboard_campanha",
        "/dashboard/campanha",
        (By.XPATH, "//h1 | //main//*[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'anal') or @role='img']"),
        "AC-07 · Dashboard analítico",
    ),
    ("#123", "issue_123_campanhas_gestao", "/campanhas", (By.XPATH, "//table | //main//*[self::article or @role='row']"), "AC-08 · Campanhas da entidade"),
]

@pytest.mark.smoke
@pytest.mark.e2e
@pytest.mark.parametrize(
    "issue, nome_evidencia, rota, localizador, descricao", ROTAS_SMOKE
)
def test_validar_rotas_smoke(
    driver, issue, nome_evidencia, rota, localizador, descricao
):
    pagina = RotasSmokePage(driver)
    visivel = pagina.validar_rota(
        rota, localizador, nome_evidencia=nome_evidencia
    )
    assert visivel, f"[{issue}] Falha ao carregar rota '{rota}' ({descricao})."