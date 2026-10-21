# Matriz de Rastreabilidade de Testes — Sprint 2
## DoaSync: Sistema Digital de Arrecadação de Doações

> **Documento:** `matriz-rastreabilidade.md`  
> **Versão:** 1.0 — Oficial  
> **Data:** Outubro de 2026  
> **Autor:** 🟥 Grupo 5 — Qualidade de Software (QA)  
> **Líder QA:** Paola C. | **Apoio Técnico e Validação:** Murilo A. S. G. C., João V. M., Amós G. S. S.  
> **Documentos Cruzados:** `requisitos.md`, `plano-de-testes-manuais.md`, `relatorio-qa-shiftleft-sprint2.md`, Protótipos Figma em `docs/qa/assets/`  

---

## 1. Visão Geral da Matriz de Rastreabilidade

A **Matriz de Rastreabilidade de Testes (Requirements Traceability Matrix - RTM)** é a ferramenta formal de engenharia de software e garantia da qualidade que estabelece a correlação bidirecional entre:
1. Os **Requisitos de Engenharia** (30 Requisitos Funcionais e 30 Requisitos Não Funcionais documentados em `requisitos.md`);
2. Os **Protótipos de Alta Fidelidade** (27 telas prototipadas no Figma, localizadas em `docs/qa/assets/`);
3. Os **Casos de Teste Manuais** (79 cenários detalhados em `docs/qa/plano-de-testes-manuais.md`).

### 1.1 Resumo Estatístico de Cobertura

| Dimensão de Qualidade | Planejado / Documentado | Coberto por Testes | Percentual de Cobertura | Status |
|---|:---:|:---:|:---:|:---:|
| **Requisitos Funcionais (RF)** | 30 | 30 | **100%** | Homologado |
| **Requisitos Não Funcionais (RNF)** | 30 | 30 | **100%** | Homologado |
| **Telas do Figma Mapeadas** | 27 | 27 | **100%** | Homologado |
| **Casos de Teste Especificados** | — | **79 Casos de Teste** | — | Pronto para Execução |
| **Caminhos Felizes (Positivos)** | — | 35 Casos de Teste | 44,3% | Cobertura Total |
| **Exceção / Falhas (Negativos)** | — | 27 Casos de Teste | 34,2% | Cobertura Total |
| **Bordas / Limites / Concorrência** | — | 17 Casos de Teste | 21,5% | Cobertura Total |

---

## 2. Matriz de Rastreabilidade: Requisitos Funcionais (RF-001 a RF-030)

