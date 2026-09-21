## Conceitos e definições

Um teste **end-to-end** (E2E) são uma maneira de testes que passam de ponta a ponta atravessando todas as camadas de uma arquitetura de sistemas. **Os testes são realizados do início ao fim**, por exemplo, em uma arquitetura de sistema **MVC** (Model-View-Controller) os testes são realizados na camada de apresentação (view), validadando regras de negócio tratadas pelo controlador (controller) chegando até o modelo (model), onde os dados podem ser gravados e lidos a partir de um banco de dados.

### Tipos de testes E2E

* **E2E horizontal:** quando os testes são realizado em um mesmo nível de teste, considerando todos os fluxos do sistema. Tem como objetivo verificar se os componentes daquela camada, fazem o que deveriam fazer em um determinado contexto.

* **E2E vertical:** quando os testes são realizados em vários níveis de teste. Dessa forma é possível identificar problemas nas diferentes camadas do sistema, desde o nível unitário até a integração de todos os componentes. Nesse caso o objetivo é verificar se cada camada do sistema funciona independentemente.

### Testes End-to-End (E2E)

1. O teste cruza vários sistemas e grupos de usuários.
2. Garante que um sistema, ou parte dele, continue funcionando após alterações serem feitas.
3. Testa a maneira como vários usuários interagem no sistema.
4. Valida se cada etapa do processo foi concluída.

### Ferramenta para E2E vertical

**Testes de sistema (UI):**

* [Selenium](https://www.selenium.dev/): realiza testes automáticos através da interface web de uma aplicação, independente da linguagem de programação que ela foi construída. Possui diferentes linguagens de programação, como python, java, ruby, etc. Tem fácil instalação, pois permite realizar testes através de um plugin para o navegador, utilizando o Selenium IDE, sem a necessidade de conhecimentos de programação.

---

## Teste de Interface (UI)

UI significa **Interface de Usuário**. Os testes de interface têm como objetivo verificar se os elementos visuais e interativos de uma aplicação **são exibidos corretamente e respondem às ações do usuário conforme o esperado**.

Em aplicações web, esses testes são normalmente realizados sobre a **GUI (Graphical User Interface)**, verificando elementos como botões, campos de texto, links, menus, formulários e mensagens.

### O que pode ser validado

Os testes de UI podem verificar:

* **Elementos visuais:** se botões, campos, textos e outros componentes são apresentados corretamente;

* **Interações:** se ações como clicar, digitar, selecionar opções e enviar formulários funcionam;

* **Validação de dados:** se campos obrigatórios, formatos e limites de caracteres são respeitados;

* **Navegação:** se o usuário consegue percorrer as telas e executar as ações esperadas;

* **Responsividade e compatibilidade:** se a interface funciona corretamente em diferentes resoluções, dispositivos e navegadores;

* **Usabilidade:** se a interface é clara, consistente e fácil de utilizar.

### Testes manuais e automatizados

Os testes de interface podem ser realizados de forma **manual ou automatizada**.

Os **testes manuais** são úteis principalmente para aspectos mais subjetivos, como aparência, organização visual e facilidade de uso.

Já os **testes automatizados** permitem simular ações do usuário, como clicar em botões, preencher campos e enviar formulários. Ferramentas como o **Selenium** podem localizar elementos da página e executar essas ações automaticamente.

### UI x E2E

Embora possam utilizar a interface da aplicação, **testes de UI e testes E2E possuem focos diferentes**.

O teste de **UI** concentra-se principalmente na aparência e no comportamento dos elementos da interface. Já o teste **E2E** valida um fluxo completo da aplicação, podendo envolver frontend, backend, banco de dados e outros serviços.

Por exemplo:

**Teste de UI:** verificar se o botão "Cadastrar" está disponível e responde ao clique.

**Teste E2E:** preencher o formulário de cadastro → clicar em "Cadastrar" → processar os dados no backend → salvar o usuário → verificar se o cadastro foi concluído com sucesso.

---

## Aplicação dos testes de UI no DoaSync

Como o Selenium permite automatizar a interação com interfaces web, ele pode ser utilizado no DoaSync para simular ações realizadas por um usuário no frontend.

Alguns exemplos de testes de interface que podem ser realizados são:

* **Campos e formulários:** verificar se campos de cadastro, login, doação e agendamento são exibidos e permitem a entrada de dados corretamente;

* **Botões e links:** verificar se elementos como "Cadastrar", "Entrar", "Doar" e "Agendar" respondem ao clique e executam a ação esperada;

* **Validação de formulários:** verificar o comportamento da interface ao informar campos vazios, CPF/CNPJ inválidos ou outros dados incorretos;

* **Mensagens da interface:** verificar se mensagens de erro, sucesso ou confirmação são apresentadas corretamente;

* **Projetos:** verificar se os projetos e suas informações são apresentados corretamente na interface;

* **Navegação:** verificar se o usuário consegue navegar corretamente entre as diferentes páginas da aplicação;

* **Elementos dinâmicos:** verificar se componentes da interface são atualizados corretamente após uma ação do usuário.

---

## Relação com Selenium

O Selenium permite automatizar testes de interface simulando a interação de um usuário com uma aplicação web. Para realizar essas interações, primeiro é necessário **localizar os elementos presentes no HTML (DOM)**, identificando qual botão, campo, link ou outro componente será utilizado durante o teste.

### Localização dos elementos

Os elementos podem ser localizados utilizando diferentes estratégias, como:

* `id`: identifica um elemento pelo seu identificador único;
* `name`: localiza pelo atributo `name`;
* `class`: utiliza a classe CSS do elemento;
* `CSS Selector`: permite localizar elementos utilizando seletores CSS;
* `XPath`: permite localizar elementos através da estrutura do documento HTML.

Por exemplo, considerando o seguinte botão:

```html
<button id="btn-doar">Doar</button>
```

O Selenium pode localizar esse elemento através do `id` `btn-doar` e, após encontrá-lo, executar uma ação sobre ele.

### Ações com Selenium

Após a localização dos elementos, o Selenium pode simular diferentes ações realizadas pelo usuário, como:

* clicar em botões e links;
* preencher campos de texto;
* selecionar opções;
* enviar formulários;
* navegar entre páginas;
* verificar textos e elementos apresentados na tela.

Dessa forma, um teste de interface pode seguir o fluxo:

**Localizar elemento → executar ação → verificar resultado**

Por exemplo, em um teste de cadastro do DoaSync, o Selenium poderia **localizar o campo de nome → preencher o nome → localizar o botão "Cadastrar" → clicar no botão → verificar se a mensagem de sucesso foi apresentada**.

---

## Fontes

* [A Pirâmide de teste e os Testes end-to-end | by Anne Caroline Rocha | gtsw | Medium](https://medium.com/gtsw/a-pir%C3%A2mide-de-teste-e-os-testes-end-to-end-38f77ad3d137)
* [UI Testing: A Complete Beginner’s Guide With Examples](https://www.testim.io/blog/user-interface-testing/)
