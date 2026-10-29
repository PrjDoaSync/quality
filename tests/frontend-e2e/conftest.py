"""Fixtures compartilhadas do pytest.

Fornece o WebDriver para cada teste e tira screenshot automaticamente quando
um teste falha, anexando a evidencia na pasta reports/.
"""
import os
from datetime import datetime

import pytest

from config.driver_factory import create_driver

REPORTS_DIR = "reports"


@pytest.fixture
def driver():
    """Abre o navegador antes do teste e fecha ao final."""
    drv = create_driver()
    yield drv
    drv.quit()


@pytest.hookimpl(hookwrapper=True)
def pytest_runtest_makereport(item, call):
    """Captura screenshot quando um teste que usa 'driver' falha."""
    outcome = yield
    report = outcome.get_result()

    if report.when == "call" and report.failed:
        drv = item.funcargs.get("driver")
        if drv is not None:
            os.makedirs(REPORTS_DIR, exist_ok=True)
            timestamp = datetime.now().strftime("%Y%m%d-%H%M%S")
            nome = item.name.replace("/", "_")
            caminho = os.path.join(REPORTS_DIR, f"FALHA-{nome}-{timestamp}.png")
            try:
                drv.save_screenshot(caminho)
                print(f"\nScreenshot da falha salvo em: {caminho}")
            except Exception as exc:  # nao mascarar a falha original do teste
                print(f"\nNao foi possivel salvar screenshot: {exc}")
