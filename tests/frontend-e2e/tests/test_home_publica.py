"""Testes E2E da Home Publica de Campanhas (Tela 01).

Dependem do frontend real do DoaSync rodando em BASE_URL. Enquanto o front nao
existe, ficam marcados com @pytest.mark.e2e e podem ser pulados com:

    pytest -m "not e2e"

Servem de molde para os fluxos criticos previstos na ADR-002
(campanha -> doacao -> dashboard).
"""
import pytest

from pages.home_publica_page import HomePublicaPage


@pytest.mark.e2e
def test_home_exibe_banner_principal(driver):
    pagina = HomePublicaPage(driver)
    pagina.abrir_home()

    assert pagina.banner_visivel()


@pytest.mark.e2e
def test_home_navega_para_campanha_destaque(driver):
    pagina = HomePublicaPage(driver)
    pagina.abrir_home()

    pagina.ir_para_campanha_destaque()

    # Ao clicar em "Conhecer e doar", a URL deve mudar para a pagina de detalhes.
    assert "/campanhas/" in driver.current_url
