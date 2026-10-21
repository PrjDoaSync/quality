# Relatório de QA: Análise Shift-Left de Requisitos e Protótipos (Sprint 2)

> **Documento:** `relatorio-qa-shiftleft-sprint2.md`  
> **Projeto:** DoaSync — Sistema Digital de Arrecadação de Doações  
> **Equipe Responsável:** 🟥 Grupo 5 — Qualidade de Software (QA)  
> **Líder QA:** Paola C. | **Apoio Técnico e Validação:** Murilo A. S. G. C., João V. M., Amós G. S. S.  
> **Destinatário:** Grupo 1 (Produto/UX), Grupos 2, 3, 4 (Desenvolvimento) e Gestão  
> **Metodologia:** Shift-Left Testing / Testes Estáticos de Requisitos e Design / BDD  
> **Data:** Outubro de 2026 | **Versão:** 1.0 — Oficial  

---

## 1. Visão Geral e Escopo da Análise

### 1.1 Objetivo e Contexto da Sprint 2
O presente relatório consolida a análise estática preventiva (**Shift-Left Testing**) conduzida pelo Grupo 5 (Qualidade de Software) sobre a documentação de engenharia (`requisitos.md`, `arquitetura.md`, `rotas-api.md`, `routes-plan.md`, modelagem relacional DER) em confronto direto com as **27 telas prototipadas em alta fidelidade no Figma** (armazenadas em `docs/qa/assets/`).

O princípio do **Shift-Left Testing** é antecipar anomalias, ambiguidades, riscos de segurança, conflitos de integração e regras de negócio omissas antes que qualquer linha de código seja implementada no frontend ou backend da Sprint 2. Detectar e corrigir divergências na fase de concepção custa até **100 vezes menos** do que corrigi-las após a implementação ou em produção.

```
       PRODUTO / UX (Grupo 1)
               │
               ▼
      [SHIFT-LEFT QA - Grupo 5]  ◄─── (Análise de telas, regras, dados e APIs)
               │
       ┌───────┴───────┐
       ▼               ▼
REFINAMENTO DE    DESENVOLVIMENTO
CRITÉRIOS/FIGMA   (Grupos 2, 3, 4)
```

### 1.2 Módulos Avaliados
A análise cobriu exaustivamente os três módulos de negócio principais desenhados para a Sprint 2:
1. **Campanhas (Grupo 2):** Vitrine pública, busca e filtros avançados, detalhes da campanha com metas híbridas, acompanhamento gerencial, wizard de criação em 3 etapas (dados básicos, metas/itens, fotos/publicação), edição de campanhas ativas e encerramento com prestação de contas.
2. **Doações (Grupo 3):** Bifurcador de modalidade (financeira vs. itens físicos), seleção de valores/cotas e recorrência, agendamento de entrega na sede ou coleta domiciliar, fluxo de identificação (login, cadastro rápido ou anônimo), gateways de pagamento (PIX, cartão e boleto), confirmação, emissão de comprovantes e recibos em PDF, e painel histórico do doador com tracking de destinação.
3. **Gestão e Dashboard (Grupo 4):** Onboarding institucional com upload de documentos comprobatórios e status de verificação, login e recuperação de senha, dashboard geral com KPIs consolidados, dashboard analítico detalhado por campanha, gestão de recebimentos com conferência física de itens, controle de estoque (entradas, saídas e destinação a famílias) e CRM de doadores com histórico individual.

---

## 2. Mapeamento de Fluxos por Módulo

Abaixo, detalha-se o comportamento esperado para cada uma das telas prototipadas, mapeando o **Caminho Feliz (*Happy Path*)** e os **Caminhos de Exceção e Borda (*Edge Cases*)**.

```
Mapeamento de Fluxos:
├── Módulo Campanhas (Telas 01 a 10)
├── Módulo Doações (Telas 11 a 20)
└── Módulo Gestão e Dashboard (Telas 21 a 27)
```

---

### 2.1 Módulo: Campanhas

#### Tela 01: Home Pública (`campanhas-home-publica.png`)
* **Objetivo:** Ponto de entrada de visitantes e doadores para engajamento e descoberta de causas.
* **Caminho Feliz (Happy Path):**
  1. O visitante acessa a URL raiz e visualiza o banner principal ("Encontre uma causa. Faça a diferença").
  2. O sistema exibe com destaque a campanha urgente ("Mesa solidária"), exibindo imagem de capa, badge "Urgente - 14 dias restantes", valores arrecadados, percentual atingido (61%) e total de itens recebidos (260 de 480).
  3. A seção "Mais causas para apoiar" exibe cards de campanhas ativas com barras de progresso proporcionais.
  4. O usuário clica em "Conhecer e doar" ou "Ver campanha" e é direcionado à página de detalhes da campanha escolhida.
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Borda:* Campanha destaque encerra ou atinge 100% enquanto o usuário está na tela. A UI deve atualizar a badge para "Meta atingida" ou substituir automaticamente o destaque.
  * *Exceção:* Falha de carregamento da imagem de capa via CDN. O frontend deve exibir placeholder institucional elegante sem quebrar o layout.
  * *Borda:* Redução de viewport (mobile). O card de destaque horizontal deve converter-se em empilhamento vertical (100% width) sem rolagem horizontal (RNF-014).

#### Tela 02: Listagem com Filtros Avançados (`campanhas-listagem-com-filtros.png`)
* **Objetivo:** Pesquisa refinada de campanhas ativas e encerradas.
* **Caminho Feliz (Happy Path):**
  1. O usuário digita um termo no input de busca ("alimento") e/ou seleciona filtros em dropdowns: Entidade ("Rede Esperança"), Categoria ("Alimentação"), Status ("Ativa") e Urgência ("Todas").
  2. Clica em "Aplicar filtros".
  3. A listagem atualiza via requisição assíncrona, exibindo contador ("6 campanhas encontradas · 5 ativas e 1 encerrada") e os respectivos cards paginados.
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Exceção (Empty State):* Filtros combinados não retornam resultados. A tela deve exibir ilustração amigável de estado vazio com botão "Limpar filtros", em vez de uma tela em branco.
  * *Segurança:* Injeção de caracteres especiais (`<script>`, SQL injection) na barra de pesquisa. A entrada deve ser sanitizada no front e rejeitada com `422` ou tratada sem erros no backend (RNF-004).
  * *Exceção:* Usuário desmarca todos os filtros ou clica em "Limpar". A listagem deve resetar para a ordenação padrão sem recarregar a página inteira.

