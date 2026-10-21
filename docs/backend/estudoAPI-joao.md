# Estudos e Estrutura de Testes de Backend

**Sprint: Estudos e Estrutura de Testes    
**Projeto: DoaSync

---

## 1. Introdução

Os testes de backend são fundamentais para garantir que uma aplicação funcione corretamente, principalmente quando ela disponibiliza informações e funcionalidades por meio de APIs.

No projeto DoaSync, os testes de API podem ser utilizados para verificar se as requisições estão sendo processadas corretamente, se as respostas possuem os dados esperados e se os diferentes componentes do sistema conseguem se comunicar de forma adequada.

Este estudo tem como objetivo apresentar os principais conceitos relacionados a testes de backend e servir como base para a utilização futura do **RESTAssured** na automação desses testes.

---

## 2. O que são testes de API?

Os testes de API têm como objetivo verificar o comportamento de uma API por meio do envio de requisições e da análise das respostas retornadas pelo servidor.

Uma requisição HTTP normalmente possui:

- **Método HTTP:** define a operação que será realizada;
- **URL/Endpoint:** identifica o recurso que será acessado;
- **Headers:** informações adicionais da requisição;
- **Body:** dados enviados para a API, principalmente em requisições como `POST` e `PUT`.

Os principais métodos HTTP utilizados são:

| Método | Finalidade |
|---|---|
| `GET` | Consultar informações |
| `POST` | Criar um novo recurso |
| `PUT` | Atualizar um recurso |
| `PATCH` | Atualizar parcialmente um recurso |
| `DELETE` | Remover um recurso |

### Exemplo

Uma requisição para consultar uma doação poderia ser:

```http
GET /api/doacoes/123