# Guia rápido — Selenium

## Sobre

O Selenium é uma ferramenta utilizada para automação de navegadores web, permitindo simular ações de um usuário como navegar entre páginas, preencher campos, clicar em botões e validar resultados.

Neste estudo foi utilizado o Selenium WebDriver com Python e Google Chrome.

## Instalação

Instale o Selenium:

```bash
pip install selenium
```

## Localização e interação com elementos

Os elementos de uma página podem ser localizados de diferentes formas:

```python
driver.find_element(By.ID, "elemento")
driver.find_element(By.NAME, "elemento")
driver.find_element(By.CSS_SELECTOR, "button")
driver.find_element(By.XPATH, "//button")
```

Após localizar um elemento, podemos realizar ações como:

```python
campo.send_keys("Texto")  # Digitar
botao.click()             # Clicar
driver.get("URL")         # Navegar
```

## Teste E2E

O arquivo `primeiro-teste.py` contém um teste simples que:

1. Abre o Google Chrome;
2. Acessa uma página de formulário;
3. Localiza um campo e um botão;
4. Preenche o campo;
5. Clica no botão;
6. Valida a resposta apresentada.

A validação é realizada com:

```python
assert mensagem.text == "Received!"
```

## Executar

```bash
python primeiro-teste.py
```

Resultado esperado:

```text
Teste executado com sucesso!
```

## Referência

Documentação oficial do Selenium: https://www.selenium.dev/documentation/webdriver/