#### Tela 03: Detalhes da Campanha (`campanhas-detalhe-da-campanha.png`)
* **Objetivo:** Informar detalhadamente sobre a causa e permitir o início da contribuição.
* **Caminho Feliz (Happy Path):**
  1. O usuário visualiza breadcrumb de navegação, fotos da campanha, badges de status, descrição completa da ação e a seção "Itens necessários" com detalhamento de cada item (ex: Cesta básica, Kit de higiene, Leite em pó) e barras de progresso individuais.
  2. Na lateral direita fixa, o usuário confere a meta financeira (R$ 18.400,00 de R$ 30.000,00) e os dias restantes.
  3. O usuário pode clicar em "Compartilhar" (copiando o link) ou escolher entre dois botões de ação: "Doar" (financeiro) ou "Doar itens necessários".
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Exceção:* Campanha encerrada ou desativada pelo administrador acessada via link direto. O sistema deve ocultar os botões "Doar" e "Doar itens necessários", exibindo banner informativo "Esta campanha foi encerrada em dd/mm/aaaa e não recebe mais doações".
  * *Borda:* Clique em "Compartilhar" em dispositivo sem suporte à API `navigator.share`. A aplicação deve automaticamente copiar o link para a área de transferência e exibir um toast "Link copiado com sucesso!".
  * *Borda:* Meta financeira concluída (100%), mas itens físicos ainda pendentes (ou vice-versa). O botão de doação financeira deve ser desabilitado ou exibir aviso, mantendo apenas a doação de itens ativa.

#### Tela 04: Painel "Minhas Campanhas" da Entidade (`campanhas-minhas-campanhas.png`)
* **Objetivo:** Gestão operacional das campanhas pertencentes à entidade autenticada.
* **Caminho Feliz (Happy Path):**
  1. O gestor autenticado visualiza cards resumo no topo: Rascunhos (1), Ativas (1) e Encerradas (1).
  2. Filtra a tabela pelas abas de status.
  3. Em campanhas "Rascunho", aciona "Continuar criação" ou "Excluir rascunho". Em "Ativas", aciona "Acompanhar" ou "Editar". Em "Encerradas", aciona "Prestar contas".
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Exceção:* O gestor aciona "Excluir rascunho". A exclusão NÃO pode ocorrer de forma imediata; deve abrir modal de confirmação destrutivo ("Deseja realmente excluir este rascunho? Esta ação não pode ser desfeita").
  * *Segurança (Quebra de IDOR):* Tentativa de editar campanha pertencente a outra entidade manipulando o ID na URL. O backend deve barrar com `403 Forbidden` e o front exibir página de acesso negado.

#### Tela 05: Criação de Campanha — Etapa 1: Dados Básicos (`campanhas-criar-campanha-dados-basicos.png`)
* **Objetivo:** Coletar dados preliminares e período da nova campanha.
* **Caminho Feliz (Happy Path):**
  1. O gestor visualiza o stepper (Etapa 1 ativa) e o campo "Entidade responsável" já travado com cadeado.
  2. Preenche "Título da campanha *", "Descrição *", escolhe a "Categoria *" no dropdown e preenche as datas de início e término via date picker.
  3. O preview lateral atualiza em tempo real conforme a digitação.
  4. O sistema salva o rascunho automaticamente (indicador "Rascunho salvo às 10:42").
  5. O usuário clica em "Continuar".
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Exceção de Validação:* Usuário seleciona data de término anterior à data de início, ou data de término no passado. A interface deve emitir feedback de erro inline no campo: "A data de encerramento deve ser igual ou posterior à data de início".
  * *Borda:* Queda de conexão enquanto o auto-save tenta persistir o rascunho. O indicador deve mudar para "Erro ao salvar rascunho - tentativa em 5s" e desabilitar o botão "Continuar" para evitar perda de dados.

#### Tela 06: Criação de Campanha — Etapa 2: Metas e Itens (`campanhas-criar-campanha-metas-e-itens.png`)
* **Objetivo:** Definir o formato da arrecadação (financeira, física ou mista).
* **Caminho Feliz (Happy Path):**
  1. O gestor escolhe entre "Somente dinheiro", "Somente itens" ou "Dinheiro e itens".
  2. Informa a "Meta financeira (R$)" com máscara monetária.
  3. Adiciona itens físicos clicando em "+ Adicionar item", informando nome/especificação, quantidade inteira e unidade de medida.
  4. Clica em "Continuar".
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Exceção:* Seleção da modalidade "Somente itens", mas o gestor não adiciona nenhum item ou remove todos os itens da lista. O botão "Continuar" deve ficar desabilitado ou exibir erro: "Adicione ao menos um item necessário para prosseguir".
  * *Validação de Campo:* Inserção de valor negativo, zero ou texto no campo de quantidade de itens. O input deve aceitar estritamente inteiros positivos (`min="1" step="1"`).

#### Tela 07: Criação de Campanha — Etapa 3: Imagens, Revisão e Publicação (`campanhas-criar-campanha-imagens-revisao-publicacao.png`)
* **Objetivo:** Adicionar material visual, revisar dados consolidados e assinar termo de compromisso.
* **Caminho Feliz (Happy Path):**
  1. O gestor faz upload da imagem de capa (até 5MB, JPG/PNG) e adiciona até 5 fotos complementares na galeria.
  2. Confere o bloco "Revise os dados da campanha".
  3. Marca o checkbox obrigatório de veracidade e prestação de contas.
  4. Clica em "Publicar campanha". A campanha é publicada e o usuário é redirecionado ao painel.
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Exceção:* Usuário tenta clicar em "Publicar campanha" sem marcar o checkbox de aceite. A ação deve ser bloqueada e o checkbox destacado em vermelho com mensagem de obrigatoriedade.
  * *Exceção de Arquivo:* Tentativa de upload de imagem acima de 5MB ou extensão não autorizada (.pdf, .exe, .svg contendo scripts). A UI deve barrar antes do envio: "Arquivo inválido. Envie apenas imagens JPG ou PNG de até 5 MB".
  * *Concorrência:* Clique duplo (*double click*) em "Publicar campanha". O botão deve desabilitar imediatamente e exibir spinner de loading para evitar duplicidade de registros no backend.