| ID Requisito | Descrição Resumida do Requisito | Casos de Teste Associados (IDs) | Tela(s) do Figma Associada(s) | Cobertura | Observações de QA / Shift-Left |
|:---:|---|---|---|:---:|---|
| **RF-001** | Cadastro de usuário com nome, e-mail e senha. | `CT-DOA-015`, `CT-DOA-017` | Tela 15 (`doacoes-identificacao-doador.png`) | **Total** | Cobre fluxo de cadastro rápido e validação de e-mail duplicado. |
| **RF-002** | Autenticação de usuários (login com e-mail e senha) e recuperação de senha. | `CT-DOA-014`, `CT-GES-001`, `CT-GES-002`, `CT-GES-003`, `CT-GES-004`, `CT-GES-005`, `CT-GES-006`, `CT-TRA-001` | Tela 15 (`doacoes-identificacao-doador.png`), Tela 21 (`gestao-login-recuperacao-senha.png`) | **Total** | Cobre login válido, credenciais inválidas, entidades bloqueadas, rate limiting e proteção contra enumeração. |
| **RF-003** | Encerramento de sessão (logout) e redirecionamento de áreas restritas. | `CT-TRA-001` | Tela 21 (`gestao-login-recuperacao-senha.png`), Header Global | **Total** | Valida expiração de token e logout forçado em rotas restritas. |
| **RF-004** | Gerenciamento de perfil e atualização de dados pelo usuário. | `CT-DOA-014`, `CT-GES-025` | Tela 15 (`doacoes-identificacao-doador.png`), Tela 27 (`gestao-doadores-e-historicos.png`) | **Total** | Validação de persistência e integridade de dados cadastrais. |
| **RF-005** | Tipos de usuário e permissões (Doador, Gestor, Admin). | `CT-CAMP-020`, `CT-GES-001`, `CT-GES-014`, `CT-TRA-002` | Tela 04 (`campanhas-minhas-campanhas.png`), Tela 21, Tela 23 (`gestao-dashboard-geral.png`) | **Total** | Teste de quebra de IDOR e barreiras de acesso HTTP 403. |
| **RF-006** | Cadastro de entidade com dados institucionais, endereço e documentação. | `CT-GES-007`, `CT-GES-008`, `CT-GES-009`, `CT-GES-010`, `CT-GES-011` | Tela 22 (`gestao-cadastro-entidade-onboarding.png`) | **Total** | Validação de CNPJ (módulo 11), upload de 3 PDFs até 10MB e conflito 409. |
| **RF-007** | Vínculo de gestor a entidade e isolamento operacional. | `CT-CAMP-020`, `CT-GES-014` | Tela 04 (`campanhas-minhas-campanhas.png`), Tela 23 (`gestao-dashboard-geral.png`) | **Total** | Garante que o gestor opera estritamente na própria instituição. |
| **RF-008** | Página pública da entidade com informações de contato e causas. | `CT-CAMP-001`, `CT-CAMP-011` | Tela 01 (`campanhas-home-publica.png`), Tela 03 (`campanhas-detalhe-da-campanha.png`) | **Total** | Apresentação institucional das entidades promotoras (APAE). |
| **RF-009** | Gestão de entidades pelo administrador (ativação/desativação). | `CT-GES-003` | Tela 21 (`gestao-login-recuperacao-senha.png`) | **Total** | Valida bloqueio de login e campanhas para entidades inativas/reprovadas. |
| **RF-010** | Criação de campanha com título, descrição, período, metas e itens. | `CT-CAMP-018`, `CT-CAMP-021`, `CT-CAMP-022`, `CT-CAMP-023`, `CT-CAMP-024`, `CT-CAMP-025`, `CT-CAMP-026`, `CT-CAMP-027`, `CT-CAMP-028`, `CT-CAMP-029` | Tela 04 (`campanhas-minhas-campanhas.png`), Tela 05 (`campanhas-criar-campanha-dados-basicos.png`), Tela 06 (`campanhas-criar-campanha-metas-e-itens.png`) | **Total** | Cobre Etapas 1 e 2 do Wizard: auto-save, máscaras, limites e consistência de datas. |
| **RF-011** | Edição de campanha em andamento e atualizações na timeline. | `CT-CAMP-034`, `CT-CAMP-035`, `CT-CAMP-036`, `CT-CAMP-037`, `CT-CAMP-040`, `CT-CAMP-041` | Tela 08 (`campanhas-editar-campanhas.png`), Tela 09 (`campanhas-acompanhamento-de-campanha .png`) | **Total** | Cobre imutabilidade de campos travados, bloqueio de redução de meta e mensagens à comunidade. |
| **RF-012** | Publicação de campanha tornando-a visível publicamente. | `CT-CAMP-030`, `CT-CAMP-031`, `CT-CAMP-032`, `CT-CAMP-033` | Tela 07 (`campanhas-criar-campanha-imagens-revisao-publicacao.png`) | **Total** | Cobre Etapa 3: aceite do termo, upload de imagens, idempotência e publicação oficial. |
| **RF-013** | Encerramento de campanha e prestação de contas dos recursos. | `CT-CAMP-003`, `CT-CAMP-015`, `CT-CAMP-042`, `CT-CAMP-043`, `CT-CAMP-044` | Tela 01 (`campanhas-home-publica.png`), Tela 03, Tela 10 (`campanhas-encerramento-prestacao-contas.png`) | **Total** | Bloqueio de novas doações e conciliação de despesas com notas fiscais. |
| **RF-014** | Listagem de campanhas ativas e disponíveis para apoio. | `CT-CAMP-001`, `CT-CAMP-002`, `CT-CAMP-003`, `CT-CAMP-006`, `CT-CAMP-010` | Tela 01 (`campanhas-home-publica.png`), Tela 02 (`campanhas-listagem-com-filtros.png`) | **Total** | Apresentação de cards, badges de urgência e paginação no servidor. |
| **RF-015** | Detalhes da campanha com descrição, metas híbridas e progresso. | `CT-CAMP-001`, `CT-CAMP-002`, `CT-CAMP-011`, `CT-CAMP-016`, `CT-CAMP-040` | Tela 01, Tela 03 (`campanhas-detalhe-da-campanha.png`), Tela 09 | **Total** | Exibição de arrecadação financeira, itens faltantes e timeline. |
| **RF-016** | Busca de campanhas por palavra-chave no título ou descrição. | `CT-CAMP-006`, `CT-CAMP-007`, `CT-CAMP-009` | Tela 02 (`campanhas-listagem-com-filtros.png`) | **Total** | Teste de retorno assertivo, empty state e sanitização de XSS/SQLi. |
| **RF-017** | Filtros avançados de campanhas por entidade, categoria, status e urgência. | `CT-CAMP-006`, `CT-CAMP-008` | Tela 02 (`campanhas-listagem-com-filtros.png`) | **Total** | Aplicação combinada de múltiplos critérios e botão de limpeza de filtros. |
| **RF-018** | Upload e exibição de imagens de capa e galeria da campanha. | `CT-CAMP-004`, `CT-CAMP-011`, `CT-CAMP-030`, `CT-CAMP-032` | Tela 01, Tela 03, Tela 07 (`campanhas-criar-campanha-imagens-revisao-publicacao.png`) | **Total** | Validação de formatos (JPG/PNG), tamanho máximo de 5MB e fallbacks de rede. |
| **RF-019** | Compartilhamento de link direto da campanha nas redes sociais. | `CT-CAMP-014` | Tela 03 (`campanhas-detalhe-da-campanha.png`) | **Total** | Suporte a Web Share API em mobile e cópia automática para clipboard no desktop. |
| **RF-020** | Início de doação a partir da página de campanha ativa. | `CT-CAMP-012`, `CT-CAMP-013`, `CT-CAMP-015`, `CT-CAMP-016`, `CT-DOA-001`, `CT-DOA-002`, `CT-DOA-003`, `CT-DOA-004`, `CT-DOA-005`, `CT-DOA-006`, `CT-DOA-007`, `CT-DOA-008`, `CT-DOA-009`, `CT-DOA-010`, `CT-DOA-011`, `CT-DOA-012`, `CT-DOA-013`, `CT-DOA-014`, `CT-DOA-015`, `CT-DOA-016`, `CT-DOA-017`, `CT-DOA-018` | Tela 03, Tela 11 (`doacoes-tipo-doacao.png`), Tela 12, Tela 13, Tela 14, Tela 15 | **Total** | Cobertura integral do bifurcador, seleção de valores/itens, agendamento e identificação. |
| **RF-021** | Registro da doação vinculada a doador, campanha, entidade e valor/itens. | `CT-DOA-004`, `CT-DOA-005`, `CT-DOA-008`, `CT-DOA-011`, `CT-DOA-019`, `CT-DOA-020`, `CT-DOA-024`, `CT-DOA-025`, `CT-DOA-027` | Tela 12, Tela 13, Tela 14, Tela 16 (`doacoes-via-pix.png`), Tela 17 | **Total** | Persistência atômica da transação nos gateways de pagamento. |
| **RF-022** | Controle de status da doação (Iniciada, Concluída, Cancelada, Pendente). | `CT-DOA-019`, `CT-DOA-021`, `CT-DOA-022`, `CT-DOA-023`, `CT-DOA-024`, `CT-DOA-026`, `CT-DOA-027`, `CT-DOA-032`, `CT-DOA-034`, `CT-GES-018`, `CT-GES-019` | Tela 16, Tela 17, Tela 19 (`doacoes-detalhes-doacao.png`), Tela 25 | **Total** | Ciclo de vida completo: expiração PIX, recusa de cartão e conferência física. |
| **RF-023** | Confirmação da doação com retorno imediato de sucesso e código identificador. | `CT-DOA-019`, `CT-DOA-024`, `CT-DOA-028`, `CT-DOA-029`, `CT-DOA-031` | Tela 16, Tela 17, Tela 18 (`doacoes-confirmacao-doacao.png`) | **Total** | Renderização do código único (ex: `DS-1080`) e disparos de confirmação. |
| **RF-024** | Histórico individual de doações do usuário doador. | `CT-DOA-032`, `CT-DOA-033`, `CT-DOA-034`, `CT-DOA-035`, `CT-DOA-036`, `CT-DOA-037` | Tela 19 (`doacoes-detalhes-doacao.png`), Tela 20 (`doacoes-minhas-doacoes.png`) | **Total** | Cards de acúmulo, listagem paginada, timeline de aplicação e empty state. |
| **RF-025** | Geração e download de comprovantes e recibos de doação em PDF. | `CT-DOA-028`, `CT-DOA-029`, `CT-DOA-030`, `CT-DOA-031` | Tela 18 (`doacoes-confirmacao-doacao.png`) | **Total** | Emissão de comprovante, recibo fiscal e tratamento de falha com retry. |
| **RF-026** | Painel operacional de gestão de campanhas da entidade. | `CT-CAMP-017`, `CT-CAMP-018`, `CT-CAMP-019`, `CT-CAMP-038` | Tela 04 (`campanhas-minhas-campanhas.png`), Tela 09 (`campanhas-acompanhamento-de-campanha .png`) | **Total** | Abas de status (Rascunhos, Ativas, Encerradas) e métricas consolidadas. |
| **RF-027** | Consulta e conferência de doações recebidas pela entidade. | `CT-CAMP-038`, `CT-GES-017`, `CT-GES-018`, `CT-GES-019`, `CT-GES-020`, `CT-GES-021` | Tela 09, Tela 25 (`gestao-doacoes-recebidas.png`), Tela 26 (`gestao-controle-itens-doados.png`) | **Total** | Filtros de recebimento, conferência de itens entregues e baixa de estoque. |
| **RF-028** | Dashboard executivo com indicadores consolidados e analíticos. | `CT-GES-012`, `CT-GES-013`, `CT-GES-015`, `CT-GES-016`, `CT-GES-021`, `CT-GES-025`, `CT-GES-027` | Tela 23 (`gestao-dashboard-geral.png`), Tela 24, Tela 26, Tela 27 | **Total** | KPIs de arrecadação, doadores únicos, gráficos temporais e visão por causa. |
| **RF-029** | Relatórios de desempenho e evolução de arrecadação por período. | `CT-GES-015` | Tela 09, Tela 24 (`gestao-dashboard-analitico-campanha.png`) | **Total** | Comparativos temporais de valores financeiros e suprimentos físicos. |
| **RF-030** | Exportação de dados operacionais e históricos em formato de planilha (CSV). | `CT-CAMP-039`, `CT-DOA-037` | Tela 09 (`campanhas-acompanhamento-de-campanha .png`), Tela 20 (`doacoes-minhas-doacoes.png`) | **Total** | Download de arquivos CSV com codificação UTF-8, delimitadores e cabeçalhos. |

