# ADR-005: Quality gates no pipeline de CI/CD para microsserviços

- **Status:** Aceita
- **Data:** 2026-10-28
- **Decisores:** Grupo 5 (QA) — Paola C., Murilo A. S. G. C., João V. M., Amós G. S. S.
- **RNFs relacionados:** RNF-019, RNF-020, RNF-024, RNF-025, RNF-026
- **Documentos cruzados:** `docs/qa/matriz-rastreabilidade.md`, `docs/backend/relatorio-testes-api-doasync.md`

## Contexto

Com cada domínio do DoaSync caminhando para deploy independente, a qualidade não pode depender de inspeção manual a cada entrega. A matriz de rastreabilidade já define as metas de engenharia: branches protegidas com aprovação em PR (RNF-019), lint/formatters automatizados (RNF-020), CI a cada PR (RNF-024), ambientes isolados (RNF-025) e deploy automatizado sem intervenção manual (RNF-026).

O risco em microsserviços é a **variação de maturidade entre serviços**: um serviço com cobertura robusta e outro que vai para produção sem teste. Sem um portão de qualidade comum e obrigatório, a barra cai para o nível do serviço mais descuidado. É preciso um **quality gate padronizado** que todo serviço atravessa antes de integrar e de ir a produção.

## Decisão

Definir um **quality gate obrigatório no CI/CD**, idêntico para todos os serviços, disparado a cada Pull Request (RNF-024). O merge na `main` só é permitido se **todas** as etapas passarem:

1. **Build** — o serviço compila sem erros.
2. **Lint e formatação** (RNF-020) — ESLint/Prettier no frontend; Checkstyle no backend. Falha de padrão bloqueia o merge.
3. **Testes unitários e de integração** (RNF-022, ADR-002) — suíte do serviço verde, incluindo 1 sucesso + 1 falha por endpoint.
4. **Contract testing** (ADR-002) — o serviço não quebra o contrato OpenAPI que seus consumidores esperam (ADR-001).
5. **Validação de contrato OpenAPI** (RNF-018) — o arquivo OpenAPI versionado está atualizado em relação ao código; divergência bloqueia.
6. **Verificação de segurança básica** — scan de dependências vulneráveis e checagem de que não há segredos commitados (apoia ADR-004).

Regras de processo:

- **Branches protegidas** (RNF-019): `main` travada, exigindo no mínimo **1 aprovação** (QA/Dev) e o gate verde. Sem override manual.
- **Ambientes isolados** (RNF-025): pipelines promovem o artefato por Desenvolvimento → Homologação → Produção, com bancos segregados. Homologação nunca contamina produção.
- **Deploy automatizado** (RNF-026): após merge aprovado, o CD publica sem passo manual no servidor (ex.: GitHub Actions + Vercel/Render).
- **URL base parametrizada** nos testes de cada ambiente (propriedade `baseUrl`, como no `BaseTest`), permitindo rodar a mesma suíte contra mock, homologação ou produção sem alterar código.

## Consequências

**Positivas**

- Nenhum serviço vai a produção sem atravessar a mesma barra de qualidade — elimina a variação de maturidade entre serviços.
- Regressões de contrato e de padrão são barradas no PR, não em produção (Shift-Left).
- Rastreabilidade: cada merge carrega evidência (build, testes, lint, contrato) do que foi verificado.

**Negativas / riscos**

- Pipeline mais lento por PR; mitigar com paralelização e cache de dependências.
- Gate rígido pode gerar atrito se os testes forem instáveis; exige manter a suíte confiável (sem testes "flaky").

**Passa a ser obrigatório**

- Pipeline de CI com as 6 etapas em todo serviço.
- Branch `main` protegida com aprovação e gate verde.
- CD automatizado com ambientes isolados.

## Alternativas consideradas

- **Quality gate opcional por serviço.** Descartada: a barra cairia para o serviço menos disciplinado.
- **Verificação manual de qualidade antes do deploy.** Descartada: não escala com múltiplos serviços e contraria RNF-026.
- **Rodar a suíte completa só antes do deploy de produção.** Descartada: defeitos seriam descobertos tarde; o gate precisa agir no PR.
