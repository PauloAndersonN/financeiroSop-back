# financeiroSop-back

API de gestao de orcamentos, itens e medicoes. Este README resume a configuracao que estamos usando agora.

## Configuracao atual
- Banco: PostgreSQL local
- URL: `jdbc:postgresql://localhost:5432/sopdb`
- Usuario: `postgres`
- Senha: `senha`
- Schema padrao: `financeiro`
- DDL: `spring.jpa.hibernate.ddl-auto=update`
- SQL log: `spring.jpa.show-sql=true`

Arquivo: `src/main/resources/application.properties`

## Opcional: H2 em memoria
Configuracoes de H2 estao comentadas em `src/main/resources/application.properties` para rodar sem banco externo.

## Fluxograma do banco

![Fluxograma do banco](docs/banco-fluxograma.svg)

## Execucao
- Windows: `mvnw.cmd spring-boot:run`
- Linux/macOS: `./mvnw spring-boot:run`

A API sobe em `http://localhost:8080/financeiro`.
