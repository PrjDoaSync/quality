# ADR-001: Padronização de contratos de API e respostas de erro entre serviços

- **Status:** Aceita
- **Data:** 2026-10-28
- **Decisores:** Grupo 5 (QA) — Paola C., Murilo A. S. G. C., João V. M., Amós G. S. S. | Validação: Backend (Grupos 2, 3, 4)
- **RNFs relacionados:** RNF-004, RNF-011, RNF-016, RNF-018
- **Documentos cruzados:** `docs/backend/doasync-api-tests/MapeamentoAPI.md`, `docs/backend/relatorio-testes-api-doasync.md`, `docs/qa/matriz-rastreabilidade.md`

## Contexto

O DoaSync evolui de um backend monolítico em Spring Boot para domínios de negócio independentes — **Campanhas**, **Doações** e **Gestão/Dashboard** — candidatos a se tornarem microsserviços com deploy e versionamento próprios. Cada serviço expõe sua própria API REST sob o prefixo `/api/v1`.

O relatório de testes de API (S10/S11) evidenciou o risco central dessa fragmentação: **os contratos ainda são suposições**. Os exemplos no Postman estão marcados como "campos a confirmar com o Backend", e os testes RestAssured só conseguem validar status e `Content-Type` porque não há contrato fechado. Sem um padrão único de contrato e de erro, cada serviço tende a inventar o próprio formato de resposta, quebrando o consumidor (SPA) e tornando a automação de testes frágil.

Forças envolvidas:

- Vários serviços, times distintos (Grupos 2, 3 e 4) e um único frontend consumidor.
- RNF-016 exige respostas de API padronizadas; RNF-018 exige documentação OpenAPI/Swagger; RNF-004 exige validação rigorosa de entradas; RNF-011 exige mensagens de erro compreensíveis sem vazar detalhes técnicos.
- A matriz de rastreabilidade já define o envelope de erro esperado: `{ timestamp, status, error, message, path }`.

## Decisão

Adotar um **contrato de API transversal e obrigatório** para todos os serviços do DoaSync:

1. **Versionamento por caminho:** todo endpoint fica sob `/api/v1`. Mudanças incompatíveis exigem `/api/v2`, nunca alteração silenciosa do contrato vigente.

2. **Envelope de sucesso padronizado:** respostas carregam `{ "data": ... }` ou `{ "message": "...", "data": ... }`, conforme já definido em `MapeamentoAPI.md`.

3. **Envelope de erro padronizado e único** para todos os serviços, implementado via `@RestControllerAdvice` (handler global):

   ```json
   {
     "timestamp": "2026-10-28T14:32:10Z",
     "status": 422,
     "error": "Unprocessable Entity",
     "message": "O valor mínimo para doação financeira é de R$ 1,00.",
     "path": "/api/v1/doacoes"
   }
   ```

4. **Códigos HTTP com semântica fixa** (conforme `MapeamentoAPI.md`): 200/201 sucesso, 400 requisição inválida, 401 não autenticado, 403 sem permissão, 404 inexistente/sem acesso, 409 conflito/duplicidade, 422 validação de campos, 500 erro interno.

5. **Contract-first com OpenAPI/Swagger:** cada serviço publica seu contrato em `/swagger-ui.html` e versiona o arquivo OpenAPI no repositório. O contrato OpenAPI é a **fonte de verdade** e substitui os exemplos fictícios do Postman.

6. **Convenções compartilhadas:** datas `yyyy-MM-dd`, timestamps ISO-8601 UTC, valores monetários decimais (`150.00`), `Content-Type: application/json`.

## Consequências

**Positivas**

- A SPA consome qualquer serviço com um único tratador de erros, reduzindo acoplamento.
- Os testes RestAssured passam a validar campos do body (ex.: `projetosAtivos`) contra um contrato fechado, eliminando o problema do "teste verde que só prova o mock".
- Mensagens de erro ficam previsíveis e seguras (RNF-011) — sem stack traces expostos.

**Negativas / riscos**

- Exige disciplina: todo serviço novo precisa registrar o handler global e publicar OpenAPI antes de integrar.
- Mudança de contrato vira custo de versão (`/v2`), não um ajuste rápido.

**Passa a ser obrigatório**

- Handler global de exceções em cada serviço.
- Arquivo OpenAPI versionado e atualizado a cada PR que altere a API (checado no quality gate — ver ADR-005).

## Alternativas consideradas

- **Cada serviço define seu próprio formato de erro.** Descartada: multiplicaria o tratamento no frontend e tornaria a automação de API impraticável.
- **Documentação apenas em Postman/Insomnia.** Descartada como fonte de verdade: a coleção é útil para mock, mas não é gerada a partir do código e tende a divergir (como já ocorreu com os exemplos fictícios). OpenAPI gerado do código permanece sincronizado.
