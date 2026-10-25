# Ricceta Mia

API de compartilhamento de receitas com autenticação JWT, perfis, comentários, curtidas, favoritos e seguidores.

## Executar localmente

Requisitos: Java 21 ou superior e PostgreSQL acessível com as configurações de `src/main/resources/application.properties`. O Maven Wrapper está incluído no projeto.

O endereço padrão do banco é `localhost:5433/ricceta-mia`. O PostgreSQL pode ser instalado diretamente ou executado em Docker. O Docker não é um requisito do Swagger.

Inicie `RiccetaMiaApplication` no IntelliJ ou execute, no PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

O Flyway aplica as migrações ao iniciar e o Hibernate valida as tabelas. O banco precisa estar disponível para a aplicação iniciar.

## Documentação Swagger

Com a aplicação em execução na porta padrão:

- Interface interativa: <http://localhost:8080/swagger-ui.html>
- Especificação OpenAPI em JSON: <http://localhost:8080/v3/api-docs>
- Especificação OpenAPI em YAML: <http://localhost:8080/v3/api-docs.yaml>

São 27 operações organizadas em sete grupos. A implementação utiliza springdoc 3.1.1 com Spring Boot 4.1.1, seguindo a [compatibilidade do springdoc](https://springdoc.org/).

### Testar uma operação autenticada

1. Em **Autenticação**, expanda `POST /auth/register`, clique em **Try it out**, informe os dados e execute. Use um e-mail e um nickname ainda não cadastrados.
2. Execute `POST /auth/login` com o e-mail e a senha cadastrados.
3. Copie o valor de `token` da resposta, sem aspas.
4. Clique em **Authorize**, cole somente o token (sem escrever `Bearer`) e confirme.
5. Execute `GET /users/me` para consultar o perfil autenticado.

Login e cadastro são públicos. As demais operações exigem token. Token ausente, inválido ou expirado retorna **401**, assim como credenciais incorretas no login. O **403** fica reservado para operações permitidas somente ao autor, como editar a receita de outra pessoa. Todos os erros usam `application/problem+json`. As operações executadas pelo Swagger usam o banco conectado à aplicação, portanto cadastros e alterações são reais.

### Organização no código

- `config/OpenAPIConfig.java`: identificação da API e esquema Bearer/JWT.
- `doc/*ControllerDoc.java`: interfaces com grupos, descrições, parâmetros e respostas, seguindo o modelo da aula.
- `controller/*Controller.java`: implementam as interfaces de documentação e mantêm as rotas e regras existentes.
- `dto/request`: exemplos e descrições dos dados enviados, além das validações existentes.
- `config/SecurityConfig.java`: acesso público à interface e à especificação Swagger, preservando a proteção das demais rotas.

## Front-end

O front-end (React + Vite) fica em `frontend/`. Com a API rodando:

```powershell
cd frontend
npm install
npm run dev
```

Abra <http://localhost:5173>. Detalhes de rotas e organização em [frontend/README.md](frontend/README.md).

## Testes

```powershell
.\mvnw.cmd test
```

Os testes precisam de um PostgreSQL. Para usar um banco exclusivo de testes, defina `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` e `SPRING_DATASOURCE_PASSWORD` na sessão do terminal antes de executar o comando. O Flyway criará as tabelas nesse banco.

`SwaggerIntegrationTests` inicia a aplicação em uma porta disponível e verifica a interface, os recursos do Swagger, as 27 operações, os modelos de dados, as operações públicas e a autenticação JWT. O teste de autenticação cadastra um usuário com e-mail aleatório, publica uma receita, curte e favorita essa receita, e remove o usuário (e, em cascata, a receita) ao terminar. Prefira sempre um banco de testes.
