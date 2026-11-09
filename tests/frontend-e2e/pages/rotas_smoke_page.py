import os

from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait

from pages.base_page import BasePage


class RotasSmokePage(BasePage):
    """Page Object para validar o carregamento básico das rotas (Smoke Tests)."""

    def validar_rota(
        self,
        caminho: str,
        localizador_esperado: tuple,
        timeout: int = 10,
        nome_evidencia: str | None = None,
    ) -> bool:
        """
        Navega para a rota parametrizada a partir da BASE_URL e verifica se o elemento esperado aparece.
        """
        self.abrir(caminho)
        WebDriverWait(self.driver, timeout).until(
            EC.presence_of_element_located(localizador_esperado)
        )
        if nome_evidencia:
            diretorio_evidencias = os.path.join("reports", "evidencias")
            os.makedirs(diretorio_evidencias, exist_ok=True)
            caminho_evidencia = os.path.join(
                diretorio_evidencias, f"{nome_evidencia}.png"
            )
            self.driver.save_screenshot(caminho_evidencia)
        return True