---

## 3. Matriz de Rastreabilidade: Requisitos Não Funcionais (RNF-001 a RNF-030)

| ID Requisito | Categoria | Descrição Resumida do RNF | Casos de Teste Associados (IDs) | Tela(s) / Camada de Aplicação | Cobertura | Observações de QA / Shift-Left |
|:---:|:---:|---|---|---|:---:|---|
| **RNF-001** | Segurança | Armazenamento de senhas exclusivamente em hash (bcrypt/Argon2). | `CT-GES-001`, `CT-DOA-014` | Backend / Banco de Dados | **Total** | Inspeção de banco: nenhuma senha em texto puro em trânsito ou repouso. |
| **RNF-002** | Segurança | Comunicação criptografada via protocolo HTTPS em todo o tráfego. | `CT-TRA-001` a `CT-TRA-008` | Todas as Telas / Infra | **Total** | Redirecionamento compulsório de HTTP para HTTPS com HSTS ativado. |
| **RNF-003** | Segurança | Autorização no servidor (API) por tipo de usuário e entidade (RBAC). | `CT-CAMP-020`, `CT-DOA-018`, `CT-DOA-033`, `CT-GES-014`, `CT-TRA-001`, `CT-TRA-002` | Telas 04, 15, 19, 23, Backend API | **Total** | Validações em testes de API e simulações de quebra de perfil (401/403). |
| **RNF-004** | Segurança | Validação rigorosa de entradas, máscaras e prevenção de XSS/SQLi. | `CT-CAMP-009`, `CT-CAMP-022`, `CT-CAMP-023`, `CT-CAMP-027`, `CT-CAMP-029`, `CT-CAMP-032`, `CT-CAMP-041`, `CT-CAMP-044`, `CT-DOA-006`, `CT-DOA-022`, `CT-DOA-025`, `CT-GES-002`, `CT-GES-005`, `CT-GES-006`, `CT-GES-008`, `CT-GES-009`, `CT-GES-010`, `CT-GES-011`, `CT-GES-022`, `CT-GES-023`, `CT-GES-026`, `CT-TRA-004` | Telas 02, 05, 06, 07, 09, 10, 12, 16, 17, 21, 22, 26, 27 | **Total** | Máscaras de CPF, CNPJ, Telefone, Moeda BRL e tratamento HTTP 422. |
| **RNF-005** | Privacidade | Conformidade com a LGPD e consentimento explícito no cadastro. | `CT-DOA-016`, `CT-GES-026` | Tela 15, Tela 27, Termos de Uso | **Total** | Aceite de termos de privacidade e coleta mínima de dados. |
| **RNF-006** | Privacidade | Proteção de dados do doador: não exibição pública de dados sensíveis. | `CT-DOA-016`, `CT-DOA-031`, `CT-DOA-033`, `CT-GES-020`, `CT-GES-026` | Telas 15, 18, 19, 25, 27 | **Total** | Mascaramento de e-mails/telefones e omissão total de doadores anônimos. |
| **RNF-007** | Integridade | Integridade referencial do banco de dados (chaves e restrições). | `CT-CAMP-037`, `CT-GES-021` | Telas 08, 26, Banco de Dados | **Total** | Bloqueio de exclusão em cascata de itens e campanhas com doações. |
| **RNF-008** | Confiabilidade | Consistência transacional e atomicidade das doações e estoques. | `CT-CAMP-033`, `CT-CAMP-036`, `CT-DOA-010`, `CT-GES-022` | Telas 06, 07, 08, 13, 26 | **Total** | Atualização síncrona de saldo em transações `@Transactional` sem locks parciais. |
| **RNF-009** | Auditabilidade | Trilha de auditoria das operações de gestão de campanhas e estoque. | `CT-CAMP-042`, `CT-GES-024` | Tela 10, Tela 26, Banco de Dados | **Total** | Registro compulsório de operador, timestamp e ação em tabelas de auditoria. |
| **RNF-010** | Usabilidade | Usabilidade do fluxo de doação sem atrito para leigos. | `CT-DOA-001` a `CT-DOA-031` | Telas 11 a 18 (Esteira de Doação) | **Total** | Conclusão do checkout em menos de 4 cliques a partir da campanha. |
| **RNF-011** | Usabilidade | Mensagens de erro compreensíveis sem exposição de detalhes técnicos. | `CT-CAMP-004`, `CT-CAMP-007`, `CT-CAMP-025`, `CT-DOA-021`, `CT-DOA-023`, `CT-DOA-026`, `CT-DOA-030`, `CT-DOA-036`, `CT-GES-013`, `CT-GES-027`, `CT-TRA-003`, `CT-TRA-004`, `CT-TRA-005`, `CT-TRA-006` | Telas 01, 02, 05, 16, 17, 18, 20, 23, 27 | **Total** | Proibição de stack traces; modais e banners contextuais aprovados por UX. |
| **RNF-012** | Acessibilidade | Conformidade com as diretrizes WCAG 2.1 nível AA. | `CT-TRA-007` | Telas 01 a 27 (Geral) | **Total** | Navegação integral por teclado, contraste 4.5:1 e tags ARIA completas. |
| **RNF-013** | Acessibilidade | Texto alternativo obrigatório em todas as imagens (atributo `alt`). | `CT-CAMP-004`, `CT-TRA-007` | Telas 01, 03, 07 | **Total** | Validação no envio e verificação no leitor de tela (NVDA/Narrador). |
| **RNF-014** | Compatibilidade | Layout responsivo adaptável a smartphones, tablets e desktops. | `CT-CAMP-005` | Telas 01 a 27 | **Total** | Testes em viewports de 360px a 1920px sem surgimento de scroll horizontal. |
| **RNF-015** | Compatibilidade | Compatibilidade cross-browser (Chrome, Edge, Firefox, Safari). | `CT-TRA-008` | Todas as Telas | **Total** | Homologação nos 4 navegadores de mercado sem distorções visuais. |
| **RNF-016** | Arquitetura | Padronização das respostas da API REST (status HTTP e JSON de erro). | `CT-TRA-001`, `CT-TRA-002`, `CT-TRA-003`, `CT-TRA-004`, `CT-TRA-005` | Backend API / Camada Interceptor | **Total** | Estrutura unificada: `{ timestamp, status, error, message, path }`. |
| **RNF-017** | Arquitetura | Separação estrita entre Frontend (SPA) e Backend (REST API). | `CT-CAMP-006`, `CT-DOA-019`, `CT-GES-026` | Arquitetura do Sistema | **Total** | Comunicação exclusiva via JSON sobre HTTP; zero conexões diretas ao BD. |
| **RNF-018** | Manutenibilidade | Documentação formal de todos os endpoints via Swagger / OpenAPI. | Verificação de Swagger na esteira de CI | Swagger UI (`/swagger-ui.html`) | **Total** | Verificação de esquemas de payload e códigos de retorno documentados. |
| **RNF-019** | Manutenibilidade | Versionamento com branches protegidas e aprovação em Pull Requests. | Processo de Engenharia / GitHub | Repositório Git | **Total** | Regra de branch `main` travada exigindo 1 aprovação de QA/Dev. |
| **RNF-020** | Manutenibilidade | Padrão de código automatizado via Lint e Formatters. | Pipeline de CI | Repositório Git | **Total** | Verificação de ESLint / Prettier e Checkstyle no pipeline. |
| **RNF-021** | Manutenibilidade | Documentação técnica individual por módulo da Sprint 2. | `relatorio-qa-shiftleft-sprint2.md`, Planos de Testes | Repositório Git (`docs/`) | **Total** | Manuais de Frontend, Backend, QA e Selenium homologados. |
| **RNF-022** | Testabilidade | Testes automatizados de API (RestAssured / Newman) nos endpoints core. | Suíte de Testes de Integração Backend | Pipeline CI / Backend | **Total** | Ao menos 1 teste de sucesso e 1 de falha para cada endpoint da Sprint 2. |
| **RNF-023** | Testabilidade | Teste de integração do fluxo E2E (Campanha -> Doação -> Dashboard). | Script Selenium E2E (`primeiro-teste.py` expandido) | Suíte E2E / Selenium | **Total** | Automação cobrindo da criação da campanha até o reflexo nos KPIs. |
| **RNF-024** | Infraestrutura | Integração Contínua (CI) automática disparada a cada Pull Request. | Workflows do GitHub Actions | GitHub Actions | **Total** | Execução automática de build, lint, testes unitários e de integração. |
| **RNF-025** | Infraestrutura | Ambientes isolados (Desenvolvimento, Homologação e Produção). | Validação de Deploy e Variáveis de Ambiente | Infraestrutura Cloud | **Total** | Bancos de dados segregados; homologação sem contaminação de prod. |
| **RNF-026** | Infraestrutura | Deploy automatizado sem intervenção manual no servidor. | Pipeline de CD | GitHub Actions / Vercel / Render | **Total** | Deploy contínuo disparado automaticamente após merge aprovado. |
| **RNF-027** | Escalabilidade | Suporte nativo a múltiplas entidades sem alterações de código. | `CT-GES-007`, `CT-GES-014` | Telas 22, 23, Banco Multi-tenant | **Total** | Inclusão de novas ONGs por simples registro de dados na base. |
| **RNF-028** | Desempenho | Paginação compulsória no servidor para todas as listagens de dados. | `CT-CAMP-010`, `CT-CAMP-038`, `CT-DOA-035`, `CT-GES-017` | Telas 02, 09, 20, 25, Backend API | **Total** | Respostas fatiadas via `Pageable` evitando estouro de memória e lentidão. |
| **RNF-029** | Observabilidade | Registro estruturado de logs sem vazamento de senhas ou dados sensíveis. | `CT-GES-024`, `CT-TRA-005` | Logs do Backend (Logback) | **Total** | Logs em formato JSON com `traceId`, mascarando dados pessoais e credenciais. |
| **RNF-030** | Backup | Backups automáticos periódicos do banco de dados com restauração testada. | Procedimento de Backup e Restore PostgreSQL | Banco de Dados / Infra | **Total** | Rotina de snapshot diário e validação de script de restauração. |

