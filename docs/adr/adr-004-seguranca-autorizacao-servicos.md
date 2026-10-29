# ADR-004: Segurança e autorização entre serviços (RBAC, guest token, LGPD)

- **Status:** Aceita
- **Data:** 2026-10-28
- **Decisores:** Grupo 5 (QA) — Paola C., Murilo A. S. G. C., João V. M., Amós G. S. S. | Validação: Backend
- **RNFs relacionados:** RNF-001, RNF-003, RNF-005, RNF-006
- **Documentos cruzados:** `docs/qa/relatorio-qa-shiftleft-sprint2.md`, `docs/qa/matriz-rastreabilidade.md`, `docs/backend/doasync-api-tests/MapeamentoAPI.md`

## Contexto

Em microsserviços, a autorização não pode viver só no frontend nem só em um serviço: cada serviço precisa confiar na identidade que recebe e validar a permissão por conta própria. O DoaSync tem três perfis (Doador, Gestor, Admin — RF-005), é multi-tenant por entidade (RNF-027) e manipula dados sensíveis de doadores sob a LGPD (RNF-005, RNF-006).

A análise Shift-Left levantou riscos de segurança concretos que esta ADR precisa resolver:

- **Conflito crítico (Tela 15):** o endpoint `POST /api/v1/doacoes` exige token Bearer, mas o protótipo permite **doação anônima** e **cadastro rápido**. Como autenticar uma doação anônima sem quebrar com 401?
- **Quebra de IDOR (Telas 04 e 19):** um gestor manipulando o ID na URL tentando editar campanha de outra entidade, ou um doador acessando doação de terceiro. Deve responder 403/404.
- **Enumeração de contas (Tela 21):** recuperação de senha não pode revelar se o e-mail existe.
- **LGPD (Tela 25):** doador anônimo não pode ter nome/contato exibidos nem registrados.

## Decisão

Adotar um **modelo de segurança padronizado e validado em cada serviço**:

1. **Autenticação por token em todas as chamadas**, inclusive anônimas. Resolvendo o conflito da Tela 15: o fluxo de doação anônima e de cadastro rápido emite um **guest token** (token de convidado de escopo reduzido) antes de chamar `POST /api/v1/doacoes`. Assim nenhuma chamada é verdadeiramente sem token — elimina-se o 401 indevido sem abrir a API.

2. **Autorização no servidor via RBAC** (RNF-003): cada serviço valida perfil (Doador/Gestor/Admin) **e** vínculo de entidade (tenant) a cada requisição. A permissão nunca é assumida a partir do frontend.

3. **Isolamento multi-tenant obrigatório:** toda consulta a recurso de entidade filtra pelo tenant do token. Acesso cruzado responde **403 Forbidden** (recurso de outra entidade) ou **404 Not Found** (para não confirmar a existência de uma doação de terceiro, conforme Tela 19).

4. **Senhas apenas em hash forte** (RNF-001): bcrypt ou Argon2, nunca em texto puro, nem em trânsito nem em repouso.

5. **Proteção contra enumeração e abuso** (Tela 21): recuperação de senha sempre responde mensagem neutra idêntica; rate limiting aplicado a login e recuperação.

6. **Minimização e proteção de dados pessoais** (RNF-005, RNF-006, LGPD): coleta mínima, consentimento explícito no cadastro, mascaramento de dados sensíveis e omissão total de doadores anônimos em grids e logs (alinhado à ADR-003).

7. **HTTPS compulsório** em todo o tráfego entre cliente e serviços e entre serviços (RNF-002).

## Consequências

**Positivas**

- O conflito arquitetural da doação anônima deixa de ser um bloqueio: guest token concilia o protótipo com a exigência de token da API.
- IDOR e vazamento entre tenants são barrados no servidor, com casos de teste já previstos na matriz (ex.: `CT-CAMP-020`, `CT-DOA-033`).
- Conformidade LGPD verificável e auditável.

**Negativas / riscos**

- Validar perfil + tenant em cada serviço adiciona lógica repetida; mitigar com biblioteca/filtro de segurança compartilhado.
- Guest tokens exigem política de expiração e escopo bem definidos para não virar brecha.

**Passa a ser obrigatório**

- Filtro de autorização (perfil + tenant) em cada serviço.
- Guest token para fluxos anônimos/cadastro rápido.
- Testes de segurança negativos (401/403/404) por endpoint sensível.

## Alternativas consideradas

- **Autorização só no API gateway.** Descartada: um serviço chamado internamente ficaria sem validação de tenant, reabrindo o IDOR.
- **Permitir `POST /api/v1/doacoes` sem token para doação anônima.** Descartada: abriria a API a abuso e exigiria caminho de código separado e inseguro. Guest token resolve com um único fluxo.
- **Confiar no frontend para esconder dados do doador anônimo.** Descartada: viola LGPD; a omissão precisa ocorrer no servidor.
