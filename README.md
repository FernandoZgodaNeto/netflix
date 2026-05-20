# Executar

## 1. Construir o projeto

Na pasta raiz do projeto, abra o terminal ou CMD e execute o seguinte comando:

```bash
mvn clean package
```

Aguarde a construção do projeto terminar.  
Isso irá gerar um arquivo `.jar` com todas as dependências necessárias.

---

## 2. Executar o programa

Depois, execute o seguinte comando para rodar o programa:

```bash
java -cp "target\netflix-1.0-SNAPSHOT-jar-with-dependencies.jar" NetflixAnalyzer "input\netflix_titles.csv" "output"
```

Pressione `ENTER` e pronto!

---

# Se houver erro

- Verifique se o arquivo `netflix_titles.csv` existe em:

```text
input\
```

- Verifique se o Java está instalado:

Abra o CMD e execute:

```bash
java -version
```