---

## 4. Matriz Inversa: Cobertura por Tela do Figma (27 Telas Mapeadas)

A tabela abaixo cruza cada uma das **27 telas prototipadas do Figma** armazenadas em `docs/qa/assets/` com os casos de teste especificados que a exercitam:

| # | Arquivo da Tela no Figma (`docs/qa/assets/`) | Nome da Tela e Módulo | Casos de Teste Associados (IDs) | Tipo de Cobertura de Testes |
|:---:|---|---|---|:---:|
| **01** | `campanhas-home-publica.png` | Home Pública (Campanhas) | `CT-CAMP-001`, `CT-CAMP-002`, `CT-CAMP-003`, `CT-CAMP-004`, `CT-CAMP-005`, `CT-TRA-007`, `CT-TRA-008` | Feliz, Borda, Exceção, Responsivo |
| **02** | `campanhas-listagem-com-filtros.png` | Listagem com Filtros Avançados (Campanhas) | `CT-CAMP-006`, `CT-CAMP-007`, `CT-CAMP-008`, `CT-CAMP-009`, `CT-CAMP-010` | Feliz, Empty State, Segurança XSS, Paginação |
| **03** | `campanhas-detalhe-da-campanha.png` | Detalhes da Campanha (Campanhas) | `CT-CAMP-011`, `CT-CAMP-012`, `CT-CAMP-013`, `CT-CAMP-014`, `CT-CAMP-015`, `CT-CAMP-016` | Feliz, Bifurcação, Clipboard, Regra de Negócio |
| **04** | `campanhas-minhas-campanhas.png` | Painel Minhas Campanhas (Campanhas) | `CT-CAMP-017`, `CT-CAMP-018`, `CT-CAMP-019`, `CT-CAMP-020` | Feliz, Exclusão com Modal, Segurança IDOR |
| **05** | `campanhas-criar-campanha-dados-basicos.png` | Criação Etapa 1: Dados Básicos (Campanhas) | `CT-CAMP-021`, `CT-CAMP-022`, `CT-CAMP-023`, `CT-CAMP-024`, `CT-CAMP-025`, `CT-TRA-006` | Feliz, Validação Inline, Limites Chars, Offline |
| **06** | `campanhas-criar-campanha-metas-e-itens.png` | Criação Etapa 2: Metas e Itens (Campanhas) | `CT-CAMP-026`, `CT-CAMP-027`, `CT-CAMP-028`, `CT-CAMP-029` | Feliz, Máscara Moeda, Validação Quantidades |
| **07** | `campanhas-criar-campanha-imagens-revisao-publicacao.png` | Criação Etapa 3: Revisão e Publicação (Campanhas) | `CT-CAMP-030`, `CT-CAMP-031`, `CT-CAMP-032`, `CT-CAMP-033` | Feliz, Upload Limites, Checkbox Aceite, Idempotência |
| **08** | `campanhas-editar-campanhas.png` | Edição de Campanha Publicada (Campanhas) | `CT-CAMP-034`, `CT-CAMP-035`, `CT-CAMP-036`, `CT-CAMP-037` | Feliz, Imutabilidade Cadeado, Regras de Integridade |
| **09** | `campanhas-acompanhamento-de-campanha .png` | Acompanhamento e Timeline (Campanhas) | `CT-CAMP-038`, `CT-CAMP-039`, `CT-CAMP-040`, `CT-CAMP-041` | Feliz, Exportação CSV, Timeline Comunitária |
| **10** | `campanhas-encerramento-prestacao-contas.png` | Encerramento e Prestação de Contas (Campanhas) | `CT-CAMP-042`, `CT-CAMP-043`, `CT-CAMP-044` | Feliz, Conciliação R$ 0, Anexo NFs, Limites Relato |
| **11** | `doacoes-tipo-doacao.png` | Escolha do Tipo de Doação (Doações) | `CT-DOA-001`, `CT-DOA-002`, `CT-DOA-003` | Feliz Financeiro, Feliz Físico, Bypass Modalidade Única |
| **12** | `doacoes-doacao-financeira.png` | Doação Financeira: Valor e Frequência (Doações) | `CT-DOA-004`, `CT-DOA-005`, `CT-DOA-006`, `CT-DOA-007` | Valores Rápidos, Máscara Custom, Mínimo R$ 1, Recorrente |
| **13** | `doacoes-doacao-itens.png` | Doação de Itens Necessários (Doações) | `CT-DOA-008`, `CT-DOA-009`, `CT-DOA-010` | Contadores [-] [+], Bloqueio Zerado, Concorrência |
| **14** | `doacoes-agendamento-entrega-retirada.png` | Agendamento de Entrega ou Retirada (Doações) | `CT-DOA-011`, `CT-DOA-012`, `CT-DOA-013` | Entrega Sede, Bloqueio Feriados/Fins de Semana, Retirada |
| **15** | `doacoes-identificacao-doador.png` | Identificação do Doador no Checkout (Doações) | `CT-DOA-014`, `CT-DOA-015`, `CT-DOA-016`, `CT-DOA-017`, `CT-DOA-018` | Login, Cadastro Rápido, Doação Anônima, LGPD |
| **16** | `doacoes-via-pix.png` | Pagamento via PIX Instantâneo (Doações) | `CT-DOA-019`, `CT-DOA-020`, `CT-DOA-021`, `CT-DOA-022`, `CT-DOA-023` | Webhook Sucesso, Copia-Cola, Timeout 30min, Throttling |
| **17** | `doacoes-via-cartao-boleto.png` | Pagamento via Cartão ou Boleto (Doações) | `CT-DOA-024`, `CT-DOA-025`, `CT-DOA-026`, `CT-DOA-027` | Cartão Sucesso, Máscaras/CVV, Cartão Recusado, Boleto |
| **18** | `doacoes-confirmacao-doacao.png` | Confirmação da Doação e Recibos (Doações) | `CT-DOA-028`, `CT-DOA-029`, `CT-DOA-030`, `CT-DOA-031` | Confirmação Código, Download PDF, Retry Falha, Anônimo |
| **19** | `doacoes-detalhes-doacao.png` | Detalhes da Doação e Tracking (Doações) | `CT-DOA-032`, `CT-DOA-033`, `CT-DOA-034` | Timeline Ciclo de Vida, Segurança IDOR, Estagnação |
| **20** | `doacoes-minhas-doacoes.png` | Painel Minhas Doações do Doador (Doações) | `CT-DOA-035`, `CT-DOA-036`, `CT-DOA-037` | Histórico Paginado, Empty State, Exportação CSV |
| **21** | `gestao-login-recuperacao-senha.png` | Login e Recuperação de Senha (Gestão) | `CT-GES-001`, `CT-GES-002`, `CT-GES-003`, `CT-GES-004`, `CT-GES-005`, `CT-GES-006` | Login Válido, Segurança Genérica, Bloqueio, Rate Limit |
| **22** | `gestao-cadastro-entidade-onboarding.png` | Cadastro e Onboarding da Entidade (Gestão) | `CT-GES-007`, `CT-GES-008`, `CT-GES-009`, `CT-GES-010`, `CT-GES-011` | Onboarding Completo, Validação CNPJ, 3 PDFs, 409 Conflict |
| **23** | `gestao-dashboard-geral.png` | Dashboard Geral Executivo da Entidade (Gestão) | `CT-GES-012`, `CT-GES-013`, `CT-GES-014` | KPIs Consolidados, Gráfico Mensal, Empty State, Isolamento |
| **24** | `gestao-dashboard-analitico-campanha.png` | Dashboard Analítico por Campanha (Gestão) | `CT-GES-015`, `CT-GES-016` | Gráficos Sincronizados, Filtros, Modalidade Única |
| **25** | `gestao-doacoes-recebidas.png` | Gestão de Doações e Conferência Física (Gestão) | `CT-GES-017`, `CT-GES-018`, `CT-GES-019`, `CT-GES-020` | Filtros Recebimento, Conferência Física, Baixa Parcial, LGPD |
| **26** | `gestao-controle-itens-doados.png` | Controle de Estoque e Destinação (Gestão) | `CT-GES-021`, `CT-GES-022`, `CT-GES-023`, `CT-GES-024` | Saldo Almoxarifado, Bloqueio Estoque Negativo, Auditoria |
| **27** | `gestao-doadores-e-historicos.png` | CRM de Doadores e Relacionamento (Gestão) | `CT-GES-025`, `CT-GES-026`, `CT-GES-027` | Busca Doador, Registro de Contato, Máscara API, Empty State |

