# Testes de API — DoaSync

## Requisição (Request)

É o que mandamos pra API. Toda requisição tem, no mínimo:

- **Método HTTP**: `GET`, `POST`, `PUT`, `PATCH`, `DELETE`
- **URL/Endpoint/Rota**: caminho que identifica a ação que será feita
- **Headers** (opcional): dados sobre a requisição, como token de quem está fazendo a requisição (em casos que é preciso validação)
- **Body** (opcional, comum em `POST`/`PATCH`): dados passados no body, para criar ou atualizar algo

## Resposta (Response)

É o que a API devolve após rodar a requisição. Toda resposta tem:

- **Status HTTP**: números que informam se a requisição deu certo ou errado (e que tipo de erro foi esse)
- **Headers**: metadados da resposta, tipo `Content-Type`
- **Body**: dados que retornam numa consulta, ou mensagem de erro

## O que são testes de API

São testes automatizados que criamos e definimos com informações já existentes (ou não) do nosso sistema, fazendo as devidas validações se os dados que a consulta na API retornou estão ou não condizentes com nossos critérios.

### Os 4 pilares

| Pilar | O que valida |
|---|---|
| **1. Status HTTP** | Número que representa o resultado da requisição. Só de olhar pra ele já sabemos se deu certo ou erro, e o código dá uma ideia de qual tipo de erro foi |
| **2. Headers** | O cabeçalho/etiqueta que diz como o dado no body deve ser tratado |
| **3. Body** | Se o conteúdo retornado bate com o que esperamos |
| **4. Contrato** | Não só o conteúdo, mas o **tipo** dele — `50.00` não é a mesma coisa que `"50.00"` |

## Funcional x Integração x Contrato

Três categorias de teste, cada uma com um propósito diferente.

### Teste funcional
Valida o comportamento de uma única chamada isolada, podendo checar status, body e contrato, sem envolver outros sistemas ou equipes.

### Teste de integração
Valida se os serviços vinculados à API estão funcionando corretamente após a requisição (CRUD no banco, disparo de mensageria, etc).

### Teste de contrato
Valida a forma da resposta, olhando a fundo para a tipagem do body. Exemplo: se o time mobile espera da API um retorno `"R$ 50,00"` e o time do backend altera para `50.00`, a tipagem mudou. Essa validação é crítica porque evita subir pra produção algo que certamente vai quebrar em outros lugares que ainda não estão adaptados àquele modelo.

---

## Cenários de teste no DoaSync

### Criação de doação

| | |
|---|---|
| **Endpoint proposto** | `POST /doacoes` |
| **Tipo** | Funcional |
| **Pilares testados** | Status HTTP + Body + Contrato |

**✅ Sucesso — a doação é criada**
- Status esperado: `201`
- Body esperado: objeto com os dados da doação criada, incluindo o id gerado (`int id`, `string doador`, `double valor`, `enum status`)

**❌ Falha — usuário não está autenticado**
- Status esperado: `401`
- Body esperado: mensagem informando que o usuário não está autorizado a fazer essa ação

**❌ Falha — campo obrigatório faltando**
- Status esperado: `400`
- Body esperado: mensagem de erro, ex: *"O campo 'NOME_DO_CAMPO' é obrigatório"*

**❌ Falha — valor inválido (zero ou negativo)**
- Status esperado: `400`
- Body esperado: mensagem explicando a regra violada, ex: *"O valor da doação deve ser maior que zero"*

---

### Listagem de doações

| | |
|---|---|
| **Endpoint proposto** | `GET /doacoes` |
| **Tipo** | Funcional |
| **Pilares testados** | Status HTTP + Body |

**✅ Sucesso (caso 1) — existem doações cadastradas**
- Status esperado: `200`
- Body esperado: array com objetos de doação

**✅ Sucesso (caso 2) — não existem doações cadastradas**
- Status esperado: `200`
- Body esperado: array vazio `[]`

**❌ Falha** — não há cenário de falha esperado para este endpoint nas condições atuais (sem filtros ou parâmetros obrigatórios)

---

### Busca de doação específica

| | |
|---|---|
| **Endpoint proposto** | `GET /doacoes/{id}` |
| **Tipo** | Funcional |
| **Pilares testados** | Status HTTP + Body |

**✅ Sucesso — quando há doação e é retornado o objeto dela**
- Status esperado: `200`
- Body esperado: objeto com os dados da doação (`id`, `doador`, `valor`, `status`)

**❌ Falha — doação não existe no sistema**
- Status esperado: `404`
- Body esperado: mensagem de erro, ex: *"Doação não encontrada"*

---

### Conclusão de doação

| | |
|---|---|
| **Endpoint proposto** | `PATCH /doacoes/{id}/concluir` |
| **Tipo** | Funcional |
| **Pilares testados** | Status HTTP + Body |

**✅ Sucesso — doação existe e está com status "pendente"**
- Status esperado: `200`
- Body esperado: campo `status` muda para `"concluida"`

**❌ Falha — doação já estava concluída antes**
- Status esperado: `400`
- Body esperado: mensagem de erro, ex: *"Essa doação já está concluída"*

**❌ Falha — doação não existe no sistema**
- Status esperado: `404`
- Body esperado: mensagem de erro, ex: *"Doação não encontrada"*

---

### Cancelar doação

| | |
|---|---|
| **Endpoint proposto** | `PATCH /doacoes/{id}/cancel` |
| **Tipo** | Funcional |
| **Pilares testados** | Status HTTP + Body |

**✅ Sucesso — doação existe e não está paga**
- Status esperado: `200`
- Body esperado: campo `status` muda para `"cancelado"`

**❌ Falha — doação não existe**
- Status esperado: `404`
- Body esperado: mensagem de falha, ex: *"Essa doação não existe"*

**❌ Falha — doação existe mas tem status "concluída"**
- Status esperado: `400`
- Body esperado: mensagem de falha, ex: *"Essa doação não pode ser cancelada pois já está concluída"*

---

### Editar valor da doação

| | |
|---|---|
| **Endpoint proposto** | `PATCH /doacoes/{id}` |
| **Tipo** | Funcional |
| **Pilares testados** | Status HTTP + Body + Contrato |

**Body enviado:** `{ "valor": 75.00 }`

**✅ Sucesso — doação existe e está como pendente**
- Status esperado: `200`
- Body esperado: mensagem de sucesso, ex: *"Valor da doação alterado com sucesso"*

**❌ Falha — doação não existe**
- Status esperado: `404`
- Body esperado: mensagem de falha, ex: *"Doação não encontrada"*

**❌ Falha — doação está concluída ou cancelada**
- Status esperado: `400`
- Body esperado: mensagem de falha, ex: *"A doação não pode estar concluída ou cancelada"*

**❌ Falha — valor inválido**
- Status esperado: `400`
- Body esperado: mensagem de falha, ex: *"O valor da doação deve ser maior que zero"*