#### Tela 08: Edição de Campanha Publicada (`campanhas-editar-campanhas.png`)
* **Objetivo:** Atualizar metadados de uma campanha em andamento sem corromper a integridade das doações já recebidas.
* **Caminho Feliz (Happy Path):**
  1. O gestor visualiza campos imutáveis travados (Entidade responsável e Data de início, com ícone de cadeado).
  2. Edita descrição, categoria, imagem de capa ou prorroga a data de término.
  3. Clica em "Salvar alterações".
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Regra de Negócio Rígida:* O gestor tenta reduzir a meta financeira para um valor menor do que o já arrecadado (ex.: arrecadado R$ 18.400,00 e o usuário tenta alterar a meta para R$ 15.000,00). O campo deve exibir erro impeditivo: "A meta não pode ser inferior ao valor já arrecadado".
  * *Regra de Itens:* O gestor tenta excluir um item físico da lista que já possui doações confirmadas. O botão de exclusão do item deve estar desabilitado com tooltip: "Não é possível remover itens com doações recebidas".

#### Tela 09: Acompanhamento da Campanha (`campanhas-acompanhamento-de-campanha .png`)
* **Objetivo:** Monitoramento analítico da campanha em tempo real e comunicação com a comunidade.
* **Caminho Feliz (Happy Path):**
  1. O gestor acompanha métricas consolidadas (Financeiro, Itens, Famílias beneficiadas).
  2. Visualiza a tabela de doações recebidas com paginação e filtros.
  3. Clica em "Exportar" para baixar relatório CSV.
  4. Na seção lateral, redige uma novidade no textarea "Mensagem aos doadores" e clica em "Publicar atualização", inserindo novo marco na timeline pública.
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Exceção:* Envio de atualização na timeline com campo de mensagem em branco. A ação deve ser bloqueada.
  * *Borda:* Clique em "Exportar" em campanha com volume massivo de doações. A UI deve exibir feedback "Gerando arquivo CSV..." e não travar a interface da aplicação.

#### Tela 10: Encerramento e Prestação de Contas (`campanhas-encerramento-prestacao-contas.png`)
* **Objetivo:** Formalizar o encerramento da campanha comprovando a destinação integral dos recursos arrecadados.
* **Caminho Feliz (Happy Path):**
  1. A tela apresenta a conciliação: Total arrecadado (R$ 15.000,00), Recursos aplicados (R$ 15.000,00) e Saldo restante (R$ 0,00).
  2. O gestor visualiza a tabela de despesas com notas fiscais vinculadas, redige o relato de impacto, confere os comprovantes em anexo e valida o checklist de conferência.
  3. Marca o checkbox de declaração e clica em "Publicar prestação de contas".
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Regra de Negócio Omissa:* O que acontece se houver sobra de caixa (Saldo restante > R$ 0,00)? O sistema permite publicar ou exige destinação residual documentada?
  * *Exceção:* O gestor tenta publicar sem anexar nenhum comprovante fiscal ou com a soma das despesas divergente do valor total arrecadado. O checklist deve bloquear a publicação enquanto houver pendência de conciliação.

---

### 2.2 Módulo: Doações

```
Fluxo de Doação:
[Escolha do Tipo] ──► [Financeira] ──────────────┐
                  └──► [Itens Físicos] ──► [Agendamento]
                                                │
                                                ▼
                                    [Identificação do Doador]
                                                │
                                                ▼
                                     [Pagamento (se financeiro)]
                                                │
                                                ▼
                                    [Confirmação e Comprovantes]
```

#### Tela 11: Escolha do Tipo de Doação (`doacoes-tipo-doacao.png`)
* **Objetivo:** Direcionar o doador entre a esteira financeira e a esteira de entrega física de suprimentos.
* **Caminho Feliz (Happy Path):**
  1. O doador visualiza os dois cards de modalidade: "Doação financeira" e "Doação de itens".
  2. Seleciona o card desejado (destaque com borda azul e radio preenchido).
  3. O botão primário altera dinamicamente seu rótulo ("Continuar com doação financeira" ou "Continuar com doação de itens").
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Borda:* Campanha cadastrada como "Somente dinheiro" ou "Somente itens". O bifurcador NÃO deve ser exibido; o fluxo deve iniciar diretamente na tela correspondente à única modalidade permitida.

#### Tela 12: Doação Financeira — Valor e Frequência (`doacoes-doacao-financeira.png`)
* **Objetivo:** Definir o montante e a periodicidade da contribuição monetária.
* **Caminho Feliz (Happy Path):**
  1. O doador clica em um botão de valor pré-definido (R$ 25, R$ 50, R$ 100, R$ 200) ou digita um valor customizado no campo "Ou informe outro valor (R$)".
  2. Seleciona a frequência: "Única" ou "Recorrente - semanal".
  3. O resumo lateral atualiza automaticamente.
  4. Clica em "Continuar para identificação".
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Validação de Campo:* Doador digita valor inferior a R$ 1,00 ou valor nulo/negativo. A interface deve emitir alerta: "O valor mínimo para doação financeira é de R$ 1,00".
  * *Borda:* Seleção de "Recorrente - semanal". O sistema deve deixar explícito na UI que o DoaSync não realiza débito automático em conta e que lembretes serão enviados por e-mail/notificação.

#### Tela 13: Doação de Itens Necessários (`doacoes-doacao-itens.png`)
* **Objetivo:** Selecionar quais itens da lista oficial da campanha e em quais quantidades o doador irá fornecer.
* **Caminho Feliz (Happy Path):**
  1. A tela lista os itens ativos da campanha com barras de progresso (ex: Cesta básica, Kit de higiene).
  2. O doador marca o checkbox do item e ajusta o contador numérico através dos seletores `[-]` e `[+]`.
  3. A sidebar agrupa as quantidades selecionadas ("5 unidades · 2 tipos de item").
  4. Clica em "Continuar para identificação".
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Exceção:* Doador clica em avançar com quantidade total de itens zerada. O botão deve permanecer desabilitado.
  * *Borda / Concorrência:* O doador seleciona doar 80 cestas básicas (quantidade faltante exata). Durante o processo, outro doador confirma a entrega de 20. Ao submeter, a UI deve alertar: "A campanha agora necessita de apenas 60 unidades deste item. Deseja ajustar?".

