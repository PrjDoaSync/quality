# Contratos de API revisados – DoaSync

> Documento de referência para a revisão de contratos com o Backend e validação pela QA.
> Baseado no boarding de backend do repositório `PrjDoaSync/doa-sync` e nos contratos consolidados em `docs/routes-plan.md` e `docs/backend/rotas-api.md`.

## 1. Objetivo

Este documento tem como objetivo:

- mapear, junto ao Backend, quais endpoints alimentam cada tela do Figma;
- revisar campos obrigatórios, tipos de dados e códigos HTTP esperados;
- registrar a referência de contrato para QA e desenvolvimento;
- manter uma coleção de endpoints em formato importável para Postman/Insomnia, sem adotar nova ferramenta.

## 2. Responsabilidade e ownership

- Autoria da especificação: Backend.
- Contribuição e validação: QA.
- Repositório: documentação e contratos ficam versionados no repositório do projeto, como referência para implementação e testes.

## 3. Convenções gerais

- Prefixo base: `/api/v1`
- Content-Type: `application/json`
- Autenticação: `Authorization: Bearer <token>`
- Formato de datas: `yyyy-MM-dd`
- Formato de timestamps: ISO-8601 UTC
- Valores monetários: decimal (`150.00`)
- Envelope padrão de resposta: `{ "data": ... }` ou `{ "message": "...", "data": ... }`

### Códigos HTTP esperados

| Código | Uso |
|---|---|
| 200 | Sucesso em consulta ou atualização |
| 201 | Recurso criado |
| 400 | Requisição inválida |
| 401 | Não autenticado |
| 403 | Sem permissão |
| 404 | Recurso inexistente ou sem acesso |
| 409 | Conflito / duplicidade / transição inválida |
| 422 | Validação de campos |
| 500 | Erro interno |

## 4. Mapeamento de endpoints por tela (Figma)

| Tela / fluxo | Endpoint principal | Método | Observação |
|---|---|---|---|
| Login | `/api/v1/auth/login` | POST | Autenticação do usuário |
| Cadastro de doador PF/PJ | `/api/v1/doadores` | POST | Cadastro público |
| Perfil do usuário | `/api/v1/usuarios/me` | GET/PATCH | Dados do próprio usuário |
| Histórico de doações do usuário | `/api/v1/usuarios/me/doacoes` | GET | Visualização de transações |
| Listagem de entidades | `/api/v1/entidades` | GET | Diretório público |
| Cadastro de entidade | `/api/v1/entidades` | POST | Cadastro de instituição |
| Detalhe de entidade | `/api/v1/entidades/{id}` | GET | Perfil público / interno |
| Edição de entidade | `/api/v1/entidades/{id}` | PATCH | Atualização de dados |
| Aprovação de entidade | `/api/v1/entidades/{id}/status` | PATCH | ADMIN |
| Listagem de campanhas | `/api/v1/campanhas` | GET | Vitrine pública / filtro |
| Detalhe de campanha | `/api/v1/campanhas/{id}` | GET | Exibe progresso da campanha |
| Criação de campanha | `/api/v1/campanhas` | POST | Entidade aprovada |
| Edição de campanha | `/api/v1/campanhas/{id}` | PATCH | Dono da campanha |
| Encerramento/desativação de campanha | `/api/v1/campanhas/{id}/status` | PATCH | Dono/Admin |
| Realização de doação | `/api/v1/doacoes` | POST | Fluxo principal de doação |
| Comprovante de doação | `/api/v1/doacoes/{id}` | GET | Consulta de comprovante |
| Dashboard admin | `/api/v1/dashboard/kpis` | GET | KPIs do sistema |
| Gestão de usuários (admin) | `/api/v1/usuarios` | GET | Listagem do admin |
| Ativar/inativar usuário | `/api/v1/usuarios/{id}/status` | PATCH | ADMIN |

## 5. Contratos revisados por recurso

### 5.1 Auth

#### POST `/api/v1/auth/login`

Request:

```json
{
  "email": "a@b.com",
  "senha": "123456"
}