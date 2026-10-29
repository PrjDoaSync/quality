# Ambiente de Testes — DoaSync

> **Projeto:** DoaSync — Sistema Digital de Arrecadação de Doações
> **Equipe Responsável:** 🟥 Grupo 5 — Qualidade de Software (QA)
> **Tecnologias:** RestAssured + JUnit 5 (backend/API) · Selenium WebDriver + pytest (frontend/E2E)

Este diretório reúne o ambiente de testes automatizados do DoaSync, organizado segundo a estratégia de testes definida em [`docs/adr/adr-002-estrategia-testes-microsservicos.md`](../docs/adr/adr-002-estrategia-testes-microsservicos.md).

## Estrutura

```
tests/
├── backend-api/     Testes de API (RestAssured + JUnit 5 + Maven)
└── frontend-e2e/    Testes de interface ponta a ponta (Selenium + pytest)
```

Os dois ambientes seguem o mesmo princípio: **URL base parametrizada por ambiente**, para que a mesma suíte rode contra mock, homologação ou produção sem alterar código (padrão herdado do projeto de estudo `doasync-api-tests` e formalizado na ADR-005).

## Camadas da pirâmide de testes (ADR-002)

| Camada | Onde fica | Ferramenta |
|---|---|---|
| Integração / API (1 sucesso + 1 falha por endpoint) | `tests/backend-api` | RestAssured + JUnit 5 |
| E2E (fluxos críticos: campanha → doação → dashboard) | `tests/frontend-e2e` | Selenium + pytest |

> Testes unitários de cada serviço vivem no repositório do próprio serviço (backend/frontend), não aqui. Este diretório concentra as camadas de integração e E2E, que são responsabilidade do QA.

## Pré-requisitos gerais

- **Backend-api:** Java 21+ e Maven 3.9+ (ou o Maven embutido do IntelliJ).
- **Frontend-e2e:** Python 3.11+, Google Chrome e `pip`.

Cada subpasta tem o próprio README com instruções detalhadas de execução.

## Como executar rapidamente

```bash
# API (contra o mock padrão)
cd tests/backend-api
mvn test

# E2E (contra a URL definida em .env ou variável de ambiente)
cd tests/frontend-e2e
pip install -r requirements.txt
pytest
```

## Relação com os estudos

Este ambiente evolui os estudos já existentes no repositório:

- `docs/backend/doasync-api-tests` — primeiro projeto RestAssured (S10/S11).
- `docs/selenium/primeiro-teste.py` — primeiro script Selenium.

Aqui eles ganham estrutura de ambiente: configuração por ambiente, utilitários reaproveitáveis, Page Objects, relatórios e testes positivos e negativos.
