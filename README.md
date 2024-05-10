# Gestão de Estacionamento API

Este é um projeto de demonstração para um sistema de gestão de estacionamento desenvolvido com Spring Boot.

## Funcionalidades Principais

- Cadastro de usuários e admin do sistemas.
- Gestão de estacionamento, incluindo adição, remoção e atualização de registros de veículos estacionados.
- Geração de relatórios em formato JPS para análise de dados.
- Controle de acesso com níveis de permissão de administrador e usuário.

## Pré-requisitos

- JDK 21 
- Maven 3.x instalado.
- PostgreSQL instalado 

## Configuração do Banco de Dados

O projeto é configurado para usar o PostgreSQL como banco de dados principal. Certifique-se de configurar corretamente o banco de dados em `src/main/resources/application.properties`.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/seu_banco_de_dados
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

## Clone o repositório

- git clone https://github.com/KarMiguel/parking-api.git

## Documentação da API

- http://localhost:8090/docs.html
