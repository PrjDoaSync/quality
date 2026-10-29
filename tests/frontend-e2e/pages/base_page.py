"""Classe base dos Page Objects.

Concentra as interacoes comuns com a pagina (abrir, achar elemento com espera,
clicar, digitar, ler texto), para que os Page Objects especificos fiquem
limpos e os testes nao falem diretamente com o Selenium.
"""
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait

from config.settings import settings


class BasePage:
    def __init__(self, driver):
        self.driver = driver
        self.wait = WebDriverWait(driver, settings.WAIT_TIMEOUT)

    def abrir(self, caminho: str = ""):
        """Navega para BASE_URL + caminho relativo."""
        url = settings.BASE_URL.rstrip("/") + "/" + caminho.lstrip("/")
        self.driver.get(url.rstrip("/"))
        return self

    def encontrar(self, localizador):
        """Espera o elemento estar presente e o devolve."""
        return self.wait.until(EC.presence_of_element_located(localizador))

    def clicar(self, localizador):
        self.wait.until(EC.element_to_be_clickable(localizador)).click()

    def digitar(self, localizador, texto: str):
        campo = self.encontrar(localizador)
        campo.clear()
        campo.send_keys(texto)

    def texto_de(self, localizador) -> str:
        return self.encontrar(localizador).text

    def titulo(self) -> str:
        return self.driver.title
