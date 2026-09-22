# Contribuindo com o Ricetta Mia

## Organização do código

Cada domínio funcional tem seu próprio pacote em `com.ricettamia.api`, com as
camadas `controller`, `service`, `repository`, `entity`, `dto` e `validation`.
Trabalhe dentro do pacote do seu domínio. Mudanças em `common` afetam todos,
então avise o grupo antes de alterar algo lá.

| Pacote     | Responsável     | Escopo                                         |
|------------|-----------------|------------------------------------------------|
| `auth`     | Giovana         | Cadastro, login, JWT, autorização              |
| `perfil`   | Giovana         | Perfil do usuário                              |
| `receita`  | Gustavo         | Receitas e ingredientes da receita             |
| `preparo`  | Maria Fernanda  | Preparos e feed                                |
| `catalogo` | Nick            | Categorias e ingredientes (admin)              |
| `social`   | Rafaela         | Comentários, curtidas, favoritos, seguir       |

## Branches

A `main` é protegida: nada é commitado direto nela. Crie branches a partir da
`main` atualizada, seguindo o padrão `<tipo>/<RFxx>-<descricao-curta>`:

- `feature/RF03-crud-receita`
- `fix/RF07-validacao-comentario`
- `chore/config-swagger`
- `docs/readme-setup`

Use letras minúsculas e hífens na descrição. Omita o `RFxx` quando a tarefa não
estiver ligada a um requisito.

## Commits

Seguimos o [Conventional Commits](https://www.conventionalcommits.org/pt-br/):

```
<tipo>(<escopo opcional>): <descrição no imperativo>
```

Tipos: `feat`, `fix`, `refactor`, `test`, `docs`, `chore`, `build`, `style`.
O escopo costuma ser o domínio. Exemplos:

- `feat(receita): adiciona endpoint de criação de receita`
- `fix(auth): corrige expiração do token JWT`
- `build: adiciona dependência do springdoc`

## Migrations (Flyway)

- As migrations ficam em `src/main/resources/db/migration`, no formato
  `V<n>__<descricao>.sql` (ex.: `V3__cria_tabela_receita.sql`).
- Uma migration que já está na `main` nunca é editada. Para corrigir algo, crie
  uma nova.
- Antes de abrir o PR, confira se ninguém usou o mesmo número de versão. Se
  houver conflito, renumere a sua.

## Pull Requests

- Todo PR precisa da revisão e aprovação de **outro integrante** antes do merge.
  Ninguém aprova o próprio PR. A responsabilidade pelo código é do grupo todo.
- O PR precisa compilar (`./mvnw verify`) e estar atualizado com a `main`.
- Descreva o que foi feito e cite o requisito (RFxx) relacionado.
- Prefira PRs pequenos e focados em uma única tarefa.

## Ambiente local

1. Copie `.env.example` para `.env` e preencha as variáveis.
2. Suba o banco: `docker compose up -d`.
3. Copie `src/main/resources/application-dev.example.yml` para
   `application-dev.yml` e ajuste os valores. Esse arquivo não vai para o Git.
4. Rode a aplicação com o profile dev:
   `./mvnw spring-boot:run -Dspring-boot.run.profiles=dev`.
5. Swagger UI: http://localhost:8080/swagger-ui.html
