# Plano de Testes Manuais — Sprint 2
## DoaSync: Sistema Digital de Arrecadação de Doações

> **Documento:** `plano-de-testes-manuais.md`  
> **Versão:** 1.0 — Oficial  
> **Data:** Outubro de 2026  
> **Autor:** 🟥 Grupo 5 — Qualidade de Software (QA)  
> **Líder QA:** Paola C. | **Apoio Técnico e Validação:** Murilo A. S. G. C., João V. M., Amós G. S. S.  
> **Público-alvo:** Equipes de Desenvolvimento (Grupos 2, 3, 4), Produto/UX (Grupo 1) e Analistas de Testes  
> **Documentos de Referência:** `requisitos.md`, `relatorio-qa-shiftleft-sprint2.md`, Protótipos Figma em `docs/qa/assets/`  

---

## 1. Introdução e Diretrizes de Execução

### 1.1 Objetivo
Este documento estabelece a especificação completa da **Suíte de Testes Manuais** para a **Sprint 2** da plataforma DoaSync. O objetivo primário é fornecer um roteiro exaustivo, determinístico e auditável que cubra todos os fluxos de usuário desenhados nas **27 telas prototipadas do Figma**, vinculando cada cenário aos **Requisitos Funcionais (RF)** e **Requisitos Não Funcionais (RNF)**.

### 1.2 Convenções e Classificação dos Testes
Cada caso de teste segue a estrutura formal padronizada:
- **Identificador (ID):**
  - `CT-CAMP-xxx`: Módulo de Campanhas (Grupo 2 / Telas 01 a 10)
  - `CT-DOA-xxx`: Módulo de Doações (Grupo 3 / Telas 11 a 20)
  - `CT-GES-xxx`: Módulo de Gestão e Dashboard (Grupo 4 / Telas 21 a 27)
  - `CT-TRA-xxx`: Cenários Transversais de Rede, Segurança e Acessibilidade
- **Tipo de Teste:**
  - **Positivo (*Happy Path*):** Execução do fluxo de acordo com o comportamento esperado de negócio.
  - **Negativo (*Exception Path*):** Inserção de dados incorretos, violação de regras e simulação de falhas de rede/servidor para validar a resiliência e mensagens de erro da UI.
  - **Borda (*Edge Case*):** Teste de valores limítrofes, concorrência, formatações especiais, máscaras e comportamento em limites extremos de dados.

### 1.3 Critérios de Aceite Globais
1. **Validação de Entrada:** Nenhum formulário deve permitir submissão com campos obrigatórios vazios ou formatos inválidos.
2. **Máscaras Obrigatórias:** Moeda (`R$ 0.000,00`), CPF (`000.000.000-00`), CNPJ (`00.000.000/0000-00`), Telefone (`(00) 00000-0000`) e Datas (`DD/MM/AAAA`).
3. **Resiliência e Mensagens Amigáveis (RNF-011):** Nenhuma falha HTTP (4xx, 5xx) pode renderizar tela em branco, quebrar o layout ou exibir *stack traces* técnicos.
4. **Idempotência (RNF-008):** Todos os botões de ação destrutiva ou submissão devem desabilitar-se imediatamente ao primeiro clique com indicador visual de carregamento.

---

## 2. Módulo 1: Campanhas (Grupo 2)

```
Fluxos de Campanhas:
[Vitrine / Home] ──► [Listagem e Busca] ──► [Detalhes da Campanha] ──► [Compartilhamento]
        │
[Painel Entidade] ──► [Wizard Criação: Etapas 1, 2 e 3] ──► [Edição] ──► [Prestação de Contas]
```

---

### Tela 01: Home Pública (`campanhas-home-publica.png`)

