"""Page Object do formulario de referencia do Selenium.

Usa a pagina oficial de exemplo do Selenium (a mesma do estudo
docs/selenium/primeiro-teste.py). Serve ao smoke test, que valida que o
ambiente E2E esta de pe (driver, Page Objects, esperas e asercoes) mesmo antes
do frontend real do DoaSync existir.
"""
from selenium.webdriver.common.by import By

from pages.base_page import BasePage

URL_FORM = "https://www.selenium.dev/selenium/web/web-form.html"


class WebFormPage(BasePage):
    CAMPO_TEXTO = (By.NAME, "my-text")
    BOTAO_SUBMIT = (By.CSS_SELECTOR, "button")
    MENSAGEM = (By.ID, "message")

    def abrir_formulario(self):
        self.driver.get(URL_FORM)
        return self

    def preencher_texto(self, valor: str):
        self.digitar(self.CAMPO_TEXTO, valor)

    def enviar(self):
        self.clicar(self.BOTAO_SUBMIT)

    def mensagem_confirmacao(self) -> str:
        return self.texto_de(self.MENSAGEM)
