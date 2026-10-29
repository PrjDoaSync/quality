# Architecture Decision Records (ADRs) — Qualidade de Software

> **Projeto:** DoaSync — Sistema Digital de Arrecadação de Doações
> **Equipe Responsável:** 🟥 Grupo 5 — Qualidade de Software (QA)
> **Líder QA:** Paola C. | **Apoio Técnico e Validação:** Murilo A. S. G. C., João V. M., Amós G. S. S.
> **Data:** Outubro de 2026 | **Versão:** 1.0 — Oficial

---

## 1. O que é uma ADR

Uma **Architecture Decision Record (ADR)** é um registro curto e versionado que documenta uma decisão arquitetural significativa, o contexto em que ela foi tomada e as consequências esperadas. Cada ADR é imutável: quando uma decisão muda, cria-se uma nova ADR que **substitui** (supersedes) a anterior, preservando o histórico.

Estas ADRs consolidam **padrões de qualidade de software voltados para arquitetura de microsserviços** aplicáveis ao DoaSync. Elas traduzem para decisões de engenharia os requisitos não funcionais (RNF) já homologados em `docs/qa/matriz-rastreabilidade.md` e as inconsistências levantadas em `docs/qa/relatorio-qa-shiftleft-sprint2.md`.

## 2. Contexto arquitetural do DoaSync

- **Separação estrita Frontend (SPA) / Backend (REST API)** — RNF-017.
- Backend em **Spring Boot**, prefixo base `/api/v1`, comunicação exclusiva via JSON sobre HTTPS — RNF-002, RNF-016.
- Evolução para **múltiplos serviços de negócio** (Campanhas, Doações, Gestão/Dashboard), hoje tratados como domínios lógicos e candidatos naturais a microsserviços.
- **Multi-tenant** por entidade (APAE, Associação Amor Inclusivo e futuras ONGs) — RNF-027.
- Qualidade garantida por **QA Shift-Left**, testes de API (RestAssured/Newman), E2E (Selenium) e CI/CD — RNF-022 a RNF-026.
- **LGPD** e proteção de dados sensíveis do doador como requisito transversal — RNF-005, RNF-006, RNF-029.

## 3. Índice de ADRs

| ADR | Título | Status | RNFs relacionados |
|:---:|---|:---:|---|
| [ADR-001](adr-001-padronizacao-contratos-api.md) | Padronização de contratos de API e respostas de erro entre serviços | Aceita | RNF-004, RNF-011, RNF-016, RNF-018 |
| [ADR-002](adr-002-estrategia-testes-microsservicos.md) | Estratégia de testes em pirâmide e contract testing para microsserviços | Aceita | RNF-022, RNF-023, RNF-024 |
| [ADR-003](adr-003-observabilidade-logs-estruturados.md) | Observabilidade, logs estruturados e rastreamento distribuído | Aceita | RNF-009, RNF-011, RNF-029 |
| [ADR-004](adr-004-seguranca-autorizacao-servicos.md) | Segurança e autorização entre serviços (RBAC, guest token, LGPD) | Aceita | RNF-001, RNF-003, RNF-005, RNF-006 |
| [ADR-005](adr-005-quality-gates-ci-cd.md) | Quality gates no pipeline de CI/CD para microsserviços | Aceita | RNF-019, RNF-020, RNF-024, RNF-025, RNF-026 |

## 4. Status possíveis

- **Proposta** — em discussão, ainda não aprovada.
- **Aceita** — decisão vigente e em uso.
- **Substituída** — superada por uma ADR mais recente (indicar qual).
- **Depreciada** — não se aplica mais, sem substituição direta.

## 5. Template padrão

Toda nova ADR deve seguir a estrutura abaixo:

```markdown
# ADR-NNN: Título curto da decisão

- **Status:** Proposta | Aceita | Substituída | Depreciada
- **Data:** aaaa-mm-dd
- **Decisores:** nomes
- **RNFs relacionados:** RNF-xxx

## Contexto
Qual é o problema e as forças envolvidas.

## Decisão
O que foi decidido, de forma objetiva.

## Consequências
Positivas, negativas e riscos. O que passa a ser obrigatório.

## Alternativas consideradas
Opções descartadas e o porquê.
```
