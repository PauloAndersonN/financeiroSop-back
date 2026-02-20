# financeiroSop-back

API de gestao de orcamentos, itens e medicoes. Este README resume a configuracao que estamos usando agora.

## Configuracao atual
- Banco: H2 em memoria
- URL: `jdbc:h2:mem:sopdb;DB_CLOSE_DELAY=-1;INIT=CREATE SCHEMA IF NOT EXISTS FINANCEIRO`
- Usuario: `sa`
- Senha: (vazia)
- Schema padrao: `FINANCEIRO`
- DDL: `spring.jpa.hibernate.ddl-auto=create`
- Carga inicial: `spring.sql.init.mode=always` (usa `data.sql`)
- Console H2: `http://localhost:8080/h2-console`

Arquivo: `src/main/resources/application.properties`

## Fluxograma do banco

![Fluxograma do banco](docs/banco-fluxograma.svg)

## Execucao
- Windows: `mvnw.cmd spring-boot:run`
- Linux/macOS: `./mvnw spring-boot:run`

A API sobe em `http://localhost:8080/financeiro`.
