"""Page Object da Home Publica de Campanhas (Tela 01 do Figma).

Os localizadores abaixo sao placeholders a confirmar quando o frontend real
existir (data-testid e o seletor recomendado por ser estavel a mudancas de
estilo). Enquanto isso, o Page Object documenta a intencao de cada elemento e
serve de molde para os demais.
"""
from selenium.webdriver.common.by import By

from pages.base_page import BasePage


class HomePublicaPage(BasePage):
    # Localizadores (a confirmar com o Frontend)
    BANNER_PRINCIPAL = (By.CSS_SELECTOR, "[data-testid='banner-principal']")
    CARD_CAMPANHA_DESTAQUE = (By.CSS_SELECTOR, "[data-testid='campanha-destaque']")
    BOTAO_CONHECER_E_DOAR = (By.CSS_SELECTOR, "[data-testid='btn-conhecer-doar']")
    LISTA_CAMPANHAS = (By.CSS_SELECTOR, "[data-testid='lista-campanhas']")

    def abrir_home(self):
        return self.abrir("/")

    def banner_visivel(self) -> bool:
        return self.encontrar(self.BANNER_PRINCIPAL).is_displayed()

    def ir_para_campanha_destaque(self):
        self.clicar(self.BOTAO_CONHECER_E_DOAR)
