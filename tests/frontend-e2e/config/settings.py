"""Configuracao de ambiente da suite E2E.

Le as variaveis de .env (via python-dotenv) com fallback para variaveis de
ambiente do sistema. Mantem a URL base parametrizada por ambiente, o mesmo
principio da suite de API (ADR-005): a mesma suite roda contra local,
homologacao ou producao sem alterar codigo.
"""
import os

from dotenv import load_dotenv

load_dotenv()


def _get_bool(name: str, default: bool) -> bool:
    valor = os.getenv(name)
    if valor is None:
        return default
    return valor.strip().lower() in ("1", "true", "yes", "sim")


class Settings:
    BASE_URL: str = os.getenv("BASE_URL", "http://localhost:5173")
    BROWSER: str = os.getenv("BROWSER", "chrome").strip().lower()
    HEADLESS: bool = _get_bool("HEADLESS", False)
    WAIT_TIMEOUT: int = int(os.getenv("WAIT_TIMEOUT", "10"))


settings = Settings()
