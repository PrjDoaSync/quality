"""Smoke test do ambiente E2E.

Valida que o ambiente esta de pe (driver, Page Object, esperas e asercoes)
usando a pagina de referencia do Selenium. Equivale ao estudo
docs/selenium/primeiro-teste.py, agora dentro da estrutura de Page Objects e
pytest. Nao depende do frontend real do DoaSync.
"""
import pytest

from pages.web_form_page import WebFormPage


@pytest.mark.smoke
def test_formulario_de_referencia_confirma_envio(driver):
    pagina = WebFormPage(driver)
    pagina.abrir_formulario()

    pagina.preencher_texto("Teste com Selenium - DoaSync")
    pagina.enviar()

    assert pagina.mensagem_confirmacao() == "Received!"


@pytest.mark.smoke
@pytest.mark.negativo
def test_mensagem_nao_aparece_antes_do_envio(driver):
    """Negativo: a mensagem de confirmacao so existe apos o envio."""
    from selenium.webdriver.common.by import By

    pagina = WebFormPage(driver)
    pagina.abrir_formulario()

    mensagem = driver.find_element(By.ID, "message")
    assert mensagem.text == ""
