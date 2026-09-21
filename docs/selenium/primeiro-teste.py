from selenium import webdriver
from selenium.webdriver.common.by import By
import time

driver = webdriver.Chrome()

# Abre a página
driver.get("https://www.selenium.dev/selenium/web/web-form.html")
time.sleep(2)

# Localiza os elementos
campo_texto = driver.find_element(By.NAME, "my-text")
botao = driver.find_element(By.CSS_SELECTOR, "button")

# Digita no campo
campo_texto.send_keys("Teste com Selenium")
time.sleep(2)

# Clica no botão
botao.click()
time.sleep(2)

# Verifica o resultado
mensagem = driver.find_element(By.ID, "message")

assert mensagem.text == "Received!"

print("Teste executado com sucesso!")

# Mantém aberto por alguns segundos para visualizar o resultado
time.sleep(5)

driver.quit()