#### Tela 14: Agendamento de Entrega ou Retirada (`doacoes-agendamento-entrega-retirada.png`)
* **Objetivo:** Coordenar a logística de recepção física dos mantimentos doados.
* **Caminho Feliz (Happy Path):**
  1. Doador escolhe entre "Vou entregar na sede" e "Solicitar retirada".
  2. Ao selecionar entrega na sede, visualiza o endereço institucional e horário comercial.
  3. Seleciona data da entrega no date picker e a faixa de horário no dropdown ("10h às 11h").
  4. Adiciona observações opcionais e clica em "Confirmar agendamento".
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Exceção:* Seleção de data em fins de semana, feriados ou fora do período ativo da campanha. O calendário do date picker deve ter esses dias desabilitados para clique.
  * *Regra Omissa:* Ao selecionar "Solicitar retirada", o protótipo não exibe campos para informar o endereço de coleta, ponto de referência nem telefone do doador. Se o doador for anônimo, a retirada torna-se operacionalmente inviável!

#### Tela 15: Identificação do Doador (`doacoes-identificacao-doador.png`)
* **Objetivo:** Definir o vínculo cadastral do doador mantendo baixa fricção.
* **Caminho Feliz (Happy Path):**
  1. O doador escolhe uma das três vias:
     * **Já tenho uma conta:** Preenche e-mail e senha -> "Entrar e continuar".
     * **Cadastro rápido:** Preenche apenas Nome completo e E-mail -> "Cadastrar e continuar".
     * **Doação anônima:** Lê o aviso de privacidade e clica em "Continuar anonimamente".
  2. O sistema direciona para a etapa de pagamento (se financeiro) ou agendamento (se itens).
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Conflito de Arquitetura Crítico:* A rota de backend `POST /api/v1/doacoes` exige token Bearer de autenticação. Como a requisição é autenticada se o usuário optar por "Doação anônima" ou "Cadastro rápido"?
  * *Borda:* Usuário seleciona "Cadastro rápido", mas informa um e-mail já registrado com conta completa no sistema. O sistema deve alertar: "Este e-mail já possui cadastro. Por favor, informe sua senha para prosseguir".

#### Tela 16: Pagamento via PIX (`doacoes-via-pix.png`)
* **Objetivo:** Efetuar a liquidação financeira instantânea.
* **Caminho Feliz (Happy Path):**
  1. A tela apresenta o QR Code do PIX e o campo do código Copia-e-Cola.
  2. O usuário clica em "Copiar código" (feedback visual de cópia bem-sucedida).
  3. No app bancário, realiza o pagamento.
  4. A tela escuta o webhook de liquidação ou o usuário clica em "Verificar pagamento", sendo direcionado à confirmação.
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Exceção de Timeout:* O cronômetro de 30 minutos expira sem detecção de pagamento. O QR Code deve ser ofuscado/bloqueado com mensagem: "Código PIX expirado. Clique aqui para gerar um novo código".
  * *Borda:* Cliques repetidos em "Verificar pagamento" sem confirmação bancária. A UI deve exibir feedback sutil ("Pagamento ainda não identificado. Aguarde alguns instantes...") e aplicar throttling para evitar sobrecarga na API.

#### Tela 17: Pagamento via Cartão ou Boleto (`doacoes-via-cartao-boleto.png`)
* **Objetivo:** Oferecer meios alternativos de pagamento bancário.
* **Caminho Feliz (Happy Path):**
  1. Usuário seleciona Cartão de Crédito, preenche número do cartão, nome impresso, validade (MM/AA) e CVV.
  2. Clica em "Doar R$ 100,00 com cartão".
  3. A transação é autorizada pela adquirente e a tela de confirmação é exibida.
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Divergência de Arquitetura:* O MVP aprovado em `rotas-api.md` prevê **exclusivamente PIX** (`formaPagamento: "PIX"`). Se este fluxo for mantido, exige decisão arquitetural sobre gateway e segurança PCI-DSS.
  * *Exceção:* Cartão recusado por falta de limite ou suspeita de fraude. A UI deve exibir mensagem amigável sem expor códigos técnicos da adquirente: "Não foi possível autorizar a transação. Verifique os dados ou tente outro meio de pagamento".

#### Tela 18: Confirmação da Doação e Recibo (`doacoes-confirmacao-doacao.png`)
* **Objetivo:** Dar retorno imediato do sucesso da contribuição e emitir comprovantes oficiais.
* **Caminho Feliz (Happy Path):**
  1. Usuário visualiza mensagem de agradecimento, código único da doação (ex.: `DS-1080`), detalhes da transação e cards para download em PDF: "Comprovante de pagamento" e "Recibo de doação".
  2. Recebe confirmação no e-mail informado.
  3. Pode clicar em "Minhas doações" para acompanhar o histórico.
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Exceção:* Falha na geração do PDF no servidor ao clicar em "Baixar PDF". O sistema deve reexecutar a geração ou exibir modal com opção de reenvio por e-mail.
  * *Borda:* Doador anônimo na tela de confirmação. O botão "Reenviar documentos para seu e-mail" deve estar desabilitado ou solicitar um e-mail temporário exclusivamente para envio do comprovante.

#### Tela 19: Detalhe da Doação e Tracking de Destinação (`doacoes-detalhes-doacao.png`)
* **Objetivo:** Transparência total sobre a aplicação do recurso doado.
* **Caminho Feliz (Happy Path):**
  1. O doador visualiza a linha do tempo do ciclo de vida da sua contribuição: `Pendente` (02/10) -> `Recebida` (02/10) -> `Aplicada` (05/10).
  2. Lê a mensagem personalizada de agradecimento enviada pela entidade.
  3. Confere a destinação registrada ("Compra de alimentos não perecíveis para as cestas") e baixa o recibo.
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Borda:* Doação com status estagnado em "Recebida" há mais de 30 dias. A UI deve indicar status amigável ("Em fase de alocação pela entidade").
  * *Segurança:* Doador autenticado tenta acessar o detalhe de doação de outro usuário via manipulação de ID na URL. O sistema deve responder com `404 Not Found` para não confirmar a existência da doação a terceiros.

