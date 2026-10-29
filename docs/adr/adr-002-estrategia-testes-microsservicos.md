# ADR-002: Estratégia de testes em pirâmide e contract testing para microsserviços

- **Status:** Aceita
- **Data:** 2026-10-28
- **Decisores:** Grupo 5 (QA) — Paola C., Murilo A. S. G. C., João V. M., Amós G. S. S.
- **RNFs relacionados:** RNF-022, RNF-023, RNF-024
- **Documentos cruzados:** `docs/backend/relatorio-testes-api-doasync.md`, `docs/selenium/README.md`, `docs/qa/matriz-rastreabilidade.md`

## Contexto

Com os domínios de Campanhas, Doações e Gestão caminhando para serviços independentes, o fluxo de ponta a ponta mais crítico do DoaSync cruza vários serviços: **criar campanha → doar → refletir no dashboard** (RNF-023). Testar esse fluxo apenas de ponta a ponta é lento, instável e caro; testar só unidades isoladas não pega quebras de integração entre serviços.

O relatório de API deixou duas lições que orientam esta decisão: um teste sem asserção passa sempre (um 404 pode parecer sucesso), e um mock só é tão confiável quanto os exemplos que o alimentam — **um teste verde contra o mock não prova que a API real responderá igual**. Em um cenário de múltiplos serviços, cada serviço mockando o outro com suposições diferentes é uma fonte garantida de defeitos de integração.

Hoje já existem: suíte RestAssured (`doasync-api-tests`), coleção Postman com mock e um primeiro script Selenium.

## Decisão

Adotar a **pirâmide de testes** como padrão de qualidade, com uma camada explícita de **contract testing** entre serviços:

1. **Base — Testes unitários** (muitos, rápidos): regras de negócio de cada serviço isoladas, sem rede nem banco. Ferramenta: JUnit no backend; Vitest/Jest no frontend.

2. **Meio — Testes de integração / API por serviço** (RNF-022): cada serviço tem, no mínimo, **1 teste de sucesso e 1 de falha por endpoint** (ex.: 200 e 401 sem token). Ferramenta: RestAssured (projeto `doasync-api-tests`) e Newman para a coleção Postman. URL base sempre parametrizada (propriedade `baseUrl`), como já feito no `BaseTest`.

3. **Contract testing entre serviços** (camada-chave para microsserviços): o consumidor (ex.: serviço de Doações consumindo Campanhas, ou a SPA consumindo a API) define o contrato que espera; o provedor valida que o atende. O contrato OpenAPI padronizado na **ADR-001** é a referência. Isso substitui o risco do "mock baseado em suposição" por verificação bilateral.

4. **Topo — Testes E2E** (poucos, só fluxos críticos, RNF-023): automação Selenium cobrindo **campanha → doação → dashboard**. E2E não substitui as camadas inferiores; serve apenas para validar a jornada completa.

5. **Asserção mínima obrigatória:** todo teste confere o **status HTTP** junto com o resto. Nenhum teste de leitura é aceito sem asserção de status (lição direta do relatório de API).

## Consequências

**Positivas**

- Defeitos de integração entre serviços são capturados no contract testing, antes do E2E — mais barato e mais rápido (princípio Shift-Left).
- A suíte permanece executável em CI a cada PR (RNF-024), porque o grosso dos testes é rápido (base da pirâmide).
- O investimento já feito (RestAssured, Postman, Selenium) é reaproveitado, cada um no seu nível.

**Negativas / riscos**

- Contract testing exige manutenção dos contratos quando a API muda; se negligenciado, os testes passam mas a integração real quebra.
- E2E com Selenium é sensível a mudanças de UI; por isso fica restrito a poucos fluxos.

**Passa a ser obrigatório**

- 1 teste de sucesso + 1 de falha por endpoint em cada serviço.
- Asserção de status em todo teste.
- Contrato OpenAPI como base do contract testing entre serviços.

## Alternativas consideradas

- **Só testes E2E.** Descartada: lentos, instáveis e ruins para localizar a causa raiz entre vários serviços.
- **Cada serviço mocka os outros com exemplos próprios.** Descartada: reproduz exatamente o risco documentado do mock baseado em suposição e não garante compatibilidade entre serviços.
- **Testar só na borda (API gateway).** Descartada: esconderia quebras de contrato internas entre serviços.
