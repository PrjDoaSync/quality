# ADR-003: Observabilidade, logs estruturados e rastreamento distribuído

- **Status:** Aceita
- **Data:** 2026-10-28
- **Decisores:** Grupo 5 (QA) — Paola C., Murilo A. S. G. C., João V. M., Amós G. S. S.
- **RNFs relacionados:** RNF-009, RNF-011, RNF-029
- **Documentos cruzados:** `docs/qa/matriz-rastreabilidade.md`, `docs/qa/relatorio-qa-shiftleft-sprint2.md`

## Contexto

Em uma arquitetura de microsserviços, uma única ação de usuário — por exemplo, concluir uma doação via PIX — atravessa vários serviços: Doações recebe a requisição, aciona o gateway de pagamento, escuta o webhook de liquidação e notifica Gestão/Dashboard para atualizar os KPIs. Quando algo falha nesse caminho, **o log isolado de um serviço não conta a história completa**: não há como amarrar o erro do dashboard à requisição original do doador.

O projeto já exige trilha de auditoria das operações de gestão (RNF-009) e logs estruturados sem vazamento de senhas ou dados sensíveis (RNF-029). A análise Shift-Left também apontou cenários de borda difíceis de diagnosticar sem rastreamento: PIX com timeout de 30 min, webhook não detectado, doação estagnada em "Recebida" por mais de 30 dias. Sem correlação entre serviços, investigar esses casos em produção vira tentativa e erro.

## Decisão

Adotar **observabilidade padronizada** em todos os serviços do DoaSync, com três pilares:

1. **Logs estruturados em JSON** (RNF-029): toda entrada de log é um objeto JSON com, no mínimo, `timestamp`, `level`, `service`, `traceId`, `message`. Ferramenta: Logback com encoder JSON no backend Spring Boot.

2. **Rastreamento distribuído via `traceId`:** cada requisição que entra pela borda recebe um `traceId` único, propagado em todos os serviços subsequentes via header (`traceparent` / W3C Trace Context). Assim, a jornada "doação → webhook → atualização de dashboard" é reconstruível por um único identificador. A SPA inclui o `traceId` nas mensagens de erro técnicas de suporte (não visíveis ao usuário final).

3. **Mascaramento obrigatório de dados sensíveis** (RNF-029, alinhado à ADR-004): senhas **nunca** são logadas; CPF, CNPJ, e-mail e telefone são mascarados; doadores anônimos jamais têm identidade registrada em log (reforça a regra LGPD da Tela 25).

4. **Trilha de auditoria de negócio** (RNF-009): operações administrativas sensíveis — encerramento de campanha, conferência física de itens, baixa de estoque, exportação de relatório — gravam registro com `usuario`, `acao`, `descricao`, `dataHora`, no formato já previsto no endpoint `/api/v1/dashboard/auditoria`. Auditoria de negócio é persistida em tabela; logs de aplicação são efêmeros.

5. **Mensagens de erro de usuário desacopladas do log técnico** (RNF-011): o usuário vê mensagem amigável; o detalhe técnico e o `traceId` ficam só no log.

## Consequências

**Positivas**

- Um defeito que cruza serviços é investigável por um único `traceId`, reduzindo drasticamente o tempo de diagnóstico.
- Conformidade com LGPD reforçada: nenhum dado sensível vaza para logs.
- A trilha de auditoria dá suporte direto à prestação de contas das entidades (Tela 10).

**Negativas / riscos**

- Propagação de `traceId` exige configuração em todos os serviços e no cliente HTTP entre eles; se um serviço quebrar a cadeia, o rastro fica incompleto.
- Logs em JSON são mais verbosos; exigem política de retenção para não inflar armazenamento.

**Passa a ser obrigatório**

- Encoder JSON e `traceId` em todos os serviços.
- Filtro de mascaramento de dados sensíveis antes de qualquer escrita de log.
- Registro em tabela de auditoria para operações administrativas sensíveis.

## Alternativas consideradas

- **Logs em texto livre por serviço.** Descartada: impossível correlacionar entre serviços e fácil vazar dado sensível.
- **Rastreamento só na borda (API gateway).** Descartada: não revela o que acontece entre serviços internos (ex.: webhook de pagamento).
- **Auditoria apenas via logs de aplicação.** Descartada: logs são efêmeros; auditoria de negócio precisa de persistência durável e consultável.