#### Tela 20: Painel "Minhas Doações" do Doador (`doacoes-minhas-doacoes.png`)
* **Objetivo:** Central de acompanhamento de todas as doações já realizadas pelo usuário.
* **Caminho Feliz (Happy Path):**
  1. Doador visualiza cards de resumo financeiro acumulado (R$ 200,00), itens entregues (3 unidades) e doações com pendências (2 doações).
  2. Consulta a tabela de histórico com data, código, contribuição, campanha, status de pagamento e destinação.
  3. Clica em "Exportar histórico" ou em "Ver detalhe" de uma linha.
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Empty State:* Usuário recém-cadastrado sem nenhuma doação realizada. O painel deve apresentar mensagem acolhedora com botão central "Conhecer causas e doar agora".

---

### 2.3 Módulo: Gestão e Dashboard

#### Tela 21: Login da Entidade e Recuperação de Senha (`gestao-login-recuperacao-senha.png`)
* **Objetivo:** Autenticação dos administradores de entidade e fluxo de autoatendimento para senhas esquecidas.
* **Caminho Feliz (Happy Path):**
  1. O gestor informa e-mail institucional e senha, marcando opcionalmente "Manter conectado". Clica em "Entrar" e é direcionado ao Dashboard da entidade.
  2. Se esquecer a senha: no painel direito, insere o e-mail de recuperação e clica em "Enviar link de recuperação". O sistema exibe confirmação em verde ("Solicitação enviada. Link válido por 30 minutos").
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Segurança:* Informação de e-mail inexistente na recuperação de senha. Para evitar enumeração de contas, a tela DEVE exibir a mesma mensagem neutra de sucesso ("Se este e-mail estiver cadastrado, você receberá um link...").
  * *Proteção contra Abuso (Rate Limiting):* Solicitações repetidas de recuperação. A UI deve desabilitar o botão por 60 segundos com contador regressivo visível.
  * *Bloqueio de Entidade:* Usuário com credenciais corretas, mas entidade com status `INATIVA` ou `REPROVADA`. A autenticação deve ser recusada com mensagem clara: "Acesso bloqueado. Entre em contato com a administração da plataforma".

#### Tela 22: Cadastro de Entidade e Onboarding (`gestao-cadastro-entidade-onboarding.png`)
* **Objetivo:** Submissão de dados cadastrais e documentação comprobatória da ONG para análise prévia.
* **Caminho Feliz (Happy Path):**
  1. A entidade preenche Razão Social, CNPJ, e-mail institucional, telefone, área de atuação, endereço e dados do responsável legal.
  2. Faz upload dos 3 documentos obrigatórios em PDF/imagem (até 10MB): Comprovante de CNPJ, Estatuto Social e Documento do Responsável.
  3. Clica em "Salvar informações". A tela atualiza o stepper para o estado "Em análise", informando prazo de 2 dias úteis.
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Exceção:* CNPJ com dígitos verificadores inválidos ou já cadastrado na base de dados. A UI deve acusar erro inline impeditivo `409 Conflict`.
  * *Borda:* Usuário anexa arquivo com tamanho superior a 10MB. O frontend deve rejeitar imediatamente antes de iniciar o upload.

#### Tela 23: Dashboard Geral da Entidade (`gestao-dashboard-geral.png`)
* **Objetivo:** Painel executivo consolidado com indicadores-chave e visão de saúde financeira da entidade.
* **Caminho Feliz (Happy Path):**
  1. O gestor visualiza KPIs: Arrecadação financeira (R$ 33.400,00), Doadores únicos (176), Campanhas ativas (1) e Taxa de conclusão (50%).
  2. Analisa o gráfico de área temporal com a curva cumulativa de arrecadação do mês.
  3. Verifica o card de alertas "O que precisa de atenção" e a tabela sintética de campanhas.
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Empty State:* Entidade recém-aprovada que não possui dados históricos. Os gráficos devem exibir visual informativo neutro e os KPIs zerados, sem quebrar bibliotecas visuais (como Chart.js ou Recharts).

#### Tela 24: Dashboard Analítico por Campanha (`gestao-dashboard-analitico-campanha.png`)
* **Objetivo:** Análise detalhada do desempenho de uma campanha específica.
* **Caminho Feliz (Happy Path):**
  1. Gestor seleciona a campanha no dropdown ("Mesa solidária") e define o período no date picker.
  2. Visualiza dois gráficos analíticos sincronizados: "Evolução financeira" e "Evolução dos itens".
  3. Acompanha o detalhamento das metas faltantes (R$ 11.600,00 e 220 itens em 14 dias restantes).
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Borda:* Campanha cadastrada estritamente na modalidade "Somente dinheiro". O gráfico "Evolução dos itens" deve ser omitido ou exibir badge "Modalidade não aplicável a esta campanha".

#### Tela 25: Gestão de Doações Recebidas (`gestao-doacoes-recebidas.png`)
* **Objetivo:** Conciliar entradas financeiras e atestar a conferência física de mantimentos entregues.
* **Caminho Feliz (Happy Path):**
  1. O operador visualiza a lista unificada de doações financeiras e itens.
  2. Para doações de itens pendentes (ex: Mariana Lima - 2 cestas + 3 kits), clica em "Confirmar v".
  3. O formulário inferior de conferência abre; o operador valida os itens recebidos fisicamente, preenche a observação e clica em "Confirmar recebimento".
  4. O status da doação muda para "Confirmado" e os itens entram no estoque.
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Regra de Negócio Crítica:* O doador agendou entregar 3 kits de higiene, mas entregou apenas 2 fisicamente. O operador pode editar a quantidade real no formulário de conferência? Como o sistema trata a unidade não entregue (permanece pendente ou é cancelada)?
  * *LGPD:* Exibição de doadores anônimos no grid. O sistema deve ocultar nome e contato, exibindo estritamente "Doador anônimo".

