"""Fabrica de WebDriver.

Centraliza a criacao do navegador para que os testes nao saibam detalhes de
configuracao. Suporta Chrome e Firefox, modo headless e usa o Selenium Manager
(nativo do Selenium 4) para resolver o driver, sem download manual.
"""
from selenium import webdriver
from selenium.webdriver.chrome.options import Options as ChromeOptions
from selenium.webdriver.firefox.options import Options as FirefoxOptions

from config.settings import settings


def create_driver():
    """Cria e devolve um WebDriver conforme o ambiente configurado."""
    browser = settings.BROWSER

    if browser == "chrome":
        options = ChromeOptions()
        if settings.HEADLESS:
            options.add_argument("--headless=new")
        options.add_argument("--window-size=1920,1080")
        options.add_argument("--no-sandbox")
        options.add_argument("--disable-dev-shm-usage")
        driver = webdriver.Chrome(options=options)

    elif browser == "firefox":
        options = FirefoxOptions()
        if settings.HEADLESS:
            options.add_argument("--headless")
        driver = webdriver.Firefox(options=options)

    else:
        raise ValueError(
            f"Navegador nao suportado: '{browser}'. Use 'chrome' ou 'firefox'."
        )

    driver.implicitly_wait(settings.WAIT_TIMEOUT)
    return driver
