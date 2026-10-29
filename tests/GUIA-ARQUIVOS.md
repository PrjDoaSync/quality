# Guia dos arquivos do ambiente de testes

> **Projeto:** DoaSync — Sistema Digital de Arrecadação de Doações
> **Equipe:** 🟥 Grupo 5 — Qualidade de Software (QA)
> **Objetivo:** explicar, arquivo por arquivo, para que serve cada parte dos dois ambientes de teste.

A pasta `tests/` guarda dois ambientes, um para cada lado da aplicação:

| Ambiente | Pasta | Tecnologia | O que testa |
|---|---|---|---|
| Backend / API | `tests/backend-api` | RestAssured + JUnit 5 (Java/Maven) | As rotas da API (`/api/v1/...`) |
| Frontend / E2E | `tests/frontend-e2e` | Selenium + pytest (Python) | A interface pelo navegador |

Os dois seguem a estratégia de testes da [ADR-002](../docs/adr/adr-002-estrategia-testes-microsservicos.md) e o princípio de URL base parametrizada por ambiente da [ADR-005](../docs/adr/adr-005-quality-gates-ci-cd.md).

---

## 1. Ambiente de backend — `tests/backend-api`

Projeto Maven em Java. Evolui o estudo `docs/backend/doasync-api-tests`.

```
tests/backend-api/
├── pom.xml
├── README.md
├── .gitignore
└── src/test/java
    ├── config/
    │   ├── Environment.java
    │   ├── BaseTest.java
    │   └── Endpoints.java
    └── dashboard/
        ├── DashboardKpisTest.java
        ├── DashboardFinanceiroTest.java
        ├── DashboardAuditoriaTest.java
        └── DashboardRelatorioTest.java
```

### `pom.xml`
Arquivo de configuração do Maven. Declara as dependências (RestAssured, JUnit 5, validador de JSON Schema), a versão do Java e o plugin **Surefire**, que é quem encontra e executa as classes de teste. Também define a propriedade `baseUrl` (a URL da API sob teste) e a repassa para os testes, permitindo trocar de ambiente sem alterar código: `mvn test -DbaseUrl=https://...`.

### `README.md`
Instruções de como rodar a suíte: pré-requisitos (Java e Maven), comandos de execução, como apontar para outra API, estrutura de pastas e as convenções de escrita de teste.

### `.gitignore`
Lista o que o Git deve ignorar (pasta `target/` com os arquivos compilados, arquivos do IntelliJ/VS Code). Evita subir lixo de build para o repositório.

### `config/Environment.java`
Resolve a configuração do ambiente. Lê a propriedade `baseUrl` (com o mock do Postman como padrão), monta o prefixo `/api/v1` e lê um token opcional (`-Dtoken=...`) para quando a API exigir autenticação. Concentra "de onde vêm os dados de ambiente" em um lugar só.

### `config/BaseTest.java`
Classe base que **todas** as classes de teste herdam. Antes de rodar os testes, ela define a URL base uma única vez e monta uma `RequestSpecification` pronta — com `Content-Type: application/json` e, se houver token, o header `Authorization`. Assim cada teste só se preocupa com a chamada em si.

### `config/Endpoints.java`
Catálogo central com os caminhos da API (ex.: `/api/v1/dashboard/kpis`, `/api/v1/campanhas`). Mantém os endpoints em um só lugar, em vez de espalhar strings pelos testes. Quando o contrato mudar, atualiza-se aqui.

### `dashboard/Dashboard*Test.java`
As classes de teste de fato, uma por endpoint da US-01:

- `DashboardKpisTest` → `GET /api/v1/dashboard/kpis`
- `DashboardFinanceiroTest` → `GET /api/v1/dashboard/financeiro`
- `DashboardAuditoriaTest` → `GET /api/v1/dashboard/auditoria`
- `DashboardRelatorioTest` → `POST /api/v1/relatorios/exportar`

Cada uma traz **um caso positivo** (ex.: retorna 200 e JSON) e **um caso negativo** (ex.: caminho inexistente retorna 404), seguindo a regra da ADR-002. Toda asserção confere o status HTTP — um teste sem conferência de status passaria até com um 404.

---

## 2. Ambiente de frontend — `tests/frontend-e2e`

Projeto Python com pytest. Evolui o estudo `docs/selenium/primeiro-teste.py`.