#### Tela 26: Controle de Itens Doados e Estoque (`gestao-controle-itens-doados.png`)
* **Objetivo:** Gestão de almoxarifado dos suprimentos recebidos e registro de destinação às famílias atendidas.
* **Caminho Feliz (Happy Path):**
  1. O gestor acompanha a tabela "Saldo por item" (Saldo = Inicial + Entradas - Saídas).
  2. No bloco "Registrar saída e destinação", seleciona o item ("Cesta básica"), quantidade ("10 cestas"), destino ("Núcleo Vila Mariana"), data e anexa o recibo assinado.
  3. Clica em "Confirmar saída". O saldo de estoque é debitado imediatamente e uma nova linha é gravada no histórico de movimentações.
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Regra Impeditiva de Estoque:* Tentativa de registrar saída em quantidade superior ao saldo em estoque (ex.: saldo 82 cestas e o operador tenta dar saída de 90). A operação deve ser estritamente bloqueada para impedir estoque negativo.
  * *Auditoria (RNF-009):* O registro de saída deve capturar obrigatoriamente o ID e nome do operador logado que realizou o despacho.

#### Tela 27: Gestão de Doadores e Relacionamento (`gestao-doadores-e-historicos.png`)
* **Objetivo:** Painel de relacionamento (CRM) com a base de apoiadores da instituição.
* **Caminho Feliz (Happy Path):**
  1. O gestor pesquisa apoiadores por nome ou e-mail na tabela.
  2. Ao clicar em uma linha (ex: Ana Oliveira), o painel lateral abre exibindo o perfil do doador, total acumulado (R$ 450,00 em 3 doações), histórico completo de contribuições e botão "Registrar contato".
* **Caminhos de Exceção e Borda (Edge Cases):**
  * *Conflito de Privacidade (LGPD / RNF-006):* O protótipo exibe na tela os dados de contato mascarados (`a***@gmail.com`, `(11) 9****-**42`). **Risco grave:** se a API trafegar esses dados em texto claro e a máscara for aplicada apenas via CSS/React no navegador, há violação de segurança. A anonimização deve ocorrer obrigatoriamente no backend.

---

## 3. Inconsistências e Divergências (Figma vs. Requisitos & Dados)

A tabela abaixo cruza formalmente as telas do Figma com a especificação funcional (`requisitos.md`), o contrato de endpoints (`rotas-api.md`), o planejamento técnico (`routes-plan.md`) e os diagramas de entidade-relacionamento (DER):

| # | Funcionalidade no Figma | O que o Figma Desenhou | O que consta em Requisitos / Rotas / BD | Impacto / Divergência de QA |
|---|---|---|---|---|
| **01** | **Doação de Itens Físicos** | Telas 03, 06, 09, 11, 13, 14, 25, 26 com seleção de cestas, cobertores, kits e agendamento. | `requisitos.md` e `rotas-api.md` tratam doações **exclusivamente como financeiras** (moeda R$). | **CRÍTICO:** O backend atual não possui endpoints para itens, tabelas de unidades de medida nem payload para itens em `POST /api/v1/doacoes`. |
| **02** | **Controle de Estoque e Almoxarifado** | Tela 26 com entradas, saídas, destinos e saldo em estoque de mantimentos físicos. | Ausente nos requisitos de MVP. Presente de forma genérica no DER antigo (`ItemPedido` / `Produto`). | **CRÍTICO:** O escopo do Grupo 4 planejado para a Sprint 2 não contempla módulo WMS (estoque) na API Spring Boot. |
| **03** | **Meios de Pagamento (Cartão e Boleto)** | Tela 17 desenha formulário completo de Cartão de Crédito e emissão de Boleto Bancário. | `rotas-api.md` (rota 18) restringe expressamente: `formaPagamento: "PIX"`. Decisão PV01 pendente. | **ALTO:** Ausência de gateway contratado e regras de segurança PCI-DSS para manipulação de cartão no frontend. |
| **04** | **Identificação do Doador no Checkout** | Tela 15 oferece "Doação Anônima" e "Cadastro Rápido" (apenas Nome + E-mail, sem senha). | RF-020 exige login/cadastro para doar. `POST /api/v1/doacoes` exige token Bearer de usuário autenticado. | **CRÍTICO:** Se o front enviar requisição sem token JWT, o backend retornará `401 Unauthorized`. Incompatibilidade total de fluxo. |
| **05** | **Doação Recorrente** | Tela 12 permite marcar "Recorrente - semanal" com lembretes programados. | O modelo de dados e os requisitos não preveem doações recorrentes, planos ou cron jobs de notificação. | **MÉDIO:** Risco de promessa de funcionalidade ao usuário sem infraestrutura de mensageria no backend. |
| **06** | **Categorias e Urgência de Campanhas** | Telas 01, 02 e 05 exigem "Categoria" (Alimentação, Saúde, etc.) e badge "Urgente". | O objeto `Campanha` em `rotas-api.md` e no banco NÃO possui os campos `categoria` nem `urgente`. | **MÉDIO:** Campos exibidos na UI que não serão persistidos nem filtrados pela API REST. |
| **07** | **Upload de Documentos no Onboarding da Entidade** | Tela 22 exige upload de 3 PDFs (CNPJ, Estatuto, Doc do Responsável). | `POST /api/v1/entidades` recebe estritamente um JSON textual. Não há suporte a multipart/form-data. | **ALTO:** Impossibilidade de concluir o onboarding desenhado no Figma através do contrato atual de API. |
| **08** | **Prestação de Contas e Anexo de Despesas** | Tela 10 detalha despesas financeiras com upload de notas fiscais (PDF) pós-encerramento. | Requisitos preveem apenas encerramento de campanha (RF-013), sem entidade de despesas ou notas fiscais. | **MÉDIO:** Falta de tabelas no banco de dados para suportar a árvore de prestação de contas. |
| **09** | **Recuperação de Senha por E-mail** | Tela 21 possui card funcional para envio de link de recuperação válido por 30 min. | Documentado no `routes-plan.md` como **fora do escopo do MVP** (bloqueio PV05 - falta de provedor SMTP). | **BAIXO / MÉDIO:** Botão na interface chamará uma rota inexistente na Sprint 2. |
| **10** | **Retirada de Itens em Domicílio** | Tela 14 permite escolher "Solicitar retirada", mas não solicita endereço de coleta. | Ausente em todos os requisitos. O DER possui tabela `Endereco`, mas o fluxo do Figma omite os inputs. | **ALTO:** Falha de UX que inviabiliza a coleta caso o usuário marque essa opção. |

