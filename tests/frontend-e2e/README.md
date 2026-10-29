# frontend-e2e — Testes de interface (Selenium + pytest)

Ambiente de testes ponta a ponta da SPA do DoaSync. Evolui o estudo `docs/selenium/primeiro-teste.py` para uma estrutura de projeto com Page Objects, configuração por ambiente, fixtures do pytest, screenshot automático em falha e relatório HTML.

Cobre a camada de topo da pirâmide de testes (ADR-002): poucos fluxos críticos, como **campanha → doação → dashboard**.

## Pré-requisitos

- Python 3.11 ou superior.
- Google Chrome instalado (o Selenium 4 resolve o driver sozinho via Selenium Manager).
- `pip`.

## Instalação

```powershell
cd tests/frontend-e2e
python -m venv .venv
.venv\Scripts\Activate.ps1
pip install -r requirements.txt
```

## Configuração por ambiente

Copie `.env.example` para `.env` e ajuste:

```
BASE_URL=http://localhost:5173   # URL da SPA sob teste
BROWSER=chrome                   # chrome | firefox
HEADLESS=false                   # true em CI
WAIT_TIMEOUT=10                  # segundos
```

A mesma suíte roda contra local, homologação ou produção só trocando `BASE_URL` — mesmo princípio da suíte de API (ADR-005).

## Como rodar

Suíte completa (gera `reports/report.html`):

```powershell
pytest
```

Só o smoke test (valida o ambiente, não depende do front real):

```powershell
pytest -m smoke
```

Pulando os testes que exigem o frontend real:

```powershell
pytest -m "not e2e"
```

Um arquivo só:

```powershell
pytest tests/test_smoke_ambiente.py
```

## Estrutura

```
frontend-e2e/
├── config/
│   ├── settings.py          Lê .env (BASE_URL, BROWSER, HEADLESS, timeout)
│   └── driver_factory.py    Cria o WebDriver (Chrome/Firefox, headless)
├── pages/                   Page Objects
│   ├── base_page.py         Interações comuns (abrir, clicar, digitar, esperar)
│   ├── home_publica_page.py Tela 01 — Home Pública de Campanhas
│   └── web_form_page.py     Formulário de referência do Selenium (smoke)
├── tests/
│   ├── test_smoke_ambiente.py  Valida o ambiente (roda sem o front real)
│   └── test_home_publica.py    E2E da Home (depende de BASE_URL)
├── conftest.py              Fixture do driver + screenshot em falha
├── pytest.ini               Configuração e marcadores
└── requirements.txt
```

## Marcadores (pytest)

- `smoke` — cenários rápidos de saúde do ambiente/UI.
- `e2e` — fluxos ponta a ponta (dependem do front real).
- `negativo` — cenários de exceção e validação.

## Padrão Page Object

Os testes nunca falam direto com o Selenium: eles usam Page Objects. Cada tela do Figma vira uma classe em `pages/`, com os localizadores e as ações daquela tela. Isso mantém os testes legíveis e concentra a manutenção num só lugar quando a UI muda.

Os localizadores em `home_publica_page.py` usam `data-testid` e estão marcados como **a confirmar com o Frontend** — atributos de teste dedicados são mais estáveis que classes de estilo.

## Evidências

- Relatório HTML da execução: `reports/report.html`.
- Screenshot de cada teste que falhar: `reports/FALHA-<teste>-<timestamp>.png`.
