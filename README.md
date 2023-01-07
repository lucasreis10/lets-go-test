# catalogo-service


Projeto de estudo utilizando <b>Clean Architecture, TDD e DDD</b>.


# Links
- [Local](http://localhost:8080)
- [Github](https://github.com/lucasreis10/catalogo-service)
- [Pipeline](https://github.com/lucasreis10/catalogo-service/actions)
- [Swagger](http://localhost:8080/swagger-ui/index.html)

## Pre requisitos para execução local

É necessario instalar as seguintes ferramentas para executar a POC localmente:

* [JDK-11](https://www.oracle.com/br/java/technologies/javase/jdk11-archive-downloads.html)
* [docker](https://docs.docker.com/engine/install/)
* [docker-compose](https://docs.docker.com/compose/install/)

## Comandos importantes

### Rodar testes

<br/>
<i> No diretório raiz do projeto execute: </i>
<br/>

```shell script 
 ./gradlew test -i
 ```

### Rodar instância do MYSQL

<br/>
<i> No diretório raiz do projeto execute: </i>
<br/>

```shell script 
 docker-compose up -d
 ```