---

## 4. Regras de Negócio Omissas e Cenários de Falha Não Mapeados

### 4.1 Validações de Entrada, Máscaras e Limites de Caracteres
A documentação de design e os requisitos não especificam os limites de tamanho de campos, permitindo potenciais falhas de layout (*overflow*) ou ataques de negação de serviço:
* **Títulos e Nomes:**
  * Título da campanha: sugerir limite de **min: 10, max: 80 caracteres**.
  * Descrição da campanha: sugerir limite de **min: 50, max: 2.000 caracteres**.
  * Relato de prestação de contas: sugerir limite de **min: 30, max: 1.500 caracteres**.
* **Máscaras Obrigatórias no Frontend:**
  * Moeda: `R$ 0.000,00` (rejeitar centavos com 3 casas, vírgulas múltiplas ou letras).
  * Documentos: CPF (`000.000.000-00`) e CNPJ (`00.000.000/0000-00`) com cálculo de dígito verificador no front antes do dispatch.
  * Telefone/WhatsApp: `(00) 00000-0000` (validar nono dígito em celulares).
  * Datas: `DD/MM/AAAA` (bloquear datas inválidas como 31/02/2026).
* **Validação de Quantidade de Itens:**
  * O contador de itens deve rejeitar números negativos, decimais (ex: 2,5 kits) e possuir limite máximo prudencial por transação (ex: 999 unidades).

### 4.2 Comportamento em Falhas de Rede, Timeout e Erros HTTP
O Figma apresenta apenas telas de sucesso ("caminho dourado"). A equipe de desenvolvimento precisa de especificações claras para os seguintes cenários:
* **Perda de Conexão (Offline):**
  * Se o usuário preencher o wizard de criação de campanha ou checkout de doação e a conexão cair, a aplicação deve salvar o estado em `localStorage`/`IndexedDB` e exibir uma barra superior: *"Você está offline. Suas alterações foram salvas localmente."*
* **Timeout na API de Pagamento PIX:**
  * Se o gateway demorar mais de 10 segundos para gerar o QR Code, a tela não deve travar. Deve exibir: *"Instabilidade no serviço de pagamentos. [Tentar novamente]"*.
* **Mapeamento de Códigos HTTP na Interface:**
  * **HTTP 401 (Não Autenticado):** Redirecionar imediatamente para tela de login com toast informativo: *"Sua sessão expirou. Faça login novamente."*
  * **HTTP 403 (Acesso Negado):** Exibir tela amigável de erro de permissão, sem deslogar o usuário.
  * **HTTP 409 (Conflito de Negócio):** Exibir banner de aviso contextual (ex: *"Esta campanha já foi encerrada e não aceita novas doações"* ou *"CNPJ já cadastrado"*).
  * **HTTP 422 (Erro de Validação de Dados):** Destacar os campos correspondentes com borda vermelha e texto explicativo abaixo do input (`font.caption`).
  * **HTTP 500 (Erro Interno):** Modal centralizado amigável: *"Desculpe, ocorreu uma instabilidade em nossos servidores. Nossa equipe já foi notificada."* (NUNCA expor stack traces - RNF-011).

### 4.3 Estados Ausentes de Componentes (Design System)
Para evitar que os desenvolvedores criem soluções arbitrárias, o Grupo 1 deve desenhar no Figma:
* **Empty States (Estados Vazios):**
  * Busca de campanhas sem nenhum resultado encontrado.
  * Painel "Minhas Doações" para um doador que acabou de se cadastrar.
  * Dashboard de uma entidade que ainda não cadastrou campanhas.
  * Estoque de itens sem nenhuma movimentação.
* **Loading States (Estados de Carregamento):**
  * Skeletons de cards na listagem de campanhas enquanto a requisição `GET /campanhas` está pendente.
  * Botões primários com spinner animado e texto substituído (ex: de "Publicar campanha" para "Publicando...") com atributo `disabled`.
* **Idempotência no Frontend:**
  * Bloqueio obrigatório contra duplo clique em todos os botões de envio de formulários e pagamentos (`disabled` no momento do primeiro clique), prevenindo geração de transações em duplicidade (RNF-008).

---

## 5. Dúvidas e Propostas de Refinamento de Critérios de Aceite para Produto/UX (Grupo 1)

### 5.1 Lista de Perguntas Objetivas para o Grupo 1 (Produto / UX)
1. **Escopo de Itens Físicos na Sprint 2:** A doação de itens físicos (cestas básicas, kits, cobertores) e o controle de estoque de mantimentos fazem parte do escopo de entrega da Sprint 2 ou foram desenhados como protótipos de visão futura?
2. **Autenticação de Doadores no Checkout:** Como deve operar o backend caso o usuário escolha "Doação Anônima" ou "Cadastro Rápido"? Deve ser criado um usuário fantasma / temporário com token anônimo ou o requisito RF-020 será mantido exigindo conta obrigatória?
3. **Meios de Pagamento Reais:** Confirmamos a exclusão das telas de Cartão de Crédito e Boleto Bancário nesta sprint, concentrando os esforços de desenvolvimento e teste estritamente no fluxo de PIX?
4. **Logística de Retirada de Itens:** Na opção "Solicitar retirada", onde o doador deve inserir seu endereço de coleta e telefone? Será adicionada uma etapa de endereço no formulário?
5. **Critério de Sobra de Caixa na Prestação de Contas:** Em campanhas com arrecadação financeira excedente (arrecadado > aplicado), qual é a regra de destinação do saldo residual para permitir a publicação da prestação de contas?
6. **Mapeamento de Categorias no Backend:** O modelo de dados da entidade `Campanha` será alterado para receber o campo `categoria` presente nos filtros do protótipo?