#### CT-CAMP-001: Navegação na vitrine e redirecionamento para campanha destaque
- **Tela / Módulo Relacionado:** Tela 01 — Home Pública (`campanhas-home-publica.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-014, RF-015
- **Tipo de Teste:** Positivo
- **Pré-condições:** Aplicação em execução; existir ao menos uma campanha ativa cadastrada como urgente (ex: "Mesa solidária" da APAE).
- **Passo a Passo:**
  1. Acessar a URL inicial do sistema DoaSync (`/`).
  2. Verificar a renderização do banner principal ("Encontre uma causa. Faça a diferença").
  3. Localizar o card da campanha em destaque ("Mesa solidária").
  4. Conferir os dados exibidos: foto de capa, badge "Urgente - 14 dias restantes", valores arrecadados (R$ 18.400,00 / R$ 30.000,00), porcentagem (61%) e itens recebidos (260 de 480).
  5. Clicar no botão "Conhecer e doar" da campanha destaque.
- **Dados de Teste / Entradas:** N/A (navegação pública).
- **Resultado Esperado:** O navegador redireciona para a URL `/campanhas/mesa-solidaria` (Tela 03 - Detalhes da Campanha), carregando todas as informações correspondentes sem erros de console.

#### CT-CAMP-002: Redirecionamento a partir da seção "Mais causas para apoiar"
- **Tela / Módulo Relacionado:** Tela 01 — Home Pública (`campanhas-home-publica.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-014, RF-015
- **Tipo de Teste:** Positivo
- **Pré-condições:** Existirem múltiplas campanhas ativas no banco de dados.
- **Passo a Passo:**
  1. Acessar a Home do DoaSync (`/`).
  2. Rolar a página até a seção "Mais causas para apoiar".
  3. Verificar a exibição do grid com cards de campanhas secundárias com suas respectivas barras de progresso.
  4. Clicar no botão "Ver campanha" do card "Inverno Aquecido".
- **Dados de Teste / Entradas:** N/A.
- **Resultado Esperado:** O sistema redireciona com sucesso para a página de detalhes da campanha selecionada (`/campanhas/inverno-aquecido`).

#### CT-CAMP-003: Atualização de status da campanha destaque ao atingir 100% ou encerramento
- **Tela / Módulo Relacionado:** Tela 01 — Home Pública (`campanhas-home-publica.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-013, RF-014
- **Tipo de Teste:** Borda
- **Pré-condições:** Usuário com a Home aberta no navegador; em paralelo, a campanha destaque recebe a doação que completa 100% da meta ou é encerrada pelo gestor.
- **Passo a Passo:**
  1. Acessar a Home pública.
  2. No painel de gestão, encerrar a campanha destaque ou registrar doação completando o montante.
  3. Atualizar a página Home (ou aguardar pooling/revalidação de cache).
- **Dados de Teste / Entradas:** Campanha ID: 1, Status atualizado para "ENCERRADA" ou Arrecadação = R$ 30.000,00.
- **Resultado Esperado:** O card de destaque não exibe mais o botão "Conhecer e doar" ativo; a badge atualiza para "Meta atingida" ou "Encerrada", ou o sistema substitui dinamicamente o destaque pela próxima campanha urgente ativa.

#### CT-CAMP-004: Resiliência no carregamento de imagens de capa via CDN com fallback institucional
- **Tela / Módulo Relacionado:** Tela 01 — Home Pública (`campanhas-home-publica.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-018, RNF-011
- **Tipo de Teste:** Negativo / Exceção
- **Pré-condições:** Simular falha de rede/bloqueio na URL da imagem da campanha (ex: bloquear requisição à CDN no DevTools).
- **Passo a Passo:**
  1. No DevTools (Network Request Blocking), bloquear URLs com padrão `*.png` ou `*.jpg` do bucket de mídia.
  2. Acessar a página inicial do DoaSync (`/`).
  3. Observar a área onde a imagem da campanha destaque e os cards deveriam aparecer.
- **Dados de Teste / Entradas:** Requisições de imagens retornando erro `404 Not Found` ou `net::ERR_BLOCKED_BY_CLIENT`.
- **Resultado Esperado:** O layout não quebra; a UI substitui a imagem ausente por um placeholder institucional elegante com o logo do DoaSync e a tag `alt` descritiva correspondente (RNF-013).

#### CT-CAMP-005: Responsividade mobile e empilhamento vertical do card de destaque
- **Tela / Módulo Relacionado:** Tela 01 — Home Pública (`campanhas-home-publica.png`) / Módulo Campanhas
- **Requisito Vinculado:** RNF-014
- **Tipo de Teste:** Borda / Compatibilidade
- **Pré-condições:** Navegador em modo de emulação de dispositivos móveis (ex: iPhone 14, largura 390px, e Moto G4, largura 360px).
- **Passo a Passo:**
  1. Abrir a Home em viewport de 360px de largura.
  2. Inspecionar visualmente o banner e o card de destaque "Mesa solidária".
  3. Realizar scroll vertical até o rodapé.
- **Dados de Teste / Entradas:** Resoluções: 360x640, 390x844, 768x1024.
- **Resultado Esperado:** O card de destaque horizontal se reconfigura para empilhamento vertical (100% width); não há rolagem horizontal (`overflow-x: hidden`); os botões possuem área de toque mínima de 44x44px.

---

### Tela 02: Listagem com Filtros Avançados (`campanhas-listagem-com-filtros.png`)

#### CT-CAMP-006: Pesquisa textual e aplicação combinada de filtros
- **Tela / Módulo Relacionado:** Tela 02 — Listagem com Filtros (`campanhas-listagem-com-filtros.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-014, RF-016, RF-017
- **Tipo de Teste:** Positivo
- **Pré-condições:** Existirem campanhas cadastradas com diferentes entidades, categorias e status.
- **Passo a Passo:**
  1. Acessar a rota `/campanhas`.
  2. No campo de busca por palavra-chave, digitar "alimento".
  3. No dropdown "Entidade", selecionar "Rede Esperança".
  4. No dropdown "Categoria", selecionar "Alimentação".
  5. No dropdown "Status", selecionar "Ativa".
  6. No dropdown "Urgência", selecionar "Todas".
  7. Clicar no botão "Aplicar filtros".
- **Dados de Teste / Entradas:** Busca: `"alimento"`, Entidade: `"Rede Esperança"`, Categoria: `"Alimentação"`, Status: `"Ativa"`.
- **Resultado Esperado:** A listagem atualiza via requisição assíncrona exibindo contador exato (ex: "6 campanhas encontradas · 5 ativas e 1 encerrada") e renderiza apenas os cards compatíveis com os critérios informados.

#### CT-CAMP-007: Busca sem resultados (Empty State com botão "Limpar filtros")
- **Tela / Módulo Relacionado:** Tela 02 — Listagem com Filtros (`campanhas-listagem-com-filtros.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-016, RNF-011
- **Tipo de Teste:** Borda / Negativo
- **Pré-condições:** Usuário na tela de listagem de campanhas.
- **Passo a Passo:**
  1. No campo de busca, digitar um termo inexistente: `TermoInexistenteXYZ123`.
  2. Clicar em "Aplicar filtros".
- **Dados de Teste / Entradas:** Termo: `"TermoInexistenteXYZ123"`.
- **Resultado Esperado:** A listagem oculta os cards de campanhas e exibe um componente de **Empty State** com ilustração amigável, mensagem "Nenhuma campanha encontrada com os filtros selecionados" e botão destacado "Limpar filtros".

#### CT-CAMP-008: Limpeza e reset de filtros sem recarregamento total
- **Tela / Módulo Relacionado:** Tela 02 — Listagem com Filtros (`campanhas-listagem-com-filtros.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-017
- **Tipo de Teste:** Positivo
- **Pré-condições:** Filtros aplicados na tela com resultado filtrado exibido.
- **Passo a Passo:**
  1. Com os filtros preenchidos, clicar no link/botão "Limpar filtros".
- **Dados de Teste / Entradas:** Ação de clique em "Limpar filtros".
- **Resultado Esperado:** Todos os campos de busca e dropdowns voltam aos valores padrão (`""` e `"Todas"`); a listagem exibe todas as campanhas ativas sem recarregar a página inteira (Single Page Application behavior).

#### CT-CAMP-009: Sanitização contra injeção de script (XSS) e SQL Injection na busca
- **Tela / Módulo Relacionado:** Tela 02 — Listagem com Filtros (`campanhas-listagem-com-filtros.png`) / Módulo Campanhas
- **Requisito Vinculado:** RNF-004
- **Tipo de Teste:** Negativo / Segurança
- **Pré-condições:** Usuário com acesso ao campo de busca de campanhas.
- **Passo a Passo:**
  1. No campo de busca, inserir o payload de XSS: `<script>alert('XSS')</script>`.
  2. Clicar em "Aplicar filtros".
  3. No mesmo campo, inserir payload de SQLi: `' OR '1'='1' -- `.
  4. Clicar em "Aplicar filtros".
- **Dados de Teste / Entradas:**
  - Payload 1: `<script>alert('XSS')</script>`
  - Payload 2: `' OR '1'='1' -- `
- **Resultado Esperado:** Nenhum pop-up ou script é executado no navegador; os caracteres `<` e `>` são codificados como entidades HTML; a API trata a string de forma parametrizada retornando zero resultados ou erro controlado `422/400`, sem expor erro interno 500 ou quebras de sintaxe SQL.

#### CT-CAMP-010: Paginação da listagem de campanhas
- **Tela / Módulo Relacionado:** Tela 02 — Listagem com Filtros (`campanhas-listagem-com-filtros.png`) / Módulo Campanhas
- **Requisito Vinculado:** RNF-028
- **Tipo de Teste:** Positivo / Borda
- **Pré-condições:** Base de dados contendo mais de 12 campanhas cadastradas.
- **Passo a Passo:**
  1. Acessar a listagem `/campanhas`.
  2. Rolar até o final da página e verificar o componente de paginação ("Página 1 de 3").
  3. Clicar no botão "Próxima página" ou no número "2".
- **Dados de Teste / Entradas:** Parâmetros de URL: `?page=2&size=12`.
- **Resultado Esperado:** O sistema carrega os próximos 12 itens; a URL reflete o parâmetro de paginação; o topo da listagem volta a ficar visível com scroll suave; a resposta da API retorna estritamente a fatia de registros paginada (`Pageable`).

---

### Tela 03: Detalhes da Campanha (`campanhas-detalhe-da-campanha.png`)

#### CT-CAMP-011: Visualização completa de detalhes, metas híbridas e itens necessários
- **Tela / Módulo Relacionado:** Tela 03 — Detalhe da Campanha (`campanhas-detalhe-da-campanha.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-015, RF-018
- **Tipo de Teste:** Positivo
- **Pré-condições:** Campanha "Mesa solidária" ativa com meta financeira e lista de itens físicos configurada.
- **Passo a Passo:**
  1. Acessar `/campanhas/mesa-solidaria`.
  2. Conferir:
     - Breadcrumb de navegação (`Início > Campanhas > Mesa solidária`).
     - Galeria de imagens com imagem principal e miniaturas.
     - Descrição completa do projeto social.
     - Barra de progresso financeira: R$ 18.400,00 arrecadados de R$ 30.000,00 (61%).
     - Seção "Itens necessários": lista com "Cesta básica" (140 de 200 recebidas), "Kit de higiene" (80 de 180 recebidos), "Leite em pó" (40 de 100 latas).
     - Informações da entidade promotora (APAE), dias restantes (14 dias).
- **Dados de Teste / Entradas:** URL de campanha com metas híbridas.
- **Resultado Esperado:** Todos os blocos são renderizados fielmente ao protótipo do Figma, com textos legíveis e barras de porcentagem preenchidas proporcionalmente.

#### CT-CAMP-012: Início de contribuição financeira através do botão "Doar"
- **Tela / Módulo Relacionado:** Tela 03 — Detalhe da Campanha (`campanhas-detalhe-da-campanha.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-020
- **Tipo de Teste:** Positivo
- **Pré-condições:** Campanha ativa.
- **Passo a Passo:**
  1. Na barra lateral direita fixa da campanha, clicar no botão primário "Doar".
- **Dados de Teste / Entradas:** Clique no botão "Doar".
- **Resultado Esperado:** O doador é direcionado para a Tela 12 (`/doacoes/financeira?campanhaId=1`), com a campanha já pré-selecionada no fluxo.

#### CT-CAMP-013: Início de doação de mantimentos através do botão "Doar itens necessários"
- **Tela / Módulo Relacionado:** Tela 03 — Detalhe da Campanha (`campanhas-detalhe-da-campanha.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-020
- **Tipo de Teste:** Positivo
- **Pré-condições:** Campanha configurada com recebimento de itens físicos.
- **Passo a Passo:**
  1. Na barra lateral direita, clicar no botão secundário "Doar itens necessários".
- **Dados de Teste / Entradas:** Clique no botão "Doar itens necessários".
- **Resultado Esperado:** O doador é direcionado para a Tela 13 (`/doacoes/itens?campanhaId=1`), apresentando a listagem de suprimentos físicos da campanha para seleção de quantidades.

#### CT-CAMP-014: Compartilhamento de campanha com suporte a Web Share API e fallback de clipboard
- **Tela / Módulo Relacionado:** Tela 03 — Detalhe da Campanha (`campanhas-detalhe-da-campanha.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-019
- **Tipo de Teste:** Positivo / Borda
- **Pré-condições:** Testar em navegador desktop (sem `navigator.share`) e dispositivo mobile compatível.
- **Passo a Passo:**
  1. No desktop, clicar no botão "Compartilhar".
  2. Verificar o clipboard (colar em bloco de notas).
  3. No celular, clicar em "Compartilhar".
- **Dados de Teste / Entradas:** Clique no botão "Compartilhar".
- **Resultado Esperado:** No desktop, copia a URL completa da campanha para a área de transferência e exibe um toast informativo "Link da campanha copiado com sucesso!". No mobile, abre o menu nativo de compartilhamento do sistema operacional.

#### CT-CAMP-015: Acesso direto a campanha encerrada com bloqueio de doações
- **Tela / Módulo Relacionado:** Tela 03 — Detalhe da Campanha (`campanhas-detalhe-da-campanha.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-013, RF-020
- **Tipo de Teste:** Negativo / Regra de Negócio
- **Pré-condições:** Campanha com status "ENCERRADA" na base de dados.
- **Passo a Passo:**
  1. Acessar a URL direta da campanha encerrada (`/campanhas/campanha-encerrada-id`).
  2. Verificar os botões de ação na barra lateral.
- **Dados de Teste / Entradas:** URL de campanha com `status: "ENCERRADA"`.
- **Resultado Esperado:** Os botões "Doar" e "Doar itens necessários" ficam totalmente ocultos ou desabilitados com rótulo "Campanha Encerrada"; um banner informativo amarelo no topo exibe: "Esta campanha foi encerrada em DD/MM/AAAA e não recebe novas contribuições."

#### CT-CAMP-016: Meta financeira 100% atingida com arrecadação de itens físicos ainda em aberto
- **Tela / Módulo Relacionado:** Tela 03 — Detalhe da Campanha (`campanhas-detalhe-da-campanha.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-015, RF-020
- **Tipo de Teste:** Borda
- **Pré-condições:** Campanha com meta financeira atingida (R$ 30.000,00 de R$ 30.000,00 - 100%), mas itens com pendência (260 de 480 itens).
- **Passo a Passo:**
  1. Acessar a página de detalhes da campanha.
  2. Verificar a barra lateral de doação.
- **Dados de Teste / Entradas:** Campanha mista com meta monetária completa.
- **Resultado Esperado:** A meta financeira exibe badge verde "Meta financeira concluída! Parabéns!". O botão "Doar" (financeiro) exibe aviso ou fica desabilitado, enquanto o botão "Doar itens necessários" permanece destacado e funcional.

---

### Tela 04: Painel "Minhas Campanhas" da Entidade (`campanhas-minhas-campanhas.png`)

#### CT-CAMP-017: Visualização gerencial e filtragem por abas de status
- **Tela / Módulo Relacionado:** Tela 04 — Minhas Campanhas (`campanhas-minhas-campanhas.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-026
- **Tipo de Teste:** Positivo
- **Pré-condições:** Usuário autenticado como Gestor de Entidade (APAE).
- **Passo a Passo:**
  1. Realizar login como gestor da APAE.
  2. Acessar o menu lateral "Campanhas" -> "Minhas Campanhas".
  3. Conferir os cards de resumo no topo: Rascunhos (1), Ativas (1), Encerradas (1).
  4. Clicar na aba "Rascunhos".
  5. Clicar na aba "Ativas".
  6. Clicar na aba "Encerradas".
- **Dados de Teste / Entradas:** Navegação por abas de status.
- **Resultado Esperado:** A tabela exibe apenas as campanhas vinculadas à entidade do gestor e pertencentes à aba ativa selecionada. Cada linha exibe as ações contextuais corretas (ex: "Editar" para ativas; "Continuar criação" para rascunhos; "Prestar contas" para encerradas).

#### CT-CAMP-018: Continuação de edição a partir de rascunho salvo
- **Tela / Módulo Relacionado:** Tela 04 — Minhas Campanhas (`campanhas-minhas-campanhas.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-010, RF-026
- **Tipo de Teste:** Positivo
- **Pré-condições:** Existir um rascunho de campanha salvo ("Apoio às Famílias").
- **Passo a Passo:**
  1. Na aba "Rascunhos", localizar a campanha "Apoio às Famílias".
  2. Clicar na ação "Continuar criação".
- **Dados de Teste / Entradas:** Clique em "Continuar criação" no rascunho ID: 3.
- **Resultado Esperado:** O sistema redireciona para o Wizard de Criação carregando todos os dados previamente preenchidos na etapa onde o gestor parou.

#### CT-CAMP-019: Exclusão de rascunho com modal de confirmação destrutivo
- **Tela / Módulo Relacionado:** Tela 04 — Minhas Campanhas (`campanhas-minhas-campanhas.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-026
- **Tipo de Teste:** Positivo / Borda
- **Pré-condições:** Existir um rascunho salvo na lista.
- **Passo a Passo:**
  1. Na aba "Rascunhos", clicar no botão/ícone "Excluir rascunho".
  2. Observar se ocorre exclusão imediata.
  3. No modal de confirmação exibido, verificar o texto de alerta.
  4. Clicar em "Cancelar" e conferir que o rascunho permanece na lista.
  5. Clicar novamente em "Excluir" e em seguida confirmar no botão "Sim, excluir rascunho".
- **Dados de Teste / Entradas:** Confirmação de exclusão em modal.
- **Resultado Esperado:** O rascunho NUNCA é excluído sem confirmação; após confirmar no modal, o rascunho é removido da lista, o contador do card decresce para 0 e um toast verde "Rascunho excluído com sucesso" é renderizado.

#### CT-CAMP-020: Bloqueio de acesso indevido (IDOR) a campanhas de outra entidade
- **Tela / Módulo Relacionado:** Tela 04 — Minhas Campanhas (`campanhas-minhas-campanhas.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-005, RNF-003
- **Tipo de Teste:** Negativo / Segurança
- **Pré-condições:** Gestor A autenticado (pertencente à APAE, Entidade ID: 1); Campanha ID: 99 pertencente à Associação Amor Inclusivo (Entidade ID: 2).
- **Passo a Passo:**
  1. Com a sessão do Gestor A aberta, digitar diretamente na barra de endereços do navegador a URL: `/gestao/campanhas/99/editar`.
- **Dados de Teste / Entradas:** URL forçada com ID de campanha alheia.
- **Resultado Esperado:** A aplicação barra o acesso; o backend retorna HTTP `403 Forbidden`; a interface exibe tela de "Acesso Negado: você não possui permissão para gerenciar esta campanha" e não vaza nenhum dado sigiloso da entidade proprietária.

---

### Tela 05: Criação de Campanha — Etapa 1: Dados Básicos (`campanhas-criar-campanha-dados-basicos.png`)

#### CT-CAMP-021: Preenchimento válido de dados básicos com visualização de preview e auto-save
- **Tela / Módulo Relacionado:** Tela 05 — Criação Etapa 1 (`campanhas-criar-campanha-dados-basicos.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-010
- **Tipo de Teste:** Positivo
- **Pré-condições:** Gestor autenticado com vínculo ativo a uma entidade aprovada.
- **Passo a Passo:**
  1. Acessar `/gestao/campanhas/criar`.
  2. Verificar o stepper: "1 Dados básicos" ativo, "2 Metas e itens" e "3 Revisão" pendentes.
  3. Verificar que o campo "Entidade responsável" já vem preenchido e desabilitado com ícone de cadeado.
  4. Preencher o campo "Título da campanha *" com "Campanha do Agasalho Solidário 2026".
  5. Preencher "Descrição *" com texto detalhado sobre a arrecadação.
  6. Selecionar "Categoria *" no dropdown: "Vestuário e Acolhimento".
  7. Informar a "Data de início *" como a data atual e a "Data de término *" para 30 dias à frente.
  8. Observar o painel lateral de preview em tempo real e o indicador de rascunho salvo ("Rascunho salvo às HH:MM").
  9. Clicar no botão "Continuar".
- **Dados de Teste / Entradas:**
  - Título: `"Campanha do Agasalho Solidário 2026"` (38 caracteres)
  - Descrição: `"Arrecadação de cobertores, agasalhos e roupas de frio para pessoas em vulnerabilidade atendidas pela entidade no inverno de 2026."` (134 caracteres)
  - Categoria: `"Vestuário e Acolhimento"`
  - Data Início: `21/10/2026`, Data Término: `21/11/2026`
- **Resultado Esperado:** O preview reflete os textos conforme a digitação; o rascunho é persistido; ao clicar em "Continuar", o sistema avança para a Etapa 2 (Metas e Itens) sem erros.

#### CT-CAMP-022: Validação de campos obrigatórios vazios na Etapa 1
- **Tela / Módulo Relacionado:** Tela 05 — Criação Etapa 1 (`campanhas-criar-campanha-dados-basicos.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-010, RNF-004
- **Tipo de Teste:** Negativo
- **Pré-condições:** Formulário da Etapa 1 em branco.
- **Passo a Passo:**
  1. Acessar `/gestao/campanhas/criar`.
  2. Sem preencher nenhum campo, clicar diretamente em "Continuar".
- **Dados de Teste / Entradas:** Todos os campos com strings vazias `""`.
- **Resultado Esperado:** O formulário não avança; os campos Título, Descrição, Categoria, Data de Início e Data de Término recebem contorno vermelho e exibem mensagens de erro inline:
  - "O título da campanha é obrigatório."
  - "A descrição da campanha é obrigatória."
  - "Selecione uma categoria."
  - "Informe a data de início e término."

#### CT-CAMP-023: Validação de limites de caracteres (mínimo e máximo de título e descrição)
- **Tela / Módulo Relacionado:** Tela 05 — Criação Etapa 1 (`campanhas-criar-campanha-dados-basicos.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-010, RNF-004
- **Tipo de Teste:** Borda
- **Pré-condições:** Gestor na tela de criação de campanha.
- **Passo a Passo:**
  1. No campo Título, inserir apenas 5 caracteres ("Ajuda") e tentar continuar.
  2. Inserir 81 caracteres no Título (limite máximo é 80).
  3. No campo Descrição, inserir 20 caracteres (mínimo sugerido: 50).
  4. Inserir 2.001 caracteres na Descrição (limite máximo: 2.000).
- **Dados de Teste / Entradas:**
  - Título curto: `"Ajuda"` (5 chars)
  - Título longo: string de 81 caracteres
  - Descrição curta: `"Campanha de doação."` (20 chars)
  - Descrição longa: string de 2.001 caracteres
- **Resultado Esperado:**
  - Para título curto: "O título deve ter no mínimo 10 caracteres."
  - Para título longo: O campo trava a digitação no 80º caractere ou acusa: "O título não pode exceder 80 caracteres."
  - Para descrição curta: "A descrição deve conter no mínimo 50 caracteres para informar adequadamente os doadores."
  - Contador de caracteres visível indicando `X / 80` e `X / 2000`.

#### CT-CAMP-024: Validação de consistência cronológica das datas de vigência
- **Tela / Módulo Relacionado:** Tela 05 — Criação Etapa 1 (`campanhas-criar-campanha-dados-basicos.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-010
- **Tipo de Teste:** Negativo / Borda
- **Pré-condições:** Gestor preenchendo as datas no date picker.
- **Passo a Passo:**
  1. Informar a Data de Início como `25/10/2026`.
  2. Informar a Data de Término como `20/10/2026` (término anterior ao início).
  3. Tentar avançar clicando em "Continuar".
  4. Em novo teste, informar Data de Término com data no passado (ex: `01/01/2025`).
- **Dados de Teste / Entradas:**
  - Teste A: Início: `25/10/2026`, Término: `20/10/2026`
  - Teste B: Término: `01/01/2025`
- **Resultado Esperado:** O date picker bloqueia a seleção ou o campo exibe mensagem de erro impeditiva inline: "A data de término deve ser igual ou posterior à data de início" e "A data de término não pode estar no passado." O botão "Continuar" é desabilitado.

#### CT-CAMP-025: Queda de conexão de rede durante auto-save de rascunho
- **Tela / Módulo Relacionado:** Tela 05 — Criação Etapa 1 (`campanhas-criar-campanha-dados-basicos.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-010, RNF-011
- **Tipo de Teste:** Negativo / Resiliência
- **Pré-condições:** Gestor digitando na tela; conexão de internet é interrompida.
- **Passo a Passo:**
  1. Preencher título e descrição.
  2. Desconectar o cabo de rede ou ativar modo offline no DevTools.
  3. Continuar digitando na descrição para disparar o auto-save.
  4. Observar o indicador de salvamento no rodapé.
- **Dados de Teste / Entradas:** Interrupção de rede (`net::ERR_INTERNET_DISCONNECTED`).
- **Resultado Esperado:** O indicador de rascunho muda de status para "Falha na conexão. Rascunho salvo localmente (tentando reconectar...)"; o botão "Continuar" exibe aviso para impedir transição com perda de sincronização no backend.

---

### Tela 06: Criação de Campanha — Etapa 2: Metas e Itens (`campanhas-criar-campanha-metas-e-itens.png`)

#### CT-CAMP-026: Configuração válida de metas híbridas (financeira com máscara e lista de itens)
- **Tela / Módulo Relacionado:** Tela 06 — Criação Etapa 2 (`campanhas-criar-campanha-metas-e-itens.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-010
- **Tipo de Teste:** Positivo
- **Pré-condições:** Etapa 1 concluída com sucesso.
- **Passo a Passo:**
  1. Na Etapa 2, selecionar o card "Dinheiro e itens".
  2. No campo "Meta financeira (R$)", digitar `3000000`.
  3. Verificar a formatação monetária automática.
  4. Na seção de itens físicos, clicar em "+ Adicionar item".
  5. Inserir Nome do item: "Cesta básica", Quantidade: "200", Unidade de medida: "Unidades".
  6. Clicar novamente em "+ Adicionar item" e preencher "Cobertor", Quantidade: "150", Unidade: "Peças".
  7. Clicar em "Continuar".
- **Dados de Teste / Entradas:**
  - Modalidade: `"Dinheiro e itens"`
  - Meta Financeira: `R$ 30.000,00`
  - Item 1: `"Cesta básica"`, Qtd: `200`, Unidade: `"Unidades"`
  - Item 2: `"Cobertor"`, Qtd: `150`, Unidade: `"Peças"`
- **Resultado Esperado:** A máscara formata para `R$ 30.000,00`; os itens são adicionados à tabela dinâmica com opções de edição/exclusão; ao clicar em "Continuar", avança para a Etapa 3.

#### CT-CAMP-027: Validação da máscara monetária e bloqueio de caracteres não numéricos
- **Tela / Módulo Relacionado:** Tela 06 — Criação Etapa 2 (`campanhas-criar-campanha-metas-e-itens.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-010, RNF-004
- **Tipo de Teste:** Borda / Negativo
- **Pré-condições:** Gestor no campo "Meta financeira (R$)".
- **Passo a Passo:**
  1. Tentar digitar letras ("abc"), caracteres especiais ("!@#") e múltiplos pontos decimais no campo.
  2. Tentar colar o texto: `R$ 15.000,555` (três casas decimais).
- **Dados de Teste / Entradas:** Entradas inválidas: `"letras"`, `"-500"`, `"15.000,555"`.
- **Resultado Esperado:** O campo aceita estritamente números; formata automaticamente da direita para a esquerda em padrão `BRL`; bloqueia sinais negativos e limita estritamente a 2 casas de centavos.

#### CT-CAMP-028: Seleção da modalidade "Somente itens" sem adicionar nenhum item à lista
- **Tela / Módulo Relacionado:** Tela 06 — Criação Etapa 2 (`campanhas-criar-campanha-metas-e-itens.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-010
- **Tipo de Teste:** Negativo
- **Pré-condições:** Gestor na Etapa 2.
- **Passo a Passo:**
  1. Marcar a opção "Somente itens".
  2. Verificar que o campo "Meta financeira" é desabilitado/ocultado.
  3. Não adicionar nenhum item físico na lista (lista vazia).
  4. Clicar em "Continuar".
- **Dados de Teste / Entradas:** Lista de itens = `[]`.
- **Resultado Esperado:** A interface impede o avanço e destaca a seção de itens em vermelho com alerta: "Adicione ao menos um item necessário com quantidade válida para prosseguir nesta modalidade."

#### CT-CAMP-029: Inserção de quantidade inválida em itens físicos (negativos, decimais, zero e limite 999)
- **Tela / Módulo Relacionado:** Tela 06 — Criação Etapa 2 (`campanhas-criar-campanha-metas-e-itens.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-010, RNF-004
- **Tipo de Teste:** Negativo / Borda
- **Pré-condições:** Formulário de item físico aberto.
- **Passo a Passo:**
  1. No campo de quantidade do item, inserir `0`.
  2. Tentar inserir `-10`.
  3. Tentar inserir `2,5`.
  4. Inserir `1000` (limite prudencial sugerido: 999 por item).
- **Dados de Teste / Entradas:** Quantidades: `0`, `-10`, `2.5`, `1000`.
- **Resultado Esperado:** O input rejeita caracteres decimais e negativos (`type="number" min="1" step="1"`); acusa erro para quantidade zero ("A quantidade deve ser de no mínimo 1 unidade") e emite aviso caso ultrapasse o limite parametrizado.

---

### Tela 07: Criação de Campanha — Etapa 3: Imagens, Revisão e Publicação (`campanhas-criar-campanha-imagens-revisao-publicacao.png`)

#### CT-CAMP-030: Upload válido de capa e galeria, revisão, aceite do termo e publicação oficial
- **Tela / Módulo Relacionado:** Tela 07 — Criação Etapa 3 (`campanhas-criar-campanha-imagens-revisao-publicacao.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-012, RF-018
- **Tipo de Teste:** Positivo
- **Pré-condições:** Etapas 1 e 2 concluídas com sucesso.
- **Passo a Passo:**
  1. Fazer upload da Imagem de Capa (arquivo `capa_campanha.jpg`, tamanho 2.1 MB).
  2. Adicionar 3 imagens na galeria secundária (`foto1.png`, `foto2.jpg`, `foto3.jpg`).
  3. Verificar o bloco consolidado "Revise os dados da campanha": conferir título, datas, entidade, meta financeira e itens.
  4. Marcar o checkbox obrigatório: "Declaro que todas as informações são verídicas e me comprometo a prestar contas dos recursos arrecadados".
  5. Clicar no botão primário "Publicar campanha".
- **Dados de Teste / Entradas:** Imagens JPG/PNG válidas <= 5MB; Checkbox = `true`.
- **Resultado Esperado:** O botão exibe spinner de carregamento ("Publicando..."); a campanha é persistida no backend com status `PUBLICADA`; o sistema redireciona o usuário para o painel de campanhas com modal/toast de sucesso "Campanha publicada com sucesso!".

#### CT-CAMP-031: Tentativa de publicação sem marcação do termo de compromisso obrigatório
- **Tela / Módulo Relacionado:** Tela 07 — Criação Etapa 3 (`campanhas-criar-campanha-imagens-revisao-publicacao.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-012
- **Tipo de Teste:** Negativo
- **Pré-condições:** Imagens enviadas, dados revisados, mas checkbox de aceite desmarcado (`checked = false`).
- **Passo a Passo:**
  1. Deixar o checkbox de veracidade e prestação de contas desmarcado.
  2. Clicar no botão "Publicar campanha".
- **Dados de Teste / Entradas:** Checkbox = `false`.
- **Resultado Esperado:** A publicação é bloqueada; o checkbox é realçado com contorno vermelho pulsante e exibe mensagem de erro: "É obrigatório aceitar o termo de responsabilidade e prestação de contas para publicar a campanha."

#### CT-CAMP-032: Upload de arquivo excedendo limite de 5MB ou extensão não autorizada
- **Tela / Módulo Relacionado:** Tela 07 — Criação Etapa 3 (`campanhas-criar-campanha-imagens-revisao-publicacao.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-018, RNF-004
- **Tipo de Teste:** Negativo / Segurança
- **Pré-condições:** Gestor no componente de upload de imagens.
- **Passo a Passo:**
  1. Tentar fazer upload de imagem `foto_pesada.jpg` com 8.5 MB.
  2. Tentar fazer upload de arquivo executável `script.exe` ou `documento.pdf`.
  3. Tentar fazer upload de imagem vetorial `vetor.svg` contendo tag `<script>`.
- **Dados de Teste / Entradas:** Arquivos: `8.5MB.jpg`, `app.exe`, `doc.pdf`, `test.svg`.
- **Resultado Esperado:** O frontend valida os metadados antes de iniciar o upload e recusa os arquivos: "Arquivo inválido. O tamanho máximo permitido é de 5 MB e o formato deve ser exclusivamente JPG ou PNG." Nenhum arquivo não autorizado é transferido para o servidor.

#### CT-CAMP-033: Proteção contra clique duplo (idempotência) no botão "Publicar campanha"
- **Tela / Módulo Relacionado:** Tela 07 — Criação Etapa 3 (`campanhas-criar-campanha-imagens-revisao-publicacao.png`) / Módulo Campanhas
- **Requisito Vinculado:** RNF-008
- **Tipo de Teste:** Borda / Resiliência
- **Pré-condições:** Todos os campos da Etapa 3 preenchidos e válidos.
- **Passo a Passo:**
  1. Clicar rapidamente duas ou mais vezes consecutivas no botão "Publicar campanha".
  2. Verificar as requisições enviadas na aba Network do DevTools.
- **Dados de Teste / Entradas:** Cliques consecutivos com intervalo inferior a 200ms.
- **Resultado Esperado:** Ao primeiro clique, o botão é imediatamente colocado em estado `disabled`, renderiza um loader animado e impede submissões adicionais. Apenas uma única requisição `POST /api/v1/campanhas` é enviada, prevenindo duplicação no banco de dados.

---

### Tela 08: Edição de Campanha Publicada (`campanhas-editar-campanhas.png`)

#### CT-CAMP-034: Edição permitida de campos abertos em campanha ativa
- **Tela / Módulo Relacionado:** Tela 08 — Editar Campanha (`campanhas-editar-campanhas.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-011
- **Tipo de Teste:** Positivo
- **Pré-condições:** Campanha "Mesa solidária" ativa com doações já recebidas.
- **Passo a Passo:**
  1. Acessar `/gestao/campanhas/1/editar`.
  2. Alterar o texto da Descrição da campanha (acrescentar novo parágrafo).
  3. Prorrogar a Data de Término em mais 15 dias.
  4. Fazer upload de uma nova foto de capa.
  5. Clicar em "Salvar alterações".
- **Dados de Teste / Entradas:** Descrição atualizada; Data Término: `06/12/2026`.
- **Resultado Esperado:** O sistema processa a requisição `PUT /api/v1/campanhas/1`; exibe toast verde "Alterações salvas com sucesso!"; ao consultar a página pública da campanha, os novos dados já estão visíveis.

#### CT-CAMP-035: Verificação de imutabilidade de campos travados após publicação
- **Tela / Módulo Relacionado:** Tela 08 — Editar Campanha (`campanhas-editar-campanhas.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-011
- **Tipo de Teste:** Negativo / Integridade
- **Pré-condições:** Campanha ativa em modo de edição.
- **Passo a Passo:**
  1. Acessar a tela de edição.
  2. Tentar clicar e alterar o campo "Entidade responsável".
  3. Tentar alterar o campo "Data de início" da campanha.
  4. Inspecionar o código HTML dos campos via DevTools.
- **Dados de Teste / Entradas:** Tentativa de alteração manual e via injeção DOM (`element.value = ...`).
- **Resultado Esperado:** Os campos apresentam atributo `disabled` ou `readOnly` com ícone de cadeado visível. Se o usuário forçar a alteração no DOM e submeter, o backend rejeita o payload ou descarta a alteração desses campos mantendo os valores originais intactos.

#### CT-CAMP-036: Bloqueio de redução da meta financeira para valor abaixo do já arrecadado
- **Tela / Módulo Relacionado:** Tela 08 — Editar Campanha (`campanhas-editar-campanhas.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-011, RNF-008
- **Tipo de Teste:** Negativo / Regra de Negócio
- **Pré-condições:** Campanha "Mesa solidária" com R$ 18.400,00 já arrecadados.
- **Passo a Passo:**
  1. Na tela de edição, localizar o campo "Meta financeira (R$)".
  2. Apagar o valor de R$ 30.000,00 e inserir `R$ 15.000,00` (valor inferior ao montante já doado).
  3. Clicar em "Salvar alterações".
- **Dados de Teste / Entradas:** Meta financeira proposta: `R$ 15.000,00` (arrecadado: `R$ 18.400,00`).
- **Resultado Esperado:** A interface barra a submissão e emite erro inline impeditivo: "A meta não pode ser inferior ao valor já arrecadado (R$ 18.400,00)". O registro permanece com o valor de R$ 30.000,00 inalterado.

#### CT-CAMP-037: Bloqueio de exclusão de item físico com doações confirmadas
- **Tela / Módulo Relacionado:** Tela 08 — Editar Campanha (`campanhas-editar-campanhas.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-011, RNF-007
- **Tipo de Teste:** Negativo / Regra de Negócio
- **Pré-condições:** Item "Cesta básica" possui 140 unidades já doadas e confirmadas na campanha.
- **Passo a Passo:**
  1. Na tabela de itens da campanha, localizar a linha de "Cesta básica".
  2. Tentar clicar no botão de lixeira / exclusão do item.
- **Dados de Teste / Entradas:** Tentativa de exclusão de item com histórico financeiro/físico associado.
- **Resultado Esperado:** O ícone de exclusão está desabilitado (estilo acinzentado com cursor `not-allowed`); ao passar o mouse, exibe tooltip explicativo: "Não é possível remover itens com doações já registradas."

---

### Tela 09: Acompanhamento da Campanha (`campanhas-acompanhamento-de-campanha .png`)

#### CT-CAMP-038: Consulta de métricas em tempo real e listagem de doações da campanha
- **Tela / Módulo Relacionado:** Tela 09 — Acompanhamento da Campanha (`campanhas-acompanhamento-de-campanha .png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-026, RF-027
- **Tipo de Teste:** Positivo
- **Pré-condições:** Gestor autenticado com campanha ativa com arrecadações parciais.
- **Passo a Passo:**
  1. Acessar `/gestao/campanhas/1/acompanhar`.
  2. Conferir cards de indicadores no topo:
     - Arrecadação financeira: R$ 18.400,00 de R$ 30.000,00 (61%).
     - Itens recebidos: 260 de 480 (54%).
     - Famílias beneficiadas: estimativa calculada.
  3. Visualizar a tabela de doações recebidas com colunas: Data, Doador, Tipo (Financeira/Itens), Valor/Quantidade e Status.
  4. Testar a ordenação por data e valor.
- **Dados de Teste / Entradas:** Consulta e ordenação na tabela.
- **Resultado Esperado:** Os dados coincidem exatamente com os registros da base; a tabela é paginada e permite visualização dos detalhes de cada entrada.

#### CT-CAMP-039: Exportação de relatório CSV com grande volume de dados
- **Tela / Módulo Relacionado:** Tela 09 — Acompanhamento da Campanha (`campanhas-acompanhamento-de-campanha .png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-030
- **Tipo de Teste:** Positivo / Borda
- **Pré-condições:** Campanha com mais de 100 doações registradas.
- **Passo a Passo:**
  1. Na tela de acompanhamento, clicar no botão "Exportar" (ícone de planilha).
  2. Observar a resposta da interface.
  3. Abrir o arquivo baixado em um leitor de planilhas (Excel ou LibreOffice).
- **Dados de Teste / Entradas:** Clique em "Exportar CSV".
- **Resultado Esperado:** A UI exibe feedback visual não bloqueante ("Gerando arquivo CSV..."); o download de `relatorio-doacoes-campanha-1.csv` é concluído; o arquivo possui codificação UTF-8 com BOM (acentuação correta) e separador ponto-e-vírgula (`;`), contendo todas as linhas e cabeçalhos correspondentes.

#### CT-CAMP-040: Publicação de atualização com mensagem na timeline da campanha
- **Tela / Módulo Relacionado:** Tela 09 — Acompanhamento da Campanha (`campanhas-acompanhamento-de-campanha .png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-011, RF-015
- **Tipo de Teste:** Positivo
- **Pré-condições:** Gestor na seção lateral "Comunicar com a comunidade".
- **Passo a Passo:**
  1. No textarea "Mensagem aos doadores", digitar: "Chegamos a 60% da meta! Muito obrigado a todos os apoiadores da APAE. As primeiras 100 cestas já foram encomendadas."
  2. Clicar no botão "Publicar atualização".
  3. Acessar a página pública de detalhes da campanha (Tela 03).
- **Dados de Teste / Entradas:** Texto de mensagem com 145 caracteres.
- **Resultado Esperado:** O sistema persiste o marco de atualização; exibe confirmação no painel gerencial; na página pública da campanha, a mensagem aparece com carimbo de data/hora na aba "Atualizações da Campanha".

#### CT-CAMP-041: Tentativa de envio de atualização na timeline com texto em branco
- **Tela / Módulo Relacionado:** Tela 09 — Acompanhamento da Campanha (`campanhas-acompanhamento-de-campanha .png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-011, RNF-004
- **Tipo de Teste:** Negativo
- **Pré-condições:** Campo de mensagem vazio ou contendo apenas espaços em branco.
- **Passo a Passo:**
  1. No campo de mensagem, pressionar apenas a barra de espaço várias vezes (`"   "`).
  2. Clicar em "Publicar atualização".
- **Dados de Teste / Entradas:** Texto = `"   "`.
- **Resultado Esperado:** O botão permanece desabilitado ou acusa erro inline: "A mensagem de atualização não pode estar em branco." Nenhuma publicação vazia é gravada.

---

### Tela 10: Encerramento e Prestação de Contas (`campanhas-encerramento-prestacao-contas.png`)

#### CT-CAMP-042: Prestação de contas com conciliação integral (Saldo R$ 0,00), anexo de NFs e publicação
- **Tela / Módulo Relacionado:** Tela 10 — Encerramento e Prestação de Contas (`campanhas-encerramento-prestacao-contas.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-013, RNF-009
- **Tipo de Teste:** Positivo
- **Pré-condições:** Campanha encerrada com R$ 15.000,00 arrecadados.
- **Passo a Passo:**
  1. Acessar `/gestao/campanhas/1/prestacao-contas`.
  2. Verificar o bloco de conciliação: Total arrecadado: R$ 15.000,00; Recursos aplicados: R$ 15.000,00; Saldo restante: R$ 0,00.
  3. Na tabela de despesas, cadastrar compras de alimentos com datas e valores correspondentes.
  4. Fazer upload dos comprovantes em PDF das Notas Fiscais.
  5. Redigir o "Relato de impacto social alcançado".
  6. Validar os itens do checklist de conferência.
  7. Marcar o checkbox "Declaro sob as penas da lei que todas as despesas informadas foram integralmente destinadas à causa".
  8. Clicar no botão "Publicar prestação de contas".
- **Dados de Teste / Entradas:** Despesas somando R$ 15.000,00; Comprovantes anexados; Relato com 350 caracteres.
- **Resultado Esperado:** O sistema atualiza o status da campanha para `CONTAS_PRESTADAS`; os dados de conciliação passam a ser exibidos publicamente na página da campanha em seção de transparência pública.

#### CT-CAMP-043: Bloqueio de publicação com conciliação divergente ou sem anexo de notas fiscais
- **Tela / Módulo Relacionado:** Tela 10 — Encerramento e Prestação de Contas (`campanhas-encerramento-prestacao-contas.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-013
- **Tipo de Teste:** Negativo / Regra de Negócio
- **Pré-condições:** Campanha com R$ 15.000,00 arrecadados, mas despesas cadastradas somam apenas R$ 12.000,00 (Saldo restante = R$ 3.000,00 sem justificativa).
- **Passo a Passo:**
  1. Preencher despesas somando R$ 12.000,00.
  2. Deixar de anexar o comprovante em PDF de uma das despesas.
  3. Tentar clicar em "Publicar prestação de contas".
- **Dados de Teste / Entradas:** Arrecadado: R$ 15.000,00; Despesas: R$ 12.000,00; Saldo: R$ 3.000,00.
- **Resultado Esperado:** O botão de publicação permanece desabilitado; o checklist acusa pendências em vermelho: "O saldo residual de R$ 3.000,00 deve ter sua destinação informada" e "Existem despesas sem nota fiscal comprobatória anexada."

#### CT-CAMP-044: Validação de limites de caracteres no relato de impacto social
- **Tela / Módulo Relacionado:** Tela 10 — Encerramento e Prestação de Contas (`campanhas-encerramento-prestacao-contas.png`) / Módulo Campanhas
- **Requisito Vinculado:** RF-013, RNF-004
- **Tipo de Teste:** Borda
- **Pré-condições:** Gestor no campo "Relato de impacto".
- **Passo a Passo:**
  1. Digitar menos de 30 caracteres ("Campanha deu tudo certo.") e tentar submeter.
  2. Digitar mais de 1.500 caracteres no relato.
- **Dados de Teste / Entradas:** Texto curto: 24 caracteres; Texto longo: 1.501 caracteres.
- **Resultado Esperado:** Para texto inferior a 30 caracteres: "O relato de impacto deve conter no mínimo 30 caracteres detalhando os resultados obtidos." Para texto superior a 1.500 caracteres, o campo restringe a digitação ou avisa sobre o limite excedido.

---

## 3. Módulo 2: Doações (Grupo 3)

```
Fluxo Completo de Doação:
[Escolha do Tipo] ──► [Financeira (R$ / Frequência)] ────────────┐
                  └──► [Itens Físicos] ──► [Agendamento]        │
                                                │               ▼
                                                └─► [Identificação do Doador]
                                                            │
                                                            ▼
                                                [Gateway de Pagamento: PIX]
                                                            │
                                                            ▼
                                                [Confirmação e Comprovantes]
```

---

### Tela 11: Escolha do Tipo de Doação (`doacoes-tipo-doacao.png`)

#### CT-DOA-001: Seleção da modalidade de doação financeira e redirecionamento
- **Tela / Módulo Relacionado:** Tela 11 — Tipo de Doação (`doacoes-tipo-doacao.png`) / Módulo Doações
- **Requisito Vinculado:** RF-020
- **Tipo de Teste:** Positivo
- **Pré-condições:** Acessar a esteira de doação de uma campanha mista ativa.
- **Passo a Passo:**
  1. Acessar `/doacoes/iniciar?campanhaId=1`.
  2. Visualizar os dois cards: "Doação financeira" (com ícone de moeda) e "Doação de itens" (com ícone de caixa/mantimento).
  3. Clicar no card "Doação financeira".
  4. Observar a alteração do estilo visual (borda azul e radio selecionado) e do rótulo do botão.
  5. Clicar em "Continuar com doação financeira".
- **Dados de Teste / Entradas:** Seleção: `"FINANCEIRA"`.
- **Resultado Esperado:** O doador é redirecionado para a Tela 12 (`/doacoes/financeira?campanhaId=1`).

#### CT-DOA-002: Seleção da modalidade de doação de itens físicos e redirecionamento
- **Tela / Módulo Relacionado:** Tela 11 — Tipo de Doação (`doacoes-tipo-doacao.png`) / Módulo Doações
- **Requisito Vinculado:** RF-020
- **Tipo de Teste:** Positivo
- **Pré-condições:** Campanha com itens físicos ativos.
- **Passo a Passo:**
  1. Na Tela 11, clicar no card "Doação de itens".
  2. Verificar o destaque visual no card de itens.
  3. Clicar no botão "Continuar com doação de itens".
- **Dados de Teste / Entradas:** Seleção: `"ITENS"`.
- **Resultado Esperado:** O doador é direcionado para a Tela 13 (`/doacoes/itens?campanhaId=1`).

#### CT-DOA-003: Bypass automático do bifurcador em campanha restrita a modalidade única
- **Tela / Módulo Relacionado:** Tela 11 — Tipo de Doação (`doacoes-tipo-doacao.png`) / Módulo Doações
- **Requisito Vinculado:** RF-020
- **Tipo de Teste:** Borda / Usabilidade
- **Pré-condições:** Campanha 2 cadastrada exclusivamente como "Somente dinheiro".
- **Passo a Passo:**
  1. Na página de detalhes da Campanha 2, clicar em "Doar".
  2. Observar a tela de destino.
- **Dados de Teste / Entradas:** Campanha ID: 2 (`modalidade: "SOMENTE_DINHEIRO"`).
- **Resultado Esperado:** O sistema não exibe a Tela 11 de escolha de modalidade; o usuário é conduzido diretamente para a Tela 12 (Doação Financeira), evitando cliques desnecessários.

---

### Tela 12: Doação Financeira — Valor e Frequência (`doacoes-doacao-financeira.png`)

#### CT-DOA-004: Seleção de valor pré-definido e contribuição única
- **Tela / Módulo Relacionado:** Tela 12 — Doação Financeira (`doacoes-doacao-financeira.png`) / Módulo Doações
- **Requisito Vinculado:** RF-020, RF-021
- **Tipo de Teste:** Positivo
- **Pré-condições:** Doador na tela de doação financeira.
- **Passo a Passo:**
  1. Clicar no botão de valor rápido "R$ 50".
  2. Verificar que o botão selecionado adquire fundo azul destacado.
  3. Na seção de frequência, marcar "Única".
  4. Conferir o resumo lateral: "Valor da doação: R$ 50,00", "Frequência: Pagamento único".
  5. Clicar no botão "Continuar para identificação".
- **Dados de Teste / Entradas:** Valor: `R$ 50,00`; Frequência: `"UNICA"`.
- **Resultado Esperado:** O valor de R$ 50,00 é consolidado no estado da sessão e o sistema avança para a Tela 15 (Identificação do Doador).

#### CT-DOA-005: Inserção de valor customizado com máscara monetária
- **Tela / Módulo Relacionado:** Tela 12 — Doação Financeira (`doacoes-doacao-financeira.png`) / Módulo Doações
- **Requisito Vinculado:** RF-020, RF-021
- **Tipo de Teste:** Positivo
- **Pré-condições:** Doador na tela de doação financeira.
- **Passo a Passo:**
  1. No campo "Ou informe outro valor (R$)", digitar `15050`.
  2. Verificar a aplicação da máscara monetária.
  3. Conferir o resumo lateral atualizado dinamicamente com o novo valor.
  4. Clicar em "Continuar para identificação".
- **Dados de Teste / Entradas:** Dígitos: `15050` -> formatado como `R$ 150,50`.
- **Resultado Esperado:** O campo exibe `R$ 150,50`; o resumo lateral sincroniza instantaneamente; o sistema avança para a identificação com o valor exato.

#### CT-DOA-006: Validação de valor mínimo de doação financeira (menor que R$ 1,00 ou nulo)
- **Tela / Módulo Relacionado:** Tela 12 — Doação Financeira (`doacoes-doacao-financeira.png`) / Módulo Doações
- **Requisito Vinculado:** RF-020, RNF-004
- **Tipo de Teste:** Negativo / Borda
- **Pré-condições:** Doador na tela de doação financeira.
- **Passo a Passo:**
  1. No campo de valor customizado, digitar `0,50` (cinquenta centavos).
  2. Tentar avançar clicando em "Continuar para identificação".
  3. Limpar o campo deixando o valor em branco (`R$ 0,00`) e tentar avançar.
- **Dados de Teste / Entradas:** Valores: `R$ 0,50` e `R$ 0,00`.
- **Resultado Esperado:** A interface bloqueia o avanço e emite alerta de validação inline: "O valor mínimo para doação financeira é de R$ 1,00." O botão de prosseguir permanece desabilitado.

#### CT-DOA-007: Seleção de doação recorrente semanal e exibição de aviso informativo
- **Tela / Módulo Relacionado:** Tela 12 — Doação Financeira (`doacoes-doacao-financeira.png`) / Módulo Doações
- **Requisito Vinculado:** RF-020
- **Tipo de Teste:** Positivo / Borda
- **Pré-condições:** Doador selecionando valor válido.
- **Passo a Passo:**
  1. Selecionar o valor de R$ 25,00.
  2. Na frequência, marcar a opção "Recorrente - semanal".
  3. Inspecionar o texto informativo abaixo da seleção.
- **Dados de Teste / Entradas:** Frequência: `"RECORRENTE_SEMANAL"`.
- **Resultado Esperado:** A interface exibe aviso claro: "Atenção: o DoaSync não realiza débitos automáticos no seu banco. Você receberá um lembrete semanal por e-mail com o código PIX para efetuar sua doação."

---

### Tela 13: Doação de Itens Necessários (`doacoes-doacao-itens.png`)

#### CT-DOA-008: Seleção de múltiplos itens com ajuste de quantidades pelos botões `[-]` e `[+]`
- **Tela / Módulo Relacionado:** Tela 13 — Doação de Itens (`doacoes-doacao-itens.png`) / Módulo Doações
- **Requisito Vinculado:** RF-020, RF-021
- **Tipo de Teste:** Positivo
- **Pré-condições:** Campanha com itens ativos: Cesta básica (faltam 60) e Kit de higiene (faltam 100).
- **Passo a Passo:**
  1. Acessar `/doacoes/itens?campanhaId=1`.
  2. No item "Cesta básica", marcar o checkbox e clicar 3 vezes no botão `[+]` (quantidade: 3).
  3. No item "Kit de higiene", marcar o checkbox e clicar 2 vezes no botão `[+]` (quantidade: 2).
  4. No kit de higiene, clicar 1 vez no botão `[-]` (quantidade passa para 1).
  5. Conferir a barra lateral de resumo: "Total selecionado: 4 unidades · 2 tipos de item".
  6. Clicar em "Continuar para identificação".
- **Dados de Teste / Entradas:** Cesta básica: 3 unidades; Kit higiene: 1 unidade.
- **Resultado Esperado:** Os seletores incrementam e decrementam corretamente; o botão `[-]` desabilita ao atingir quantidade 1; o resumo totaliza 4 itens e o sistema avança para a próxima etapa.

#### CT-DOA-009: Bloqueio de avanço com quantidade de itens zerada
- **Tela / Módulo Relacionado:** Tela 13 — Doação de Itens (`doacoes-doacao-itens.png`) / Módulo Doações
- **Requisito Vinculado:** RF-020
- **Tipo de Teste:** Negativo
- **Pré-condições:** Tela de itens aberta sem nenhuma seleção.
- **Passo a Passo:**
  1. Desmarcar todos os checkboxes de itens.
  2. Tentar clicar no botão "Continuar para identificação".
- **Dados de Teste / Entradas:** Seleção: 0 itens.
- **Resultado Esperado:** O botão "Continuar para identificação" permanece em estado `disabled` (cinza) com tooltip: "Selecione ao menos um item e a quantidade que deseja doar."

#### CT-DOA-010: Concorrência na doação de itens físicos (estoque/meta atingida durante seleção)
- **Tela / Módulo Relacionado:** Tela 13 — Doação de Itens (`doacoes-doacao-itens.png`) / Módulo Doações
- **Requisito Vinculado:** RF-020, RNF-008
- **Tipo de Teste:** Borda / Concorrência
- **Pré-condições:** Campanha necessita de apenas 5 cestas básicas para fechar a meta total. Doador A seleciona 5 cestas. Antes de o Doador A submeter, o Doador B confirma a entrega de 3 cestas.
- **Passo a Passo:**
  1. Doador A seleciona 5 cestas básicas.
  2. Em segundo navegador, confirmar doação de 3 cestas para a mesma campanha.
  3. Doador A clica em "Continuar para identificação".
- **Dados de Teste / Entradas:** Solicitação de 5 unidades quando restam apenas 2 unidades em aberto.
- **Resultado Esperado:** O sistema intercepta o conflito (`409 Conflict`) e exibe aviso amigável na tela: "A campanha agora necessita de apenas 2 unidades deste item. Ajustamos a quantidade automaticamente para 2 unidades para você prosseguir."

---

### Tela 14: Agendamento de Entrega ou Retirada (`doacoes-agendamento-entrega-retirada.png`)

#### CT-DOA-011: Agendamento de entrega física na sede em dia útil e horário comercial
- **Tela / Módulo Relacionado:** Tela 14 — Agendamento (`doacoes-agendamento-entrega-retirada.png`) / Módulo Doações
- **Requisito Vinculado:** RF-020, RF-021
- **Tipo de Teste:** Positivo
- **Pré-condições:** Itens físicos selecionados.
- **Passo a Passo:**
  1. Selecionar o card "Vou entregar na sede da entidade".
  2. Conferir o endereço institucional exibido (Rua das Palmeiras, 120 - Centro).
  3. No calendário (date picker), selecionar a próxima terça-feira (dia útil).
  4. No dropdown "Faixa de horário", selecionar "10h às 11h".
  5. No campo opcional "Observações para a equipe", inserir: "Entrega em caixas fechadas".
  6. Clicar em "Confirmar agendamento".
- **Dados de Teste / Entradas:** Modalidade: `"SEDE"`, Data: dia útil futuro, Horário: `"10:00 - 11:00"`.
- **Resultado Esperado:** Os dados de agendamento são vinculados à doação física; o sistema direciona para a tela de Identificação do Doador ou Confirmação.

#### CT-DOA-012: Bloqueio de seleção de datas inválidas no date picker (passado, fins de semana e feriados)
- **Tela / Módulo Relacionado:** Tela 14 — Agendamento (`doacoes-agendamento-entrega-retirada.png`) / Módulo Doações
- **Requisito Vinculado:** RF-020
- **Tipo de Teste:** Negativo / Borda
- **Pré-condições:** Doador no calendário de agendamento.
- **Passo a Passo:**
  1. Abrir o calendário do date picker.
  2. Tentar clicar em datas anteriores ao dia atual.
  3. Tentar clicar em sábados ou domingos.
  4. Tentar clicar em um feriado nacional cadastrado.
- **Dados de Teste / Entradas:** Seleção de datas passadas e dias não úteis.
- **Resultado Esperado:** Todas as datas passadas, fins de semana e feriados possuem atributo visual desabilitado (`disabled`), cor cinza e cursor bloqueado, impedindo a seleção por clique ou digitação.

#### CT-DOA-013: Solicitação de retirada domiciliar e validação de dados de endereço e contato
- **Tela / Módulo Relacionado:** Tela 14 — Agendamento (`doacoes-agendamento-entrega-retirada.png`) / Módulo Doações
- **Requisito Vinculado:** RF-020
- **Tipo de Teste:** Positivo / Borda
- **Pré-condições:** Doador selecionando modalidade "Solicitar retirada".
- **Passo a Passo:**
  1. Clicar no card "Solicitar retirada".
  2. Verificar se o formulário expande campos para Endereço de Coleta (Rua, Número, Bairro, CEP, Telefone/WhatsApp).
  3. Tentar avançar sem preencher o endereço.
  4. Preencher endereço completo e telefone válido com máscara `(11) 98765-4321`.
  5. Clicar em "Confirmar agendamento".
- **Dados de Teste / Entradas:**
  - Rua: `"Av. Paulista"`, Nº: `"1000"`, Bairro: `"Bela Vista"`, CEP: `"01310-100"`
  - Telefone: `"(11) 98765-4321"`
- **Resultado Esperado:** O sistema exige obrigatoriamente os dados de localização para a logística da entidade caso retirada seja escolhida; avança com sucesso após validação de CEP e telefone.

---

### Tela 15: Identificação do Doador (`doacoes-identificacao-doador.png`)

#### CT-DOA-014: Identificação via login de usuário cadastrado
- **Tela / Módulo Relacionado:** Tela 15 — Identificação do Doador (`doacoes-identificacao-doador.png`) / Módulo Doações
- **Requisito Vinculado:** RF-002, RF-020
- **Tipo de Teste:** Positivo
- **Pré-condições:** Doador possui conta criada previamente (`doador@gmail.com`).
- **Passo a Passo:**
  1. Na aba "Já tenho uma conta", inserir e-mail `doador@gmail.com` e senha válida.
  2. Clicar no botão "Entrar e continuar".
- **Dados de Teste / Entradas:** E-mail: `doador@gmail.com`, Senha: `SenhaValida123!`.
- **Resultado Esperado:** O sistema autentica o usuário, armazena o token JWT na sessão e direciona imediatamente para a etapa de pagamento (PIX) com a doação atrelada ao perfil do doador.

#### CT-DOA-015: Identificação via Cadastro Rápido (Nome + E-mail)
- **Tela / Módulo Relacionado:** Tela 15 — Identificação do Doador (`doacoes-identificacao-doador.png`) / Módulo Doações
- **Requisito Vinculado:** RF-001, RF-020
- **Tipo de Teste:** Positivo
- **Pré-condições:** Visitante sem cadastro prévio.
- **Passo a Passo:**
  1. Clicar na aba/card "Cadastro rápido".
  2. Preencher Nome Completo: "Carlos Eduardo Silva".
  3. Preencher E-mail: "carlos.silva.teste@gmail.com".
  4. Clicar em "Cadastrar e continuar".
- **Dados de Teste / Entradas:** Nome: `"Carlos Eduardo Silva"`, E-mail: `"carlos.silva.teste@gmail.com"`.
- **Resultado Esperado:** O sistema cria a conta do doador com credenciais provisórias ou sem senha (enviando link de ativação por e-mail), gera token de autenticação e avança para a tela de pagamento.

#### CT-DOA-016: Identificação via Doação Anônima com conformidade à LGPD
- **Tela / Módulo Relacionado:** Tela 15 — Identificação do Doador (`doacoes-identificacao-doador.png`) / Módulo Doações
- **Requisito Vinculado:** RF-020, RNF-005, RNF-006
- **Tipo de Teste:** Positivo / LGPD
- **Pré-condições:** Doador deseja não expor seus dados públicos.
- **Passo a Passo:**
  1. Clicar na opção "Doação anônima".
  2. Ler a mensagem de aviso: "Seus dados pessoais não serão solicitados nem compartilhados publicamente ou com a entidade."
  3. Clicar no botão "Continuar anonimamente".
- **Dados de Teste / Entradas:** Modo: `"ANONIMO"`.
- **Resultado Esperado:** A aplicação avança para o pagamento gerando uma sessão temporária; o payload de criação da doação marca a flag `anonima: true`, garantindo que nas APIs públicas e no painel da entidade o registro apareça como "Doador anônimo".

#### CT-DOA-017: Cadastro rápido informando e-mail já existente com conta completa
- **Tela / Módulo Relacionado:** Tela 15 — Identificação do Doador (`doacoes-identificacao-doador.png`) / Módulo Doações
- **Requisito Vinculado:** RF-001, RF-020
- **Tipo de Teste:** Negativo / Borda
- **Pré-condições:** E-mail `usuario.existente@gmail.com` já registrado no banco de dados.
- **Passo a Passo:**
  1. Na aba "Cadastro rápido", digitar Nome e o e-mail `usuario.existente@gmail.com`.
  2. Clicar em "Cadastrar e continuar".
- **Dados de Teste / Entradas:** E-mail duplicado: `usuario.existente@gmail.com`.
- **Resultado Esperado:** O sistema não cria conta duplicada; exibe mensagem informativa com opção de troca rápida: "Este e-mail já possui cadastro. Por favor, informe sua senha para prosseguir" e direciona o foco para o formulário de login.

#### CT-DOA-018: Autenticação da requisição e prevenção de erro `401 Unauthorized` no checkout anônimo
- **Tela / Módulo Relacionado:** Tela 15 — Identificação do Doador (`doacoes-identificacao-doador.png`) / Módulo Doações
- **Requisito Vinculado:** RF-020, RNF-003
- **Tipo de Teste:** Negativo / Segurança
- **Pré-condições:** Usuário anônimo finalizando a doação; backend configurado para exigir autenticação.
- **Passo a Passo:**
  1. Optar por doação anônima.
  2. Submeter a requisição `POST /api/v1/doacoes`.
  3. Inspecionar o cabeçalho Authorization no DevTools.
- **Dados de Teste / Entradas:** Requisição anônima.
- **Resultado Esperado:** A aplicação utiliza um token anônimo (*guest token*) ou o endpoint permite chamadas públicas com payload anônimo sem disparar erro HTTP 401; a doação é registrada com sucesso.

---

### Tela 16: Pagamento via PIX (`doacoes-via-pix.png`)

#### CT-DOA-019: Geração de QR Code e código Copia-e-Cola com liquidação instantânea
- **Tela / Módulo Relacionado:** Tela 16 — Pagamento PIX (`doacoes-via-pix.png`) / Módulo Doações
- **Requisito Vinculado:** RF-021, RF-022, RF-023
- **Tipo de Teste:** Positivo
- **Pré-condições:** Doação financeira no valor de R$ 50,00 iniciada.
- **Passo a Passo:**
  1. Acessar a tela de pagamento via PIX.
  2. Verificar a renderização da imagem do QR Code dinâmico e o código alfanumérico Copia-e-Cola.
  3. Verificar o cronômetro regressivo ("Válido por 29:59 minutos").
  4. Simular o pagamento no mock do gateway de pagamento enviando webhook de liquidação (`status: "PAGO"`).
- **Dados de Teste / Entradas:** Valor: R$ 50,00; Gateway Webhook Event: `payment.approved`.
- **Resultado Esperado:** A tela escuta o evento via WebSocket ou polling e atualiza automaticamente (sem refresh) redirecionando para a Tela 18 (Confirmação da Doação) com status `CONCLUIDA`.

#### CT-DOA-020: Copiar código PIX para área de transferência com feedback visual
- **Tela / Módulo Relacionado:** Tela 16 — Pagamento PIX (`doacoes-via-pix.png`) / Módulo Doações
- **Requisito Vinculado:** RF-021
- **Tipo de Teste:** Positivo
- **Pré-condições:** Doador na tela do PIX.
- **Passo a Passo:**
  1. Clicar no botão "Copiar código PIX".
  2. Abrir um editor de texto e colar (Ctrl + V).
- **Dados de Teste / Entradas:** Clique em "Copiar código".
- **Resultado Esperado:** O texto do código EMV do PIX é copiado fielmente; o botão altera seu rótulo temporariamente para "Código copiado!" com ícone de check verde e exibe toast de confirmação.

#### CT-DOA-021: Expiração do tempo limite de 30 minutos do QR Code PIX
- **Tela / Módulo Relacionado:** Tela 16 — Pagamento PIX (`doacoes-via-pix.png`) / Módulo Doações
- **Requisito Vinculado:** RF-022, RNF-011
- **Tipo de Teste:** Negativo / Timeout
- **Pré-condições:** Doador com a tela do PIX aberta; tempo do cronômetro expira (ou forçar expiração via mock).
- **Passo a Passo:**
  1. Aguardar o cronômetro atingir `00:00` (ou simular expiração no frontend adiantando o relógio).
  2. Observar a tela do PIX.
- **Dados de Teste / Entradas:** Cronômetro = `00:00`.
- **Resultado Esperado:** O QR Code é ofuscado com uma camada cinza semitransparente; o código copia-e-cola é desabilitado; a tela exibe mensagem: "Tempo para pagamento expirado" e disponibiliza o botão "Gerar novo código PIX"; a doação original no backend é marcada com status `CANCELADA_TIMEOUT`.

#### CT-DOA-022: Verificação manual repetida de pagamento PIX e throttling de requisições
- **Tela / Módulo Relacionado:** Tela 16 — Pagamento PIX (`doacoes-via-pix.png`) / Módulo Doações
- **Requisito Vinculado:** RF-022, RNF-004
- **Tipo de Teste:** Borda / Resiliência
- **Pré-condições:** Doador na tela do PIX antes da liquidação bancária.
- **Passo a Passo:**
  1. Clicar repetidas vezes e rapidamente no botão "Verificar pagamento".
  2. Observar as requisições disparadas na aba Network.
- **Dados de Teste / Entradas:** 10 cliques rápidos consecutivos.
- **Resultado Esperado:** O sistema aplica throttling/debounce no frontend; exibe feedback sutil "Aguardando confirmação bancária..."; impede disparos descontrolados contra a API evitando erro `429 Too Many Requests`.

#### CT-DOA-023: Timeout na comunicação com o gateway de pagamento PIX (> 10 segundos)
- **Tela / Módulo Relacionado:** Tela 16 — Pagamento PIX (`doacoes-via-pix.png`) / Módulo Doações
- **Requisito Vinculado:** RF-022, RNF-011
- **Tipo de Teste:** Negativo / Erro de Rede
- **Pré-condições:** Simular lentidão extrema ou indisponibilidade no gateway de geração de PIX.
- **Passo a Passo:**
  1. Configurar proxy/mock para atrasar a resposta em 12 segundos ou retornar erro HTTP 504 Gateway Timeout.
  2. Doador tenta gerar o PIX.
- **Dados de Teste / Entradas:** Atraso de 12 segundos no endpoint do gateway.
- **Resultado Esperado:** A interface não trava nem fica em loading infinito; após 10 segundos, encerra a espera e exibe mensagem amigável: "Instabilidade temporária no serviço de pagamentos. Nenhuma cobrança foi gerada. [Tentar novamente]".

---

### Tela 17: Pagamento via Cartão ou Boleto (`doacoes-via-cartao-boleto.png`)

#### CT-DOA-024: Pagamento bem-sucedido com cartão de crédito
- **Tela / Módulo Relacionado:** Tela 17 — Cartão / Boleto (`doacoes-via-cartao-boleto.png`) / Módulo Doações
- **Requisito Vinculado:** RF-021, RF-022, RF-023
- **Tipo de Teste:** Positivo
- **Pré-condições:** Aba "Cartão de Crédito" selecionada.
- **Passo a Passo:**
  1. Inserir Número do Cartão: `4532 1170 8890 1234` (Visa válido de teste).
  2. Inserir Nome no Cartão: "MARIANA LIMA".
  3. Inserir Validade: `11/28`.
  4. Inserir CVV: `123`.
  5. Clicar no botão "Doar R$ 100,00 com cartão".
- **Dados de Teste / Entradas:** Cartão de crédito de testes com limite aprovado.
- **Resultado Esperado:** O botão exibe spinner "Processando pagamento..."; a transação é autorizada pela adquirente; o sistema redireciona com sucesso para a Tela 18 (Confirmação).

#### CT-DOA-025: Validação de máscaras e campos obrigatórios de cartão de crédito
- **Tela / Módulo Relacionado:** Tela 17 — Cartão / Boleto (`doacoes-via-cartao-boleto.png`) / Módulo Doações
- **Requisito Vinculado:** RF-021, RNF-004
- **Tipo de Teste:** Negativo / Borda
- **Pré-condições:** Doador na aba Cartão de Crédito.
- **Passo a Passo:**
  1. Inserir número com menos de 16 dígitos (`1234`).
  2. Inserir validade no passado (`05/22`).
  3. Inserir CVV com 2 dígitos (`12`).
  4. Tentar submeter o formulário.
- **Dados de Teste / Entradas:** Número: `1234`, Validade: `05/22`, CVV: `12`.
- **Resultado Esperado:** A submissão é bloqueada; os campos recebem destaque de erro:
  - "Número de cartão incompleto."
  - "Data de validade vencida."
  - "Código de segurança deve conter 3 ou 4 dígitos."

#### CT-DOA-026: Cartão recusado pela adquirente (saldo insuficiente ou suspeita de fraude)
- **Tela / Módulo Relacionado:** Tela 17 — Cartão / Boleto (`doacoes-via-cartao-boleto.png`) / Módulo Doações
- **Requisito Vinculado:** RF-022, RNF-011
- **Tipo de Teste:** Negativo
- **Pré-condições:** Utilizar cartão de teste configurado para retorno de recusa (ex: cartão com final 0002 - Saldo Insuficiente).
- **Passo a Passo:**
  1. Preencher os dados do cartão de teste de recusa.
  2. Clicar em "Doar R$ 100,00 com cartão".
- **Dados de Teste / Entradas:** Cartão final 0002 (Recusa bancária).
- **Resultado Esperado:** A interface não expõe códigos técnicos do adquirente; exibe banner vermelho de erro: "Não foi possível autorizar a transação. Por favor, verifique os dados informados, consulte seu banco ou tente doar via PIX."

#### CT-DOA-027: Emissão de Boleto Bancário com código de barras e data de vencimento
- **Tela / Módulo Relacionado:** Tela 17 — Cartão / Boleto (`doacoes-via-cartao-boleto.png`) / Módulo Doações
- **Requisito Vinculado:** RF-021, RF-022
- **Tipo de Teste:** Positivo
- **Pré-condições:** Selecionar a aba "Boleto Bancário".
- **Passo a Passo:**
  1. Clicar na aba "Boleto Bancário".
  2. Conferir o valor da doação e o prazo de vencimento informado (3 dias úteis).
  3. Clicar em "Gerar boleto bancário".
- **Dados de Teste / Entradas:** Seleção: `"BOLETO"`.
- **Resultado Esperado:** O boleto é gerado com linha digitável válida e botão para download do arquivo PDF; o status da doação é registrado como `PENDENTE_BOLETO`.

---

### Tela 18: Confirmação da Doação e Recibo (`doacoes-confirmacao-doacao.png`)

#### CT-DOA-028: Exibição da confirmação, código identificador e download do comprovante em PDF
- **Tela / Módulo Relacionado:** Tela 18 — Confirmação (`doacoes-confirmacao-doacao.png`) / Módulo Doações
- **Requisito Vinculado:** RF-023, RF-025
- **Tipo de Teste:** Positivo
- **Pré-condições:** Doação concluída com sucesso.
- **Passo a Passo:**
  1. Verificar a tela de confirmação renderizada.
  2. Conferir:
     - Mensagem de sucesso: "Doação confirmada! Obrigado por apoiar a APAE."
     - Código único da doação (ex: `DS-1080`).
     - Detalhes: Data/hora, campanha, valor (R$ 50,00) e meio de pagamento.
  3. Clicar no botão "Baixar comprovante (PDF)".
  4. Clicar no botão "Baixar recibo de doação (PDF)".
- **Dados de Teste / Entradas:** Clique nos botões de download.
- **Resultado Esperado:** Os arquivos `comprovante-DS1080.pdf` e `recibo-DS1080.pdf` são baixados no dispositivo; os PDFs contêm o cabeçalho oficial do DoaSync, dados da entidade, CNPJ, valor e chave de autenticidade eletrônica.

#### CT-DOA-029: Reenvio de comprovante de doação por e-mail
- **Tela / Módulo Relacionado:** Tela 18 — Confirmação (`doacoes-confirmacao-doacao.png`) / Módulo Doações
- **Requisito Vinculado:** RF-023, RF-025
- **Tipo de Teste:** Positivo
- **Pré-condições:** Doador cadastrado na tela de confirmação.
- **Passo a Passo:**
  1. Clicar no botão "Reenviar comprovante por e-mail".
- **Dados de Teste / Entradas:** Clique na ação de reenvio.
- **Resultado Esperado:** O sistema dispara o e-mail transacional com os anexos e exibe toast informativo na tela: "Comprovante enviado com sucesso para o e-mail cadastrado!".

#### CT-DOA-030: Falha no serviço de geração de PDF do comprovante com retry amigável
- **Tela / Módulo Relacionado:** Tela 18 — Confirmação (`doacoes-confirmacao-doacao.png`) / Módulo Doações
- **Requisito Vinculado:** RF-025, RNF-011
- **Tipo de Teste:** Negativo / Resiliência
- **Pré-condições:** Simular indisponibilidade temporária na biblioteca/serviço de renderização de PDF do backend.
- **Passo a Passo:**
  1. Na tela de confirmação, clicar em "Baixar comprovante (PDF)".
  2. O backend responde com erro `500` na rota de PDF.
- **Dados de Teste / Entradas:** Falha de PDF no servidor.
- **Resultado Esperado:** O navegador não quebra nem exibe página de erro em branco; um modal amigável informa: "Não foi possível gerar o arquivo PDF neste momento. Uma cópia detalhada foi enviada para o seu e-mail ou você pode [Tentar gerar novamente]."

#### CT-DOA-031: Doador anônimo na tela de confirmação (sem e-mail salvo na sessão)
- **Tela / Módulo Relacionado:** Tela 18 — Confirmação (`doacoes-confirmacao-doacao.png`) / Módulo Doações
- **Requisito Vinculado:** RF-023, RF-025, RNF-006
- **Tipo de Teste:** Borda / LGPD
- **Pré-condições:** Doação concluída sob a modalidade anônima.
- **Passo a Passo:**
  1. Visualizar a tela de confirmação de doação anônima.
  2. Verificar o comportamento do botão "Reenviar para e-mail".
- **Dados de Teste / Entradas:** Sessão anônima.
- **Resultado Esperado:** Como não há e-mail atrelado à conta, o botão solicita um e-mail temporário exclusivamente para despacho do recibo ("Informe um e-mail para receber o recibo") ou permanece desabilitado, enfatizando o botão direto de download do PDF na tela.

---

### Tela 19: Detalhe da Doação e Tracking de Destinação (`doacoes-detalhes-doacao.png`)

#### CT-DOA-032: Visualização da linha do tempo do ciclo de vida da doação e mensagem de impacto
- **Tela / Módulo Relacionado:** Tela 19 — Detalhes da Doação (`doacoes-detalhes-doacao.png`) / Módulo Doações
- **Requisito Vinculado:** RF-022, RF-024
- **Tipo de Teste:** Positivo
- **Pré-condições:** Doador autenticado consultando doação antiga que já foi aplicada pela entidade.
- **Passo a Passo:**
  1. Acessar `/doador/doacoes/DS-1080`.
  2. Conferir a linha do tempo (Timeline):
     - `Pendente` (02/10 às 14:20) - Check verde.
     - `Recebida` (02/10 às 14:22) - Check verde.
     - `Aplicada` (05/10 às 09:15) - Check verde.
  3. Ler o bloco de destinação dos recursos: "Compra de alimentos não perecíveis para composição de 50 cestas básicas."
  4. Ler a mensagem personalizada de agradecimento enviada pela APAE.
- **Dados de Teste / Entradas:** Consulta da doação `DS-1080`.
- **Resultado Esperado:** A timeline é renderizada com as datas e status corretos; o relato de aplicação é visível promovendo total transparência ao doador.

#### CT-DOA-033: Tentativa de visualização de doação de terceiro via manipulação de ID na URL (IDOR)
- **Tela / Módulo Relacionado:** Tela 19 — Detalhes da Doação (`doacoes-detalhes-doacao.png`) / Módulo Doações
- **Requisito Vinculado:** RF-024, RNF-003, RNF-006
- **Tipo de Teste:** Negativo / Segurança
- **Pré-condições:** Doador A logado (ID de usuário: 10); Doação `DS-9999` pertence ao Doador B (ID de usuário: 20).
- **Passo a Passo:**
  1. Com a sessão do Doador A ativa, digitar na URL: `/doador/doacoes/DS-9999`.
- **Dados de Teste / Entradas:** URL forçada com ID de doação de terceiro.
- **Resultado Esperado:** O sistema não exibe os dados da doação de terceiro; a API retorna HTTP `404 Not Found` (para não confirmar a existência da doação) ou `403 Forbidden`; a tela exibe mensagem "Doação não encontrada ou você não possui autorização para visualizá-la."

#### CT-DOA-034: Doação estagnada em status "Recebida" há mais de 30 dias sem alocação
- **Tela / Módulo Relacionado:** Tela 19 — Detalhes da Doação (`doacoes-detalhes-doacao.png`) / Módulo Doações
- **Requisito Vinculado:** RF-022, RF-024
- **Tipo de Teste:** Borda
- **Pré-condições:** Doação recebida há 40 dias, mas a entidade ainda não registrou a saída/aplicação do recurso.
- **Passo a Passo:**
  1. Acessar os detalhes da doação.
  2. Verificar o terceiro nó da timeline ("Aplicação do recurso").
- **Dados de Teste / Entradas:** Doação com data de recebimento > 30 dias e sem alocação.
- **Resultado Esperado:** A timeline exibe o estágio como "Em fase de alocação pela entidade", com texto explicativo acolhedor tranquilizando o doador de que a prestação de contas ocorre ao final do ciclo da campanha.

---

### Tela 20: Painel "Minhas Doações" do Doador (`doacoes-minhas-doacoes.png`)

#### CT-DOA-035: Visualização dos cards consolidados e tabela de histórico com filtros
- **Tela / Módulo Relacionado:** Tela 20 — Minhas Doações (`doacoes-minhas-doacoes.png`) / Módulo Doações
- **Requisito Vinculado:** RF-024
- **Tipo de Teste:** Positivo
- **Pré-condições:** Doador com histórico de contribuições financeiras e físicas.
- **Passo a Passo:**
  1. Acessar o menu "Minhas Doações".
  2. Conferir os cards de resumo no topo:
     - Total doado em dinheiro: R$ 200,00.
     - Total de itens entregues: 3 unidades.
     - Doações pendentes: 2 doações.
  3. Consultar a tabela de histórico: colunas Data, Código, Contribuição, Campanha, Pagamento e Destinação.
  4. Clicar no botão "Ver detalhe" da linha da doação `DS-1080`.
- **Dados de Teste / Entradas:** Navegação no painel do doador.
- **Resultado Esperado:** Os valores acumulados batem com a soma real das doações concluídas do usuário; o clique em "Ver detalhe" redireciona para a Tela 19 correspondente.

#### CT-DOA-036: Empty State para doador recém-cadastrado sem doações anteriores
- **Tela / Módulo Relacionado:** Tela 20 — Minhas Doações (`doacoes-minhas-doacoes.png`) / Módulo Doações
- **Requisito Vinculado:** RF-024, RNF-011
- **Tipo de Teste:** Borda
- **Pré-condições:** Usuário recém-cadastrado autenticado que nunca efetuou nenhuma contribuição.
- **Passo a Passo:**
  1. Acessar o painel "Minhas Doações".
  2. Observar os cards e a tabela.
- **Dados de Teste / Entradas:** Usuário com zero registros em `doacoes`.
- **Resultado Esperado:** Os cards de resumo exibem valores zerados (`R$ 0,00` e `0 itens`); a tabela dá lugar a um componente amigável de **Empty State** com a mensagem "Você ainda não realizou nenhuma doação. Que tal fazer a diferença hoje?" e botão centralizado "Conhecer causas e doar agora".

#### CT-DOA-037: Exportação do histórico de doações do doador em formato CSV
- **Tela / Módulo Relacionado:** Tela 20 — Minhas Doações (`doacoes-minhas-doacoes.png`) / Módulo Doações
- **Requisito Vinculado:** RF-024, RF-030
- **Tipo de Teste:** Positivo
- **Pré-condições:** Doador com doações registradas.
- **Passo a Passo:**
  1. No painel Minhas Doações, clicar no botão "Exportar histórico".
- **Dados de Teste / Entradas:** Clique em "Exportar histórico".
- **Resultado Esperado:** O navegador baixa o arquivo `minhas-doacoes-historico.csv` contendo todas as transações do doador com data, campanha, valor/itens, status e código de rastreio.

---

## 4. Módulo 3: Gestão e Dashboard (Grupo 4)

```
Fluxos de Gestão e Dashboard:
[Login e Recuperação] ──► [Onboarding Entidade] ──► [Dashboard Geral]
                                                          │
          ┌───────────────────────────────────────────────┼───────────────────────────────┐
          ▼                                               ▼                               ▼
[Dashboard por Campanha]                     [Gestão de Recebimentos]            [Controle de Estoque]
                                                          │                               │
                                                          ▼                               ▼
                                               [Conferência Física]             [Registro de Saídas]
```

---

### Tela 21: Login da Entidade e Recuperação de Senha (`gestao-login-recuperacao-senha.png`)

#### CT-GES-001: Autenticação válida de gestor de entidade com credenciais corretas
- **Tela / Módulo Relacionado:** Tela 21 — Login e Recuperação (`gestao-login-recuperacao-senha.png`) / Módulo Gestão
- **Requisito Vinculado:** RF-002, RF-005
- **Tipo de Teste:** Positivo
- **Pré-condições:** Gestor cadastrado e vinculado a uma entidade ativa (`gestor@apae.org.br`).
- **Passo a Passo:**
  1. Acessar `/gestao/login`.
  2. No painel esquerdo ("Acesso da Entidade"), inserir E-mail institucional `gestor@apae.org.br`.
  3. Inserir Senha válida `SenhaForte@2026`.
  4. Marcar o checkbox "Manter conectado".
  5. Clicar no botão "Entrar".
- **Dados de Teste / Entradas:** E-mail: `gestor@apae.org.br`, Senha: `SenhaForte@2026`.
- **Resultado Esperado:** O sistema autentica o usuário com sucesso, armazena o token seguro e redireciona para a Tela 23 (Dashboard Geral da Entidade).

#### CT-GES-002: Tentativa de login com senha incorreta ou e-mail inexistente
- **Tela / Módulo Relacionado:** Tela 21 — Login e Recuperação (`gestao-login-recuperacao-senha.png`) / Módulo Gestão
- **Requisito Vinculado:** RF-002, RNF-004
- **Tipo de Teste:** Negativo / Segurança
- **Pré-condições:** Usuário na tela de login.
- **Passo a Passo:**
  1. Inserir e-mail `gestor@apae.org.br` com senha errada `SenhaErrada123`.
  2. Clicar em "Entrar".
  3. Inserir e-mail não existente `inexistente@ong.org` com qualquer senha e clicar em "Entrar".
- **Dados de Teste / Entradas:** Credenciais inválidas.
- **Resultado Esperado:** Em ambos os casos, o sistema emite mensagem genérica de segurança: "E-mail ou senha inválidos. Por favor, tente novamente." O sistema NUNCA especifica se o erro foi no e-mail ou na senha (evitando enumeração de contas).

#### CT-GES-003: Login de entidade com status bloqueado, inativo ou reprovado
- **Tela / Módulo Relacionado:** Tela 21 — Login e Recuperação (`gestao-login-recuperacao-senha.png`) / Módulo Gestão
- **Requisito Vinculado:** RF-002, RF-009
- **Tipo de Teste:** Negativo / Regra de Negócio
- **Pré-condições:** Gestor possui credenciais válidas, porém a entidade foi inativada pelo administrador geral (`status: "INATIVA"` ou `"REPROVADA"`).
- **Passo a Passo:**
  1. Preencher e-mail e senha da entidade inativa.
  2. Clicar em "Entrar".
- **Dados de Teste / Entradas:** Credenciais de entidade com `status: "INATIVA"`.
- **Resultado Esperado:** A autenticação é barrada; a interface exibe banner informativo vermelho: "Acesso temporariamente bloqueado. O cadastro da sua instituição está inativo ou em análise. Entre em contato com a administração da plataforma."

#### CT-GES-004: Solicitação de recuperação de senha com e-mail cadastrado
- **Tela / Módulo Relacionado:** Tela 21 — Login e Recuperação (`gestao-login-recuperacao-senha.png`) / Módulo Gestão
- **Requisito Vinculado:** RF-002
- **Tipo de Teste:** Positivo
- **Pré-condições:** E-mail `gestor@apae.org.br` cadastrado no sistema.
- **Passo a Passo:**
  1. No painel direito ("Esqueceu sua senha?"), inserir o e-mail `gestor@apae.org.br`.
  2. Clicar no botão "Enviar link de recuperação".
- **Dados de Teste / Entradas:** E-mail: `gestor@apae.org.br`.
- **Resultado Esperado:** O sistema dispara o e-mail contendo o token de redefinição com validade de 30 minutos; a tela exibe confirmação em verde: "Solicitação enviada! Se este e-mail estiver cadastrado, você receberá o link para criar uma nova senha em instantes."

#### CT-GES-005: Recuperação de senha com e-mail não cadastrado (prevenção de enumeração)
- **Tela / Módulo Relacionado:** Tela 21 — Login e Recuperação (`gestao-login-recuperacao-senha.png`) / Módulo Gestão
- **Requisito Vinculado:** RF-002, RNF-004
- **Tipo de Teste:** Negativo / Segurança
- **Pré-condições:** E-mail `naocadastrado@teste.com` não existe no banco de dados.
- **Passo a Passo:**
  1. Inserir `naocadastrado@teste.com` no campo de recuperação.
  2. Clicar em "Enviar link de recuperação".
- **Dados de Teste / Entradas:** E-mail inexistente.
- **Resultado Esperado:** A tela exibe exatamente a mesma mensagem neutra de sucesso do CT-GES-004 ("Solicitação enviada! Se este e-mail estiver cadastrado..."), prevenindo que invasores descubram quais e-mails possuem conta no sistema.

#### CT-GES-006: Rate limiting contra requisições repetidas de recuperação de senha
- **Tela / Módulo Relacionado:** Tela 21 — Login e Recuperação (`gestao-login-recuperacao-senha.png`) / Módulo Gestão
- **Requisito Vinculado:** RF-002, RNF-004
- **Tipo de Teste:** Borda / Segurança
- **Pré-condições:** Usuário acabou de enviar uma solicitação de recuperação.
- **Passo a Passo:**
  1. Imediatamente após o envio, tentar clicar novamente em "Enviar link de recuperação".
- **Dados de Teste / Entradas:** Clique imediato repetido.
- **Resultado Esperado:** O botão fica desabilitado por 60 segundos com contador regressivo visível ("Aguarde 59s para reenviar"), prevenindo ataques de força bruta e sobrecarga no serviço de e-mail.

---

### Tela 22: Cadastro de Entidade e Onboarding (`gestao-cadastro-entidade-onboarding.png`)

#### CT-GES-007: Preenchimento completo de cadastro da ONG e upload dos 3 documentos obrigatórios
- **Tela / Módulo Relacionado:** Tela 22 — Cadastro de Entidade (`gestao-cadastro-entidade-onboarding.png`) / Módulo Gestão
- **Requisito Vinculado:** RF-006
- **Tipo de Teste:** Positivo
- **Pré-condições:** Nova entidade iniciando o processo de onboarding.
- **Passo a Passo:**
  1. Acessar `/gestao/onboarding`.
  2. Preencher Razão Social: "Associação de Pais e Amigos dos Excepcionais de São Paulo".
  3. Preencher CNPJ válido com máscara: `12.345.678/0001-90`.
  4. Preencher E-mail institucional, Telefone, Endereço completo e Responsável Legal.
  5. Fazer upload dos 3 documentos obrigatórios:
     - Comprovante de CNPJ (`cnpj.pdf`, 1.2 MB).
     - Estatuto Social registrado (`estatuto.pdf`, 3.5 MB).
     - Documento de identificação do responsável (`doc_responsavel.pdf`, 1.8 MB).
  6. Clicar no botão "Salvar informações e submeter para análise".
- **Dados de Teste / Entradas:** Dados cadastrais válidos e 3 PDFs <= 10MB.
- **Resultado Esperado:** O formulário é gravado com sucesso; o stepper atualiza o status para "Em análise" com mensagem explicativa: "Cadastro submetido com sucesso! Nossa equipe analisará os documentos em até 2 dias úteis."

#### CT-GES-008: Validação de máscara e cálculo do dígito verificador de CNPJ inválido
- **Tela / Módulo Relacionado:** Tela 22 — Cadastro de Entidade (`gestao-cadastro-entidade-onboarding.png`) / Módulo Gestão
- **Requisito Vinculado:** RF-006, RNF-004
- **Tipo de Teste:** Negativo / Borda
- **Pré-condições:** Entidade no campo CNPJ.
- **Passo a Passo:**
  1. Digitar o CNPJ com dígitos verificadores matematicamente incorretos: `11.111.111/1111-11`.
  2. Tentar avançar para o próximo campo.
- **Dados de Teste / Entradas:** CNPJ: `11.111.111/1111-11`.
- **Resultado Esperado:** O frontend executa o algoritmo de validação do módulo 11 do CNPJ e exibe erro inline imediatamente: "CNPJ inválido. Verifique os números digitados." O envio do formulário fica bloqueado.

#### CT-GES-009: Cadastro de entidade com CNPJ já existente na base de dados (`409 Conflict`)
- **Tela / Módulo Relacionado:** Tela 22 — Cadastro de Entidade (`gestao-cadastro-entidade-onboarding.png`) / Módulo Gestão
- **Requisito Vinculado:** RF-006, RNF-004
- **Tipo de Teste:** Negativo
- **Pré-condições:** CNPJ `12.345.678/0001-90` já aprovado e cadastrado para outra entidade.
- **Passo a Passo:**
  1. Preencher o formulário informando o CNPJ duplicado.
  2. Clicar em "Salvar informações".
- **Dados de Teste / Entradas:** CNPJ já existente no banco.
- **Resultado Esperado:** O backend responde com erro HTTP `409 Conflict`; a interface exibe erro destacado no campo CNPJ: "Este CNPJ já está cadastrado na plataforma. Caso pertença à sua entidade, solicite acesso ao administrador."

#### CT-GES-010: Upload de documento excedendo 10MB ou formato não suportado
- **Tela / Módulo Relacionado:** Tela 22 — Cadastro de Entidade (`gestao-cadastro-entidade-onboarding.png`) / Módulo Gestão
- **Requisito Vinculado:** RF-006, RNF-004
- **Tipo de Teste:** Negativo / Borda
- **Pré-condições:** Entidade na seção de upload de documentos comprobatórios.
- **Passo a Passo:**
  1. Tentar fazer upload de arquivo PDF com 12.5 MB no campo "Estatuto Social".
  2. Tentar fazer upload de planilha `.xlsx` ou executável `.exe`.
- **Dados de Teste / Entradas:** Arquivos: `estatuto_pesado.pdf` (12.5 MB) e `tabela.xlsx`.
- **Resultado Esperado:** O componente rejeita o arquivo antes da submissão com aviso: "Arquivo não suportado. Os documentos devem estar em formato PDF, JPG ou PNG com tamanho máximo de 10 MB cada."

#### CT-GES-011: Validação de campos obrigatórios não preenchidos no onboarding
- **Tela / Módulo Relacionado:** Tela 22 — Cadastro de Entidade (`gestao-cadastro-entidade-onboarding.png`) / Módulo Gestão
- **Requisito Vinculado:** RF-006, RNF-004
- **Tipo de Teste:** Negativo
- **Pré-condições:** Formulário de onboarding em branco.
- **Passo a Passo:**
  1. Sem preencher os campos, clicar em "Salvar informações".
- **Dados de Teste / Entradas:** Campos vazios.
- **Resultado Esperado:** A tela destaca todos os campos não preenchidos com contorno vermelho e exibe mensagens de obrigatoriedade; o botão não dispara requisição ao servidor.

---

### Tela 23: Dashboard Geral da Entidade (`gestao-dashboard-geral.png`)

#### CT-GES-012: Visualização de KPIs consolidados e gráfico temporal de arrecadação do mês
- **Tela / Módulo Relacionado:** Tela 23 — Dashboard Geral (`gestao-dashboard-geral.png`) / Módulo Gestão
- **Requisito Vinculado:** RF-028
- **Tipo de Teste:** Positivo
- **Pré-condições:** Gestor da APAE autenticado; entidade com campanhas e doações em andamento.
- **Passo a Passo:**
  1. Acessar `/gestao/dashboard`.
  2. Conferir os 4 KPIs no topo:
     - Arrecadação financeira acumulada: R$ 33.400,00.
     - Doadores únicos: 176 doadores.
     - Campanhas ativas: 1 campanha.
     - Taxa de conclusão das metas: 50%.
  3. Inspecionar o gráfico de área temporal ("Evolução diária de arrecadação do mês").
  4. Verificar a tabela sintética de campanhas no rodapé e o card de avisos "O que precisa de atenção".
- **Dados de Teste / Entradas:** Consulta do Dashboard geral.
- **Resultado Esperado:** Todos os KPIs exibem os valores exatos calculados a partir dos registros da entidade; o gráfico renderiza os pontos sem distorções ou travamentos.

#### CT-GES-013: Visualização do Dashboard para entidade recém-aprovada sem dados (Empty State)
- **Tela / Módulo Relacionado:** Tela 23 — Dashboard Geral (`gestao-dashboard-geral.png`) / Módulo Gestão
- **Requisito Vinculado:** RF-028, RNF-011
- **Tipo de Teste:** Borda
- **Pré-condições:** Entidade recém-aprovada que ainda não criou nenhuma campanha nem recebeu doações.
- **Passo a Passo:**
  1. Acessar o Dashboard da nova entidade.
- **Dados de Teste / Entradas:** Base de dados com zero transações para a entidade logada.
- **Resultado Esperado:** Os KPIs exibem `R$ 0,00` e `0`; a área do gráfico exibe estado vazio ilustrado com mensagem "Você ainda não possui movimentações financeiras este mês"; a tabela exibe botão de ação primária "Criar sua primeira campanha". A biblioteca gráfica não quebra.

#### CT-GES-014: Isolamento de dados entre entidades (Gestor A não visualiza KPIs da Entidade B)
- **Tela / Módulo Relacionado:** Tela 23 — Dashboard Geral (`gestao-dashboard-geral.png`) / Módulo Gestão
- **Requisito Vinculado:** RF-005, RF-007, RNF-003
- **Tipo de Teste:** Negativo / Segurança
- **Pré-condições:** Gestor da APAE (Entidade ID: 1) logado. Entidade Associação Amor Inclusivo (ID: 2) possui R$ 50.000,00 arrecadados.
- **Passo a Passo:**
  1. Consultar os KPIs da tela.
  2. Inspecionar a resposta JSON da requisição `GET /api/v1/gestao/dashboard` no DevTools.
- **Dados de Teste / Entradas:** Requisição de dashboard por gestor autenticado.
- **Resultado Esperado:** A resposta da API contém estritamente os totais da Entidade 1; nenhum dado financeiro ou de doadores da Entidade 2 é enviado no payload.

---

### Tela 24: Dashboard Analítico por Campanha (`gestao-dashboard-analitico-campanha.png`)

#### CT-GES-015: Filtragem por campanha e período com sincronização dos gráficos financeiro e físico
- **Tela / Módulo Relacionado:** Tela 24 — Dashboard Analítico (`gestao-dashboard-analitico-campanha.png`) / Módulo Gestão
- **Requisito Vinculado:** RF-028, RF-029
- **Tipo de Teste:** Positivo
- **Pré-condições:** Gestor autenticado.
- **Passo a Passo:**
  1. Acessar `/gestao/dashboard/analitico`.
  2. No dropdown de campanhas, selecionar "Mesa solidária".
  3. No filtro de período, selecionar "Últimos 30 dias".
  4. Observar os dois gráficos analíticos sincronizados:
     - Gráfico superior: "Evolução financeira" (linha de arrecadação vs. meta).
     - Gráfico inferior: "Evolução dos itens arrecadados" (barras por categoria).
  5. Conferir os cards de metas restantes: R$ 11.600,00 faltantes e 220 itens pendentes em 14 dias restantes.
- **Dados de Teste / Entradas:** Campanha: `"Mesa solidária"`, Período: `"Últimos 30 dias"`.
- **Resultado Esperado:** Ambos os gráficos atualizam com os dados específicos da campanha selecionada, permitindo cruzamento analítico de dados.

#### CT-GES-016: Visualização analítica de campanha modalidade "Somente dinheiro"
- **Tela / Módulo Relacionado:** Tela 24 — Dashboard Analítico (`gestao-dashboard-analitico-campanha.png`) / Módulo Gestão
- **Requisito Vinculado:** RF-028
- **Tipo de Teste:** Borda
- **Pré-condições:** Seleção de campanha que arrecada exclusivamente valores financeiros.
- **Passo a Passo:**
  1. No dropdown de campanhas, escolher "Reforma da Sala de Fisioterapia" (modalidade: somente dinheiro).
  2. Observar a seção de itens.
- **Dados de Teste / Entradas:** Campanha estritamente financeira.
- **Resultado Esperado:** O gráfico de "Evolução dos itens" é ocultado ou exibe badge informativa: "Esta campanha arrecada exclusivamente recursos financeiros; não há metas físicas associadas."

---

### Tela 25: Gestão de Doações Recebidas (`gestao-doacoes-recebidas.png`)

#### CT-GES-017: Consulta e filtragem da listagem unificada de doações recebidas
- **Tela / Módulo Relacionado:** Tela 25 — Doações Recebidas (`gestao-doacoes-recebidas.png`) / Módulo Gestão
- **Requisito Vinculado:** RF-027
- **Tipo de Teste:** Positivo
- **Pré-condições:** Existirem doações financeiras e físicas cadastradas.
- **Passo a Passo:**
  1. Acessar `/gestao/doacoes-recebidas`.
  2. Filtrar por status: "Pendente de conferência".
  3. Conferir os registros apresentados na tabela: Data, Doador, Tipo, Descrição/Quantidade e Ações.
- **Dados de Teste / Entradas:** Filtro: `status: "PENDENTE_CONFERENCIA"`.
- **Resultado Esperado:** A tabela exibe apenas as doações físicas de mantimentos aguardando recebimento na sede.

#### CT-GES-018: Conferência física integral de doação de itens e confirmação de recebimento
- **Tela / Módulo Relacionado:** Tela 25 — Doações Recebidas (`gestao-doacoes-recebidas.png`) / Módulo Gestão
- **Requisito Vinculado:** RF-022, RF-027
- **Tipo de Teste:** Positivo
- **Pré-condições:** Doadora Mariana Lima agendou entrega de 2 cestas básicas e 3 kits de higiene. Mantimentos entregues na sede.
- **Passo a Passo:**
  1. Na linha da doação de Mariana Lima, clicar no botão de ação "Confirmar v".
  2. O formulário inferior de conferência física se expande.
  3. O operador valida os checkboxes: "2 cestas básicas conferidas" e "3 kits de higiene conferidos".
  4. No campo observação, preencher: "Itens em perfeito estado, lacrados e dentro da validade".
  5. Clicar no botão "Confirmar recebimento".
- **Dados de Teste / Entradas:** Quantidades conferidas = Quantidades agendadas.
- **Resultado Esperado:** O status da doação muda para `CONFIRMADA`; um toast verde notifica o sucesso; os itens entram automaticamente no estoque da entidade (Tela 26).

#### CT-GES-019: Conferência física divergente/parcial (recebimento menor que o agendado)
- **Tela / Módulo Relacionado:** Tela 25 — Doações Recebidas (`gestao-doacoes-recebidas.png`) / Módulo Gestão
- **Requisito Vinculado:** RF-022, RF-027
- **Tipo de Teste:** Borda / Regra de Negócio
- **Pré-condições:** Doação agendada com 3 kits de higiene. Doador entregou fisicamente apenas 2 kits.
- **Passo a Passo:**
  1. Abrir o formulário de conferência física da doação.
  2. No campo de quantidade recebida de kits de higiene, alterar o valor de 3 para `2`.
  3. Preencher a justificativa obrigatória: "Doador entregou 2 unidades por falta de estoque pessoal".
  4. Clicar em "Confirmar recebimento parcial".
- **Dados de Teste / Entradas:** Qtd recebida: `2` (agendada: `3`).
- **Resultado Esperado:** O sistema atualiza a doação para `RECEBIDA_PARCIAL`; credita exatamente 2 unidades no saldo de estoque; a unidade faltante é baixada ou cancelada sem gerar saldo falso no almoxarifado.

#### CT-GES-020: Privacidade e conformidade LGPD de doadores anônimos no grid gerencial
- **Tela / Módulo Relacionado:** Tela 25 — Doações Recebidas (`gestao-doacoes-recebidas.png`) / Módulo Gestão
- **Requisito Vinculado:** RF-027, RNF-006
- **Tipo de Teste:** Negativo / Segurança
- **Pré-condições:** Existirem doações marcadas como anônimas na base de dados.
- **Passo a Passo:**
  1. Na listagem de doações recebidas, localizar uma doação com flag `anonima: true`.
  2. Conferir o nome e os dados de contato do doador na tela.
  3. Inspecionar a requisição na aba Network do DevTools.
- **Dados de Teste / Entradas:** Consulta de doações anônimas.
- **Resultado Esperado:** A tela renderiza estritamente "Doador anônimo" na coluna doador; a resposta JSON da API NÃO envia nome, telefone, CPF nem e-mail do doador, garantindo total anonimização no backend (RNF-006).

---

### Tela 26: Controle de Itens Doados e Estoque (`gestao-controle-itens-doados.png`)

#### CT-GES-021: Consulta de saldo em estoque e registro de saída/destinação com comprovante
- **Tela / Módulo Relacionado:** Tela 26 — Controle de Itens (`gestao-controle-itens-doados.png`) / Módulo Gestão
- **Requisito Vinculado:** RF-027, RF-028
- **Tipo de Teste:** Positivo
- **Pré-condições:** Entidade possui saldo em estoque de Cestas Básicas (82 cestas disponíveis).
- **Passo a Passo:**
  1. Acessar `/gestao/estoque`.
  2. Verificar a tabela "Saldo por item": Cesta básica (Saldo: 82, Entradas: 140, Saídas: 58).
  3. No bloco lateral "Registrar saída e destinação":
     - Selecionar o item: "Cesta básica".
     - Informar a quantidade de saída: "10".
     - Informar o destino: "Famílias do Núcleo Comunitário Vila Mariana".
     - Selecionar a data da entrega: data atual.
     - Fazer upload do recibo de entrega assinado (`recibo_entrega.pdf`).
  4. Clicar no botão "Confirmar saída".
- **Dados de Teste / Entradas:** Item: `"Cesta básica"`, Qtd: `10`, Destino: `"Núcleo Vila Mariana"`.
- **Resultado Esperado:** O saldo de cestas básicas na tabela atualiza imediatamente para 72 unidades (82 - 10); uma nova linha é gravada no "Histórico de Movimentações" contendo data, quantidade, destino e link para o comprovante anexado.

#### CT-GES-022: Tentativa de saída de item com quantidade superior ao saldo em estoque
- **Tela / Módulo Relacionado:** Tela 26 — Controle de Itens (`gestao-controle-itens-doados.png`) / Módulo Gestão
- **Requisito Vinculado:** RNF-004, RNF-008
- **Tipo de Teste:** Negativo / Regra de Negócio
- **Pré-condições:** Saldo atual em estoque de Cestas Básicas = 82 unidades.
- **Passo a Passo:**
  1. No formulário de saída de estoque, selecionar "Cesta básica".
  2. No campo quantidade, digitar `90` (quantidade maior que o saldo em estoque).
  3. Preencher destino e clicar em "Confirmar saída".
- **Dados de Teste / Entradas:** Quantidade informada: `90` (saldo disponível: `82`).
- **Resultado Esperado:** A transação é sumariamente bloqueada; o campo quantidade recebe destaque de erro: "Quantidade indisponível. O saldo atual em estoque é de 82 unidades." O saldo do estoque não se torna negativo.

#### CT-GES-023: Registro de saída sem preenchimento dos campos obrigatórios
- **Tela / Módulo Relacionado:** Tela 26 — Controle de Itens (`gestao-controle-itens-doados.png`) / Módulo Gestão
- **Requisito Vinculado:** RNF-004
- **Tipo de Teste:** Negativo
- **Pré-condições:** Formulário de registro de saída em branco.
- **Passo a Passo:**
  1. Clicar diretamente no botão "Confirmar saída" sem preencher os campos.
- **Dados de Teste / Entradas:** Campos vazios.
- **Resultado Esperado:** A interface acusa obrigatoriedade nos campos: "Selecione o item", "Informe a quantidade", "Informe o destino/beneficiário" e "Selecione a data da saída".

#### CT-GES-024: Auditoria do operador logado na saída de estoque
- **Tela / Módulo Relacionado:** Tela 26 — Controle de Itens (`gestao-controle-itens-doados.png`) / Módulo Gestão
- **Requisito Vinculado:** RNF-009
- **Tipo de Teste:** Positivo / Auditoria
- **Pré-condições:** Operador "João Silva" logado.
- **Passo a Passo:**
  1. Registrar uma saída válida de estoque.
  2. Consultar o registro na tabela de histórico de movimentações.
- **Dados de Teste / Entradas:** Saída registrada pelo usuário logado.
- **Resultado Esperado:** O registro de histórico exibe explicitamente a coluna "Responsável pela saída: João Silva" com carimbo de data e hora exatos, atendendo à trilha de auditoria (RNF-009).

---

### Tela 27: Gestão de Doadores e Históricos (`gestao-doadores-e-historicos.png`)

#### CT-GES-025: Busca de apoiadores, visualização do perfil detalhado e registro de contato no CRM
- **Tela / Módulo Relacionado:** Tela 27 — Gestão de Doadores (`gestao-doadores-e-historicos.png`) / Módulo Gestão
- **Requisito Vinculado:** RF-028
- **Tipo de Teste:** Positivo
- **Pré-condições:** Doadora Ana Oliveira cadastrada com histórico de doações na APAE.
- **Passo a Passo:**
  1. Acessar `/gestao/doadores`.
  2. No campo de busca, digitar "Ana Oliveira".
  3. Clicar na linha de Ana Oliveira na tabela.
  4. Observar a abertura do painel lateral direito de perfil:
     - Nome: Ana Oliveira.
     - Total acumulado: R$ 450,00 em 3 doações.
     - Histórico individual com datas e campanhas apoiadas.
  5. Clicar no botão "Registrar contato", redigir anotação ("Ligação de agradecimento realizada em 21/10") e salvar.
- **Dados de Teste / Entradas:** Busca: `"Ana Oliveira"`, Contato: `"Ligação de agradecimento realizada"`.
- **Resultado Esperado:** O painel lateral renderiza todas as doações vinculadas à doadora; o novo contato é anexado à linha do tempo do CRM com sucesso.

#### CT-GES-026: Verificação de anonimização e mascaramento de dados sensíveis na resposta da API
- **Tela / Módulo Relacionado:** Tela 27 — Gestão de Doadores (`gestao-doadores-e-historicos.png`) / Módulo Gestão
- **Requisito Vinculado:** RNF-004, RNF-005, RNF-006
- **Tipo de Teste:** Negativo / Segurança / LGPD
- **Pré-condições:** Tela de doadores aberta com dados mascarados no protótipo (`a***@gmail.com`, `(11) 9****-**42`).
- **Passo a Passo:**
  1. Abrir a aba Network do DevTools.
  2. Localizar a requisição `GET /api/v1/gestao/doadores/1`.
  3. Inspecionar o corpo JSON retornado pela API.
- **Dados de Teste / Entradas:** Inspeção de payload de dados pessoais.
- **Resultado Esperado:** A máscara deve ser gerada **no backend**; a API não deve trafegar o e-mail ou telefone completo em texto claro se o usuário não possuir nível de privilégio administrativo para visualização de dados brutos (evitando que a máscara seja apenas estética via CSS/JavaScript no navegador).

#### CT-GES-027: Pesquisa de doador inexistente na base do CRM
- **Tela / Módulo Relacionado:** Tela 27 — Gestão de Doadores (`gestao-doadores-e-historicos.png`) / Módulo Gestão
- **Requisito Vinculado:** RF-028, RNF-011
- **Tipo de Teste:** Borda
- **Pré-condições:** Gestor na tela de CRM.
- **Passo a Passo:**
  1. No campo de busca, digitar `DoadorInexistente999`.
- **Dados de Teste / Entradas:** Termo de busca sem correspondência.
- **Resultado Esperado:** A tabela exibe estado vazio: "Nenhum doador encontrado com o termo pesquisado."

---

## 5. Casos de Teste Transversais: Resiliência, Rede e Acessibilidade

---

#### CT-TRA-001: Sessão expirada com resposta HTTP 401 Unauthorized
- **Tela / Módulo Relacionado:** Todas as telas autenticadas (Gestão e Doador)
- **Requisito Vinculado:** RF-002, RNF-003, RNF-016
- **Tipo de Teste:** Negativo / Segurança
- **Pré-condições:** Usuário com token JWT expirado ou inválido.
- **Passo a Passo:**
  1. Estando em uma tela restrita (ex: `/gestao/dashboard`), alterar ou invalidar o token no `localStorage` via console.
  2. Tentar realizar uma ação que dispare requisição (ex: navegar entre abas ou salvar dados).
- **Dados de Teste / Entradas:** Token expirado -> API responde HTTP `401 Unauthorized`.
- **Resultado Esperado:** O interceptor HTTP do frontend captura o erro 401; limpa a sessão local; redireciona o usuário para `/login` e exibe um toast informativo: "Sua sessão expirou por inatividade. Por favor, faça login novamente."

#### CT-TRA-002: Tentativa de acesso não autorizado com resposta HTTP 403 Forbidden
- **Tela / Módulo Relacionado:** Rotas administrativas e restritas
- **Requisito Vinculado:** RF-005, RNF-003, RNF-016
- **Tipo de Teste:** Negativo / Segurança
- **Pré-condições:** Doador comum autenticado tentando acessar URL de gestão de entidade (`/gestao/campanhas/criar`).
- **Passo a Passo:**
  1. Com a sessão de doador comum, digitar no navegador: `/gestao/campanhas/criar`.
- **Dados de Teste / Entradas:** Requisição com role `ROLE_DOADOR` em rota que exige `ROLE_GESTOR`.
- **Resultado Esperado:** O backend responde com HTTP `403 Forbidden`; a aplicação renderiza uma página amigável de erro de permissão ("Você não tem permissão para acessar esta área") sem deslogar o usuário e sem quebras de layout.

#### CT-TRA-003: Tratamento de conflito de concorrência com HTTP 409 Conflict
- **Tela / Módulo Relacionado:** Telas de Doação e Edição de Campanhas
- **Requisito Vinculado:** RNF-011, RNF-016
- **Tipo de Teste:** Negativo / Resiliência
- **Pré-condições:** Campanha acabou de ser encerrada enquanto doador estava no checkout.
- **Passo a Passo:**
  1. Doador tenta submeter doação para campanha cujo status foi alterado para `ENCERRADA` no banco no mesmo instante.
- **Dados de Teste / Entradas:** Requisição de doação para campanha recém-fechada -> API responde `409 Conflict`.
- **Resultado Esperado:** A UI exibe um banner de alerta informativo contextual: "Não foi possível concluir a operação: esta campanha acabou de ser encerrada e não aceita mais contribuições." O valor não é cobrado.

#### CT-TRA-004: Erro de validação de dados no servidor com HTTP 422 Unprocessable Entity
- **Tela / Módulo Relacionado:** Todos os formulários da aplicação
- **Requisito Vinculado:** RNF-004, RNF-011, RNF-016
- **Tipo de Teste:** Negativo / Resiliência
- **Pré-condições:** Bypassar validação client-side via modificação no console e enviar dados em formato inválido para o backend.
- **Passo a Passo:**
  1. Submeter payload com campo `metaFinanceira: -50.00`.
- **Dados de Teste / Entradas:** API responde `422 Unprocessable Entity` com array de erros `{ field: "metaFinanceira", message: "Valor deve ser positivo" }`.
- **Resultado Esperado:** O frontend intercepta a resposta padronizada, localiza o campo `metaFinanceira`, aplica borda vermelha e renderiza o texto retornado pela API logo abaixo do input (`font.caption`).

#### CT-TRA-005: Tratamento de erro interno HTTP 500 sem vazamento de stack trace
- **Tela / Módulo Relacionado:** Todas as telas do sistema
- **Requisito Vinculado:** RNF-011, RNF-016
- **Tipo de Teste:** Negativo / Segurança
- **Pré-condições:** Forçar erro 500 no backend (ex: simular desconexão momentânea com o banco de dados).
- **Passo a Passo:**
  1. Disparar uma requisição que gere exceção não tratada no backend.
  2. Observar a tela do usuário.
- **Dados de Teste / Entradas:** Resposta HTTP `500 Internal Server Error`.
- **Resultado Esperado:** A tela NÃO exibe stack trace em Java/Spring, dump de memória nem tela padrão do servidor (Whitelabel Error Page); renderiza modal ou banner elegante: "Desculpe, ocorreu uma instabilidade em nossos servidores. Nossa equipe técnica já foi notificada. Por favor, tente novamente em alguns instantes."

#### CT-TRA-006: Comportamento offline com sincronização local
- **Tela / Módulo Relacionado:** Telas de formulários extensos (Wizard de Criação de Campanha e Onboarding)
- **Requisito Vinculado:** RNF-011
- **Tipo de Teste:** Negativo / Borda
- **Pré-condições:** Usuário preenchendo o wizard de campanha; conexão de internet cai.
- **Passo a Passo:**
  1. Desconectar a internet no meio do preenchimento da Etapa 2.
  2. Tentar avançar ou observar a tela.
  3. Reconectar a internet.
- **Dados de Teste / Entradas:** Status de rede: `navigator.onLine = false`.
- **Resultado Esperado:** A barra superior exibe aviso amarelo fixo: "Você está offline. Suas alterações foram salvas localmente neste navegador." Ao restabelecer a conexão, o sistema sincroniza os dados automaticamente com o backend e oculta o aviso.

#### CT-TRA-007: Acessibilidade WCAG 2.1 AA — Navegação completa por teclado e leitor de tela
- **Tela / Módulo Relacionado:** Telas 01 a 27 (foco no fluxo completo de doação Telas 11 a 18)
- **Requisito Vinculado:** RNF-012, RNF-013
- **Tipo de Teste:** Positivo / Acessibilidade
- **Pré-condições:** Navegador sem uso do mouse; leitor de tela ativo (NVDA ou Narrador do Windows).
- **Passo a Passo:**
  1. Acessar a Home pública.
  2. Navegar estritamente utilizando as teclas `Tab`, `Shift + Tab`, `Enter`, `Espaço` e setas direcionais.
  3. Concluir uma doação completa até a tela de confirmação.
- **Dados de Teste / Entradas:** Navegação por teclado.
- **Resultado Esperado:**
  - O indicador de foco (`focus-visible`) é perfeitamente visível com anel de alto contraste em todos os links e botões interativos.
  - A ordem de tabulação é lógica e acompanha o fluxo de leitura natural.
  - Todos os botões, links e imagens possuem atributos `aria-label`, `role` e `alt` preenchidos e lidos corretamente pelo leitor de tela.
  - Não ocorrem armadilhas de teclado (*keyboard traps*).

#### CT-TRA-008: Compatibilidade cross-browser (Chrome, Edge, Firefox e Safari)
- **Tela / Módulo Relacionado:** Telas principais do sistema (Home, Detalhes, Criação, Doação e Dashboards)
- **Requisito Vinculado:** RNF-015
- **Tipo de Teste:** Positivo / Compatibilidade
- **Pré-condições:** Ambiente de testes com Google Chrome (versão recente), Microsoft Edge, Mozilla Firefox e Apple Safari (Webkit).
- **Passo a Passo:**
  1. Executar o fluxo completo de criação de campanha e o fluxo completo de doação em cada um dos 4 navegadores.
  2. Comparar a renderização dos componentes, date pickers, modais e transições CSS.
- **Dados de Teste / Entradas:** Navegadores desktop nas últimas versões estáveis.
- **Resultado Esperado:** A interface renderiza com fidelidade visual homogênea em todos os motores (Blink, Gecko e WebKit); todas as validações de máscara, inputs de data e downloads de PDF funcionam identicamente.

---
*Plano de Testes Manuais aprovado pela Equipe 5 — Qualidade de Software (QA) do Projeto DoaSync.*