```
tests/frontend-e2e/
├── requirements.txt
├── pytest.ini
├── .env.example
├── .gitignore
├── README.md
├── conftest.py
├── config/
│   ├── __init__.py
│   ├── settings.py
│   └── driver_factory.py
├── pages/
│   ├── __init__.py
│   ├── base_page.py
│   ├── home_publica_page.py
│   └── web_form_page.py
└── tests/
    ├── __init__.py
    ├── test_smoke_ambiente.py
    └── test_home_publica.py
```

### `requirements.txt`
Lista as bibliotecas Python necessárias e suas versões (selenium, pytest, pytest-html, python-dotenv, webdriver-manager). Instala tudo com `pip install -r requirements.txt`.

### `pytest.ini`
Arquivo de configuração do pytest. Define onde estão os testes, o padrão de nome dos arquivos de teste (`test_*.py`), a geração do relatório HTML e os **marcadores** (`smoke`, `e2e`, `negativo`) usados para rodar só um grupo de testes.

### `.env.example`
Modelo das variáveis de ambiente. Você copia para um arquivo `.env` e ajusta: `BASE_URL` (endereço da aplicação), `BROWSER` (chrome/firefox), `HEADLESS` (abrir ou não a janela) e `WAIT_TIMEOUT` (tempo de espera). O `.env` fica fora do Git.

### `.gitignore`
Ignora o que não deve ir para o repositório: o `.env` (pode ter dados sensíveis), a pasta `reports/`, o cache do Python e o ambiente virtual (`.venv/`).

### `README.md`
Instruções de instalação e execução do ambiente E2E: criar o ambiente virtual, instalar dependências, configurar o `.env` e rodar o pytest.

### `conftest.py`
Arquivo especial do pytest com as *fixtures* (preparação compartilhada entre os testes). Aqui ele: abre o navegador antes de cada teste, fecha ao final e **tira um screenshot automático quando um teste falha**, salvando a imagem na pasta `reports/` como evidência.

### `config/settings.py`
Lê o arquivo `.env` (via python-dotenv) e expõe as configurações de ambiente para o resto do projeto. É o equivalente ao `Environment.java` do backend.

### `config/driver_factory.py`
"Fábrica" do navegador. Cria o WebDriver do Selenium já configurado conforme o `settings.py` (Chrome ou Firefox, com ou sem janela). Centraliza a criação do navegador para os testes não precisarem saber esses detalhes.

### `pages/` — Page Objects
Padrão em que **cada tela vira uma classe**, com os localizadores dos elementos e as ações daquela tela. Deixa os testes legíveis e concentra a manutenção: se a UI mudar, ajusta-se o Page Object, não os testes.

- **`base_page.py`** — classe base com as ações comuns a qualquer página: abrir, clicar, digitar, esperar um elemento aparecer, ler texto. As outras páginas herdam dela.
- **`home_publica_page.py`** — representa a Tela 01 (Home Pública de Campanhas) do DoaSync. Tem os localizadores (banner, card de destaque, botão "Conhecer e doar") e ações como ir para a campanha em destaque. Os localizadores estão marcados como "a confirmar com o Frontend".
- **`web_form_page.py`** — representa o formulário de exemplo oficial do Selenium, usado pelo teste de fumaça. Permite validar o ambiente mesmo antes do frontend real existir.

### `config/__init__.py`, `pages/__init__.py`, `tests/__init__.py`
Arquivos vazios que marcam cada pasta como um **pacote Python**. É o que permite os imports entre os módulos (ex.: `from pages.base_page import BasePage`).

### `tests/` — os testes
Os arquivos que o pytest executa.

- **`test_smoke_ambiente.py`** — teste de *fumaça*: confirma que o ambiente está de pé (navegador, Page Object, esperas e asserções) usando a página de exemplo do Selenium. É a versão organizada do estudo `primeiro-teste.py` e **não depende do frontend real** do DoaSync.
- **`test_home_publica.py`** — testes ponta a ponta da Home do DoaSync. **Dependem do frontend real** rodando na `BASE_URL`, por isso ficam marcados com `@pytest.mark.e2e` e podem ser pulados com `pytest -m "not e2e"`.

---

## 3. Como os arquivos se conectam

**Backend:** `mvn test` lê o `pom.xml` → o Surefire executa as classes `*Test` → cada teste herda do `BaseTest` (que usou `Environment`) e chama os caminhos de `Endpoints`.

**Frontend:** `pytest` lê o `pytest.ini` e o `conftest.py` → o `conftest` usa o `driver_factory` (que usou o `settings`) para abrir o navegador → os arquivos `test_*.py` executam e conversam com a aplicação através dos **Page Objects** em `pages/`.