---

### 5.2 Sugestões de Novos Critérios de Aceite no Padrão BDD (Gherkin)

Abaixo, o Grupo 5 propõe 5 critérios de aceite refinados e testáveis para guiar o desenvolvimento e os testes automatizados da Sprint 2:

#### Cenário 1: Doação Física de Itens com Agendamento
```gherkin
Funcionalidade: Doação de Itens Necessários e Agendamento
  Como um doador
  Quero escolher itens da lista de uma campanha e agendar a entrega na sede
  Para apoiar a instituição com suprimentos materiais

  Cenário: Seleção válida de itens com agendamento em horário comercial
    Dado que estou na página da campanha ativa "Mesa solidária"
    E a campanha possui meta aberta para "Cesta básica"
    Quando eu seleciono doar "2" unidades de "Cesta básica"
    E seleciono a modalidade "Vou entregar na sede"
    E escolho uma data futura em dia útil e horário entre "09h e 17h"
    E concluo a identificação
    Então o sistema deve registrar a doação com status "Pendente de entrega"
    E o sistema deve emitir o comprovante com código identificador e instruções de entrega
    E a quantidade de itens faltantes na campanha NÃO deve ser reduzida até a conferência física pela entidade

  Cenário: Tentativa de agendamento em data inválida
    Dado que estou na etapa de agendamento de entrega de itens
    Quando eu tento selecionar um domingo ou feriado no calendário
    Então o sistema deve manter essas datas desabilitadas para clique
    E não deve permitir o envio do formulário
```

#### Cenário 2: Identificação no Checkout — Doação Anônima vs. LGPD
```gherkin
Funcionalidade: Identificação e Privacidade do Doador
  Como um cidadão que deseja contribuir
  Quero realizar uma doação de forma anônima
  Para que minha identidade não seja exibida publicamente nem para a instituição

  Cenário: Conclusão de doação anônima bem-sucedida
    Dado que selecionei doar o valor de "R$ 50,00" via PIX
    Quando eu opto pela opção "Doação anônima" na etapa de identificação
    E concluo o pagamento do PIX com sucesso
    Então a tela de confirmação deve exibir o comprovante na tela
    E o sistema NÃO deve exibir meu nome na listagem pública da campanha
    E a tela de gestão da entidade deve exibir o registro como "Doador anônimo"
    E a API pública não deve trafegar meu nome ou e-mail na resposta JSON
```

#### Cenário 3: Validação de Metas na Edição de Campanhas Ativas
```gherkin
Funcionalidade: Integridade de Metas em Campanhas Publicadas
  Como um gestor de entidade
  Quero editar os dados de uma campanha ativa
  Sem comprometer os valores já arrecadados

  Cenário: Bloqueio de redução de meta financeira abaixo do arrecadado
    Dado que a campanha "Mesa solidária" possui "R$ 18.400,00" já arrecadados
    Quando eu tento alterar o campo "Meta financeira" para "R$ 15.000,00"
    E clico em "Salvar alterações"
    Então o sistema deve impedir o salvamento
    E deve exibir a mensagem de erro: "A meta não pode ser inferior ao valor já arrecadado"
    E a campanha deve permanecer com sua meta original inalterada
```

#### Cenário 4: Conferência Física e Atualização de Estoque pela Entidade
```gherkin
Funcionalidade: Conferência de Itens Entregues e Entrada em Estoque
  Como operador da entidade assistencial
  Quero atestar o recebimento físico dos mantimentos entregues
  Para atualizar o estoque da instituição e dar baixa no compromisso do doador

  Cenário: Conferência com recebimento integral dos itens agendados
    Dado que existe uma doação de "2 cestas básicas" com status "A confirmar"
    Quando eu acesso a tela "Gestão de doações recebidas"
    E clico em "Confirmar recebimento de itens"
    E informo que recebi "2" unidades conferidas
    E confirmo a operação
    Então o status da doação deve atualizar para "Confirmado"
    E o saldo de "Cesta básica" no controle de itens deve ser incrementado em exatamente 2 unidades
    E a destinação deve constar como "Não destinados" até a efetiva distribuição
```

#### Cenário 5: Resiliência e Exibição de Erro no Pagamento PIX com Timeout
```gherkin
Funcionalidade: Expiração e Resiliência do QR Code PIX
  Como um doador pagando via PIX
  Quero ser informado claramente caso o prazo para pagamento expire
  Para não transferir dinheiro para uma ordem cancelada

  Cenário: Expiração do tempo limite de 30 minutos do PIX
    Dado que o sistema gerou o QR Code PIX para a doação "DS-1080"
    Quando o cronômetro de 30 minutos atinge "00:00" sem confirmação bancária
    Então o QR Code e o código copia-e-cola devem ser desabilitados
    E o sistema deve exibir a mensagem: "Tempo para pagamento expirado"
    E deve disponibilizar o botão "Gerar novo código PIX"
    E a doação original deve ter seu status atualizado para "Cancelada/Expirada"
```

---

## 6. Conclusão e Próximos Passos do QA

Este relatório formaliza a postura proativa da equipe de QA no ciclo **Shift-Left**: identificamos preventivamente **10 pontos críticos de divergência**, mapeamos **27 fluxos com dezenas de edge cases**, alertamos sobre regras omissas de **LGPD e segurança de pagamento**, e entregamos **5 especificações em BDD** prontas para guiar o desenvolvimento orientado a testes (TDD/BDD).

### Encaminhamentos Imediatos:
1. **Reunião de Alinhamento (QA + Grupo 1):** Sanar as 6 perguntas da Seção 5.1 para travar o escopo oficial da Sprint 2.
2. **Ajuste de Protótipos (Grupo 1):** Criação dos estados de erro, loading e empty states no Figma.
3. **Atualização de Contratos (Grupo 5 + Backend):** Adequação do Swagger/OpenAPI caso o escopo de itens físicos seja homologado.
4. **Automação de Testes:** Elaboração dos scripts E2E (Selenium/Python e Cypress) e testes de contrato (RestAssured) baseados nos cenários BDD acima documentados.

---
*Relatório emitido pela Equipe 5 — Qualidade de Software (QA) do Projeto DoaSync.*