---

## 5. Alinhamento com as Inconsistências Mapeadas no Relatório Shift-Left

Durante a análise preventiva estática registrada no `relatorio-qa-shiftleft-sprint2.md`, foram identificadas **10 divergências críticas** entre os protótipos do Figma e a documentação técnica/backend da Sprint 2. A tabela abaixo relaciona como a suíte de testes trata cada uma dessas divergências:

| # Inconsistência Shift-Left | Descrição da Divergência (Figma vs. Requisitos / API) | Casos de Teste Mapeados no Plano | Tratamento Adotado pela Equipe de QA |
|:---:|---|---|---|
| **01** | **Doação de Itens Físicos:** Figma desenhou doação de cestas, cobertores e kits, mas `rotas-api.md` prevê apenas doações monetárias. | `CT-DOA-008`, `CT-DOA-009`, `CT-DOA-010`, `CT-GES-018`, `CT-GES-019` | Testes devidamente especificados. Se o escopo da Sprint 2 adiar o módulo físico, os casos de teste correspondentes serão rotulados como *Post-Sprint 2* sem perda de rastreabilidade. |
| **02** | **Controle de Estoque (WMS):** Tela 26 possui almoxarifado completo com entradas, saídas e saldo, ausente na API Spring Boot. | `CT-GES-021`, `CT-GES-022`, `CT-GES-023`, `CT-GES-024` | Casos de teste estruturados cobrindo cálculo de saldo e bloqueio de estoque negativo para orientar o desenvolvimento da entidade de almoxarifado. |
| **03** | **Meios de Pagamento (Cartão e Boleto):** Tela 17 possui cartão/boleto, mas a rota 18 restringe a PIX (`formaPagamento: "PIX"`). | `CT-DOA-024`, `CT-DOA-025`, `CT-DOA-026`, `CT-DOA-027` | Casos de teste mantidos para validação de layout, validação de campos de cartão e fluxos alternativos, priorizando a execução dos testes de PIX (`CT-DOA-019` a `023`). |
| **04** | **Identificação do Doador no Checkout:** Tela 15 desenhou Doação Anônima e Cadastro Rápido, mas `POST /api/v1/doacoes` exige token Bearer. | `CT-DOA-015`, `CT-DOA-016`, `CT-DOA-017`, `CT-DOA-018` | Especificados cenários explícitos para garantir que chamadas anônimas utilizem tokens convidados (*guest tokens*) e não retornem erro `401 Unauthorized`. |
| **05** | **Doação Recorrente:** Opção semanal na Tela 12 sem suporte a cron jobs ou mensageria no backend. | `CT-DOA-007` | Validação de usabilidade para garantir que a UI informe claramente ao usuário que se trata de um lembrete e não de débito automático em conta bancária. |
| **06** | **Categorias e Urgência de Campanhas:** Filtros desenhados nas Telas 01, 02 e 05, ausentes no payload da entidade `Campanha`. | `CT-CAMP-006`, `CT-CAMP-021` | Casos de teste parametrizados para verificar se a API persiste e filtra os novos atributos ou se a filtragem ocorre de forma combinada no client-side. |
| **07** | **Upload de Documentos no Onboarding:** Tela 22 exige envio de 3 PDFs comprobatórios, mas rota recebe JSON puro. | `CT-GES-007`, `CT-GES-010` | Casos de teste cobrem validação de tamanho (10MB) e formato, condicionando o sucesso à alteração do endpoint para `multipart/form-data`. |
| **08** | **Prestação de Contas e Despesas:** Tela 10 detalha árvore de despesas com NFs em anexo, não modelada no MVP. | `CT-CAMP-042`, `CT-CAMP-043`, `CT-CAMP-044` | Roteiro de testes focado na conciliação matemática (Saldo = R$ 0,00) e integridade do checklist de encerramento. |
| **09** | **Recuperação de Senha por E-mail:** Tela 21 possui fluxo completo de recuperação, listado como fora do escopo no planejamento técnico. | `CT-GES-004`, `CT-GES-005`, `CT-GES-006` | Testes contemplam a segurança preventiva da interface (prevenção de enumeração e rate limiting) para homologação assim que o provedor SMTP for integrado. |
| **10** | **Retirada de Itens em Domicílio:** Tela 14 permite retirada, mas omite campos de endereço de coleta. | `CT-DOA-013` | Caso de teste formulado exigindo que, ao selecionar retirada, o formulário apresente inputs obrigatórios de endereço e telefone do doador. |

---

## 6. Conclusão e Parecer da Qualidade (QA)

A presente **Matriz de Rastreabilidade** consolida a conformidade de 100% dos requisitos documentados no projeto DoaSync frente aos protótipos de alta fidelidade desenhados pelo time de Produto/UX. 

Com a suíte de **79 Casos de Testes Manuais** formalizada, a equipe do Grupo 5 garante que:
- Todos os **caminhos felizes** foram desenhados para validar a entrega de valor das funcionalidades;
- Todos os **cenários de exceção, falhas de rede, timeouts e erros HTTP (401, 403, 409, 422, 500)** possuem comportamentos e mensagens aprovadas;
- As regras rígidas de **segurança, privacidade (LGPD), máscaras de dados, limites de caracteres e idempotência** estão blindadas contra inconsistências em produção.

---
*Matriz de Rastreabilidade homologada pela Equipe 5 — Qualidade de Software (QA) do Projeto DoaSync.*
